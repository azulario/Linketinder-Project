package com.linketinder

import com.linketinder.dao.CandidatoDAO
import com.linketinder.dao.EmpresaDAO
import com.linketinder.dao.VagaDAO
import com.linketinder.model.Candidato
import com.linketinder.model.Empresa
import com.linketinder.model.Vaga
import java.time.LocalDate

class Main {
    private CandidatoDAO candidatoDAO
    private EmpresaDAO empresaDAO
    private VagaDAO vagaDAO
    private Scanner input

    Main() {
        this.candidatoDAO = new CandidatoDAO()
        this.empresaDAO = new EmpresaDAO()
        this.vagaDAO = new VagaDAO()
        this.input = new Scanner(System.in)
    }

    static void main(String[] args) {
        Main app = new Main()
        app.exibir()
    }

    void exibir() {
        Boolean continuar = true

        while (continuar) {
            println "\n" + "=" * 50
            println "LINKETINDER - MENU PRINCIPAL"
            println "=" * 50
            println "1. Listar Candidatos"
            println "2. Listar Empresas"
            println "3. Listar Vagas"
            println "4. Cadastrar novo Candidato"
            println "5. Cadastrar nova Empresa"
            println "6. Cadastrar nova Vaga"
            println "7. Sair"
            println "=" * 50
            print "Escolha uma opção: "

            String opcao = input.nextLine()

            switch (opcao) {
                case "1":
                    listarCandidatos()
                    break
                case "2":
                    listarEmpresas()
                    break
                case "3":
                    listarVagas()
                    break
                case "4":
                    cadastrarCandidato()
                    break
                case "5":
                    cadastrarEmpresa()
                    break
                case "6":
                    cadastrarVaga()
                    break
                case "7":
                    continuar = false
                    println "Saindo do sistema. Até logo!"
                    break
                default:
                    println "Opção inválida. Tente novamente."
            }
        }

        input.close()
    }


}
