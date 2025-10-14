package com.linketinder.view

import com.linketinder.database.Database
import spock.lang.Specification

/**
 * Teste para cadastro de candidatos
 *
 * Este teste verifica se um novo candidato é adicionado corretamente na lista de candidatos
 */
class MenuCadastroCandidatoSpec extends Specification {

    def "deve cadastrar um novo candidato com sucesso"() {
        given: "um banco de dados e um menu"
        Database database = new Database()

        // Guarda quantos candidatos existem antes de cadastrar
        int candidatosIniciais = database.candidatos.size()

        // Simula o usuário digitando os dados do novo candidato
        // Cada linha representa uma resposta para cada pergunta do cadastro
        String input = """Sandubinha Ilustre
sanduba@email.com
111.222.333-44
22
BA
40000-000
Desenvolvedor iniciante aprendendo Groovy
Groovy, Java, Git
"""
        // Redireciona a entrada do teclado para usar nosso texto simulado
        System.setIn(new ByteArrayInputStream(input.bytes))

        Menu menu = new Menu(database)

        when: "cadastramos um novo candidato"
        menu.cadastrarCandidato()

        then: "a lista de candidatos deve ter mais um candidato"
        database.candidatos.size() == candidatosIniciais + 1

        and: "o novo candidato deve ter o nome correto"
        database.candidatos.last().nome == "Sandubinha Ilustre"

        and: "o novo candidato deve ter o email correto"
        database.candidatos.last().email == "sanduba@email.com"

        and: "o novo candidato deve ter as competências corretas"
        database.candidatos.last().competencias.contains("Groovy")
        database.candidatos.last().competencias.contains("Java")
    }
}

