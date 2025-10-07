package com.linketinder

import com.linketinder.database.Database
import com.linketinder.view.Menu

class Main {
    static void main(String[] args) {
        println "Bem-vindo ao Linketinder!"
        Database database = new Database()
        Menu menu = new Menu(database)
        menu.exibir()
    }
}
