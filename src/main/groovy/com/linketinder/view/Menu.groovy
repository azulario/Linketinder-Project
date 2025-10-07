package com.linketinder.view

import com.linketinder.database.Database
import com.linketinder.model.Candidato
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga

class Menu {
    private Database database
    private Scanner scanner

    Menu(Database database) {
        this.database = database
        this.scanner = new Scanner(System.in)
    }

    void exibir() {
        Boolean continuar = true

        while (continuar) {
            println "\n" + "=" * 50
            println "LINKETINDER - MENU PRINCIPAL"
            println "=" * 50
            println "1. Listar Candidatos"
            println "2. Listar Empresas"
            println "3. Listar Vagas"
            println "4. Cadastrar novo Candidato"
            println "5. Cadastrar nova Empresa"
            println "6. Candidato curtir Vaga"
            println "7. Empresa curtir Candidato"
            println "8. Sair"
            println "=" * 50
            print "Escolha uma opção: "

            String opcao = scanner.nextLine()

            switch (opcao) {
                case "1":
                    listarCandidatos()
                    break
                case "2":
                    listarEmpresas()
                    break
                case "3":
                    listarVagas()
                    break
                case "4":
                    cadastrarCandidato()
                    break
                case "5":
                    cadastrarEmpresa()
                    break
                case "6":
                    candidatoCurtirVaga()
                    break
                case "7":
                    empresaCurtirCandidato()
                    break
                case "8":
                    continuar = false
                    println "Saindo do Linketinder. Até logo!"
                    break
                default:
                    println "Opção inválida. Tente novamente."
            }
        }

        scanner.close()
    }

    private void listarCandidatos() {
        println "### LISTA DE CANDIDATOS ###"

        if (database.candidatos.isEmpty()) {
            println "Nenhum candidato cadastrado."
            return
        }

        database.candidatos.eachWithIndex{ Candidato candidato, int i ->
            println "\nCandidato ${i + 1}:"
            candidato.exibirInfo()
            println ""
        }

    }

    private void listarEmpresas() {
        println "### LISTA DE EMPRESAS ###"

        if (database.empresas.isEmpty()) {
            println "Nenhuma empresa cadastrada."
            return
        }

        database.empresas.eachWithIndex{ Empresa empresa, int i ->
            println "\nEmpresa ${i + 1}:"
            empresa.exibirInfo()
            println ""
        }

    }

    private void listarVagas() {
        println "### LISTA DE VAGAS ###"

        if (database.vagas.isEmpty()) {
            println "Nenhuma vaga cadastrada."
            return
        }

        database.vagas.eachWithIndex{ vaga, int i ->
            println "\nVaga ${i + 1}:"
            vaga.exibirInfo()
            println ""
        }

    }

    private void cadastrarCandidato() {
        println "### CADASTRO DE CANDIDATO ###"

        print "Nome: "
        String nome = scanner.nextLine()

        print "Email: "
        String email = scanner.nextLine()

        print "CPF: "
        String cpf = scanner.nextLine()

        print "Idade: "
        Integer idade = Integer.parseInt(scanner.nextLine())
        print "Estado: "
        String estado = scanner.nextLine()

        print "CEP: "
        String cep = scanner.nextLine()

        print "Descrição: "
        String descricao = scanner.nextLine()

        // split separa a string em uma lista com base no delimitador (vírgula)
        // collect it.trim() remove espaços em branco no início e no fim de cada competência
        print "Competências (separadas por vírgula): "
        List<String> competencias = scanner.nextLine().split(",").collect { it.trim() }

        Candidato novoCandidato = new Candidato(
                nome,
                email,
                cpf,
                idade,
                estado,
                cep,
                descricao,
                competencias
        )

        database.adicionarCandidato(novoCandidato)
        println "Candidato cadastrado com sucesso!"

    }

    private void cadastrarEmpresa() {
        println "### CADASTRO DE EMPRESA ###"

        print "Nome: "
        String nome = scanner.nextLine()

        print "Email: "
        String email = scanner.nextLine()

        print "CNPJ: "
        String cnpj = scanner.nextLine()

        print "País: "
        String pais = scanner.nextLine()

        print "Estado: "
        String estado = scanner.nextLine()

        print "CEP: "
        String cep = scanner.nextLine()

        print "Descrição: "
        String descricao = scanner.nextLine()

        print "Competências buscadas (separadas por vírgula): "
        List<String> competencias = scanner.nextLine().split(",").collect { it.trim() }

        Empresa novaEmpresa = new Empresa(
                nome,
                email,
                cnpj,
                pais,
                estado,
                cep,
                descricao,
                competencias
        )

        database.adicionarEmpresa(novaEmpresa)
        println "Empresa cadastrada com sucesso!"
    }

    private void candidatoCurtirVaga() {
        println "### CANDIDATO CURTIR VAGA ###"

        if (database.candidatos.isEmpty() || database.vagas.isEmpty()) {
            println "Não há candidatos ou vagas disponiveis."
            return
        }

        println "Candidatos disponíveis: "
        database.candidatos.eachWithIndex { Candidato candidato, int i ->
            println "${i + 1}. ${candidato.nome}"
        }

        println "Escolha o número do candidato: "
        Integer numCandidato = scanner.nextLine().toInteger() - 1 // -1 para ajustar ao índice da lista

        if (numCandidato < 0 || numCandidato >= database.candidatos.size()) {
            println "Número de candidato inválido."
            return

        }

        Candidato candidatoSelecionado = database.candidatos[numCandidato]

        println "Vagas disponíveis: "
        database.vagas.eachWithIndex { vaga, int i ->
            println "${i + 1}. ${vaga.titulo} na ${vaga.empresa.nome}"
        }

        println "Escolha o número da vaga: "
        Integer numVaga = scanner.nextLine().toInteger() - 1 // -1 para ajustar ao índice da lista

        if (numVaga < 0 || numVaga >= database.vagas.size()) {
            println "Número de vaga inválido."
            return
        }

        Vaga vaga = database.vagas[numVaga]
        Candidato.curtirVaga(vaga)

        println "\n✓ ${candidato.nome} curtiu a vaga ${vaga.titulo}!"
    }

}