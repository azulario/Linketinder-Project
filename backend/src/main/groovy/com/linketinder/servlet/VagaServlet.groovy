package com.linketinder.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import com.linketinder.service.VagaService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDate
import java.time.LocalDateTime

class VagaServlet extends HttpServlet {

    VagaService vagaService
    Gson gson

    @Override
    void init() throws ServletException {
        vagaService = new VagaService()
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .setPrettyPrinting()
                .create()
        println "VagaServlet inicializado com sucesso."
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo == null || pathInfo == "/") {
                listarTodas(response)
            } else {
                String idStr = pathInfo.substring(1)
                Integer id = Integer.parseInt(idStr)
                buscarPorId(id, response)
            }

        } catch (NumberFormatException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                    "ID inválido. Deve ser um número inteiro.")
        } catch (Exception e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro interno do servidor: ${e.message}")
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            String body = request.reader.text

            VagaDTO dto = gson.fromJson(body, VagaDTO.class)

            // Validação dos campos obrigatórios
            if (!dto.titulo || !dto.descricao || dto.empresaId == null ||
                dto.empresaId <= 0 || !dto.competencias || dto.competencias.isEmpty()) {
                enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Título, descrição, empresaId válido e pelo menos uma competência são obrigatórios.")
                return
            }

            Vaga vaga = vagaService.cadastrar(dto)

            Map<String, Object> resultado = [
                    sucesso: true,
                    mensagem: "Vaga cadastrada com sucesso",
                    vaga: vaga
            ]

            response.status = HttpServletResponse.SC_CREATED
            response.writer.write(gson.toJson(resultado))

        } catch (JsonSyntaxException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                    "JSON inválido: ${e.message}")

        } catch (Exception e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro interno do servidor: ${e.message}")
        }
    }

    private void listarTodas(HttpServletResponse response) {
        List<Vaga> vagas = vagaService.listarTodas()

        Map<String, Object> resultado = [
                sucesso: true,
                total: vagas ? vagas.size() : 0,
                vagas: vagas ?: []
        ]

        response.status = HttpServletResponse.SC_OK
        response.writer.write(gson.toJson(resultado))
    }

    private void buscarPorId(Integer id, HttpServletResponse response) {
        Vaga vaga = vagaService.buscarPorId(id)

        if (vaga) {
            Map<String, Object> resultado = [
                    sucesso: true,
                    vaga: vaga
            ]
            response.status = HttpServletResponse.SC_OK
            response.writer.write(gson.toJson(resultado))
        } else {
            enviarErro(response, HttpServletResponse.SC_NOT_FOUND,
                    "Vaga com ID ${id} não encontrada.")
        }
    }

    private void enviarErro(HttpServletResponse response, int status, String mensagem) {
        response.status = status
        Map<String, Object> erro = [
                sucesso: false,
                erro: mensagem
        ]
        response.writer.write(gson.toJson(erro))
    }

    @Override
    void destroy() {
        println "VagaServlet destruído."
    }
}
