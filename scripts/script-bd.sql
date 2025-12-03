-- =====================================================================
-- SCRIPT DE CRIACAO - BANCO DE DADOS AZURE SQL
-- PROJETO: SkillMatch AI
-- =====================================================================

-- === 0. LIMPEZA (Para testes, se as tabelas já existirem) ===
IF OBJECT_ID('TB_SKILL_USUARIO_HABILIDADE', 'U') IS NOT NULL
    DROP TABLE TB_SKILL_USUARIO_HABILIDADE;
GO

IF OBJECT_ID('TB_SKILL_VAGA_HABILIDADE', 'U') IS NOT NULL
    DROP TABLE TB_SKILL_VAGA_HABILIDADE;
GO

IF OBJECT_ID('TB_SKILL_HABILIDADE', 'U') IS NOT NULL
    DROP TABLE TB_SKILL_HABILIDADE;
GO

IF OBJECT_ID('TB_SKILL_USUARIO', 'U') IS NOT NULL
    DROP TABLE TB_SKILL_USUARIO;
GO

IF OBJECT_ID('TB_SKILL_VAGA', 'U') IS NOT NULL
    DROP TABLE TB_SKILL_VAGA;
GO

-- === 1. CRIACAO DAS TABELAS ===

-- Tabela principal de Habilidades (Hard Skills, Soft Skills, Ferramentas)
CREATE TABLE TB_SKILL_HABILIDADE (
    id_habilidade           INT IDENTITY(1,1) PRIMARY KEY,
    nm_habilidade           NVARCHAR(100) NOT NULL UNIQUE,
    ds_categoria_habilidade NVARCHAR(50)  NOT NULL CHECK (ds_categoria_habilidade IN ('HARD_SKILL', 'SOFT_SKILL', 'FERRAMENTA'))
);
GO

-- Tabela de Usuários / Trabalhadores
CREATE TABLE TB_SKILL_USUARIO (
    id_usuario              INT IDENTITY(1,1) PRIMARY KEY,
    nm_usuario              NVARCHAR(100) NOT NULL,
    ds_email                NVARCHAR(100) NOT NULL UNIQUE,
    ds_senha                NVARCHAR(255) NOT NULL,
    ds_titulo_cargo         NVARCHAR(100)
);
GO

-- Tabela de Vagas de Emprego
CREATE TABLE TB_SKILL_VAGA (
    id_vaga                 INT IDENTITY(1,1) PRIMARY KEY,
    nm_empresa              NVARCHAR(100) NOT NULL,
    ds_titulo_vaga          NVARCHAR(100) NOT NULL,
    ds_descricao_vaga       NVARCHAR(MAX)
);
GO

-- === 2. TABELAS PIVOT (Relacionamentos Many-to-Many) ===

-- Tabela que liga quais HABILIDADES um USUARIO POSSUI
CREATE TABLE TB_SKILL_USUARIO_HABILIDADE (
    id_usuario_habilidade   INT IDENTITY(1,1) PRIMARY KEY,
    id_usuario              INT NOT NULL,
    id_habilidade           INT NOT NULL,
    CONSTRAINT fk_usuario_hab_usu FOREIGN KEY (id_usuario) REFERENCES TB_SKILL_USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_hab_hab FOREIGN KEY (id_habilidade) REFERENCES TB_SKILL_HABILIDADE(id_habilidade) ON DELETE CASCADE,
    CONSTRAINT uk_usuario_habilidade UNIQUE (id_usuario, id_habilidade)
);
GO

-- Tabela que liga quais HABILIDADES uma VAGA REQUER
CREATE TABLE TB_SKILL_VAGA_HABILIDADE (
    id_vaga_habilidade      INT IDENTITY(1,1) PRIMARY KEY,
    id_vaga                 INT NOT NULL,
    id_habilidade           INT NOT NULL,
    nr_nivel_exigido        INT DEFAULT 1 NOT NULL,
    CONSTRAINT fk_vaga_hab_vaga FOREIGN KEY (id_vaga) REFERENCES TB_SKILL_VAGA(id_vaga) ON DELETE CASCADE,
    CONSTRAINT fk_vaga_hab_hab FOREIGN KEY (id_habilidade) REFERENCES TB_SKILL_HABILIDADE(id_habilidade) ON DELETE CASCADE,
    CONSTRAINT uk_vaga_habilidade UNIQUE (id_vaga, id_habilidade)
);
GO

