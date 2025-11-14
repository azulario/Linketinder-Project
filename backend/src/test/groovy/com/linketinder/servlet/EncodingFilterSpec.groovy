package com.linketinder.servlet

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import spock.lang.Specification


class EncodingFilterSpec extends Specification {

    def "deve configurar encoding UTF-8 e headers CORS corretamente"() {
        given: "um filtro de encoding e mocks de request, response e filterChain"
        def filter = new EncodingFilter()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def filterChain = Mock(FilterChain)

        when: "o filtro é executado com uma requisição GET"
        filter.doFilter(request, response, filterChain)

        then: "deve configurar o encoding e content type corretamente"
        1 * request.setCharacterEncoding("UTF-8")
        1 * response.setCharacterEncoding("UTF-8")
        1 * response.setContentType("application/json; charset=UTF-8")

        and: "deve configurar os headers CORS corretamente"
        1 * response.setHeader("Access-Control-Allow-Origin", "*")
        1 * response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        1 * response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")

        and: "deve verificar o método da requisição e continuar a cadeia"
        1 * request.getMethod() >> "GET"
        1 * filterChain.doFilter(request, response)
    }

    def "deve retornar OK para requisições OPTIONS sem continuar a cadeia"() {
        given: "um filtro de encoding e mocks configurados"
        def filter = new EncodingFilter()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def filterChain = Mock(FilterChain)

        when: "o filtro é executado com uma requisição OPTIONS (preflight CORS)"
        filter.doFilter(request, response, filterChain)

        then: "deve configurar os encodings e headers"
        1 * request.setCharacterEncoding("UTF-8")
        1 * response.setCharacterEncoding("UTF-8")
        1 * response.setContentType("application/json; charset=UTF-8")
        1 * response.setHeader("Access-Control-Allow-Origin", "*")
        1 * response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        1 * response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")

        and: "deve retornar status 200 OK"
        1 * request.getMethod() >> "OPTIONS"
        1 * response.setStatus(HttpServletResponse.SC_OK)

        and: "não deve continuar a cadeia de filtros"
        0 * filterChain.doFilter(_, _)
    }

    def "deve funcionar corretamente com diferentes métodos HTTP"() {
        given: "um filtro de encoding"
        def filter = new EncodingFilter()
        def request = Mock(HttpServletRequest)
        def response = Mock(HttpServletResponse)
        def filterChain = Mock(FilterChain)

        when: "o filtro é executado com método #metodo"
        filter.doFilter(request, response, filterChain)

        then: "deve configurar corretamente e continuar a cadeia"
        1 * request.setCharacterEncoding("UTF-8")
        1 * response.setCharacterEncoding("UTF-8")
        1 * response.setContentType("application/json; charset=UTF-8")
        1 * request.getMethod() >> metodo
        1 * filterChain.doFilter(request, response)

        where: "os métodos testados são"
        metodo << ["GET", "POST", "PUT", "DELETE", "PATCH"]
    }

}