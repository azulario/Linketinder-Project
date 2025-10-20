package com.linketinder.service

import com.linketinder.dao.VagaDAO
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga

class VagaService {
    private VagaDAO vagaDAO
    private EmpresaService empresaService

    VagaService(EmpresaService empresaService) {
        this.vagaDAO = new VagaDAO()
        this.empresaService = empresaService
    }

    List<Vaga> listarTodas() {
        return vagaDAO.listarTodas()
    }

    void cadastrar(Scanner input) {
        println "\n### CADASTRO DE VAGA ###"

        List<Empresa> empresas = empresaService.listarTodas()

        if (empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada. Cadastre uma empresa primeiro."
            return
        }

        println "\nEmpresas disponíveis:"
        empresas.eachWithIndex { Empresa empresa, int i ->
            println "${i + 1}. ${empresa.nome}"
        }

        print "\nEscolha o número da empresa: "
        Integer numEmpresa = input.nextLine().toInteger() - 1

        if (numEmpresa < 0 || numEmpresa >= empresas.size()) {
            println "Número de empresa inválido."
            return
        }

        Empresa empresaSelecionada = empresas[numEmpresa]

        print "Descrição da vaga: "
        String descricao = input.nextLine()

        print "Competências necessárias (separadas por vírgula): "
        List<String> competencias = input.nextLine().split(",").collect { it.trim() }

        Vaga novaVaga = new Vaga(
                empresaSelecionada.nome,
                descricao,
                competencias
        )
        novaVaga.empresaId = empresaSelecionada.id

        vagaDAO.inserir(novaVaga)
        println "✓ Vaga cadastrada com sucesso!"
    }

    void listar() {
        println "\n### LISTA DE VAGAS ###"

        List<Vaga> vagas = listarTodas()

        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada."
            return
        }

        vagas.eachWithIndex { Vaga vaga, int i ->
            println "\n${i + 1}. Vaga na empresa: ${vaga.nomeEmpresa}"
            vaga.exibirInfo()
            println "-" * 50
        }
    }
}

