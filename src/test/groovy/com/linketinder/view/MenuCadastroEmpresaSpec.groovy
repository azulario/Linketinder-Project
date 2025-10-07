package com.linketinder.view

import com.linketinder.database.Database
import com.linketinder.model.Empresa
import spock.lang.Specification


class MenuCadastroEmpresaSpec extends Specification {

    def "deve cadastrar nova empresa com sucesso"() {
        given: "um menu com entrada e de dados simuladas"
        Database database = new Database()
        int quantidadeInicialEmpresas = database.empresas.size() // guarda a quantidade inicial de empresas no banco de dados
        // simula o usuario digitando as informaçoes
        String input = "Tech Corp\ncontato@techcorp.com\n12.345.678/0001-99\nBrasil\nSP\n12345-678\nEmpresa de tecnologia\nJava, Python, Agile"

        InputStream inputStream = new ByteArrayInputStream(input.getBytes())
        System.setIn(inputStream)

        Menu menu = new Menu(database)

        when: "o usuário cadastra uma nova empresa"
        menu.cadastrarEmpresa()

        then: "a empresa deve estar na lista de empresas do banco de dados"
        database.empresas.size() == quantidadeInicialEmpresas + 1

        Empresa novaEmpresa = database.empresas.last()

        novaEmpresa.nome == "Tech Corp"
        novaEmpresa.email == "contato@techcorp.com"
        novaEmpresa.cnpj == "12.345.678/0001-99"
    }
}