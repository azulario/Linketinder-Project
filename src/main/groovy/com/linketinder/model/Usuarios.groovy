package com.linketinder.model

interface Usuarios {
    String getNome()
    String getEmail()
    String getCep()
    String getEstado()
    String getDescricao()
    List<String> getCompetencias()

    void exibirInfo()
}