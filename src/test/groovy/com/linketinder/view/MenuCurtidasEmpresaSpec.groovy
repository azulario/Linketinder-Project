package com.linketinder.view

import com.linketinder.database.Database
import spock.lang.Specification


class MenuCurtidasEmpresaSpec extends Specification {
    def "empresa deve curtir um candidato com sucesso"() {
        given: "um menu de dados com empresa e candidato cadastrados"
        Database database = new Database()

        // guarda a quantidade inicial de curtidas no banco de dados
        // se for traduzir ao pe da letra: "se a lista de candidatos curtidos for nula, retorna 0"
        int curtidasIniciais = database.empresas[0].candidatosCurtidos?.size() ?: 0

        // simula usuario escolhendo a empresa 1 e o candidato 1
        String input = "1\n1\n"
        // redireciona a entrada padrao (teclado) para o inputStream
        // inputStream é uma classe que permite ler bytes de um array de bytes
        System.setIn(new ByteArrayInputStream(input.bytes))

        Menu menu = new Menu(database)

        when: "a empresa curte um candidato"
        menu.empresaCurtirCandidato()

        then: "o candidato deve estar na lista de candidatos curtidos da empresa"
        database.empresas[0].candidatosCurtidos.size() == curtidasIniciais + 1
    }
}

