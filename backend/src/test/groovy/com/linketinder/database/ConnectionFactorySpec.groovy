package com.linketinder.database

import spock.lang.Specification

class ConnectionFactorySpec extends Specification {

    def cleanup() {
        // Resetar configuração após cada teste
        ConnectionFactory.resetarTestes()
        System.clearProperty("DATABASE_TYPE")
    }

    def "deve retornar instância PostgreSQL por padrão"() {
        when: "buscar instância sem configuração"
        def provider = ConnectionFactory.getInstance()

        then: "deve retornar PostgreSQLConnection"
        provider instanceof PostgreSQLConnection
        ConnectionFactory.getTipoAtual() == "postgresql"
    }

    def "deve retornar instância PostgreSQL quando configurado explicitamente"() {
        given: "tipo configurado como postgresql"
        ConnectionFactory.setTipoParaTeste("postgresql")

        when: "buscar instância"
        def provider = ConnectionFactory.getInstance()

        then: "deve retornar PostgreSQLConnection"
        provider instanceof PostgreSQLConnection
        ConnectionFactory.getTipoAtual() == "postgresql"
    }

    def "deve respeitar variável de ambiente DATABASE_TYPE"() {
        given: "variável de ambiente configurada"
        System.setProperty("DATABASE_TYPE", "postgresql")

        when: "buscar instância"
        def provider = ConnectionFactory.getInstance()

        then: "deve usar tipo da variável de ambiente"
        provider instanceof PostgreSQLConnection
    }

    def "deve lançar exceção para tipo de banco desconhecido"() {
        given: "tipo desconhecido configurado"
        ConnectionFactory.setTipoParaTeste("mysql")

        when: "tentar buscar instância"
        ConnectionFactory.getInstance()

        then: "deve lançar exceção"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("Tipo de banco de dados desconhecido")
        exception.message.contains("mysql")
    }

    def "deve resetar configuração de testes corretamente"() {
        given: "tipo configurado"
        ConnectionFactory.setTipoParaTeste("postgresql")

        when: "resetar configuração"
        ConnectionFactory.resetarTestes()

        then: "deve voltar ao tipo padrão"
        ConnectionFactory.getTipoAtual() == "postgresql"
    }

    def "deve priorizar tipo configurado sobre variável de ambiente"() {
        given: "tipo configurado e variável de ambiente setada"
        System.setProperty("DATABASE_TYPE", "outro")
        ConnectionFactory.setTipoParaTeste("postgresql")

        when: "buscar tipo atual"
        def tipo = ConnectionFactory.getTipoAtual()

        then: "deve usar tipo configurado"
        tipo == "postgresql"
    }

    def "deve ignorar variável de ambiente vazia"() {
        given: "variável de ambiente vazia"
        System.setProperty("DATABASE_TYPE", "")

        when: "buscar tipo atual"
        def tipo = ConnectionFactory.getTipoAtual()

        then: "deve usar tipo padrão"
        tipo == "postgresql"
    }

    def "deve ignorar variável de ambiente com apenas espaços"() {
        given: "variável de ambiente com espaços"
        System.setProperty("DATABASE_TYPE", "   ")

        when: "buscar tipo atual"
        def tipo = ConnectionFactory.getTipoAtual()

        then: "deve usar tipo padrão"
        tipo == "postgresql"
    }

    def "deve aceitar tipo independente de case"() {
        given: "tipo com letras maiúsculas"
        ConnectionFactory.setTipoParaTeste("POSTGRESQL")

        when: "buscar instância"
        def provider = ConnectionFactory.getInstance()

        then: "deve retornar instância correta"
        provider instanceof PostgreSQLConnection
    }
}

