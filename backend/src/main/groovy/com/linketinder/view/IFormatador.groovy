package com.linketinder.view

/**
 * Interface para formatação de objetos
 * Aplica o princípio Open/Closed Principle (OCP)
 * Permite adicionar novos formatadores (JSON, HTML, XML) sem modificar código existente
 */
interface IFormatador<T> {
    String formatar(T objeto)
}

