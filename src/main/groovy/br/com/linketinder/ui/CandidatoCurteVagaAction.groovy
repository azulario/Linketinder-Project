package br.com.linketinder.ui

import br.com.linketinder.controller.CandidatoController

class CandidatoCurteVagaAction {
    private final CandidatoController candidatoController

    CandidatoCurteVagaAction(CandidatoController candidatoController) {
        if (candidatoController == null) {
            throw new IllegalArgumentException("CandidatoController não pode ser nulo")
        }
        this.candidatoController = candidatoController
    }

    boolean executar(Object idCandidato, Object idVaga) {
        long candidatoId = validarEConverterId(idCandidato, "idCandidato")
        long vagaId = validarEConverterId(idVaga, "idVaga")
        return candidatoController.curtirVaga(String.valueOf(candidatoId), String.valueOf(vagaId))
    }

    private static long validarEConverterId(Object valor, String nomeCampo) {
        if (valor == null) {
            throw new IllegalArgumentException("$nomeCampo não pode ser nulo")
        }
        long id
        if (valor instanceof Number) {
            id = ((Number) valor).longValue()
        } else if (valor instanceof CharSequence) {
            try {
                id = Long.parseLong(valor.toString().trim())
            } catch (NumberFormatException) {
                throw new IllegalArgumentException("$nomeCampo deve ser um número válido")
            }
        } else {
            throw new IllegalArgumentException("$nomeCampo deve ser um número ou string numérica")
        }

        if (id <= 0L) {
            throw new IllegalArgumentException("$nomeCampo deve ser um número positivo")
        }
        return id
    }

    // Coberto: Implementar ação de curtir vaga por candidato [OK]
    // COberto: Validar entrada [OK]
    // COberto: Integrar com CandidatoController [OK]

}
