package com.linketinder.controller

import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.service.EmpresaService
import groovy.transform.CompileStatic

@CompileStatic
class EmpresaController {
    private final EmpresaService empresaService

    EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService
    }

    Map<String, Object> cadastrarEmpresa(EmpresaDTO dto) {
        try {
            List<String> erros = dto.validar()
            if (!erros.isEmpty()) {
                return [
                        sucesso: false,
                        mensagem: "Dados inválidos",
                        erros: erros
                ]
            }

            Empresa empresa = empresaService.cadastrar(dto)

            return [
                    sucesso: true,
                    mensagem: "Empresa cadastrada com sucesso",
                    empresa: empresa
            ]

        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao cadastrar empresa: ${e.message}"
            ] as Map<String, Object>
        }
    }

    Map<String, Object> listarTodas() {
        try {
            List<Empresa> empresas = empresaService.listarTodas()
            return [
                    sucesso: true,
                    empresas: empresas
            ]
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao listar empresas: ${e.message}"
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

            Empresa empresa = empresaService.buscarPorId(id)

            if (empresa) {
                return [
                        sucesso: true,
                        empresa: empresa
                ] as Map<String, Object>
            } else {
                return [
                        sucesso: false,
                        mensagem: "Empresa não encontrada"
                ] as Map<String, Object>
            }
        } catch (Exception e) {
            return [
                    sucesso: false,
                    mensagem: "Erro ao buscar empresa: ${e.message}"
            ] as Map<String, Object>
        }
    }

}
