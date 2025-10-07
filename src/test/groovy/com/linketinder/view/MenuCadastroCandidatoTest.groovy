package com.linketinder.view

import spock.lang.Specification
import com.linketinder.database.Database
import com.linketinder.model.Candidato


class MenuCadastroCandidatoTest extends Specification {

    //given: prepara o objeto que vai ser usado no teste
    // when: executa a ação que vai ser testada
    // then: verifica se o resultado da ação está correto

    def "deve cadastrar novo candidato com sucesso"() {
        given: "um menu com entrada e de dados simuladas"
        Database database = new Database()
        int quantidadeInicialCandidatos = database.candidatos.size() // guarda a quantidade inicial de candidatos no banco de dados
        // simula o usuario digitando as informaçoes
        String input = "Nathalia Veiga\nnath@email.com\n123.456.789-00\n33\nSP\n12345-678\nDesenvolvedora Frontend\nJavascript, React, CSS\n"

        //inputStream é uma classe que permite ler bytes de um array de bytes
        // inputStream funciona como se fosse o teclado e o System.setIn redireciona a entrada padrao (teclado) para o inputStream
        InputStream inputStream = new ByteArrayInputStream(input.getBytes())
        System.setIn(inputStream)

        Menu menu = new Menu(database)

        when: "o usuário cadastra um novo candidato"
        menu.cadastrarCandidato()

        then: "o candidato deve estar na lista de candidatos do banco de dados"
        database.candidatos.size() == quantidadeInicialCandidatos + 1

        Candidato novoCandidato = database.candidatos.last()
        novoCandidato.nome == "Nathalia Veiga"
        novoCandidato.email == "nath@email.com"
        novoCandidato.cpf == "123.456.789-00"
    }


}