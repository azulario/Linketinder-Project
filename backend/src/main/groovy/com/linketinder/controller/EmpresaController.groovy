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

}
