package com.linketinder.servlet

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.linketinder.dto.EmpresaDTO
import com.linketinder.model.Empresa
import com.linketinder.service.EmpresaService
import com.linketinder.util.LocalDateAdapter
import com.linketinder.util.LocalDateTimeAdapter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalDateTime

class EmpresaServletSpec extends Specification {

    EmpresaServlet empresaServlet
    EmpresaService empresaService
    HttpServletRequest request
    HttpServletResponse response
    Gson gson
    StringWriter stringWriter
    PrintWriter writer

    def setup() {
        empresaService = Mock(EmpresaService)
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

        empresaServlet = new EmpresaServlet()
        empresaServlet.empresaService = empresaService
        empresaServlet.gson = gson

        response.getWriter() >> writer
    }

    def cleanup() {
        writer.flush()
    }

    // ==================== TESTES DE GET ====================

    def "deve listar todas as empresas quando o pathInfo for null"() {
        given: "uma lista de empresas mockada"
        def empresas = [
                new Empresa(
                        nome: "Tech Corp",
                        email: "contato@techcorp.com",
                        cnpj: "12345678000190",
                        descricao: "Empresa de tecnologia"
                ),
                new Empresa(
                        nome: "Inovação SA",
                        email: "contato@inovacao.com",
                        cnpj: "98765432000110",
                        descricao: "Empresa de inovação"
                )
        ]
        empresas[0].id = 1
        empresas[1].id = 2

        when: "GET /api/empresas (sem ID)"
        empresaServlet.doGet(request, response)

        then: "o serviço deve listar todas as empresas"
        1 * request.getPathInfo() >> null
        1 * empresaService.listarTodas() >> empresas
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar a lista de empresas em JSON"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 2
        resultado.empresas.size() == 2
        resultado.empresas[0].nome == "Tech Corp"
        resultado.empresas[1].nome == "Inovação SA"
    }

    def "deve retornar msg quando não há empresas cadastradas"() {
        given: "service retorna lista vazia"
        empresaService.listarTodas() >> []

        when: "GET /api/empresas"
        empresaServlet.doGet(request, response)

        then: "deve retornar status 200 com lista vazia"
        1 * request.getPathInfo() >> null
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "resultado deve conter lista vazia"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.total == 0
        resultado.empresas.isEmpty()
    }

    def "deve retornar 400 quando o ID da empresa é inválido"() {
        when: "GET /api/empresas/abc (ID não numérico)"
        empresaServlet.doGet(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getPathInfo() >> "/abc"
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("ID inválido")
    }

    def "deve buscar empresa por ID quando pathInfo contém ID válido"() {
        given: "uma empresa mockada"
        def empresa = new Empresa(
                nome: "Tech Corp",
                email: "contato@techcorp.com",
                cnpj: "12345678000190",
                descricao: "Empresa de tecnologia"
        )
        empresa.id = 1
        empresa.enderecoId = 10

        when: "GET /api/empresas/1"
        empresaServlet.doGet(request, response)

        then: "deve chamar o service com o ID correto"
        1 * request.getPathInfo() >> "/1"
        1 * empresaService.buscarPorId(1) >> empresa
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "deve retornar os dados da empresa"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == true
        resultado.empresa.id == 1
        resultado.empresa.nome == "Tech Corp"
    }

    def "deve retornar 404 quando empresa não existe"() {
        given: "service retorna null"
        empresaService.buscarPorId(999) >> null

        when: "GET /api/empresas/999"
        empresaServlet.doGet(request, response)

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

    def "deve cadastrar nova empresa com dados válidos"() {
        given: "um DTO válido e resultado de sucesso"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "descricao": "Empresa de tecnologia",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567"
            }
        '''

        def empresa = new Empresa(
                nome: "Tech Corp",
                email: "contato@techcorp.com",
                cnpj: "12345678000190",
                descricao: "Empresa de tecnologia"
        )
        empresa.id = 1
        empresa.enderecoId = 10

        when: "POST /api/empresas com dados válidos"
        empresaServlet.doPost(request, response)

        then: "deve processar o JSON e cadastrar"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * empresaService.cadastrar(_ as EmpresaDTO) >> empresa
        1 * response.setStatus(HttpServletResponse.SC_CREATED)

        and: "deve retornar mensagem de sucesso"
        writer.flush()
        def jsonResponse = stringWriter.toString()
        jsonResponse.length() > 0

        def resultadoJson = gson.fromJson(jsonResponse, Map)
        resultadoJson.sucesso == true
        resultadoJson.mensagem == "Empresa cadastrada com sucesso"
        resultadoJson.empresa != null
        resultadoJson.empresa.id == 1
        resultadoJson.empresa.nome == "Tech Corp"
    }

    def "deve retornar 400 quando nome está faltando"() {
        given: "JSON sem nome"
        def jsonBody = '''{"email": "contato@techcorp.com", "cnpj": "12345678000190"}'''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando email está faltando"() {
        given: "JSON sem email"
        def jsonBody = '''{"nome": "Tech Corp", "cnpj": "12345678000190"}'''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando cnpj está faltando"() {
        given: "JSON sem cnpj"
        def jsonBody = '''{"nome": "Tech Corp", "email": "contato@techcorp.com"}'''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando pais está faltando"() {
        given: "JSON sem pais"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando estado está faltando"() {
        given: "JSON sem estado"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "cidade": "São Paulo",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando cidade está faltando"() {
        given: "JSON sem cidade"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "estado": "SP",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

    def "deve retornar 400 quando cep está faltando"() {
        given: "JSON sem cep"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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
        def jsonBody = '''{"nome": "Tech Corp"'''

        when: "POST /api/empresas com JSON inválido"
        empresaServlet.doPost(request, response)

        then: "deve retornar 400 Bad Request"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * response.setStatus(HttpServletResponse.SC_BAD_REQUEST)

        and: "mensagem de erro deve estar presente"
        def jsonResponse = stringWriter.toString()
        def resultado = gson.fromJson(jsonResponse, Map)
        resultado.sucesso == false
        resultado.erro.contains("JSON inválido")
    }

    def "deve retornar 500 quando CNPJ é inválido"() {
        given: "JSON com CNPJ inválido"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "123",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

        then: "service lança exceção de validação"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * empresaService.cadastrar(_ as EmpresaDTO) >> {
            throw new IllegalArgumentException("CNPJ inválido")
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

    def "deve retornar 500 quando email é inválido"() {
        given: "JSON com email inválido"
        def jsonBody = '''
            {
                "nome": "Tech Corp",
                "email": "emailinvalido",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

        then: "service lança exceção de validação"
        1 * request.getReader() >> new BufferedReader(new StringReader(jsonBody))
        1 * empresaService.cadastrar(_ as EmpresaDTO) >> {
            throw new IllegalArgumentException("Email inválido")
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
        empresaService.listarTodas() >> { throw new RuntimeException("Erro de banco") }

        when: "GET /api/empresas"
        empresaServlet.doGet(request, response)

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
                "nome": "Tech Corp",
                "email": "contato@techcorp.com",
                "cnpj": "12345678000190",
                "pais": "Brasil",
                "estado": "SP",
                "cidade": "São Paulo",
                "cep": "01234-567",
                "descricao": "Empresa de tecnologia"
            }
        '''
        empresaService.cadastrar(_ as EmpresaDTO) >> {
            throw new RuntimeException("Erro de conexão")
        }

        when: "POST /api/empresas"
        empresaServlet.doPost(request, response)

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

