package com.linketinder.view

import com.linketinder.database.Database
import spock.lang.Specification


class MenuCurtidasCandidatoSpec extends Specification {
    def "candidato deve curtir uma vaga com sucesso"() {
        given: "um menu de dados com candidato e vaga cadastrados"
        Database database = new Database()

        // guarda a quantidade inicial de curtidas no banco de dados
        // se for traduzir ao pe da letra: "se a lista de candidatos curtidos for nula, retorna 0"
        int curtidasIniciais = database.candidatos[0].vagasCurtidas?.size() ?: 0

        // simula usuario escolhendo o candidato 1 e a vaga 1
        String input = "1\n1\n"
        // redireciona a entrada padrao (teclado) para o inputStream
        // inputStream é uma classe que permite ler bytes de um array de bytes
        System.setIn(new ByteArrayInputStream(input.bytes))

        Menu menu = new Menu(database)

        when: "o candidato curte uma vaga"
        menu.candidatoCurtirVaga()

        then: "a vaga deve estar na lista de vagas curtidas do candidato"
        database.candidatos[0].vagasCurtidas.size() == curtidasIniciais + 1
    }
}