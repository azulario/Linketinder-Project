package com.linketinder.view

import com.linketinder.controller.CandidatoController
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import groovy.transform.CompileStatic

@CompileStatic
class MenuCandidato {
    private final CandidatoController controller
    private final IFormatador<Candidato> formatador
    private final Scanner input

    MenuCandidato(CandidatoController controller, Scanner input) {
        this.controller = controller
        this.formatador = new CandidatoFormatador()
        this.input = input
    }

    void exibir() {
        boolean voltar = false

        while (!voltar) {
            println "\n" + "-" * 60
            println "              MENU CANDIDATOS"
            println "-" * 60
            println "\n1. Cadastrar candidato"
            println "2. Listar todos os candidatos"
            println "3. Buscar candidato por ID"
            println "0. Voltar"
            print "\nEscolha uma opção: "

            try {
                Integer opcao = Integer.parseInt(input.nextLine())

                switch (opcao) {
                    case 1:
                        cadastrarCandidato()
                        break
                    case 2:
                        listarCandidatos()
                        break
                    case 3:
                        buscarCandidato()
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

    private void cadastrarCandidato() {
        println "\n### CADASTRO DE CANDIDATO ###\n"

        print "Nome: "
        String nome = input.nextLine()

        print "Email: "
        String email = input.nextLine()

        print "CPF (apenas números): "
        String cpf = input.nextLine()

        print "Data de Nascimento (AAAA-MM-DD): "
        String dataDeNascimento = input.nextLine()

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

        print "Competências (separadas por vírgula): "
        String competenciasStr = input.nextLine()
        List<String> competencias = competenciasStr.split(",").collect { it.trim() }

        CandidatoDTO candidatoDTO = new CandidatoDTO(
                nome: nome,
                email: email,
                cpf: cpf,
                dataDeNascimento: dataDeNascimento,
                pais: pais,
                estado: estado,
                cidade: cidade,
                cep: cep,
                descricao: descricao,
                competencias: competencias
        )

        Map resultado = controller.cadastrar(candidatoDTO)

        if (resultado.sucesso) {
            println "\n${resultado.mensagem}"
        } else {
            println "\n${resultado.mensagem}"
            if (resultado.erros) {
                println "\nErros encontrados:"
                resultado.erros.each { erro ->
                    println "- ${erro}"
                }
            }
        }
    }

    private void listarCandidatos() {

        println "\n### LISTA DE CANDIDATOS ###\n"

        Map resultado = controller.listarTodos()

        if (resultado.sucesso) {
            List<Candidato> candidatos = resultado.candidatos as List<Candidato>

            if (candidatos.isEmpty()) {
                println "\nNenhum candidato cadastrado."
                return
            }

            candidatos.eachWithIndex { Candidato candidato, int i ->
                println "\n${i + 1}. ${candidato.nome} ${candidato.sobrenome}"
                println formatador.formatar(candidato)
                println "-" * 80
            }

        } else {
            println "\n${resultado.mensagem}"

        }

    }

    private void buscarCandidato() {
        println "\nDigite o ID do candidato:"
        try {
            Integer id = Integer.parseInt(input.nextLine())

            Map resultado = controller.buscarPorId(id)

            if (resultado.sucesso) {
                Candidato candidato  = resultado.candidato as Candidato

                println "\n### CANDIDATO ENCONTRADO ###"
                println formatador.formatar(candidato)

            } else {
                println "\n${resultado.mensagem}"

            }

        } catch (NumberFormatException e) {
            println "\nID inválido. Por favor, insira um número válido."
        }
    }
}