-- === 3. DADOS INICIAIS (Para Testes de CRUD) ===
-- Habilidades
INSERT INTO TB_SKILL_HABILIDADE (nm_habilidade, ds_categoria_habilidade) VALUES ('Java 17', 'HARD_SKILL');
INSERT INTO TB_SKILL_HABILIDADE (nm_habilidade, ds_categoria_habilidade) VALUES ('Spring Boot', 'HARD_SKILL');
INSERT INTO TB_SKILL_HABILIDADE (nm_habilidade, ds_categoria_habilidade) VALUES ('SQL Server', 'HARD_SKILL');
INSERT INTO TB_SKILL_HABILIDADE (nm_habilidade, ds_categoria_habilidade) VALUES ('MongoDB', 'FERRAMENTA');
INSERT INTO TB_SKILL_HABILIDADE (nm_habilidade, ds_categoria_habilidade) VALUES ('Azure DevOps', 'FERRAMENTA');
GO

-- Usuários
INSERT INTO TB_SKILL_USUARIO (nm_usuario, ds_email, ds_senha, ds_titulo_cargo) VALUES ('Ana Silva', 'ana@email.com', '$2a$10$fP8.sS9.G8kFzUbGf/3Yy.AC4.M8Qe.QhJ2d.QZ.bYv1.X7n.X7mK', 'Desenvolvedora Backend Jr');
INSERT INTO TB_SKILL_USUARIO (nm_usuario, ds_email, ds_senha, ds_titulo_cargo) VALUES ('Carla Dias', 'carla@email.com', '$2a$10$fP8.sS9.G8kFzUbGf/3Yy.AC4.M8Qe.QhJ2d.QZ.bYv1.X7n.X7mK', 'Engenheira DevOps');
GO

-- Vagas
INSERT INTO TB_SKILL_VAGA (nm_empresa, ds_titulo_vaga, ds_descricao_vaga) VALUES ('Empresa A', 'Vaga Desenvolvedor Backend Pleno (Java)', 'Buscamos dev Java/Spring para atuar em projetos de IA...');
INSERT INTO TB_SKILL_VAGA (nm_empresa, ds_titulo_vaga, ds_descricao_vaga) VALUES ('Empresa C', 'Vaga SRE/DevOps Pleno', 'Vaga para DevOps com experiência em Cloud (Azure) e CI/CD...');
GO

-- Habilidades que a 'Ana' (ID 1) POSSUI
INSERT INTO TB_SKILL_USUARIO_HABILIDADE (id_usuario, id_habilidade) VALUES (1, 1); -- Java 17
INSERT INTO TB_SKILL_USUARIO_HABILIDADE (id_usuario, id_habilidade) VALUES (1, 2); -- Spring Boot
GO

-- Habilidades que a 'Carla' (ID 2) POSSUI
INSERT INTO TB_SKILL_USUARIO_HABILIDADE (id_usuario, id_habilidade) VALUES (2, 5); -- Azure DevOps
GO

-- Habilidades que a 'Vaga Backend' (ID 1) REQUER
INSERT INTO TB_SKILL_VAGA_HABILIDADE (id_vaga, id_habilidade, nr_nivel_exigido) VALUES (1, 1, 3); -- Java 17
INSERT INTO TB_SKILL_VAGA_HABILIDADE (id_vaga, id_habilidade, nr_nivel_exigido) VALUES (1, 2, 3); -- Spring Boot
INSERT INTO TB_SKILL_VAGA_HABILIDADE (id_vaga, id_habilidade, nr_nivel_exigido) VALUES (1, 3, 2); -- SQL Server
GO

-- Habilidades que a 'Vaga DevOps' (ID 2) REQUER
INSERT INTO TB_SKILL_VAGA_HABILIDADE (id_vaga, id_habilidade, nr_nivel_exigido) VALUES (2, 5, 4); -- Azure DevOps
GO

-- === 4. TABELAS PARA TRILHAS DETALHADAS (Substitui MongoDB) ===

