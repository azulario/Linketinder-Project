package com.linketinder.view

import com.linketinder.controller.VagaController
import com.linketinder.controller.EmpresaController
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import com.linketinder.model.Empresa
import groovy.transform.CompileStatic

@CompileStatic
class MenuVaga {
    private final VagaController vagaController
    private final EmpresaController empresaController
    private final IFormatador<Vaga> vagaIFormatador
    private final IFormatador<Empresa> empresaIFormatador
    private final Scanner input

    MenuVaga(VagaController vagaController, EmpresaController empresaController, Scanner input) {
        this.vagaController = vagaController
        this.empresaController = empresaController
        this.input = input
        this.vagaIFormatador = new VagaFormatador()
        this.empresaIFormatador = new EmpresaFormatador()

    }

    void exibir() {
        boolean continuar = true

        while (continuar) {
            println "\n" + "=" * 60
            println "              MENU VAGAS"
            println "=" * 60
            println "\n1. Cadastrar Vaga"
            println "2. Listar Vagas"
            println "0. Voltar ao Menu Principal"
            print "\nEscolha uma opção: "

            try {
                Integer opcao = Integer.parseInt(input.nextLine())

                switch (opcao) {
                    case 1:
                        cadastrarVaga()
                        break
                    case 2:
                        listarVagas()
                        break
                    case 0:
                        continuar = false
                        break
                    default:
                        println "Opção inválida! Tente novamente."
                }
            } catch (NumberFormatException e) {
                println "Entrada inválida! Por favor, insira um número."
            }
        }
    }

    private void cadastrarVaga() {
        println "\n### CADASTRO DE VAGA ###\n"

        Map resultadoEmpresas = empresaController.listarTodas()

        if (!resultadoEmpresas.sucesso) {
            println "Erro ao listar empresas: ${resultadoEmpresas.mensagem}"
            return
        }

        List<Empresa> empresas = resultadoEmpresas.dados as List<Empresa>

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. Cadastre uma empresa antes de criar uma vaga."
            return
        }

        println "Empresas disponíveis:"
        empresas.eachWithIndex { Empresa empresa, int i ->
            println "${i + 1}. ${empresa.nome} (ID: ${empresa.id})"
        }

        println "\nEscolha o número da empresa: "
        Integer numEmpresa = Integer.parseInt(input.nextLine()) - 1

        if (numEmpresa < 0 || numEmpresa>= empresas.size()) {
            println "Número de empresa inválido."
            return
        }

        Empresa empresaSelecionada = empresas[numEmpresa]

        print "Título da Vaga: "
        String titulo = input.nextLine()

        print "Descrição da Vaga: "
        String descricao = input.nextLine()

        print "Competencias (separadas por vírgula): "
        String competenciasInput = input.nextLine()
        List<String> competencias = competenciasInput.split(',').collect { it.trim() }

        VagaDTO vagaDTO = new VagaDTO(
            titulo: titulo,
            descricao: descricao,
            competencias: competencias,
            empresaId: empresaSelecionada.id
        )

        Map resultadoCadastro = vagaController.cadastrarVaga(vagaDTO)

        if (resultadoCadastro.sucesso) {
            println "Vaga cadastrada com sucesso!"
        } else {
            println "\n ${resultadoCadastro.mensagem}"
            if (resultadoCadastro.erros) {
                println "Erros encontrados: "
                resultadoCadastro.erros.each { erro ->
                    println "- $erro"
                }
            }
        }
    }

    private void listarVagas() {
        println "\n### LISTA DE VAGAS ###\n"

        Map resultado = vagaController.listarTodas()

        if (!resultado.sucesso) {
            println "Erro ao listar vagas: ${resultado.mensagem}"
            return
        }

        List<Vaga> vagas = resultado.dados as List<Vaga>

        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada."
            return
        }

        vagas.eachWithIndex { Vaga vaga, int i ->
            println "\n${i + 1}. ${vaga.titulo}"
            println vagaIFormatador.formatar(vaga)
            println "\n" + "-" * 60
        }
    }
}
