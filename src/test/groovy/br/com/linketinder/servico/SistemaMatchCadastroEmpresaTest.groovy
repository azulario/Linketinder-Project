package br.com.linketinder.servico

import br.com.linketinder.dominio.Empresa
import spock.lang.Specification

/**
 * Autor: Azulario
 * Teste TDD para cadastro de nova empresa no CadastroService
 */
class CadastroServiceCadastroEmpresaTest extends Specification {
    def "deve adicionar nova empresa à lista"() {
        given:
        def empresas = []
        def empresa = new Empresa("emp-99", "NovaEmpresa", "nova@empresa.com", "9999-9999", ["Java"])
        def service = new CadastroService()

        when:
        boolean resultado = service.adicionarEmpresa(empresa, empresas)

        then:
        resultado
        empresas.contains(empresa)
    }

    def "não deve adicionar empresa com id já existente"() {
        given:
        def empresaExistente = new Empresa("emp-1", "EmpresaX", "x@x.com", "1111-1111", ["Groovy"])
        def empresas = [empresaExistente]
        def empresaDuplicada = new Empresa("emp-1", "EmpresaY", "y@y.com", "2222-2222", ["Java"])
        def service = new CadastroService()

        when:
        boolean resultado = service.adicionarEmpresa(empresaDuplicada, empresas)

        then:
        !resultado
        empresas.size() == 1
        empresas[0].razaoSocial == "EmpresaX"
    }
}
