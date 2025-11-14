package com.linketinder.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.service.CandidatoService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime

class CandidatoServlet extends HttpServlet {

    CandidatoService candidatoService
    Gson gson

    @Override
    void init() throws ServletException {
        candidatoService = new CandidatoService()
        gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .serializeNulls()
            .setPrettyPrinting()
            .create()
        println "CandidatoServlet inicializado com sucesso."
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo()

            if (pathInfo ==  null || pathInfo == "/") {
                listarTodos(response)
            } else {
                String idStr = pathInfo.substring(1)
                Integer id = Integer.parseInt(idStr)
                buscarPorId(id, response)
            }

        } catch (NumberFormatException e) {
            enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                    "ID inválido.Deve ser um número inteiro.")
        } catch (Exception e) {
            enviarErro(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Erro interno do servidor: ${e.message}")
        }
    }


   @Override
    protected  void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
       try {
           String body = request.reader.text

           CandidatoDTO dto = gson.fromJson(body, CandidatoDTO.class)

           if (!dto.nome || !dto.email) {
               enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                       "Nome e email são obrigatórios.")
               return
           }

           Candidato candidato = candidatoService.cadastrar(dto)

           Map<String, Object> resultado = [
               sucesso: true,
               mensagem: "Candidato cadastrado com sucesso",
               candidato: candidato
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

    private void listarTodos(HttpServletResponse response) {
        List<Candidato> candidatos = candidatoService.listarTodos()

        Map<String, Object> resultado = [
            sucesso: true,
            total: candidatos ? candidatos.size() : 0,
            candidatos: candidatos ?: []
        ]

        response.status = HttpServletResponse.SC_OK
        response.writer.write(gson.toJson(resultado))
    }

    private void buscarPorId(Integer id, HttpServletResponse response) {
        Candidato candidato = candidatoService.buscarPorId(id)

        if (candidato) {
            Map<String, Object> resultado = [
                sucesso: true,
                candidato: candidato
            ]
            response.status = HttpServletResponse.SC_OK
            response.writer.write(gson.toJson(resultado))
        } else {
            enviarErro(response, HttpServletResponse.SC_NOT_FOUND,
                    "Candidato com ID ${id} não encontrado.")
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
        println "CandidatoServlet destruído."
    }
}