-- Tabela principal de Trilhas Detalhadas
CREATE TABLE TB_TRILHA_DETALHADA (
    id_trilha_detalhada    INT IDENTITY(1,1) PRIMARY KEY,
    id_trilha_relacional   INT NULL, -- FK para TB_SKILL_TRILHA (se existir)
    titulo_trilha          NVARCHAR(200) NOT NULL,
    descricao_completa     NVARCHAR(MAX),
    data_criacao           DATETIME2 DEFAULT GETDATE(),
    status                 NVARCHAR(50) DEFAULT 'Ativa' CHECK (status IN ('Ativa', 'Inativa', 'Em Desenvolvimento'))
);
GO

-- Tabela de Módulos (dentro de uma trilha)
CREATE TABLE TB_TRILHA_MODULO (
    id_modulo              INT IDENTITY(1,1) PRIMARY KEY,
    id_trilha_detalhada    INT NOT NULL,
    titulo_modulo          NVARCHAR(200) NOT NULL,
    duracao_horas          INT DEFAULT 0,
    ordem                  INT DEFAULT 1,
    CONSTRAINT fk_modulo_trilha FOREIGN KEY (id_trilha_detalhada) REFERENCES TB_TRILHA_DETALHADA(id_trilha_detalhada) ON DELETE CASCADE
);
GO

-- Tabela de Aulas (dentro de um módulo)
CREATE TABLE TB_TRILHA_AULA (
    id_aula                INT IDENTITY(1,1) PRIMARY KEY,
    id_modulo              INT NOT NULL,
    titulo                 NVARCHAR(200) NOT NULL,
    url_video              NVARCHAR(500),
    ordem                  INT DEFAULT 1,
    CONSTRAINT fk_aula_modulo FOREIGN KEY (id_modulo) REFERENCES TB_TRILHA_MODULO(id_modulo) ON DELETE CASCADE
);
GO

-- Tabela de Reviews (avaliações de trilhas)
CREATE TABLE TB_TRILHA_REVIEW (
    id_review              INT IDENTITY(1,1) PRIMARY KEY,
    id_trilha_detalhada    INT NOT NULL,
    id_trabalhador         INT NOT NULL,
    nota                   INT NOT NULL CHECK (nota >= 1 AND nota <= 5),
    comentario             NVARCHAR(1000),
    data_review            DATETIME2 DEFAULT GETDATE(),
    CONSTRAINT fk_review_trilha FOREIGN KEY (id_trilha_detalhada) REFERENCES TB_TRILHA_DETALHADA(id_trilha_detalhada) ON DELETE CASCADE,
    CONSTRAINT fk_review_trabalhador FOREIGN KEY (id_trabalhador) REFERENCES TB_SKILL_USUARIO(id_usuario) ON DELETE CASCADE,
    CONSTRAINT uk_review_trabalhador_trilha UNIQUE (id_trilha_detalhada, id_trabalhador)
);
GO

-- Dados de exemplo para Trilhas
INSERT INTO TB_TRILHA_DETALHADA (titulo_trilha, descricao_completa, status) 
VALUES ('Trilha Java Backend Completa', 'Aprenda Java 17, Spring Boot e desenvolvimento backend completo', 'Ativa');
GO

INSERT INTO TB_TRILHA_MODULO (id_trilha_detalhada, titulo_modulo, duracao_horas, ordem)
VALUES (1, 'Fundamentos Java 17', 20, 1);
GO

INSERT INTO TB_TRILHA_AULA (id_modulo, titulo, url_video, ordem)
VALUES (1, 'Introdução ao Java 17', 'https://example.com/java-intro', 1);
GO

-- =====================================================================
-- PROCEDIMENTOS T-SQL (Stored Procedures e Functions)
-- =====================================================================

