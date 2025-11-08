package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import com.linketinder.view.IFormatador
import com.linketinder.view.EmpresaFormatador
import groovy.transform.CompileStatic

@CompileStatic
class EmpresaService {
    private EmpresaDAO empresaDAO
    private EnderecoDAO enderecoDAO
    private IFormatador<Empresa> formatador

    EmpresaService() {
        this.empresaDAO = new EmpresaDAO()
        this.enderecoDAO = new EnderecoDAO()
        this.formatador = new EmpresaFormatador()
    }

    Empresa cadastrar(EmpresaDTO dto) {
        Endereco endereco = new Endereco(dto.pais, dto.estado, dto.cidade, dto.cep)
        Integer enderecoId = enderecoDAO.buscarOuCriar(endereco)

        Empresa empresa = new Empresa(dto.nome, dto.email, dto.cnpj, dto.descricao)
        empresa.enderecoId = enderecoId

        empresaDAO.inserir(empresa)

        return empresa
    }

    List<Empresa> listarTodas() {
        return empresaDAO.listar()
    }

    Empresa buscarPorId(Integer id) {
        return empresaDAO.buscarPorId(id)
    }

    String formatarEmpresa(Empresa empresa) {
        return formatador.formatar(empresa)
    }

    String formatarLista(List<Empresa> empresas) {
        if (empresas.isEmpty()) {
            return "Nenhuma empresa cadastrada."
        }

        StringBuilder resultado = new StringBuilder()
        empresas.eachWithIndex { Empresa empresa, int index ->
            resultado.append("\n${index + 1}.\n")
            resultado.append(formatarEmpresa(empresa))
            resultado.append("-" * 50)
            resultado.append("\n")
        }
        return resultado.toString()
    }
}

