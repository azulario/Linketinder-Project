package com.linketinder.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.linketinder.dto.VagaDTO
import com.linketinder.model.Vaga
import com.linketinder.service.VagaService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class VagaServletSpec extends Specification {

    VagaServlet vagaServlet
    VagaService vagaService
    HttpServletRequest request
    HttpServletResponse response
    Gson gson
    StringWriter stringWriter
    PrintWriter writer

    def setup() {
        vagaService = Mock(VagaService)
        request = Mock(HttpServletRequest)
        response = Mock(HttpServletResponse)
        stringWriter = new StringWriter()
        writer = new PrintWriter(stringWriter)
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .serializeNulls()
                .setPrettyPrinting()
                .create()

        vagaServlet = new VagaServlet()
        vagaServlet.vagaService = vagaService
        vagaServlet.gson = gson

        response.getWriter() >> writer
    }

    def cleanup() {
        writer.flush()
    }

    // ==================== TESTES DE GET ====================

    def "deve listar todas as vagas quando o pathInfo for null"() {
        given: "uma lista de vagas mockada"
        def vaga1 = new Vaga("Desenvolvedor Java", "Vaga para desenvolvedor Java Sênior", 1)
        vaga1.id = 1
        vaga1.competencias = ["Java", "Spring"]

        def vaga2 = new Vaga("Desenvolvedor Python", "Vaga para desenvolvedor Python", 2)
        vaga2.id = 2
        vaga2.competencias = ["Python", "Django"]

        def vagas = [vaga1, vaga2]

        when: "GET /api/vagas (sem ID)"
        vagaServlet.doGet(request, response)

        then: "o serviço deve listar todas as vagas"
        1 * request.getPathInfo() >> null
        1 * vagaService.listarTodas() >> vagas
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar a lista de vagas em JSON"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 2
        resultado.vagas.size() == 2
    }

    def "deve retornar msg quando não há vagas cadastradas"() {
        given: "service retorna lista vazia"
        vagaService.listarTodas() >> []

        when: "GET /api/vagas"
        vagaServlet.doGet(request, response)

        then: "deve retornar status 200 com lista vazia"
        1 * request.getPathInfo() >> null
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "resultado deve conter lista vazia"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 0
        resultado.vagas.isEmpty()
    }

    def "deve retornar 400 quando o ID da vaga é inválido"() {
        when: "GET /api/vagas/abc (ID não numérico)"
        vagaServlet.doGet(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getPathInfo() >> "/abc"
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("ID inválido")
    }

    def "deve buscar vaga por ID quando pathInfo contém ID válido"() {
        given: "uma vaga mockada"
        def vaga = new Vaga("Desenvolvedor Java", "Vaga para desenvolvedor Java Sênior", 1)
        vaga.id = 1
        vaga.competencias = ["Java", "Spring"]

        when: "GET /api/vagas/1"
        vagaServlet.doGet(request, response)

        then: "deve chamar o service com o ID correto"
        1 * request.getPathInfo() >> "/1"
        1 * vagaService.buscarPorId(1) >> vaga
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar os dados da vaga"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.vaga.id == 1
        resultado.vaga.titulo == "Desenvolvedor Java"
    }

    def "deve retornar 404 quando vaga não existe"() {
        given: "service retorna null"
        vagaService.buscarPorId(999) >> null

        when: "GET /api/vagas/999"
        vagaServlet.doGet(request, response)

        then: "deve retornar 404 Not Found"
        1 * request.getPathInfo() >> "/999"
        1 * response.setStatus(HttpServletResponse.SC_NOT_FOUND)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("não encontrada")
    }

    // ==================== TESTES DE POST ====================

    def "deve cadastrar nova vaga com dados válidos"() {
        given: "um DTO válido e resultado de sucesso"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 1,
                "competencias": ["Java", "Spring", "PostgreSQL"]
            }
        '''

        def vaga = new Vaga("Desenvolvedor Java", "Vaga para desenvolvedor Java Sênior", 1)
        vaga.id = 1
        vaga.competencias = ["Java", "Spring", "PostgreSQL"]

        when: "POST /api/vagas com dados válidos"
        vagaServlet.doPost(request, response)

        then: "deve processar o JSON e cadastrar"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * vagaService.cadastrar(_ as VagaDTO) >> vaga
        1 * response.setStatus(HttpServletResponse.SC_CREATED)

        and: "deve retornar mensagem de sucesso"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        jsonResponse.length() > 0

        def resultadoJson = gson.fromJson(jsonResponse, Map)
        resultadoJson.sucesso == true
        resultadoJson.mensagem == "Vaga cadastrada com sucesso"
        resultadoJson.vaga != null
        resultadoJson.vaga.id == 1
        resultadoJson.vaga.titulo == "Desenvolvedor Java"
    }

    def "deve retornar 400 quando titulo está faltando"() {
        given: "JSON sem titulo"
        def jsonBody = '''
            {
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 1,
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando descricao está faltando"() {
        given: "JSON sem descricao"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "empresaId": 1,
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando empresaId está faltando"() {
        given: "JSON sem empresaId"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando competencias está faltando"() {
        given: "JSON sem competencias"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 1
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando competencias está vazia"() {
        given: "JSON com competencias vazia"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 1,
                "competencias": []
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando empresaId é zero"() {
        given: "JSON com empresaId zero"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 0,
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando empresaId é negativo"() {
        given: "JSON com empresaId negativo"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": -1,
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatório")
    }

    def "deve retornar 400 quando JSON é inválido"() {
        given: "JSON malformado"
        def jsonBody = '''{"titulo": "Desenvolvedor Java"'''

        when: "POST /api/vagas com JSON inválido"
        vagaServlet.doPost(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("JSON inválido")
    }

    def "deve retornar 500 quando empresa não existe"() {
        given: "JSON com empresaId de empresa inexistente"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 999,
                "competencias": ["Java", "Spring"]
            }
        '''

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "service lança exceção"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * vagaService.cadastrar(_ as VagaDTO) >> {
            throw new IllegalArgumentException("Empresa não encontrada")
        }
        1 * response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        jsonResponse.length() > 0

        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro != null
    }

    // ==================== TESTES DE EXCEÇÕES ====================

    def "deve tratar exceção inesperada no GET"() {
        given: "service lança exceção"
        vagaService.listarTodas() >> { throw new RuntimeException("Erro de banco") }

        when: "GET /api/vagas"
        vagaServlet.doGet(request, response)

        then: "deve retornar erro 500"
        1 * request.getPathInfo() >> null
        1 * response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("Erro interno do servidor")
    }

    def "deve tratar exceção inesperada no POST"() {
        given: "service lança exceção"
        def jsonBody = '''
            {
                "titulo": "Desenvolvedor Java",
                "descricao": "Vaga para desenvolvedor Java Sênior",
                "empresaId": 1,
                "competencias": ["Java", "Spring"]
            }
        '''
        vagaService.cadastrar(_ as VagaDTO) >> {
            throw new RuntimeException("Erro de conexão")
        }

        when: "POST /api/vagas"
        vagaServlet.doPost(request, response)

        then: "deve retornar erro 500"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("Erro interno do servidor")
    }
}

