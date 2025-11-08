package com.linketinder.view

import com.linketinder.controller.EmpresaController
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import groovy.transform.CompileStatic

@CompileStatic
class MenuEmpresa {
    private final EmpresaController controller
    private final IFormatador<Empresa> formatador
    private final Scanner input

    MenuEmpresa(EmpresaController controller, Scanner input) {
        this.controller = controller
        this.formatador = new EmpresaFormatador()
        this.input = input
    }

    void exibir() {
        boolean voltar = false

        while (!voltar) {
            println "\n" + "-" * 60
            println "              MENU EMPRESAS"
            println "-" * 60
            println "\n1. Cadastrar empresa"
            println "2. Listar todas as empresas"
            println "3. Buscar empresa por ID"
            println "0. Voltar"
            print "\nEscolha uma opção: "

            try {
                Integer opcao = Integer.parseInt(input.nextLine())

                switch (opcao) {
                    case 1:
                        cadastrarEmpresa()
                        break
                    case 2:
                        listarEmpresas()
                        break
                    case 3:
                        listarEmpresas()
                        break
                    case 0:
                        voltar = true
                        break
                    default:
                        println "Opção inválida. Tente novamente."
                }
            } catch (NumberFormatException e) {
                println "Entrada inválida. Por favor, insira um número."
            }
        }
    }

    private void cadastrarEmpresa() {
        println "\n### CADASTRO EMPRESA ###\n"

        print "Nome: "
        String nome = input.nextLine()

        print "Email: "
        String email = input.nextLine()

        print "CNPJ (apenas números): "
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

        EmpresaDTO empresaDTO = new EmpresaDTO(
                nome: nome,
                email: email,
                cnpj: cnpj,
                pais: pais,
                estado: estado,
                cidade: cidade,
                cep: cep,
                descricao: descricao
        )

        Map resultado = controller.cadastrarEmpresa(empresaDTO)

        if (resultado.sucesso) {
            println "\n${resultado.mensagem}"
        } else {
            println "\n${resultado.mensagem}"
            if (resultado.erros) {
                println "\nErros:"
                resultado.erros.each { erro ->
                    println "- ${erro}"
                }
            }

        }
    }

    private void listarEmpresas() {
        println "\n### LISTA DE EMPRESAS ###\n"

        Map resultado = controller.listarTodas()

        if (resultado.sucesso) {
            List<Empresa> empresas = resultado.empresas as List<Empresa>

            if (empresas.isEmpty()) {
                println "Nenhuma empresa cadastrada."
            }

            empresas.eachWithIndex { Empresa empresa, int i ->
                println "\nEmpresa ${i + 1}. ${empresa.nome}"
                println formatador.formatar(empresa)
                println "-" * 50
            }
        } else {
            println "\n${resultado.mensagem}"
        }

    }

}
