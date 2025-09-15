package br.com.linketinder.ui

// Converte uma string em inteiro; retorna o valor padrão se a conversão falhar
class ConsoleUtils {
    static int parseIntInput(String input, int fallback = -1) {
        try {
            Integer.parseInt(input?.trim())
        } catch (Exception ignore) {
            fallback
        }
    }

    static boolean validateIndex(int i, int size) {
        i >= 0 && i < size
    }
}
