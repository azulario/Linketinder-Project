package com.linketinder.database

import java.sql.Connection

interface IConnectionProvider {

    Connection getConnection()

    void closeConnection()
}