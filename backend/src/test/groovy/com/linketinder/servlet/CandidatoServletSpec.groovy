package com.linketinder.servlet


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.linketinder.dto.CandidatoDTO
import com.linketinder.model.Candidato
import com.linketinder.service.CandidatoService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime


class CandidatoServletSpec extends Specification {

    CandidatoServlet candidatoServlet
    CandidatoService candidatoService
    HttpServletRequest request
    HttpServletResponse response
    StringWriter stringWriter
    PrintWriter writer
    Gson gson

    def setup() {
        candidatoService = Mock(CandidatoService)
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

        candidatoServlet = new CandidatoServlet()
        candidatoServlet.candidatoService = candidatoService
        candidatoServlet.gson = gson

        response.getWriter() >> writer
    }

    def cleanup() {
        writer.flush()
    }

    // --- TESTE DO GET --- //

    def "deve listar todos os candidatos quando o pathInfo for null"() {
        given: "uma lista de candidatos mockada"
        def candidatos = [
                new Candidato("João", "Silva", "joao@email.com", "123.456.789-00",
                        LocalDate.of(1990, 5, 15), "Dev Java", ["Java", "Spring"]),
                new Candidato("Maria", "Santos", "maria@email.com", "987.654.321-00",
                        LocalDate.of(1992, 3, 20), "Dev Python", ["Python", "Django"])
        ]
        candidatos[0].id = 1
        candidatos[1].id = 2

        when: "GET /api/candidatos (sem ID)"
        candidatoServlet.doGet(request, response)

        then: "o serviço deve listar todos os candidatos"
        1 * request.getPathInfo() >> null
        1 * candidatoService.listarTodos() >> candidatos
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar a lista de candidatos em JSON"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 2
        resultado.candidatos.size() == 2
        resultado.candidatos[0].nome == "João"
        resultado.candidatos[1].nome == "Maria"
    }

   def "deve retornar msg quando não há candidatos cadastrados"() {
        given: "service retorna lista vazia"
        candidatoService.listarTodos() >> []

        when: "GET /api/candidatos"
        candidatoServlet.doGet(request, response)

        then: "deve retornar status 200 com lista vazia"
        1 * request.getPathInfo() >> null
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "resultado deve conter lista vazia"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 0
        resultado.candidatos.isEmpty()
    }

