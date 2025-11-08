package com.linketinder.service

import com.linketinder.dao.CandidatoDAO
import com.linketinder.dao.EnderecoDAO
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.model.Endereco
import com.linketinder.view.CandidatoFormatador
import com.linketinder.view.IFormatador
import groovy.transform.CompileStatic
import java.time.LocalDate

@CompileStatic
class CandidatoService {
    private CandidatoDAO candidatoDAO
    private EnderecoDAO enderecoDAO
    private IFormatador<Candidato> formatador

    CandidatoService() {
        this.candidatoDAO = new CandidatoDAO()
        this.enderecoDAO = new EnderecoDAO()
        this.formatador = new CandidatoFormatador()
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

    String formatarCandidato(Candidato candidato) {
        return formatador.formatar(candidato)
    }

    String formatarLista(List<Candidato> candidatos) {
        if (candidatos.isEmpty()) {
            return "Nenhum candidato cadastrado."
        }

        StringBuilder resultado = new StringBuilder()
        candidatos.eachWithIndex { Candidato cand, int i ->
            resultado.append("\n${i + 1}. ${cand.nome}\n")
            resultado.append(formatador.formatar(cand))
            resultado.append("\n")
        }
        return resultado.toString()
    }
}

