package main.groovy.com.linketinder.database

import groovy.transform.CompileStatic

@CompileStatic
class ConnectionFactory {

    private static final String TIPO_PADRAO = "postgresql"

    private static final String ENV_DATABASE_TYPE = "DATABASE_TYPE"

    private static String tipoConfigurado = null

    static IConnectionProvider getInstance() {
        String tipo = determinarTipoBanco()

        println "[FACTORY] Criando conexão: ${tipo}"

        switch (tipo.toLowerCase()) {
            case "postgresql":
                return PostgreSQLConnection.getInstancia()

            default:
                throw new IllegalArgumentException(
                    "Tipo de banco de dados desconhecido: ${tipo}"
                )
        }
    }

    private static String determinarTipoBanco() {
        if (tipoConfigurado != null) {
            return tipoConfigurado

        }

        String tipoAmbiente = System.getenv(ENV_DATABASE_TYPE)
        if (tipoAmbiente != null && !tipoAmbiente.trim().isEmpty()) {
            return tipoAmbiente
        }
        return TIPO_PADRAO

    }

    static void setTipoParaTeste(String tipo) {
        tipoConfigurado = tipo
        println "[TESTE] Tipo configurado $tipo"
    }

    static void resetarTestes() {
        tipoConfigurado = null
        println "[TESTE] Teste configuração resetado"
    }

    static String getTipoAtual() {
        return determinarTipoBanco()
    }

}


