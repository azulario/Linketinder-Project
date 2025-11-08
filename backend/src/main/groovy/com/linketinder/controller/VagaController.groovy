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
}
