# Linketinder

## Autor
Azulario

## Descrição
Projeto MVP de backend para sistema de contratação inspirado no Linkedin e Tinder, desenvolvido em Groovy. Permite listar candidatos e empresas, cada um com suas competências e dados principais. Estrutura orientada a objetos, utilizando interface e herança.

> **Este projeto está em desenvolvimento para fins de aprendizado e aplicação dos conhecimentos adquiridos no programa Acelera ZG.**

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

## Como rodar os testes automatizados

1. Certifique-se de estar na raiz do projeto.
2. Execute:
   ```
   ./gradlew test
   ```
3. Os relatórios de teste estarão disponíveis em:
   - build/reports/tests/test/index.html (relatório HTML)
   - build/test-results/test/ (arquivos XML)

### Rodar um teste específico

Para rodar um teste específico, use:
```
./gradlew test --tests nome.completo.da.ClasseDeTeste
```
Exemplo:
```
./gradlew test --tests br.com.linketinder.ui.CadastroEmpresaUiMockTest
```

## Funcionalidades
- Listagem de candidatos e empresas pré-cadastrados
- Menu interativo no terminal
- Cadastro de novos candidatos e empresas (com validação de duplicidade)
- Sistema de curtidas e match entre candidatos e empresas
- Testes unitários completos (Spock)
- Simulação de entrada de dados via mocks nos testes
- Separação de responsabilidades por domínio, serviço e UI

## Estrutura do Projeto
- `src/main/groovy/dominio`: Domínio do sistema (Candidato, Empresa, etc)
- `src/main/groovy/servico`: Serviços de negócio (SistemaMatch, CadastroService)
- `src/main/groovy/ui`: Interface de usuário, ações e utilitários
- `src/test/groovy`: Testes unitários (Spock)

## Principais Mudanças e Melhorias
- **Refatoração do cadastro**: O cadastro de candidatos e empresas foi movido para a classe de serviço `CadastroService`, removendo essa responsabilidade do `SistemaMatch`.
- **Testes TDD completos**: Todos os fluxos de cadastro, validação de duplicidade e match possuem testes unitários, seguindo TDD.
- **Mocks para entrada de dados**: Testes de UI utilizam mocks para simular entrada do usuário, garantindo testes automatizados e independentes.
- **Separação de responsabilidades**: O domínio, serviços e UI estão bem separados, facilitando manutenção e evolução.
- **Checklist de TDD**: O arquivo `CHECKLIST_TESTES.txt` documenta todos os requisitos de testes e TDD realizados.
- **Código comentado e organizado**: Todos os arquivos possuem imports organizados e comentários de autoria nos testes.

## Novidade: Sistema de Curtidas e Match

Agora o Linketinder possui um sistema de curtidas e match:
- Candidatos podem curtir vagas de empresas.
- Empresas podem curtir candidatos para vagas específicas.
- Quando há curtida recíproca para a mesma vaga, ocorre um MATCH e ambos podem visualizar a relação.

### Novas opções do menu:
- 3 - Candidato curtir vaga de empresa
- 4 - Empresa curtir candidato
- 5 - Listar matches

Use essas opções para simular o funcionamento do Linketinder e visualizar matches entre candidatos e empresas!

## Observações
Este projeto é um MVP e pode ser expandido para cadastro dinâmico de candidatos e empresas.

## Testes e TDD
- Todos os testes unitários estão em `src/test/groovy`.
- O projeto segue TDD para cadastro e validação de candidatos/empresas.
- Testes de UI utilizam mocks para simular entrada de dados.

---

Projeto desenvolvido por Azulario para fins didáticos e de demonstração de boas práticas em Groovy, TDD e arquitetura orientada a serviços no programa Acelera ZG.
