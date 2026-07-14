# 📚 Biblioteca Cesumar — Sistema Web Java

Sistema web para gerenciamento de acervo bibliográfico desenvolvido com
**Java · Servlets · JSP · JSF · Maven · JDBC · SQLite**, seguindo o padrão **MVC**.

---

## 🗂 Estrutura do Projeto

```
biblioteca-cesumar/
│
├── pom.xml                          ← Configuração Maven (dependências)
│
└── src/main/
    ├── java/
    │   ├── model/
    │   │   └── Livro.java           ← Entidade (MODEL)
    │   ├── dao/
    │   │   ├── ConexaoSQLite.java   ← Conexão e criação da tabela (MODEL)
    │   │   └── LivroDAO.java        ← Acesso a dados via JDBC (MODEL)
    │   └── controller/
    │       ├── LivroServlet.java    ← Servlet Controller (CONTROLLER)
    │       └── LivroBean.java      ← Managed Bean JSF (CONTROLLER)
    │
    └── webapp/
        ├── index.jsp                ← Redirecionamento inicial
        ├── cadastro.xhtml           ← Formulário JSF (VIEW)
        ├── lista.xhtml              ← Listagem JSF (VIEW)
        ├── css/
        │   └── estilo.css           ← Folha de estilos
        ├── views/
        │   ├── lista.jsp            ← Listagem via Servlet+JSP (VIEW)
        │   └── cadastro.jsp         ← Formulário via Servlet+JSP (VIEW)
        └── WEB-INF/
            └── web.xml              ← Descritor de implantação
```

---

## ✅ Pré-requisitos

| Ferramenta | Versão mínima |
|------------|---------------|
| Java (JDK) | 17            |
| Maven      | 3.8+          |

---

## 🚀 Como executar

### Opção 1 — Maven + Cargo (mais simples, sem instalar Tomcat)

```bash
# 1. Entre na pasta do projeto
cd biblioteca-cesumar

# 2. Compile e inicie o servidor embutido
mvn package cargo:run

# 3. Acesse no navegador
# http://localhost:8080/biblioteca-cesumar
```

### Opção 2 — Tomcat externo (Apache Tomcat 10+)

```bash
# 1. Gere o arquivo WAR
mvn clean package

# 2. Copie o WAR para o Tomcat
cp target/biblioteca-cesumar.war /caminho/para/tomcat/webapps/

# 3. Inicie o Tomcat
/caminho/para/tomcat/bin/startup.sh   # Linux/Mac
/caminho/para/tomcat/bin/startup.bat  # Windows

# 4. Acesse no navegador
# http://localhost:8080/biblioteca-cesumar
```

### Opção 3 — IntelliJ IDEA (recomendado para desenvolvimento)

1. Abra o IntelliJ IDEA → **File → Open** → selecione a pasta `biblioteca-cesumar`
2. Aguarde o Maven baixar as dependências
3. Vá em **Run → Edit Configurations**
4. Clique em **+** → **Tomcat Server → Local**
5. Em **Application server**, configure o caminho do Tomcat 10
6. Na aba **Deployment**, clique em **+** → **Artifact** → selecione `biblioteca-cesumar:war exploded`
7. Clique em **Run** ▶

---

## 🌐 Páginas do sistema

| URL | Descrição |
|-----|-----------|
| `/biblioteca-cesumar/` | Redireciona para o acervo |
| `/biblioteca-cesumar/livros` | Lista de livros (JSP + Servlet) |
| `/biblioteca-cesumar/livros?acao=form` | Formulário de cadastro (JSP) |
| `/biblioteca-cesumar/cadastro.xhtml` | Formulário de cadastro (**JSF**) |
| `/biblioteca-cesumar/lista.xhtml` | Lista com exclusão (**JSF**) |

---

## 🏗 Padrão MVC aplicado

```
[Usuário]
    │
    ▼
[VIEW — JSP / JSF]
    │  Envia requisição (formulário, clique)
    ▼
[CONTROLLER — LivroServlet / LivroBean]
    │  Valida dados e aciona o DAO
    ▼
[MODEL — Livro + LivroDAO]
    │  Retorna dados processados
    ▼
[CONTROLLER] → [VIEW] → [Usuário vê o resultado]
```

---

## ✔️ Checklist de funcionalidades

- [x] Cadastro de livros (Título, Autor, Ano, ISBN)
- [x] Listagem de todos os livros
- [x] Exclusão por ID
- [x] Validação de campos vazios
- [x] Validação de ISBN (10 ou 13 dígitos)
- [x] Validação de ano inválido
- [x] Validação de ISBN duplicado
- [x] Interface com JSP + Servlet (clássico)
- [x] Interface com JSF + Managed Bean
- [x] Mensagens de sucesso e erro ao usuário
- [x] Código organizado em pacotes model / controller / dao
- [x] Comentários explicativos em todo o código
- [x] Persistência real em banco de dados via JDBC + SQLite
- [x] Uso de PreparedStatement em todas as consultas (proteção contra SQL Injection)
- [x] Restrição UNIQUE de ISBN aplicada também no nível do banco de dados

---

## 📝 Observações técnicas

- O armazenamento dos livros é feito em um **banco de dados SQLite real**, via JDBC
  (`ConexaoSQLite` + `LivroDAO`). O banco é um único arquivo (`biblioteca.db`), criado
  automaticamente na primeira execução — não é necessário instalar nenhum servidor
  de banco de dados separado. Os dados persistem entre reinicializações do servidor.
- Todas as consultas usam `PreparedStatement`, o que evita SQL Injection: os valores
  vindos do formulário nunca são concatenados diretamente na string do SQL.
- A coluna `isbn` tem uma restrição `UNIQUE` no banco, como segunda camada de proteção
  contra duplicatas (além da validação já feita na camada de aplicação).
- O projeto usa **Jakarta EE 10** (namespace `jakarta.*`), compatível com **Tomcat 10+**.
  Se usar Tomcat 9 ou inferior, troque os namespaces para `javax.*` e ajuste as versões no `pom.xml`.
- Três livros de exemplo são cadastrados automaticamente na primeira execução, apenas
  se o banco ainda estiver vazio (ver `LivroDAO.java`).
