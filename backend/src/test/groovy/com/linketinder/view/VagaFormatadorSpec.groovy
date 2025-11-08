package com.linketinder.view

import com.linketinder.model.Vaga
import com.linketinder.model.Empresa
import com.linketinder.model.Endereco
import spock.lang.Specification
import java.time.LocalDateTime

class VagaFormatadorSpec extends Specification {

    VagaFormatador formatador

    def setup() {
        formatador = new VagaFormatador()
    }

    def "deve formatar vaga com todos os dados"() {
        given: "uma vaga completa"
        def empresa = new Empresa("Tech Corp", "tech@email.com", "12.345.678/0001-90", "Tech")
        empresa.id = 1

        def vaga = new Vaga("Desenvolvedor Java Sênior", "Vaga para desenvolvedor com experiência", empresa)
        vaga.id = 10
        vaga.competencias = ["Java", "Spring Boot", "PostgreSQL", "Docker"]
        vaga.endereco = new Endereco("Brasil", "SP", "São Paulo", "01234-567")
        vaga.criadoEm = LocalDateTime.now()

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve conter todos os dados formatados"
        resultado.contains("VAGA")
        resultado.contains("ID: 10")
        resultado.contains("Título: Desenvolvedor Java Sênior")
        resultado.contains("Empresa: Tech Corp")
        resultado.contains("Descrição: Vaga para desenvolvedor com experiência")
        resultado.contains("Competências: Java, Spring Boot, PostgreSQL, Docker")
        resultado.contains("Criado em:")
    }

    def "deve formatar vaga sem ID"() {
        given: "uma vaga sem ID"
        def empresa = new Empresa("Data Corp", "data@email.com", "98.765.432/0001-10", "Data")
        def vaga = new Vaga("Analista de Dados", "Análise e interpretação de dados", empresa)
        vaga.competencias = ["Python", "SQL", "Power BI"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "não deve conter ID"
        !resultado.contains("ID:")
        resultado.contains("Título: Analista de Dados")
    }

    def "deve formatar vaga sem data de criação"() {
        given: "uma vaga sem data"
        def empresa = new Empresa("Frontend Ltd", "front@email.com", "11.222.333/0001-44", "Frontend")
        def vaga = new Vaga("Desenvolvedor Frontend", "Desenvolvimento de interfaces web", empresa)
        vaga.competencias = ["React", "TypeScript", "CSS"]
        vaga.criadoEm = null

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "não deve conter data"
        !resultado.contains("Criado em:")
        resultado.contains("Título: Desenvolvedor Frontend")
    }

    def "deve formatar vaga com competências vazias"() {
        given: "uma vaga sem competências"
        def empresa = new Empresa("Learning Co", "learn@email.com", "55.666.777/0001-88", "Learning")
        def vaga = new Vaga("Estagiário", "Vaga de estágio para aprendizado", empresa)
        vaga.competencias = []

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve mostrar competências vazia"
        resultado.contains("Competências:")
        resultado.contains("Título: Estagiário")
    }

    def "deve formatar vaga com descrição longa"() {
        given: "uma vaga com descrição extensa"
        def descricao = """Estamos buscando um profissional experiente para liderar nosso time de desenvolvimento.
        O candidato ideal terá experiência em arquitetura de software, liderança técnica e desenvolvimento de aplicações escaláveis.
        Oferecemos ambiente colaborativo, projetos desafiadores e oportunidades de crescimento."""

        def empresa = new Empresa("Tech Leaders", "leaders@email.com", "22.333.444/0001-99", "Tech Leaders")
        def vaga = new Vaga("Tech Lead", descricao, empresa)
        vaga.competencias = ["Liderança", "Arquitetura", "Java"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve conter a descrição completa"
        resultado.contains("Tech Lead")
        resultado.contains(descricao)
    }

    def "deve formatar múltiplas competências corretamente"() {
        given: "uma vaga com várias competências"
        def empresa = new Empresa("Full Stack Inc", "fullstack@email.com", "99.888.777/0001-66", "Full Stack")
        def vaga = new Vaga("Desenvolvedor Full Stack", "Desenvolvimento de aplicações web completas", empresa)
        vaga.competencias = ["JavaScript", "TypeScript", "Node.js", "React", "Angular", "MongoDB", "PostgreSQL", "AWS", "Docker", "Kubernetes"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve listar todas as competências separadas por vírgula"
        resultado.contains("JavaScript, TypeScript, Node.js, React, Angular, MongoDB, PostgreSQL, AWS, Docker, Kubernetes")
    }

    def "deve formatar vaga com título curto"() {
        given: "uma vaga com título simples"
        def empresa = new Empresa("Simple Dev", "simple@email.com", "11.111.111/0001-11", "Simple")
        def vaga = new Vaga("Dev", "Vaga para desenvolvedor", empresa)
        vaga.competencias = ["Java"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve formatar corretamente"
        resultado.contains("Título: Dev")
        resultado.contains("Descrição: Vaga para desenvolvedor")
    }

    def "deve incluir linhas separadoras na formatação"() {
        given: "qualquer vaga"
        def empresa = new Empresa("Design Co", "design@email.com", "33.444.555/0001-22", "Design")
        def vaga = new Vaga("Designer UX/UI", "Design de interfaces", empresa)
        vaga.competencias = ["Figma", "Adobe XD"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve conter linhas decorativas"
        resultado.contains("=")
    }

    def "deve mostrar número de curtidas"() {
        given: "uma vaga com curtidas"
        def empresa = new Empresa("Popular Inc", "popular@email.com", "66.777.888/0001-33", "Popular")
        def vaga = new Vaga("Vaga Popular", "Vaga muito procurada", empresa)
        vaga.competencias = ["JavaScript"]

        when: "formatar a vaga"
        def resultado = formatador.formatar(vaga)

        then: "deve mostrar o número de curtidas"
        resultado.contains("Curtidas:")
    }
}

