package com.linketinder.view

import com.linketinder.controller.CandidatoController
import com.linketinder.controller.EmpresaController
import com.linketinder.controller.VagaController
import com.linketinder.service.CandidatoService
import com.linketinder.service.EmpresaService
import com.linketinder.service.VagaService
import groovy.transform.CompileStatic

@CompileStatic
class MenuPrincipal {
    private final Scanner input
    private MenuCandidato menuCandidato
    private MenuEmpresa menuEmpresa
    private MenuVaga menuVaga

    MenuPrincipal() {
        this.input = new Scanner(System.in)

        CandidatoService candidatoService = new CandidatoService()
        EmpresaService empresaService = new EmpresaService()
        VagaService vagaService = new VagaService()

        CandidatoController candidatoController = new CandidatoController(candidatoService)
        EmpresaController empresaController = new EmpresaController(empresaService)
        VagaController vagaController = new VagaController(vagaService)

        this.menuCandidato = new MenuCandidato(candidatoController, input)
        this.menuEmpresa = new MenuEmpresa(empresaController, input)
        this.menuVaga = new MenuVaga(vagaController, empresaController input)
    }

    void exibir() {
        boolean continuar = true

        while (continuar) {
            println "\n" + "=" * 60
            println "        LINKETINDER - Sistema de Vagas"
            println "=" * 60
            println "\n1. Menu Candidatos"
            println "2. Menu Empresas"
            println "3. Menu Vagas"
            println "0. Sair"
            print "\nEscolha uma opção: "

            try {
                Integer opcao = Integer.parseInt(input.nextLine())

                switch (opcao) {
                    case 1:
                        menuCandidato.exibir()
                        break
                    case 2:
                        menuEmpresa.exibir()
                        break
                    case 3:
                        menuVaga.exibir()
                        break
                    case 0:
                        println "Saindo do sistema. Até logo!"
                        continuar = false
                        return
                    default:
                        println "Opção inválida. Por favor, tente novamente."
                }
            } catch (NumberFormatException e) {
                println "Opção inválida. Por favor, insira um número válido."
            }
        }

        input.close()
    }
}
