# ===============================================
# SCRIPT DE INFRAESTRUTURA - AZURE CLI
# PROJETO: SkillMatch AI - Web App PaaS
# ===============================================

param(
    [Parameter(Mandatory=$false)]
    [string]$ResourceGroup = "Globalsolution",
    
    [Parameter(Mandatory=$false)]
    [string]$Location = "eastus2",
    
    [Parameter(Mandatory=$false)]
    [string]$SqlServerName = "skillmatch-sql-$(Get-Random -Maximum 99999)",
    
    [Parameter(Mandatory=$false)]
    [string]$SqlDatabaseName = "skillmatch-db",
    
    [Parameter(Mandatory=$false)]
    [string]$AppServicePlanName = "skillmatch-plan",
    
    [Parameter(Mandatory=$false)]
    [string]$WebAppName = "skillmatch-app-$(Get-Random -Maximum 99999)"
)

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "SCRIPT DE INFRAESTRUTURA - WEB APP PAAS" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan

# Variáveis de ambiente obrigatórias
$SQL_ADMIN_USER = $env:SQL_ADMIN_USER
$SQL_ADMIN_PWD = $env:SQL_ADMIN_PWD

# Validação melhorada - detecta se variáveis não foram definidas ou contêm placeholders do Azure DevOps
if (-not $SQL_ADMIN_USER -or $SQL_ADMIN_USER -eq "" -or $SQL_ADMIN_USER -match '^\$\(.*\)$') {
    Write-Error "ERRO: SQL_ADMIN_USER nao foi definida ou esta vazia!"
    Write-Error "Configure a variavel SQL_ADMIN_USER na pipeline (Pipelines > Edit > Variables)"
    exit 1
}

if (-not $SQL_ADMIN_PWD -or $SQL_ADMIN_PWD -eq "" -or $SQL_ADMIN_PWD -match '^\$\(.*\)$') {
    Write-Error "ERRO: SQL_ADMIN_PWD nao foi definida ou esta vazia!"
    Write-Error "Configure a variavel SQL_ADMIN_PWD na pipeline (Pipelines > Edit > Variables) como SECRET"
    exit 1
}

Write-Host "SQL_ADMIN_USER configurado: $($SQL_ADMIN_USER.Substring(0, [Math]::Min(3, $SQL_ADMIN_USER.Length)))..." -ForegroundColor Green
Write-Host "SQL_ADMIN_PWD configurado: ***" -ForegroundColor Green

# ===============================================
# 1. RESOURCE GROUP (IDEMPOTENTE)
# ===============================================
Write-Host "`n1. Criando/Verificando Resource Group..." -ForegroundColor Yellow
$rgExists = $false
$ErrorActionPreference = 'SilentlyContinue'
$rgCheck = az group show --name $ResourceGroup --query name -o tsv 2>$null
$ErrorActionPreference = 'Continue'
if ($LASTEXITCODE -eq 0 -and $rgCheck) {
    $rgExists = $true
    Write-Host "Resource Group ja existe." -ForegroundColor Cyan
}
if (-not $rgExists) {
    az group create --name $ResourceGroup --location $Location --output none
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Falha ao criar Resource Group!"
        exit 1
    }
    Write-Host "Resource Group criado com sucesso!" -ForegroundColor Green
}

# ===============================================
# 2. AZURE SQL SERVER + DATABASE (IDEMPOTENTE)
# ===============================================
Write-Host "`n2. Criando Azure SQL Server e Database..." -ForegroundColor Yellow

