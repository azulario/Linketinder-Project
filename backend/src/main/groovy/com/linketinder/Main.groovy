package com.linketinder

import com.linketinder.service.CandidatoService
import com.linketinder.service.EmpresaService
import com.linketinder.service.VagaService

class Main {
    private CandidatoService candidatoService
    private EmpresaService empresaService
    private VagaService vagaService
    private Scanner input

    Main() {
        this.candidatoService = new CandidatoService()
        this.empresaService = new EmpresaService()
        this.vagaService = new VagaService(empresaService)
        this.input = new Scanner(System.in)
    }

    static void main(String[] args) {
        Main app = new Main()
        app.exibir()
    }

    void exibir() {
        Boolean continuar = true

        while (continuar) {
            exibirMenu()
            String opcao = input.nextLine()

            switch (opcao) {
                case "1":
                    candidatoService.listar()
                    break
                case "2":
                    empresaService.listar()
                    break
                case "3":
                    vagaService.listar()
                    break
                case "4":
                    candidatoService.cadastrar(input)
                    break
                case "5":
                    empresaService.cadastrar(input)
                    break
                case "6":
                    vagaService.cadastrar(input)
                    break
                case "7":
                    continuar = false
                    println "\nSaindo do sistema. Até logo!"
                    break
                default:
                    println "\n⚠ Opção inválida. Tente novamente."
            }
        }

        input.close()
    }

    private void exibirMenu() {
        println "\n" + "=" * 50
        println "        LINKETINDER - MENU PRINCIPAL"
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
    }
}
