package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.VagaDAO
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga
import groovy.transform.CompileStatic

@CompileStatic
class VagaService {
    private VagaDAO vagaDAO
    private EmpresaDAO empresaDAO

    VagaService() {
        this.vagaDAO = new VagaDAO()
        this.empresaDAO = new EmpresaDAO()
    }

    Vaga cadastrar(VagaDTO dto) {
        // Validar se empresa existe
        Empresa empresa = empresaDAO.buscarPorId(dto.empresaId)
        if (!empresa) {
            throw new IllegalArgumentException("Empresa não encontrada com ID: ${dto.empresaId}")
        }

        // Criar vaga
        Vaga vaga = new Vaga(dto.titulo, dto.descricao, dto.empresaId)
        vaga.competencias = dto.competencias

        // Inserir no banco
        vagaDAO.inserir(vaga)

        return vaga
    }

    List<Vaga> listarTodas() {
        return vagaDAO.listar()
    }

    Vaga buscarPorId(Integer id) {
        return vagaDAO.buscarPorId(id)
    }

    List<Vaga> listarPorEmpresa(Integer empresaId) {
        return vagaDAO.listarPorEmpresa(empresaId)
    }

    // DEPRECATED - Será removido após migração completa para MVC
    @Deprecated
    void cadastrar(Scanner input, EmpresaService empresaService) {
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

        print "Título da vaga: "
        String titulo = input.nextLine()

        print "Descrição da vaga: "
        String descricao = input.nextLine()

        print "Competências necessárias (separadas por vírgula): "
        List<String> competencias = input.nextLine().split(",").collect { it.trim() }

        Vaga novaVaga = new Vaga(titulo, descricao, empresaSelecionada.id)
        novaVaga.competencias = competencias

        vagaDAO.inserir(novaVaga)
        println "✓ Vaga cadastrada com sucesso!"
    }

    // DEPRECATED - Será removido após migração completa para MVC
    @Deprecated
    void listar() {
        println "\n### LISTA DE VAGAS ###"

        List<Vaga> vagas = listarTodas()

        if (vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada."
            return
        }

        vagas.eachWithIndex { Vaga vaga, int i ->
            println "\n${i + 1}. Vaga: ${vaga.titulo}"
            println "Descrição: ${vaga.descricao}"
            println "Empresa ID: ${vaga.empresaId}"
            println "Competências: ${vaga.competencias?.join(', ') ?: 'Nenhuma'}"
            println "-" * 50
        }
    }
}

