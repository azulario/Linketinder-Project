// Autor: Azulario
// Para executar: gradle run ou groovy Main.groovy
package br.com.linketinder

import br.com.linketinder.dominio.Candidato
import br.com.linketinder.dominio.Empresa
import br.com.linketinder.servico.SistemaMatch
import br.com.linketinder.dto.PerfilPublico
import br.com.linketinder.dto.DadosPessoais

import java.util.List
import java.util.Optional
import java.util.Scanner
import groovy.transform.Field

class Main {
    /*
     TODO: Implementar curtida às cegas:
         - Candidato só pode ver as competências/vagas que a empresa busca, não detalhes da empresa.
         - Empresa só pode ver as competências que o candidato oferece, não detalhes do candidato.
         - Garantir privacidade dos perfis até o match.
     TODO: Refatorar Main:
         - Deixar o Main apenas como orquestrador, chamando métodos das classes de domínio.
         - Mover lógicas de negócio para as classes apropriadas (Candidato, Empresa, Curtida, etc).
         - Reduzir o tamanho do Main, removendo código duplicado e responsabilidades excessivas.
         - Criar métodos utilitários para entrada/saída, se necessário.
         - Facilitar testes unitários e manutenção.
     TODO: Validar fluxos de match e curtidas recíprocas.
     TODO: Garantir tratamento de erros e entradas inválidas.
     TODO: Melhorar experiência do usuário no menu e fluxos.
    */


    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in)
        while (true) {
            println "\nMenu Principal:\n1 - Listar Candidatos\n2 - Listar Empresas\n3 - Candidato curtir vaga de empresa\n4 - Empresa curtir candidato\n5 - Listar matches\n0 - Sair"
            String opcao = System.console() ? System.console().readLine() : scanner.nextLine()
            switch(opcao) {
                case "1": listarCandidatos(); break
                case "2": listarEmpresas(); break
                case "3": candidatoCurteVaga(); break
                case "4": empresaCurteCandidato(); break
                case "5": listarMatches(); break
                case "0": println "Saindo..."; return
                default: println "Opção inválida!"
            }
        }
    }
}
