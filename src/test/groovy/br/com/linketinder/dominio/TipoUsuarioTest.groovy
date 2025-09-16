package br.com.linketinder.dominio

import spock.lang.Specification

class TipoUsuarioTest extends Specification {
    def "deve enumerar tipos de usuário corretamente"() {
        when:
        def values = TipoUsuario.getEnumConstants()

        then:
        values.contains(TipoUsuario.CANDIDATO)
        values.contains(TipoUsuario.EMPRESA)
        TipoUsuario.valueOf("CANDIDATO") == TipoUsuario.CANDIDATO
        TipoUsuario.valueOf("EMPRESA") == TipoUsuario.EMPRESA
    }

    def "deve permitir uso do tipo em outras classes"() {
        given:
        def tipoCand = TipoUsuario.CANDIDATO
        def tipoEmp = TipoUsuario.EMPRESA

        expect:
        tipoCand.toString() == "CANDIDATO"
        tipoEmp.toString() == "EMPRESA"
        tipoCand != tipoEmp
    }
}
