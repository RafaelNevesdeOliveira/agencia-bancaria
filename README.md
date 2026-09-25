# Desafio Bancário Correção

Projeto independente dos projetos 05 e 06. Esta é uma casca compilável para
implementar uma API bancária multicamada com Java 25.0.2, Spring Boot, Spring
Data JPA e PostgreSQL local.

## O que já está pronto

- projeto Maven e aplicação Spring Boot;
- conexão com PostgreSQL local;
- configuração do Swagger e OpenAPI;
- DTOs de pessoa, conta, tipo e movimentação;
- validações de entrada;
- exceções 400, 404 e 409;
- `GlobalExceptionHandler` com JSON padronizado;
- scripts de criação, carga e consultas SQL;
- arquivo `requests.http` com o fluxo esperado.

Para a Aula 28, esta versão entregue aos alunos possui somente a estrutura:

- dependências de teste e configuração compatível com o JDK 25.0.2;
- pacotes e classes de teste já criados;
- Javadocs com objetivo, limite e cenários mínimos;
- comentários `TODO` indicando o próximo passo.

Não existem `@Test`, mocks, chamadas prontas ou assertions nesta cópia. A
implementação completa fica exclusivamente no `projeto-agencia-bancaria-gabarito`.

## Ordem usada para explicar a implementação

O gabarito já contém as camadas `entity`, `repository`, `service` e
`controller`. A sequência abaixo serve como roteiro para reconstruir e explicar
o projeto durante a aula.

## 1 Entidades

### `TipoConta`

Comece por `entity/TipoConta.java`:

- `@Entity` e `@Table(name = "tipos_conta")`;
- `id Long` como chave primária identity;
- `nome String`, obrigatório e único;
- construtor protegido para o JPA;
- construtor com `nome` e getters.

### `Pessoa`

Crie em `entity/Pessoa.java`:

- `@Entity` e `@Table(name = "pessoas")`;
- `id Long`;
- `nome String`;
- `cpf String`, obrigatório e único;
- `email String`;
- construtor protegido, construtor completo e getters.

### `ContaBancaria`

Crie em `entity/ContaBancaria.java`:

- `@Entity` e `@Table(name = "contas_bancarias")`;
- `id Long`, `agencia String`, `numero String`;
- `saldo BigDecimal` e `ativa boolean`;
- `titular Pessoa` com `@ManyToOne` e `@JoinColumn(name = "titular_id")`;
- `tipoConta TipoConta` com `@ManyToOne` e `@JoinColumn(name = "tipo_conta_id")`;
- método `depositar(BigDecimal valor)`;
- método `sacar(BigDecimal valor)`;
- validações de conta ativa, valor positivo e saldo suficiente.

O saldo deve mudar somente por `depositar` e `sacar`. Não crie um setter
público para saldo.

## 2 Repositories

### `TipoContaRepository`

```java
public interface TipoContaRepository extends JpaRepository<TipoConta, Long> {
}
```

### `PessoaRepository`

```java
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    boolean existsByCpf(String cpf);
}
```

### `ContaBancariaRepository`

```java
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    boolean existsByAgenciaAndNumero(String agencia, String numero);
    List<ContaBancaria> findByTitularId(Long titularId);
}
```

O Spring Data interpreta o nome `findByTitularId`: `find` significa buscar,
`By` inicia o filtro e `TitularId` percorre o relacionamento `titular` até o
atributo `id`.

## 3 Services

### `PessoaService`

Crie com `@Service`, injeção pelo construtor e os métodos:

```java
@Transactional
PessoaResponse cadastrar(PessoaRequest request)

@Transactional(readOnly = true)
Pessoa buscarPorId(Long id)
```

Antes de salvar, use `existsByCpf`. Se já existir, lance
`CpfJaCadastradoException`.

### `ContaBancariaService`

Injete os três repositories e implemente:

```java
@Transactional
ContaBancariaResponse abrir(ContaBancariaRequest request)

@Transactional(readOnly = true)
ContaBancariaResponse buscar(Long id)

@Transactional(readOnly = true)
List<ContaBancariaResponse> listarPorPessoa(Long pessoaId)

@Transactional
ContaBancariaResponse depositar(Long id, MovimentacaoRequest request)

@Transactional
ContaBancariaResponse sacar(Long id, MovimentacaoRequest request)
```

O service coordena repositories e transações. A entidade protege o próprio
saldo. Depois de `depositar` ou `sacar`, o dirty checking do JPA deve gerar o
`UPDATE` no commit sem exigir um novo `save`.

## 4 Controllers

### `PessoaController`

- `@RestController`;
- `@RequestMapping("/api/pessoas")`;
- `@Tag(name = "Pessoas")`;
- `POST /api/pessoas` com `@Valid`;
- devolver HTTP 201 e `PessoaResponse`.

