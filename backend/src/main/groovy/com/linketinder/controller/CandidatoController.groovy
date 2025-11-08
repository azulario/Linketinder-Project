package com.linketinder.controller

import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.service.CandidatoService
import groovy.transform.CompileStatic


@CompileStatic
class CandidatoController {
    private final CandidatoService candidatoService

    CandidatoController() {
        this.candidatoService = new CandidatoService()
    }

    Map cadastrar(CandidatoDTO dto) {
        try {
            List<String> erros = dto.validar()
            if (!erros.isEmpty()) {
                return [
                        sucesso: false,
                        mensagem: "Dados inválidos",
                        erros  : erros
                ]
            }

            Candidato candidato = candidatoService.cadastrar(dto)
            return [
                    sucesso: true,
                    mensagem: "Candidato cadastrado com sucesso",
                    candidato: candidato
            ]


        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao cadastrar candidato: ${e.message}"
            ]
        }
    }

    Map listarTodos() {
        try {
            List<Candidato> candidatos = candidatoService.listarTodos()
            return [
                    sucesso: true,
                    candidatos: candidatos
            ]
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao listar candidatos: ${e.message}"
            ]
        }
    }

    Map buscarPorId(Integer id) {
        try {
            if (id == null || id <= 0) {
                return [
                        sucesso: false,
                        mensagem: "ID inválido"
                ]

            }

            Candidato candidato = candidatoService.buscarPorId(id)

            if (candidato) {
                return [
                        sucesso: true,
                        candidato: candidato
                ]
            } else {
                return [
                        sucesso: false,
                        mensagem: "Candidato não encontrado"
                ]
            }
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao buscar candidato: ${e.message}"
            ]
        }
    }
}
