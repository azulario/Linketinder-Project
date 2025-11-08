package com.linketinder.view

import com.linketinder.model.Candidato
import com.linketinder.model.Endereco
import spock.lang.Specification
import java.time.LocalDate

class CandidatoFormatadorSpec extends Specification {

    CandidatoFormatador formatador

    def setup() {
        formatador = new CandidatoFormatador()
    }

    def "deve formatar candidato com todos os dados"() {
        given: "um candidato completo"
        def candidato = new Candidato(
            "João",
            "Silva",
            "joao@email.com",
            "123.456.789-00",
            LocalDate.of(1990, 5, 15),
            "Desenvolvedor Java experiente",
            ["Java", "Spring Boot", "PostgreSQL"]
        )
        candidato.id = 1
        candidato.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        candidato.endereco.id = 1

        when: "formatar o candidato"
        def resultado = formatador.formatar(candidato)

        then: "deve conter todos os dados formatados"
        resultado.contains("CANDIDATO")
        resultado.contains("ID: 1")
        resultado.contains("Nome: João Silva")
        resultado.contains("Email: joao@email.com")
        resultado.contains("CPF: 123.456.789-00")
        resultado.contains("Data de Nascimento: 1990-05-15")
        resultado.contains("Idade:")
        resultado.contains("Descrição: Desenvolvedor Java experiente")
        resultado.contains("Competências: Java, Spring Boot, PostgreSQL")
        resultado.contains("Endereço:")
    }

    def "deve formatar candidato sem ID"() {
        given: "um candidato sem ID"
        def candidato = new Candidato(
            "Maria",
            "Santos",
            "maria@email.com",
            "987.654.321-00",
            LocalDate.of(1995, 8, 20),
            "Desenvolvedora Python",
            ["Python", "Django"]
        )

        when: "formatar o candidato"
        def resultado = formatador.formatar(candidato)

        then: "não deve conter ID"
        !resultado.contains("ID:")
        resultado.contains("Nome: Maria Santos")
    }

    def "deve formatar candidato sem endereço"() {
        given: "um candidato sem endereço"
        def candidato = new Candidato(
            "Pedro",
            "Oliveira",
            "pedro@email.com",
            "111.222.333-44",
            LocalDate.of(1988, 3, 10),
            "Analista de Sistemas",
            ["C#", ".NET"]
        )

        when: "formatar o candidato"
        def resultado = formatador.formatar(candidato)

        then: "não deve conter endereço"
        !resultado.contains("Endereço:")
        resultado.contains("Nome: Pedro Oliveira")
    }

    def "deve formatar candidato com competências vazias"() {
        given: "um candidato sem competências"
        def candidato = new Candidato(
            "Ana",
            "Costa",
            "ana@email.com",
            "555.666.777-88",
            LocalDate.of(2000, 12, 5),
            "Iniciante",
            []
        )

        when: "formatar o candidato"
        def resultado = formatador.formatar(candidato)

        then: "deve mostrar competências vazia"
        resultado.contains("Competências:")
        resultado.contains("Nome: Ana Costa")
    }

    def "deve calcular idade corretamente"() {
        given: "um candidato com data de nascimento conhecida"
        def candidato = new Candidato(
            "Carlos",
            "Ferreira",
            "carlos@email.com",
            "999.888.777-66",
            LocalDate.of(1985, 1, 1),
            "Gerente de Projetos",
            ["Gestão", "Scrum"]
        )

        when: "formatar o candidato"
        def resultado = formatador.formatar(candidato)

        then: "deve calcular e mostrar a idade"
        resultado.contains("Idade:")
        def idade = LocalDate.now().getYear() - 1985
        resultado.contains("Idade: ${idade}")
    }
}