### `ContaBancariaController`

- `@RestController`;
- `@RequestMapping("/api/contas")`;
- `@Tag(name = "Contas bancárias")`;
- `POST /api/contas`;
- `GET /api/contas/{id}`;
- `GET /api/contas/pessoa/{pessoaId}`;
- `PATCH /api/contas/{id}/depositos`;
- `PATCH /api/contas/{id}/saques`.

O controller apenas trata HTTP e chama o service. Não calcule saldo e não use
repository diretamente no controller.

## Preparar o PostgreSQL local

Execute `sql/00_criar_database.sql` conectado ao database `postgres`, com
autocommit habilitado. Depois conecte em `desafio_bancario_correcao` e execute:

1. `sql/01_criar_tabelas.sql`;
2. `sql/02_carga_dados.sql`;
3. `sql/03_consultas_evidencias.sql`.

Pelo terminal:

```bash
psql -d postgres -f sql/00_criar_database.sql
psql -d desafio_bancario_correcao -f sql/01_criar_tabelas.sql
psql -d desafio_bancario_correcao -f sql/02_carga_dados.sql
psql -d desafio_bancario_correcao -f sql/03_consultas_evidencias.sql
```

## Configurar a conexão

O padrão do `application.properties` é:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/desafio_bancario_correcao
spring.datasource.username=postgres
spring.datasource.password=
```

Se o usuário local possuir senha:

```bash
export DB_PASSWORD='SUA_SENHA'
```

## Compilar e executar

```bash
export JAVA_HOME='/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home'
mvn clean package
mvn spring-boot:run
```

- API: `http://localhost:8082`
- Swagger: `http://localhost:8082/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8082/v3/api-docs`

Antes de criar os controllers, o Swagger carregará os metadados, mas ainda não
mostrará operações da API.

## Aula 28 — ordem para criar e explicar os testes

Os arquivos estão em `src/test/java` e repetem os mesmos pacotes das classes de
produção. Essa organização facilita localizar qual classe cada teste protege.

### 1. Preparar as dependências

No `pom.xml`, use `spring-boot-starter-test` para JUnit, AssertJ e Mockito. Como
o projeto usa Spring Boot 4, use também `spring-boot-starter-webmvc-test` para
os testes da camada MVC.

O `maven-surefire-plugin` inicia o Mockito como `javaagent`. Essa configuração
é importante no JDK 25.0.2 porque evita depender do autoanexo dinâmico do agente,
que pode ser bloqueado por ambientes mais restritos.

### 2. Testar primeiro a entidade

Crie e leia nesta ordem:

1. `entity/ContaBancariaTest.java`;
2. `entity/PessoaTest.java`.

Comece criando os cenários de depósito válido e saque sem saldo. Eles devem
instanciar objetos reais e chamar diretamente `depositar` e `sacar`. Não
carregam Spring, HTTP, repository nem PostgreSQL: por isso são testes unitários
rápidos.

### 3. Testar a coordenação do service

Crie e leia:

1. `service/PessoaServiceTest.java`;
2. `service/ContaBancariaServiceTest.java`.

O service é real, mas seus repositories recebem `@Mock`. Em cada teste:

1. `when(...)` prepara o comportamento da dependência;
2. o método do service é executado;
3. AssertJ confere o retorno ou a exceção;
4. `verify(...)` comprova a colaboração esperada com o repository.

O objeto que está sendo testado nunca deve ser um mock.

### 4. Integrar a camada web

Crie e leia:

1. `controller/PessoaControllerIntegrationTest.java`;
2. `controller/ContaBancariaControllerIntegrationTest.java`.

`@WebMvcTest` sobe somente a infraestrutura MVC necessária. O `MockMvc` envia
uma requisição HTTP simulada e integra rota, JSON, Bean Validation, controller
e `GlobalExceptionHandler`. O service continua simulado com `@MockitoBean` para
que o teste seja rápido e focado no contrato HTTP.

Esse é um teste de integração da camada web, também chamado de *web slice*. Ele
não é E2E porque não inicia o servidor completo nem acessa o PostgreSQL real.

### 5. Executar com o JDK homologado

No macOS com Temurin 25.0.2:

```bash
export JAVA_HOME='/Library/Java/JavaVirtualMachines/temurin-25.jdk/Contents/Home'
export PATH="$JAVA_HOME/bin:$PATH"
mvn test
```

Executar somente uma classe:

```bash
mvn -Dtest=ContaBancariaTest test
mvn -Dtest=ContaBancariaServiceTest test
mvn -Dtest=ContaBancariaControllerIntegrationTest test
```

No estado inicial, `mvn test` apenas compila a estrutura e não executa cenários.
À medida que a turma substituir os `TODO` por testes, a contagem começará a
aparecer no relatório do Maven. Nenhum dos testes propostos exige PostgreSQL.
