package com.linketinder.model

/**
 * Interface para usuários do sistema
 * Aplica Interface Segregation Principle (ISP)
 * Contém apenas métodos essenciais de identificação
 */
interface Usuarios {
    String getNome()
    String getEmail()
    Endereco getEndereco()
}