# SQL Server
$sqlServerExists = $false
$ErrorActionPreference = 'SilentlyContinue'
$sqlCheck = az sql server show --resource-group $ResourceGroup --name $SqlServerName --query name -o tsv 2>$null
$ErrorActionPreference = 'Continue'
if ($LASTEXITCODE -eq 0 -and $sqlCheck) {
    $sqlServerExists = $true
    Write-Host "SQL Server ja existe." -ForegroundColor Cyan
}
if (-not $sqlServerExists) {
    Write-Host "Criando SQL Server: $SqlServerName" -ForegroundColor Cyan
    Write-Host "Aguarde, isso pode levar alguns minutos..." -ForegroundColor Yellow
    
    # Executa o comando e captura output/erro explicitamente
    $ErrorActionPreference = 'Continue'
    $output = az sql server create `
        --resource-group $ResourceGroup `
        --name $SqlServerName `
        --location $Location `
        --admin-user $SQL_ADMIN_USER `
        --admin-password $SQL_ADMIN_PWD `
        --output json 2>&1
    
    $exitCode = $LASTEXITCODE
    
    if ($exitCode -ne 0) {
        Write-Host "ERRO ao executar az sql server create:" -ForegroundColor Red
        Write-Host $output -ForegroundColor Red
        
        # Verifica se o erro é de nome já existente
        $outputString = $output -join " "
        if ($outputString -match "NameAlreadyExists" -or $outputString -match "already exists") {
            Write-Error "ERRO: O nome do SQL Server '$SqlServerName' ja existe no Azure (nomes devem ser unicos globalmente)."
            Write-Host "SOLUCAO: Use um nome diferente na variavel SQL_SERVER_NAME da pipeline." -ForegroundColor Yellow
            Write-Host "Exemplo: skillmatch-sql-$(Get-Random -Maximum 99999) ou skillmatch-sql-$(Get-Date -Format 'yyyyMMddHHmmss')" -ForegroundColor Cyan
        } else {
            Write-Error "Falha ao criar SQL Server! Exit code: $exitCode"
        }
        exit 1
    }
    
    Write-Host "Comando executado. Verificando se o SQL Server foi criado..." -ForegroundColor Yellow
    
    # Aguarda até 3 minutos verificando se o recurso foi criado
    $maxWait = 180 # 3 minutos
    $elapsed = 0
    $created = $false
    
    while ($elapsed -lt $maxWait -and -not $created) {
        Start-Sleep -Seconds 10
        $elapsed += 10
        
        $ErrorActionPreference = 'SilentlyContinue'
        $check = az sql server show --resource-group $ResourceGroup --name $SqlServerName --query name -o tsv 2>$null
        $ErrorActionPreference = 'Continue'
        
        if ($LASTEXITCODE -eq 0 -and $check) {
            $created = $true
            Write-Host "SQL Server criado e verificado com sucesso! (apos $elapsed segundos)" -ForegroundColor Green
        } else {
            Write-Host "Aguardando SQL Server ficar disponivel... ($elapsed/$maxWait segundos)" -ForegroundColor Cyan
        }
    }
    
    if (-not $created) {
        Write-Error "SQL Server nao foi criado apos $maxWait segundos de verificacao!"
        Write-Host "Verifique no Azure Portal se ha algum erro ou limitação na subscription 'Azure for Students'." -ForegroundColor Yellow
        Write-Host "Possivel causa: Limitação de quota ou recursos na subscription gratuita." -ForegroundColor Yellow
        exit 1
    }
}

# Firewall Rule - Permitir Azure Services
Write-Host "Configurando Firewall Rules..." -ForegroundColor Cyan
az sql server firewall-rule create `
    --resource-group $ResourceGroup `
    --server $SqlServerName `
    --name "AllowAzureServices" `
    --start-ip-address "0.0.0.0" `
    --end-ip-address "0.0.0.0" `
    --output none 2>$null

# Firewall Rule - Permitir acesso do agente (se necessário)
$agentIP = (Invoke-WebRequest -Uri "https://api.ipify.org" -UseBasicParsing).Content
az sql server firewall-rule create `
    --resource-group $ResourceGroup `
    --server $SqlServerName `
    --name "AllowAgentIP" `
    --start-ip-address $agentIP `
    --end-ip-address $agentIP `
    --output none 2>$null

# SQL Database
$sqlDbExists = $false
$ErrorActionPreference = 'SilentlyContinue'
$dbCheck = az sql db show --resource-group $ResourceGroup --server $SqlServerName --name $SqlDatabaseName --query name -o tsv 2>$null
$ErrorActionPreference = 'Continue'
if ($LASTEXITCODE -eq 0 -and $dbCheck) {
    $sqlDbExists = $true
    Write-Host "SQL Database ja existe." -ForegroundColor Cyan
}
if (-not $sqlDbExists) {
    Write-Host "Criando SQL Database: $SqlDatabaseName" -ForegroundColor Cyan
    az sql db create `
        --resource-group $ResourceGroup `
        --server $SqlServerName `
        --name $SqlDatabaseName `
        --service-objective "S0" `
        --backup-storage-redundancy "Local" `
        --output none
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Falha ao criar SQL Database!"
        exit 1
    }
    Write-Host "SQL Database criada com sucesso!" -ForegroundColor Green
}