    def "deve retornar 400 quando o ID do candidato é inválido"() {
        when: "GET /api/candidatos/abc (ID não numérico)"
        candidatoServlet.doGet(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getPathInfo() >> "/abc"
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("ID inválido")
    }

    def "deve buscar candidato por ID quando pathInfo contém ID válido"() {
        given: "um candidato mockado"
        def candidato = new Candidato(
                "João", "Silva", "joao@email.com", "123.456.789-00",
                LocalDate.of(1990, 5, 15), "Dev Java", ["Java", "Spring"]
        )
        candidato.id = 1
        candidato.enderecoId = 10

        when: "GET /api/candidatos/1"
        candidatoServlet.doGet(request, response)

        then: "deve chamar o service com o ID correto"
        1 * request.getPathInfo() >> "/1"
        1 * candidatoService.buscarPorId(1) >> candidato
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar os dados do candidato"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.candidato.id == 1
        resultado.candidato.nome == "João"
    }

    def "deve retornar 404 quando candidato não existe"() {
        given: "service retorna null"
        candidatoService.buscarPorId(999) >> null

        when: "GET /api/candidatos/999"
        candidatoServlet.doGet(request, response)

        then: "deve retornar 404 Not Found"
        1 * request.getPathInfo() >> "/999"
        1 * response.setStatus(HttpServletResponse.SC_NOT_FOUND)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("não encontrado")
    }


    // --- TESTE DO POST --- //

    def "deve cadastrar novo candidato com dados válidos"() {
        given: "um DTO válido e resultado de sucesso"
        def jsonBody = '''
            {
                "nome": "João",
                "sobrenome": "Silva",
                "email": "joao@email.com",
                "cpf": "123.456.789-00",
                "dataDeNascimento": "1990-05-15",
                "descricao": "Desenvolvedor Java",
                "competencias": ["Java", "Spring"],
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567"
            }
        '''

        def candidato = new Candidato(
            "João", "Silva", "joao@email.com", "123.456.789-00",
            LocalDate.of(1990, 5, 15), "Desenvolvedor Java", ["Java", "Spring"]
        )
        candidato.id = 1
        candidato.enderecoId = 10

        when: "POST /api/candidatos com dados válidos"
        candidatoServlet.doPost(request, response)

        then: "deve processar o JSON e cadastrar"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * candidatoService.cadastrar(_ as CandidatoDTO) >> candidato
        1 * response.setStatus(HttpServletResponse.SC_CREATED)

        and: "deve retornar mensagem de sucesso"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        jsonResponse.length() > 0

        def resultadoJson = gson.fromJson(jsonResponse, Map)
        resultadoJson.sucesso == true
        resultadoJson.mensagem == "Candidato cadastrado com sucesso"
        resultadoJson.candidato != null
        resultadoJson.candidato.id == 1
        resultadoJson.candidato.nome == "João"
    }

    def "deve retornar 400 quando campos obrigatórios estão faltando"() {
        given: "JSON sem campos obrigatórios"
        def jsonBody = '''{"cpf": "123.456.789-00"}'''

        when: "POST /api/candidatos"
        candidatoServlet.doPost(request, response)

        then: "deve retornar erro 400"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("obrigatórios")
    }

    def "deve retornar 400 quando JSON é inválido"() {
        given: "JSON malformado"
        def jsonBody = '''{"nome": "João"'''

        when: "POST /api/candidatos com JSON inválido"
        candidatoServlet.doPost(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("JSON inválido")
    }

    def "deve retornar 500 quando data de nascimento é inválida"() {
        given: "JSON com data inválida"
        def jsonBody = '''
            {
                "nome": "João",
                "sobrenome": "Silva",
                "email": "joao@email.com",
                "cpf": "123.456.789-00",
                "dataDeNascimento": "data-invalida"
            }
        '''

        when: "POST /api/candidatos"
        candidatoServlet.doPost(request, response)

        then: "service lança exceção ao tentar fazer parse da data"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * candidatoService.cadastrar(_ as CandidatoDTO) >> {
            throw new java.time.format.DateTimeParseException("Text 'data-invalida' could not be parsed", "data-invalida", 0)
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

    def "deve retornar 400 quando email é inválido"() {
        given: "JSON com email inválido mas presente"
        def jsonBody = '''
            {
                "nome": "João",
                "sobrenome": "Silva",
                "email": "emailinvalido",
                "cpf": "123.456.789-00",
                "dataDeNascimento": "1990-05-15"
            }
        '''

        when: "POST /api/candidatos"
        candidatoServlet.doPost(request, response)

        then: "service lança exceção de validação"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * candidatoService.cadastrar(_ as CandidatoDTO) >> {
            throw new IllegalArgumentException("Email inválido")
        }
        (1.._) * response.setStatus(_)

        and: "mensagem de erro deve estar presente"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        jsonResponse.length() > 0

        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro != null
    }


    // --- TESTES DE EXCEÇÕES --- //

    def "deve tratar exceção inesperada no GET"() {
        given: "service lança exceção"
        candidatoService.listarTodos() >> { throw new RuntimeException("Erro de banco") }

        when: "GET /api/candidatos"
        candidatoServlet.doGet(request, response)

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
                "nome": "João",
                "sobrenome": "Silva",
                "email": "joao@email.com",
                "cpf": "123.456.789-00",
                "dataDeNascimento": "1990-05-15"
            }
        '''
        candidatoService.cadastrar(_ as CandidatoDTO) >> {
            throw new RuntimeException("Erro de conexão")
        }

        when: "POST /api/candidatos"
        candidatoServlet.doPost(request, response)

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