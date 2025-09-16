package br.com.linketinder.ui

import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.CadastroService

/**
 * Classe de UI para cadastro de empresa via entrada simulada ou real
 * Autor: Azulario
 */
class CadastroEmpresaUi {
    private final List<Empresa> empresas
    private final CadastroService cadastroService
    private final EntradaUsuario entrada

    CadastroEmpresaUi(List<Empresa> empresas, CadastroService cadastroService, EntradaUsuario entrada) {
        this.empresas = empresas
        this.cadastroService = cadastroService
        this.entrada = entrada
    }

    boolean cadastrarEmpresa() {
        println "Digite o ID da empresa:"
        String id = entrada.lerLinha()
        println "Digite a razão social:"
        String razao = entrada.lerLinha()
        println "Digite o email:"
        String email = entrada.lerLinha()
        println "Digite o telefone:"
        String telefone = entrada.lerLinha()
        println "Digite as competências separadas por vírgula:"
        String competenciasStr = entrada.lerLinha()
        List<String> competencias = competenciasStr.split(",").collect { it.trim() }
        Empresa empresa = new Empresa(id, razao, email, telefone, competencias)
        return cadastroService.adicionarEmpresa(empresa, empresas)
    }
}