-- === PROCEDIMENTO: Calcular Compatibilidade entre Trabalhador e Vaga ===
CREATE OR ALTER PROCEDURE SP_CALCULAR_COMPATIBILIDADE
    @p_id_usuario INT,
    @p_id_vaga INT,
    @p_percentual DECIMAL(5,2) OUTPUT,
    @p_hab_comuns INT OUTPUT,
    @p_hab_total INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @v_hab_comuns_count INT;
    DECLARE @v_hab_vaga_count INT;
    
    BEGIN TRY
        -- Conta habilidades comuns entre trabalhador e vaga
        SELECT @v_hab_comuns_count = COUNT(DISTINCT u.id_habilidade)
        FROM TB_SKILL_USUARIO_HABILIDADE u
        INNER JOIN TB_SKILL_VAGA_HABILIDADE v ON u.id_habilidade = v.id_habilidade
        WHERE u.id_usuario = @p_id_usuario
          AND v.id_vaga = @p_id_vaga;
        
        -- Conta total de habilidades exigidas pela vaga
        SELECT @v_hab_vaga_count = COUNT(*)
        FROM TB_SKILL_VAGA_HABILIDADE
        WHERE id_vaga = @p_id_vaga;
        
        -- Calcula percentual de compatibilidade
        IF @v_hab_vaga_count > 0
            SET @p_percentual = ROUND((CAST(@v_hab_comuns_count AS DECIMAL) / CAST(@v_hab_vaga_count AS DECIMAL)) * 100, 2);
        ELSE
            SET @p_percentual = 0;
        
        SET @p_hab_comuns = @v_hab_comuns_count;
        SET @p_hab_total = @v_hab_vaga_count;
        
    END TRY
    BEGIN CATCH
        SET @p_percentual = 0;
        SET @p_hab_comuns = 0;
        SET @p_hab_total = 0;
        THROW;
    END CATCH
END;
GO

-- === FUNÇÃO: Verificar se Trabalhador possui Habilidade ===
CREATE OR ALTER FUNCTION FN_POSSUI_HABILIDADE (
    @p_id_usuario INT,
    @p_id_habilidade INT
)
RETURNS INT
AS
BEGIN
    DECLARE @v_count INT;
    
    SELECT @v_count = COUNT(*)
    FROM TB_SKILL_USUARIO_HABILIDADE
    WHERE id_usuario = @p_id_usuario
      AND id_habilidade = @p_id_habilidade;
    
    RETURN CASE WHEN @v_count > 0 THEN 1 ELSE 0 END;
END;
GO

-- === PROCEDIMENTO: Adicionar Habilidade a Trabalhador (com validação) ===
CREATE OR ALTER PROCEDURE SP_ADICIONAR_HABILIDADE_USUARIO
    @p_id_usuario INT,
    @p_id_habilidade INT,
    @p_sucesso INT OUTPUT,
    @p_mensagem NVARCHAR(255) OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    
    DECLARE @v_existe_usuario INT;
    DECLARE @v_existe_habilidade INT;
    DECLARE @v_ja_possui INT;
    
    SET @p_sucesso = 0;
    SET @p_mensagem = '';
    
    BEGIN TRY
        BEGIN TRANSACTION;
        
        -- Valida se usuário existe
        SELECT @v_existe_usuario = COUNT(*)
        FROM TB_SKILL_USUARIO
        WHERE id_usuario = @p_id_usuario;
        
        IF @v_existe_usuario = 0
        BEGIN
            SET @p_mensagem = 'Usuario nao encontrado';
            ROLLBACK TRANSACTION;
            RETURN;
        END;
        
        -- Valida se habilidade existe
        SELECT @v_existe_habilidade = COUNT(*)
        FROM TB_SKILL_HABILIDADE
        WHERE id_habilidade = @p_id_habilidade;
        
        IF @v_existe_habilidade = 0
        BEGIN
            SET @p_mensagem = 'Habilidade nao encontrada';
            ROLLBACK TRANSACTION;
            RETURN;
        END;
        
        -- Verifica se já possui a habilidade
        SET @v_ja_possui = dbo.FN_POSSUI_HABILIDADE(@p_id_usuario, @p_id_habilidade);
        
        IF @v_ja_possui = 1
        BEGIN
            SET @p_mensagem = 'Usuario ja possui esta habilidade';
            ROLLBACK TRANSACTION;
            RETURN;
        END;
        
        -- Adiciona habilidade
        INSERT INTO TB_SKILL_USUARIO_HABILIDADE (id_usuario, id_habilidade)
        VALUES (@p_id_usuario, @p_id_habilidade);
        
        COMMIT TRANSACTION;
        SET @p_sucesso = 1;
        SET @p_mensagem = 'Habilidade adicionada com sucesso';
        
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;
        SET @p_mensagem = 'Erro: ' + ERROR_MESSAGE();
        SET @p_sucesso = 0;
    END CATCH
END;
GO

PRINT 'Script SQL executado com sucesso!';
GO
