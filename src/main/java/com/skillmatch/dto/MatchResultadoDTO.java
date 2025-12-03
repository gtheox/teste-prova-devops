        package com.skillmatch.dto;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public class MatchResultadoDTO {

            private Long idTrabalhador;

            private Long idVaga;

            private Double percentualCompatibilidade;

            private String statusMatch; // COMPATIVEL, PARCIALMENTE_COMPATIVEL, INCOMPATIVEL

            private String mensagem;

            private String trilhaRecomendada;
        }
