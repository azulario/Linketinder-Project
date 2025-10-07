package com.linketinder.database

import com.linketinder.model.Candidato
import com.linketinder.model.Empresa

class Database {
    List<Candidato> candidatos = []
    List<Empresa> empresas = []

    Database() {
        carregarCandidatos()
        carregarEmpresas()
    }

    private void carregarCandidatos() {
        candidatos = [
                new Candidato(
                        "João Silva",
                        "joao.silva@email.com",
                        "123.456.789-00",
                        25,
                        "SP",
                        "01310-100",
                        "Desenvolvedor full-stack com 3 anos de experiência",
                        ["Java", "Spring Framework", "Angular", "PostgreSQL"]
                ),
                new Candidato(
                        "Maria Santos",
                        "maria.santos@email.com",
                        "987.654.321-00",
                        28,
                        "RJ",
                        "20040-020",
                        "Engenheira de software especializada em backend",
                        ["Python", "Django", "Docker", "AWS"]
                ),
                new Candidato(
                        "Pedro Oliveira",
                        "pedro.oliveira@email.com",
                        "456.789.123-00",
                        23,
                        "MG",
                        "30130-100",
                        "Desenvolvedor frontend focado em React",
                        ["JavaScript", "React", "TypeScript", "CSS"]
                ),
                new Candidato(
                        "Ana Costa",
                        "ana.costa@email.com",
                        "321.654.987-00",
                        30,
                        "PR",
                        "80060-000",
                        "Tech lead com experiência em arquitetura de sistemas",
                        ["Java", "Spring Framework", "Kubernetes", "Microservices"]
                ),
                new Candidato(
                        "Carlos Ferreira",
                        "carlos.ferreira@email.com",
                        "789.123.456-00",
                        26,
                        "RS",
                        "90040-000",
                        "Desenvolvedor mobile Android e iOS",
                        ["Kotlin", "Swift", "Flutter", "Firebase"]
                )
        ]
    }

    private void carregarEmpresas() {
        empresas = [
                new Empresa(
                        "Tech Solutions LTDA",
                        "contato@techsolutions.com.br",
                        "12.345.678/0001-90",
                        "Brasil",
                        "SP",
                        "01310-100",
                        "Empresa de consultoria em tecnologia",
                        ["Java", "Spring Framework", "Angular"]
                ),
                new Empresa(
                        "Inovação Digital S.A.",
                        "rh@inovacaodigital.com.br",
                        "98.765.432/0001-10",
                        "Brasil",
                        "RJ",
                        "20040-020",
                        "Startup de transformação digital",
                        ["Python", "React", "AWS", "Docker"]
                ),
                new Empresa(
                        "Code Masters",
                        "vagas@codemasters.com.br",
                        "45.678.912/0001-35",
                        "Brasil",
                        "MG",
                        "30130-100",
                        "Software house especializada em produtos web",
                        ["JavaScript", "TypeScript", "Node.js", "React"]
                ),
                new Empresa(
                        "Cloud Systems",
                        "jobs@cloudsystems.com.br",
                        "32.165.498/0001-77",
                        "Brasil",
                        "PR",
                        "80060-000",
                        "Empresa focada em soluções cloud",
                        ["Java", "Kubernetes", "Microservices", "AWS"]
                ),
                new Empresa(
                        "Mobile Apps Inc",
                        "recrutamento@mobileapps.com.br",
                        "78.912.345/0001-22",
                        "Brasil",
                        "RS",
                        "90040-000",
                        "Desenvolvimento de aplicativos mobile",
                        ["Kotlin", "Swift", "Flutter", "React Native"]
                )
        ]
    }

    void adicionarCandidato(Candidato candidato) {
        candidatos.add(candidato)
    }

    void adicionarEmpresa(Empresa empresa) {
        empresas.add(empresa)
    }
}
