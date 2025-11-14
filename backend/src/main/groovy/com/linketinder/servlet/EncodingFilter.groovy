package com.linketinder.servlet

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class EncodingFilter implements Filter {
    @Override
    void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest
        HttpServletResponse response = (HttpServletResponse) servletResponse

        request.setCharacterEncoding("UTF-8")
        response.setCharacterEncoding("UTF-8")
        response.setContentType("application/json; charset=UTF-8")

        httpResponse.setHeader("Access-Control-Allow-Origin", "*")
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization")

        if (httpRequest.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpResponse.setStatus(HttpServletResponse.SC_OK)
            return
        }

        filterChain.doFilter(request, response)
    }

    @Override
    void destroy() {

    }
}
