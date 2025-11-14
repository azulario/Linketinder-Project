package com.linketinder.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.service.EmpresaService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDate
import java.time.LocalDateTime

class EmpresaServlet extends HttpServlet {

    EmpresaService empresaService
    Gson gson

    @Override
    void init() throws ServletException {
        empresaService = new EmpresaService()
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .setPrettyPrinting()
                .create()
        println "EmpresaServlet inicializado com sucesso."
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

            EmpresaDTO dto = gson.fromJson(body, EmpresaDTO.class)

            // Validação dos campos obrigatórios
            if (!dto.nome || !dto.email || !dto.cnpj || !dto.pais ||
                !dto.estado || !dto.cidade || !dto.cep || !dto.descricao) {
                enviarErro(response, HttpServletResponse.SC_BAD_REQUEST,
                        "Nome, email, CNPJ, país, estado, cidade, CEP e descrição são obrigatórios.")
                return
            }

            Empresa empresa = empresaService.cadastrar(dto)

            Map<String, Object> resultado = [
                    sucesso: true,
                    mensagem: "Empresa cadastrada com sucesso",
                    empresa: empresa
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
        List<Empresa> empresas = empresaService.listarTodas()

        Map<String, Object> resultado = [
                sucesso: true,
                total: empresas ? empresas.size() : 0,
                empresas: empresas ?: []
        ]

        response.status = HttpServletResponse.SC_OK
        response.writer.write(gson.toJson(resultado))
    }

    private void buscarPorId(Integer id, HttpServletResponse response) {
        Empresa empresa = empresaService.buscarPorId(id)

        if (empresa) {
            Map<String, Object> resultado = [
                    sucesso: true,
                    empresa: empresa
            ]
            response.status = HttpServletResponse.SC_OK
            response.writer.write(gson.toJson(resultado))
        } else {
            enviarErro(response, HttpServletResponse.SC_NOT_FOUND,
                    "Empresa com ID ${id} não encontrada.")
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
        println "EmpresaServlet destruído."
    }
}
