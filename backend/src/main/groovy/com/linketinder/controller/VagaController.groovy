package com.linketinder.controller

import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import com.linketinder.service.VagaService
import groovy.transform.CompileStatic

@CompileStatic
class VagaController {
    private final VagaService vagaService

    VagaController(VagaService vagaService) {
        this.vagaService = vagaService
    }

    Map<String, Object> cadastrarVaga(VagaDTO dto) {
        try {
            List<String> erros = dto.validar()
            if (!erros.isEmpty()) {
                return [
                        sucesso: false,
                        mensagem: "Dados inválidos",
                        erros: erros
                ]
            }

            Vaga vaga = vagaService.cadastrar(dto)

            return [
                    sucesso: true,
                    mensagem: "Vaga cadastrada com sucesso",
                    vaga: vaga
            ]

        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao cadastrar vaga: ${e.message}"
            ] as Map<String, Object>
        }
    }

    Map<String, Object> listarTodas() {
        try {
            List<Vaga> vagas = vagaService.listarTodas()
            return [
                    sucesso: true,
                    vagas: vagas
            ]
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao listar vagas: ${e.message}"
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

            Vaga vaga = vagaService.buscarPorId(id)

            if (vaga) {
                return [
                        sucesso: true,
                        vaga: vaga
                ] as Map<String, Object>
            } else {
                return [
                        sucesso: false,
                        mensagem: "Vaga não encontrada"
                ] as Map<String, Object>
            }
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao buscar vaga: ${e.message}"
            ] as Map<String, Object>
        }
    }

    Map<String, Object> listarPorEmpresa(Integer empresaId) {
        try {
            if (empresaId == null || empresaId <= 0) {
                return [
                        sucesso: false,
                        mensagem: "ID da empresa inválido"
                ] as Map<String, Object>
            }

            List<Vaga> vagas = vagaService.listarPorEmpresa(empresaId)
            return [
                    sucesso: true,
                    vagas: vagas
            ]
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao listar vagas por empresa: ${e.message}"
            ] as Map<String, Object>
        }
    }
}
