package com.linketinder.service

import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.VagaDAO
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Empresa
import com.linketinder.view.IFormatador
import com.linketinder.view.VagaFormatador
import com.linketinder.model.Vaga
import groovy.transform.CompileStatic

@CompileStatic
class VagaService {
    private final VagaDAO vagaDAO
    private final EmpresaDAO empresaDAO
    private final IFormatador<Vaga> formatador

    VagaService() {
        this.vagaDAO = new VagaDAO()
        this.empresaDAO = new EmpresaDAO()
        this.formatador = new VagaFormatador()
    }

    VagaService(VagaDAO vagaDAO, EmpresaDAO empresaDAO) {
        this.vagaDAO = vagaDAO
        this.empresaDAO = empresaDAO
        this.formatador = new VagaFormatador()
    }

    Vaga cadastrar(VagaDTO dto) {

        Empresa empresa = empresaDAO.buscarPorId(dto.empresaId)
        if (!empresa) {
            throw new IllegalArgumentException("Empresa não encontrada com ID: ${dto.empresaId}")
        }


        Vaga vaga = new Vaga(dto.titulo, dto.descricao, dto.empresaId)
        vaga.competencias = dto.competencias


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

    String formatarVaga(Vaga vaga) {
        return formatador.formatar(vaga)
    }

    String formatarLista(List<Vaga> vagas) {
        if (vagas.isEmpty()) {
            return "Nenhuma vaga cadastrada."
        }

        StringBuilder resultado = new StringBuilder()
        vagas.eachWithIndex { Vaga vaga, int index ->
            resultado.append("\n${index + 1}.\n")
            resultado.append(formatarVaga(vaga))
            resultado.append("-" * 50)
            resultado.append("\n")
        }
        return resultado.toString()
    }
}

