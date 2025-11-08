package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import groovy.transform.CompileStatic

@CompileStatic
class EmpresaService {
    private EmpresaDAO empresaDAO
    private EnderecoDAO enderecoDAO

    EmpresaService() {
        this.empresaDAO = new EmpresaDAO()
        this.enderecoDAO = new EnderecoDAO()
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

    // DEPRECATED - Será removido após migração completa para MVC
    @Deprecated
    void cadastrar(Scanner input) {
        println "\n### CADASTRO DE EMPRESA ###"

        print "Nome: "
        String nome = input.nextLine()

        print "Email: "
        String email = input.nextLine()

        print "CNPJ: "
        String cnpj = input.nextLine()

        print "País: "
        String pais = input.nextLine()

        print "Estado: "
        String estado = input.nextLine()

        print "Cidade: "
        String cidade = input.nextLine()

        print "CEP: "
        String cep = input.nextLine()

        print "Descrição: "
        String descricao = input.nextLine()

        Endereco endereco = new Endereco(pais, estado, cidade, cep)
        Integer enderecoId = enderecoDAO.buscarOuCriar(endereco)

        Empresa novaEmpresa = new Empresa(
                nome, email, cnpj, descricao
        )
        novaEmpresa.enderecoId = enderecoId

        empresaDAO.inserir(novaEmpresa)
        println "✓ Empresa cadastrada com sucesso!"
    }

    // DEPRECATED - Será removido após migração completa para MVC
    @Deprecated
    void listar() {
        println "\n### LISTA DE EMPRESAS ###"

        List<Empresa> empresas = listarTodas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
            return
        }

        empresas.eachWithIndex { Empresa empresa, int i ->
            println "\n${i + 1}. ${empresa.nome}"
            println "Email: ${empresa.email}"
            println "CNPJ: ${empresa.cnpj}"
            println "Descrição: ${empresa.descricao}"
            println "-" * 50
        }
    }
}

