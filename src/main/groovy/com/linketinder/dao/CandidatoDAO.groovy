package com.linketinder.dao

import com.linketinder.database.DatabaseConnection
import com.linketinder.model.Candidato
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.time.LocalDateTime
import java.time.LocalDate

class CandidatoDAO {
    /**
     * Insere um novo candidato no banco de dados
     * @param candidato - objeto Candidato a ser inserido
     */
    void inserir(Candidato candidato) {