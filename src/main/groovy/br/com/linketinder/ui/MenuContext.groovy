package br.com.linketinder.ui

import br.com.linketinder.servico.SistemaMatch
import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa

import java.util.List
import java.util.Scanner

class MenuContext {
    final SistemaMatch sistema
    final List<Candidato> candidatos
    final List<Empresa> empresas
    final Scanner scanner

    MenuContext(SistemaMatch sistema, List<Candidato> candidatos, List<Empresa> empresas, Scanner scanner) {
        this.sistema = sistema
        this.candidatos = candidatos
        this.empresas = empresas
        this.scanner = scanner
    }
}
