package br.com.linketinder.ui

import br.com.linketinder.controller.EmpresaController

class EmpresaCurteCandidatoAction {
    private final EmpresaController empresaController

    EmpresaCurteCandidatoAction(EmpresaController empresaController) {
        if (empresaController == null) {
            throw new IllegalArgumentException("EmpresaController não pode ser nulo")
        }
        this.empresaController = empresaController
    }

    boolean executar(Object idEmpresa, Object idCandidato) {
        long empresaId = validarEConverterId(idEmpresa, "idEmpresa")
        long candidatoId = validarEConverterId(idCandidato, "idCandidato")
        return empresaController.curtirCandidato((int)empresaId, (int)candidatoId)
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
}
