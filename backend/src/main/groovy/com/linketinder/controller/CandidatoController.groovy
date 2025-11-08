package com.linketinder.controller

import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.service.CandidatoService


class CandidatoController {
    private final CandidatoService candidatoService

    CandidatoController() {
        this.candidatoService = new CandidatoService()
    }


    CandidatoController(CandidatoService candidatoService) {
        this.candidatoService = candidatoService
    }

    Map<String, Object> cadastrar(CandidatoDTO dto) {
        try {
            List<String> erros = dto.validar()
            if (!erros.isEmpty()) {
                return [
                        sucesso: false,
                        mensagem: "Dados inválidos",
                        erros  : erros
                ] as Map<String, Object>
            }

            Candidato candidato = candidatoService.cadastrar(dto)
            return [
                    sucesso: true,
                    mensagem: "Candidato cadastrado com sucesso",
                    candidato: candidato
            ] as Map<String, Object>


        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao cadastrar candidato: ${e.message}"
            ] as Map<String, Object>
        }
    }

    Map<String, Object> listarTodos() {
        try {
            List<Candidato> candidatos = candidatoService.listarTodos()
            return [
                    sucesso: true,
                    candidatos: candidatos
            ] as Map<String, Object>
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao listar candidatos: ${e.message}"
            ] as Map<String, Object>
        }
    }

    Map<String, Object> buscarPorId(Integer id) {
        try {
            if (id == null || id <= 0) {
                return [
                        sucesso: false,
                        mensagem: "ID inválido"
                ] as Map<String, Object>

            }

            Candidato candidato = candidatoService.buscarPorId(id)

            if (candidato) {
                return [
                        sucesso: true,
                        candidato: candidato
                ] as Map<String, Object>
            } else {
                return [
                        sucesso: false,
                        mensagem: "Candidato não encontrado"
                ] as Map<String, Object>
            }
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao buscar candidato: ${e.message}"
            ] as Map<String, Object>
        }
    }
}
