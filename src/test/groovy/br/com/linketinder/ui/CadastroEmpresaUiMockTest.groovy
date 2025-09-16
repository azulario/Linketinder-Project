package br.com.linketinder.ui

import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.CadastroService
import spock.lang.Specification

/**
 * Autor: Azulario
 * Teste TDD com mock para simular entrada de dados no cadastro de empresa via UI
 */
class CadastroEmpresaUiMockTest extends Specification {
    def "deve cadastrar nova empresa via UI simulando entrada do usuário"() {
        given:
        def empresas = []
        def entrada = Mock(EntradaUsuario)
        entrada.lerLinha() >>> ["emp-77", "EmpresaMock", "mock@empresa.com", "7777-7777", "Java,Groovy"]
        def cadastroService = new CadastroService()
        def ui = new CadastroEmpresaUi(empresas, cadastroService, entrada)

        when:
        boolean resultado = ui.cadastrarEmpresa()

        then:
        resultado
        empresas.size() == 1
        empresas[0].id == "emp-77"
        empresas[0].razaoSocial == "EmpresaMock"
        empresas[0].email == "mock@empresa.com"
        empresas[0].telefone == "7777-7777"
        empresas[0].competenciasBuscadas == ["Java", "Groovy"]
    }

    def "não deve cadastrar empresa com id duplicado via UI"() {
        given:
        def empresaExistente = new Empresa("emp-1", "EmpresaX", "x@x.com", "1111-1111", ["Java"])
        def empresas = [empresaExistente]
        def entrada = Mock(EntradaUsuario)
        entrada.lerLinha() >>> ["emp-1", "EmpresaY", "y@y.com", "2222-2222", "Groovy"]
        def cadastroService = new CadastroService()
        def ui = new CadastroEmpresaUi(empresas, cadastroService, entrada)

        when:
        boolean resultado = ui.cadastrarEmpresa()

        then:
        !resultado
        empresas.size() == 1
        empresas[0].razaoSocial == "EmpresaX"
    }
}
