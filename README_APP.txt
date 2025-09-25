│   ├── ts/                # Arquivos TypeScript (lógica do app)
│   ├── components/        # Componentes JS/TS puros (Cards, Formulários, etc)
│   └── main.ts            # Script principal do app
├── package.json           # Dependências e scripts
├── tsconfig.json          # Configuração do TypeScript
└── README.md              # Documentação do frontend
```

## Tecnologias Permitidas
- **TypeScript**: Para lógica e tipagem.
- **Bibliotecas JS**: (ex: Axios para requisições, dayjs para datas, etc)
- **Pré-processadores CSS**: (ex: SASS, LESS, PostCSS)
- **HTML/CSS/JS puros**: Sem frameworks como React, Vue ou Angular.

## Funcionamento do MVP
- O frontend é uma SPA simples ou páginas HTML que consomem a API REST do backend Groovy.
- Interação via DOM: manipulação de elementos, eventos e renderização dinâmica usando JS/TS puro.
- Ações de curtida, cadastro e matches são feitas por requisições HTTP (fetch/Axios).
- Privacidade: exibe apenas dados públicos até o match, seguindo a lógica do backend.

## Organização dos Scripts
- **main.ts**: Inicializa o app, gerencia navegação e eventos globais.
- **components/**: Scripts para criar/exibir cards, formulários, listas, etc.
- **services/**: Funções para consumir a API (ex: listar candidatos, curtir, buscar matches).
- **utils/**: Funções auxiliares (validação, formatação, etc).

## Exemplo de Serviço TypeScript
```typescript
// src/services/candidatoService.ts
import axios from 'axios';

export async function listarCandidatos() {
  const response = await axios.get('/api/candidatos');
  return response.data;
}
```

## Exemplo de Componente JS/TS Puro
```typescript
// src/components/CardCandidato.ts
export function criarCardCandidato(candidato) {
  const card = document.createElement('div');
  card.className = 'card-candidato';
  card.innerHTML = `<h3>${candidato.nome}</h3><ul>${candidato.competencias.map(c => `<li>${c}</li>`).join('')}</ul>`;
  return card;
}
```

## Estilização
- Use CSS puro ou pré-processador (SASS, LESS, etc) para estilizar componentes.
- Compile o CSS usando scripts no package.json (ex: `sass src/css:public/css`).

## Boas Práticas
- Separe lógica de apresentação (componentes) da lógica de negócio (serviços).
- Use tipagem forte no TypeScript para evitar erros.
- Documente endpoints e fluxos principais.
- Teste scripts e integração manualmente.

## Integração
- O frontend pode ser hospedado separadamente ou junto ao backend.
- Para desenvolvimento, rode backend e frontend em servidores locais diferentes e configure CORS.

---

Assim, o MVP do Linketinder terá um frontend leve, organizado e seguro, sem frameworks, apenas com bibliotecas e compiladores permitidos.
