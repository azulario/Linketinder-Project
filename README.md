# Linketinder

## Autor
Nathalia Veiga 

## Descrição
Projeto MVP de backend para sistema de contratação inspirado no Linkedin e Tinder, desenvolvido em Groovy. Permite listar candidatos e empresas, cada um com suas competências e dados principais. Estrutura orientada a objetos, utilizando interface e herança.

> **Este projeto está em desenvolvimento para fins de aprendizado e aplicação dos conhecimentos adquiridos no programa Acelera ZG.**

## Como executar (Backend)

1. Certifique-se de ter o Groovy ou o Gradle instalado.
2. Para executar via Groovy:
   ```
   groovy src/main/groovy/Main.groovy
   ```
3. Para executar via Gradle:
   ```
   ./gradlew run
   ```

## Como rodar os testes automatizados (Backend)

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

## Funcionalidades (Backend)
- Listagem de candidatos e empresas pré-cadastrados
- Menu interativo no terminal
- Cadastro de novos candidatos e empresas (com validação de duplicidade)
- Sistema de curtidas e match entre candidatos e empresas
- Testes unitários completos (Spock)
- Simulação de entrada de dados via mocks nos testes
- Separação de responsabilidades por domínio, serviço e UI

## Estrutura do Projeto (Backend)
- `src/main/groovy/dominio`: Domínio do sistema (Candidato, Empresa, etc)
- `src/main/groovy/servico`: Serviços de negócio (SistemaMatch, CadastroService)
- `src/main/groovy/ui`: Interface de usuário, ações e utilitários
- `src/test/groovy`: Testes unitários (Spock)

## Principais Mudanças e Melhorias (Backend)
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

# Frontend (MVP) — TypeScript + Tailwind + Chart.js

O frontend vive em `frontend/` e é independente do backend neste MVP. Ele usa TypeScript (TS → JS), Tailwind CSS e Chart.js para visualização.

## Estrutura (frontend/)
```
frontend/
  assets/              # imagens e estáticos
  css/                 # fonte do Tailwind (tailwind.css)
  js/                  # JS compilado a partir de ts/
  public/              # páginas HTML (abra estas no navegador)
  ts/                  # código-fonte TypeScript (models, controllers)
  package.json         # scripts de build/dev
  tailwind.config.js   # config Tailwind
  tsconfig.json        # config TypeScript
  vite.config.ts       # (opcional) config do Vite
```

## Páginas principais (public/)
- `index.html`: home e navegação.
- `cadastro-candidato.html`: formulário para cadastrar candidato; lista de candidatos com delete; persiste em localStorage.
- `cadastro-empresa.html`: formulário para cadastrar empresa; lista de empresas com delete; persiste em localStorage.
- `perfil-candidato.html`: lista de vagas disponíveis (empresas anônimas); botão “Curtir” desativado (MVP).
- `perfil-empresa.html`: tabela anônima de candidatos + gráfico de competências (Chart.js).

## Persistência (localStorage)
- Chaves utilizadas:
  - `linketinder_candidatos`
  - `linketinder_empresas`
- O “banco” em `ts/utils/BancoDeDadosFake.ts` carrega do localStorage na inicialização, e salva a cada inclusão/remoção.
- Se o storage estiver vazio, o seed inicial é aplicado e salvo.

## Como rodar o frontend
Na primeira vez:
```bash
cd frontend
npm install
```

Opção A — Fluxo simples (TS + Tailwind em watch) e abrir HTMLs:
```bash
# terminal 1: compilar TS em watch
npm run watch

# terminal 2: Tailwind em watch para gerar public/styles.css
npm run dev:css

# abra no navegador via servidor do IDE (IntelliJ):
# http://localhost:63342/Linketinder-Project/frontend/public/index.html
```

Opção B — Servir com Vite (recomendado para dev):
```bash
cd frontend
# Se o script `npm run dev` falhar por causa de vite.config.mts,
# rode o Vite diretamente — ele lê o vite.config.ts automaticamente
npx vite
```
Em produção simples (sem bundle):
```bash
# gera JS e CSS
npm run build       # TS → JS (js/)
npm run build:css   # Tailwind → public/styles.css
```

## Importante: Módulos ES (ESM) no navegador
- Os controllers são carregados com `<script type="module">` a partir de `public/*.html`.
- Dentro do JS/TS, os imports usam sufixo `.js` (ex.: `import { bancoDeDadosFake } from "../utils/BancoDeDadosFake.js";`).
- Se você vir 404 em imports: confira se o caminho tem `.js` e se o script está com `type="module"`.

## Gráficos
- `perfil-empresa.html` usa Chart.js via CDN para exibir “Candidatos por Competência”.
- A lógica atual agrupa competências de todos os candidatos e contabiliza frequências.

## Troubleshooting
- 404 ao carregar módulos: garanta `.js` nos imports ESM e `<script type="module">` nos HTMLs.
- Nada aparece nas listas após cadastro: abra o Console e verifique `localStorage.getItem('linketinder_candidatos')` e `..._empresas`.
- Rodando com Vite: se `npm run dev` falhar por `vite.config.mts` ausente, use `npx vite` (o projeto tem `vite.config.ts`).
- CSS não aplica: verifique se `public/styles.css` foi gerado (`npm run dev:css`).

## Próximos Passos (Frontend)
- Implementar a lógica de “Curtir/Match”.
- Melhorar a modelagem de “Vaga” (CRUD e vínculo com Empresa em memória/localStorage).
- Filtrar/ordenar listas (por competência, formação, etc.).
- Acessibilidade e testes de UI.

---

Projeto desenvolvido por Nathalia Veiga para fins didáticos e de demonstração de boas práticas em Groovy, TDD e arquitetura orientada a serviços no programa Acelera ZG.
