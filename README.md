# Linketinder

## Autor
Azulario

## Descrição
Projeto MVP de backend para sistema de contratação inspirado no Linkedin e Tinder, desenvolvido em Groovy. Permite listar candidatos e empresas, cada um com suas competências e dados principais. Estrutura orientada a objetos, utilizando interface e herança.

## Como executar

1. Certifique-se de ter o Groovy ou o Gradle instalado.
2. Para executar via Groovy:
   ```
   groovy src/main/groovy/Main.groovy
   ```
3. Para executar via Gradle:
   ```
   ./gradlew run
   ```

## Funcionalidades
- Listagem de candidatos e empresas pré-cadastrados
- Menu interativo no terminal

## Estrutura
- src/main/groovy/Pessoa.groovy: Interface base
- src/main/groovy/PessoaImpl.groovy: Classe abstrata base
- src/main/groovy/Candidato.groovy: Classe de candidato
- src/main/groovy/Empresa.groovy: Classe de empresa
- src/main/groovy/Main.groovy: Programa principal

## Observações
Este projeto é um MVP e pode ser expandido para cadastro dinâmico de candidatos e empresas.

