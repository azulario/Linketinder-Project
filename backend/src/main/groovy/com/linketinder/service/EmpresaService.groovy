package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import com.linketinder.view.EmpresaFormatador
import com.linketinder.view.IFormatador

class EmpresaService {
    private EmpresaDAO empresaDAO
    private EnderecoDAO enderecoDAO
    private IFormatador<Empresa> formatador

    EmpresaService() {
        this.empresaDAO = new EmpresaDAO()
        this.enderecoDAO = new EnderecoDAO()
        this.formatador = new EmpresaFormatador()
    }

    List<Empresa> listarTodas() {
        return empresaDAO.listar()
    }

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

    void listar() {
        println "\n### LISTA DE EMPRESAS ###"

        List<Empresa> empresas = listarTodas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
            return
        }

        empresas.eachWithIndex { Empresa empresa, int i ->
            println "\n${i + 1}. ${empresa.nome}"
            println formatador.formatar(empresa)
            println "-" * 50
        }
    }
}

