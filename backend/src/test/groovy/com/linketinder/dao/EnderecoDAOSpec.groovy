package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Endereco
import spock.lang.Specification
import java.sql.Connection

class EnderecoDAOSpec extends Specification {

    EnderecoDAO dao
    Connection conn

    def setup() {
        dao = new EnderecoDAO()
        conn = DatabaseConnection.getConnection()


        conn.createStatement().execute("DELETE FROM candidatos")
        conn.createStatement().execute("DELETE FROM empresas")
        conn.createStatement().execute("DELETE FROM enderecos")
    }

    def cleanup() {
        if (conn != null && !conn.isClosed()) {
            conn.close()
        }
    }

    def "deve inserir um novo endereço no banco de dados"() {
        given: "um endereço válido"
        def endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")

        when: "inserir o endereço no banco"
        dao.inserir(endereco)

        then: "o endereço deve ter um ID gerado"
        endereco.id != null
        endereco.id > 0
    }

    def "deve buscar endereço por ID"() {
        given: "um endereço inserido"
        def endereco = new Endereco("Brasil", "RJ", "Rio de Janeiro", "20000-000")
        dao.inserir(endereco)
        def id = endereco.id

        when: "buscar por ID"
        def resultado = dao.buscarPorId(id)

        then: "deve retornar o endereço"
        resultado != null
        resultado.pais == "Brasil"
        resultado.estado == "RJ"
        resultado.cidade == "Rio de Janeiro"
        resultado.cep == "20000-000"
    }

    def "deve retornar null ao buscar ID inexistente"() {
        when: "buscar por ID que não existe"
        def resultado = dao.buscarPorId(99999)

        then: "deve retornar null"
        resultado == null
    }

    def "deve buscar ou criar endereço quando não existe"() {
        given: "um novo endereço"
        def endereco = new Endereco("Brasil", "MG", "Belo Horizonte", "30000-000")

        when: "buscar ou criar"
        def id = dao.buscarOuCriar(endereco)

        then: "deve criar e retornar o ID"
        id != null
        id > 0
    }

    def "deve buscar endereço existente ao invés de criar"() {
        given: "um endereço já existente"
        def endereco1 = new Endereco("Brasil", "RS", "Porto Alegre", "90000-000")
        dao.inserir(endereco1)
        def idOriginal = endereco1.id

        and: "o mesmo endereço"
        def endereco2 = new Endereco("Brasil", "RS", "Porto Alegre", "90000-000")

        when: "buscar ou criar"
        def id = dao.buscarOuCriar(endereco2)

        then: "deve retornar o ID existente"
        id == idOriginal
    }

    def "deve atualizar um endereço existente"() {
        given: "um endereço inserido"
        def endereco = new Endereco("Brasil", "ES", "Vitória", "29000-000")
        dao.inserir(endereco)
        def id = endereco.id

        when: "atualizar o endereço"
        endereco.cidade = "Vila Velha"
        endereco.cep = "29100-000"
        dao.atualizar(endereco)

        and: "buscar novamente"
        def atualizado = dao.buscarPorId(id)

        then: "deve ter os dados atualizados"
        atualizado.cidade == "Vila Velha"
        atualizado.cep == "29100-000"
    }

    def "deve deletar um endereço"() {
        given: "um endereço inserido"
        def endereco = new Endereco("Brasil", "GO", "Goiânia", "74000-000")
        dao.inserir(endereco)
        def id = endereco.id

        when: "deletar o endereço"
        dao.deletar(id)

        and: "tentar buscar"
        def resultado = dao.buscarPorId(id)

        then: "não deve encontrar"
        resultado == null
    }
}