$SQL_CONNECTION_STRING = "Server=tcp:$SqlServerName.database.windows.net,1433;Initial Catalog=$SqlDatabaseName;Persist Security Info=False;User ID=$SQL_ADMIN_USER;Password=$SQL_ADMIN_PWD;MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30;"

# ===============================================
# 3. APP SERVICE PLAN (IDEMPOTENTE)
# ===============================================
Write-Host "`n3. Criando App Service Plan..." -ForegroundColor Yellow

$planExists = $false
$ErrorActionPreference = 'SilentlyContinue'
$planCheck = az appservice plan show --resource-group $ResourceGroup --name $AppServicePlanName --query name -o tsv 2>$null
$ErrorActionPreference = 'Continue'
if ($LASTEXITCODE -eq 0 -and $planCheck) {
    $planExists = $true
    Write-Host "App Service Plan ja existe." -ForegroundColor Cyan
}
if (-not $planExists) {
    Write-Host "Criando App Service Plan: $AppServicePlanName" -ForegroundColor Cyan
    az appservice plan create `
        --resource-group $ResourceGroup `
        --name $AppServicePlanName `
        --location $Location `
        --sku "B1" `
        --is-linux `
        --output none
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Falha ao criar App Service Plan!"
        exit 1
    }
    Write-Host "App Service Plan criado com sucesso!" -ForegroundColor Green
}

# ===============================================
# 4. WEB APP (IDEMPOTENTE)
# ===============================================
Write-Host "`n4. Criando Web App..." -ForegroundColor Yellow

$webAppExists = $false
$ErrorActionPreference = 'SilentlyContinue'
$webAppCheck = az webapp show --resource-group $ResourceGroup --name $WebAppName --query name -o tsv 2>$null
$ErrorActionPreference = 'Continue'
if ($LASTEXITCODE -eq 0 -and $webAppCheck) {
    $webAppExists = $true
    Write-Host "Web App ja existe." -ForegroundColor Cyan
}
if (-not $webAppExists) {
    Write-Host "Criando Web App: $WebAppName" -ForegroundColor Cyan
    az webapp create `
        --resource-group $ResourceGroup `
        --plan $AppServicePlanName `
        --name $WebAppName `
        --runtime "JAVA:17-java17" `
        --output none
    
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Falha ao criar Web App!"
        exit 1
    }
    Write-Host "Web App criada com sucesso!" -ForegroundColor Green
}

# Configurar App Settings (variáveis de ambiente)
Write-Host "Configurando App Settings..." -ForegroundColor Cyan
az webapp config appsettings set `
    --resource-group $ResourceGroup `
    --name $WebAppName `
    --settings `
        "SPRING_DATASOURCE_URL=jdbc:sqlserver://$SqlServerName.database.windows.net:1433;database=$SqlDatabaseName;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" `
        "SPRING_DATASOURCE_USERNAME=$SQL_ADMIN_USER" `
        "SPRING_DATASOURCE_PASSWORD=$SQL_ADMIN_PWD" `
        "SPRING_PROFILES_ACTIVE=prod" `
        "SERVER_PORT=8080" `
        "JWT_SECRET=$env:JWT_SECRET" `
    --output none

# Configurar Java version
az webapp config set `
    --resource-group $ResourceGroup `
    --name $WebAppName `
    --java-version "17" `
    --java-container "JAVA" `
    --java-container-version "17" `
    --output none

Write-Host "`n===============================================" -ForegroundColor Green
Write-Host "INFRAESTRUTURA CRIADA COM SUCESSO!" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Green
Write-Host "SQL Server: $SqlServerName.database.windows.net" -ForegroundColor Cyan
Write-Host "SQL Database: $SqlDatabaseName" -ForegroundColor Cyan
Write-Host "Web App: https://$WebAppName.azurewebsites.net" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Green

