package com.linketinder.database

import com.linketinder.model.Candidato
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga
import com.linketinder.model.Endereco
import java.time.LocalDate

class Database {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []
    List<Vaga> vagas = []

    Database() {
        carregarCandidatos()
        carregarEmpresas()
        carregarVagas()
    }

    private void carregarCandidatos() {
        Candidato candidato1 = new Candidato(
            "João",
            "Silva",
            "joao.silva@email.com",
            "123.456.789-00",
            LocalDate.of(1998, 5, 15),
            "Desenvolvedor full-stack com 3 anos de experiência",
            ["Java", "Spring Framework", "Angular", "PostgreSQL"]
        )
        candidato1.endereco = new Endereco("Brasil", "São Paulo", "São Paulo", "01310-100")

        Candidato candidato2 = new Candidato(
            "Maria",
            "Santos",
            "maria.santos@email.com",
            "987.654.321-00",
            LocalDate.of(1995, 8, 22),
            "Engenheira de software especializada em backend",
            ["Python", "Django", "Docker", "AWS"]
        )
        candidato2.endereco = new Endereco("Brasil", "Rio de Janeiro", "Rio de Janeiro", "20040-020")

        Candidato candidato3 = new Candidato(
            "Pedro",
            "Oliveira",
            "pedro.oliveira@email.com",
            "456.789.123-00",
            LocalDate.of(2000, 3, 10),
            "Desenvolvedor frontend focado em React",
            ["JavaScript", "React", "TypeScript", "CSS"]
        )
        candidato3.endereco = new Endereco("Brasil", "Minas Gerais", "Belo Horizonte", "30130-100")

        Candidato candidato4 = new Candidato(
            "Ana",
            "Costa",
            "ana.costa@email.com",
            "321.654.987-00",
            LocalDate.of(1993, 11, 5),
            "Tech lead com experiência em arquitetura de sistemas",
            ["Java", "Spring Framework", "Kubernetes", "Microservices"]
        )
        candidato4.endereco = new Endereco("Brasil", "Paraná", "Curitiba", "80060-000")

        Candidato candidato5 = new Candidato(
            "Carlos",
            "Ferreira",
            "carlos.ferreira@email.com",
            "789.123.456-00",
            LocalDate.of(1997, 7, 30),
            "Desenvolvedor mobile Android e iOS",
            ["Kotlin", "Swift", "Flutter", "Firebase"]
        )
        candidato5.endereco = new Endereco("Brasil", "Rio Grande do Sul", "Porto Alegre", "90040-000")

        candidatos = [candidato1, candidato2, candidato3, candidato4, candidato5]
    }

    private void carregarEmpresas() {
        Empresa empresa1 = new Empresa(
            "Tech Solutions LTDA",
            "contato@techsolutions.com.br",
            "12.345.678/0001-90",
            "Empresa de consultoria em tecnologia"
        )
        empresa1.endereco = new Endereco("Brasil", "São Paulo", "São Paulo", "01310-100")

        Empresa empresa2 = new Empresa(
            "Inovação Digital S.A.",
            "rh@inovacaodigital.com.br",
            "98.765.432/0001-10",
            "Startup de transformação digital"
        )
        empresa2.endereco = new Endereco("Brasil", "Rio de Janeiro", "Rio de Janeiro", "20040-020")

        Empresa empresa3 = new Empresa(
            "Code Masters",
            "vagas@codemasters.com.br",
            "45.678.912/0001-35",
            "Software house especializada em produtos web"
        )
        empresa3.endereco = new Endereco("Brasil", "Minas Gerais", "Belo Horizonte", "30130-100")

        Empresa empresa4 = new Empresa(
            "Cloud Systems",
            "jobs@cloudsystems.com.br",
            "32.165.498/0001-77",
            "Empresa focada em soluções cloud"
        )
        empresa4.endereco = new Endereco("Brasil", "Paraná", "Curitiba", "80060-000")

        Empresa empresa5 = new Empresa(
            "Mobile Apps Inc",
            "recrutamento@mobileapps.com.br",
            "78.912.345/0001-22",
            "Desenvolvimento de aplicativos mobile"
        )
        empresa5.endereco = new Endereco("Brasil", "Rio Grande do Sul", "Porto Alegre", "90040-000")

        empresas = [empresa1, empresa2, empresa3, empresa4, empresa5]
    }

    private void carregarVagas() {
        Vaga vaga1 = new Vaga(
            "Desenvolvedor Java Pleno",
            "Desenvolvimento de aplicações empresariais com Java e Spring",
            ["Java", "Spring Framework", "PostgreSQL"],
            empresas[0]
        )
        vaga1.endereco = new Endereco("Brasil", "São Paulo", "São Paulo", "01310-100")
        empresas[0].adicionarVaga(vaga1)
        vagas.add(vaga1)

        Vaga vaga2 = new Vaga(
            "Desenvolvedor Frontend Angular",
            "Criar interfaces modernas e responsivas com Angular",
            ["Angular", "TypeScript", "CSS"],
            empresas[0]
        )
        vaga2.endereco = new Endereco("Brasil", "São Paulo", "São Paulo", "01310-100")
        empresas[0].adicionarVaga(vaga2)
        vagas.add(vaga2)

        Vaga vaga3 = new Vaga(
            "Engenheiro Python",
            "Desenvolvimento de APIs e automações com Python",
            ["Python", "Django", "Docker"],
            empresas[1]
        )
        vaga3.endereco = new Endereco("Brasil", "Rio de Janeiro", "Rio de Janeiro", "20040-020")
        empresas[1].adicionarVaga(vaga3)
        vagas.add(vaga3)

        Vaga vaga4 = new Vaga(
            "Desenvolvedor Frontend React",
            "Desenvolvimento de SPAs com React e TypeScript",
            ["JavaScript", "React", "TypeScript"],
            empresas[2]
        )
        vaga4.endereco = new Endereco("Brasil", "Minas Gerais", "Belo Horizonte", "30130-100")
        empresas[2].adicionarVaga(vaga4)
        vagas.add(vaga4)

        Vaga vaga5 = new Vaga(
            "Arquiteto de Soluções Cloud",
            "Projetos de arquitetura em AWS e Azure",
            ["AWS", "Kubernetes", "Docker"],
            empresas[3]
        )
        vaga5.endereco = new Endereco("Brasil", "Paraná", "Curitiba", "80060-000")
        empresas[3].adicionarVaga(vaga5)
        vagas.add(vaga5)

        Vaga vaga6 = new Vaga(
            "Desenvolvedor Mobile Flutter",
            "Desenvolvimento multiplataforma com Flutter",
            ["Flutter", "Dart", "Firebase"],
            empresas[4]
        )
        vaga6.endereco = new Endereco("Brasil", "Rio Grande do Sul", "Porto Alegre", "90040-000")
        empresas[4].adicionarVaga(vaga6)
        vagas.add(vaga6)
    }
}
