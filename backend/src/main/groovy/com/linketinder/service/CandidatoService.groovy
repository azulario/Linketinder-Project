package com.linketinder.service

import com.linketinder.dao.CandidatoDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.model.Endereco
import groovy.transform.CompileStatic
import java.time.LocalDate

@CompileStatic
class CandidatoService {
    private CandidatoDAO candidatoDAO
    private EnderecoDAO enderecoDAO

    CandidatoService() {
        this.candidatoDAO = new CandidatoDAO()
        this.enderecoDAO = new EnderecoDAO()
    }

    Candidato cadastrar(CandidatoDTO dto) {

        Endereco endereco = new Endereco(dto.pais, dto.estado, dto.cidade, dto.cep)
        Integer enderecoId = enderecoDAO.buscarOuCriar(endereco)

        LocalDate dataDeNascimento = LocalDate.parse(dto.dataDeNascimento)

        Candidato candidato = new Candidato(
            dto.nome,
            dto.sobrenome,
            dto.email,
            dto.cpf,
            dataDeNascimento,
            dto.descricao,
            dto.competencias
        )
        candidato.enderecoId = enderecoId

        candidatoDAO.inserir(candidato)

        return candidato
    }

    List<Candidato> listarTodos() {
        return candidatoDAO.listar()
    }

    Candidato buscarPorId(Integer id) {
        return candidatoDAO.buscarPorId(id)
    }

    // DEPRECATED - Será removido após migração completa para MVC
    @Deprecated
    void cadastrar(Scanner input) {
        println "\n### CADASTRO DE CANDIDATO ###"

        print "Nome: "
        String nome = input.nextLine()

        print "Sobrenome: "
        String sobrenome = input.nextLine()

        print "Email: "
        String email = input.nextLine()

        print "CPF: "
        String cpf = input.nextLine()

        print "Data de Nascimento (AAAA-MM-DD): "
        LocalDate dataDeNascimento = LocalDate.parse(input.nextLine())

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
        List<String> competencias = input.nextLine().split(",").collect { it.trim() }

        Endereco endereco = new Endereco(pais, estado, cidade, cep)
        Integer enderecoId = enderecoDAO.buscarOuCriar(endereco)

        Candidato novoCandidato = new Candidato(
                nome,
                sobrenome,
                email,
                cpf,
                dataDeNascimento,
                descricao,
                competencias
        )
        novoCandidato.enderecoId = enderecoId

        candidatoDAO.inserir(novoCandidato)
        println "✓ Candidato cadastrado com sucesso!"
    }

    @Deprecated
    void listar() {
        println "\n### LISTA DE CANDIDATOS ###"

        List<Candidato> candidatos = listarTodos()

        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado."
            return
        }

        candidatos.eachWithIndex { Candidato candidato, int i ->
            println "\n${i + 1}. ${candidato.nome} ${candidato.sobrenome}"
            println "Email: ${candidato.email}"
            println "CPF: ${candidato.cpf}"
            println "Idade: ${candidato.idade} anos"
            println "Competências: ${candidato.competencias.join(', ')}"
            println "-" * 50
        }
    }
}

