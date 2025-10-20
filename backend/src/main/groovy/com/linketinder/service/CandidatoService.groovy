package com.linketinder.service

import com.linketinder.dao.CandidatoDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.model.Candidato
import com.linketinder.model.Endereco

import java.time.LocalDate

class CandidatoService {
    private CandidatoDAO candidatoDAO
    private EnderecoDAO enderecoDAO

    CandidatoService() {
        this.candidatoDAO = new CandidatoDAO()
        this.enderecoDAO = new EnderecoDAO()
    }

    List<Candidato> listarTodos() {
        return candidatoDAO.listar()
    }

    void cadastrar(Scanner input) {
        println "\n### CADASTRO DE CANDIDATO ###"

        print "Nome: "
        String nome = input.nextLine()

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
                nome, email, cpf, dataDeNascimento,
                descricao, competencias
        )
        novoCandidato.enderecoId = enderecoId

        candidatoDAO.inserir(novoCandidato)
        println "✓ Candidato cadastrado com sucesso!"
    }

    void listar() {
        println "\n### LISTA DE CANDIDATOS ###"

        List<Candidato> candidatos = listarTodos()

        if (candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado."
            return
        }

        candidatos.eachWithIndex { Candidato candidato, int i ->
            println "\n${i + 1}. ${candidato.nome}"
            candidato.exibirInfo()
            println "-" * 50
        }
    }
}

