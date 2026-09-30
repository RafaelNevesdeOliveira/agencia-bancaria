package br.edu.fiap.desafiobancario.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
/**
 * Configuração central dos metadados exibidos pelo Swagger.
 *
 * <p>Esta classe pertence à camada {@code config} porque configura uma
 * integração compartilhada da aplicação. Ela não contém regra bancária nem
 * acesso ao PostgreSQL.</p>
 */
@Configuration
// Registra no Swagger o cadeado "Authorize". O nome bearerAuth é o mesmo
// usado em @SecurityRequirement nos endpoints protegidos.
@SecurityScheme(
        // Identificador interno do esquema. Não é o texto que o aluno cola.
        name = "bearerAuth",
        // Autenticação HTTP, não API key nem OAuth2 com tela de consentimento.
        type = SecuritySchemeType.HTTP,
        // O Swagger envia Authorization: Bearer <token>.
        scheme = "bearer",
        // Só documenta o formato. A validação real fica no JwtDecoder.
        bearerFormat = "JWT",
        description = "Cole somente o accessToken retornado por POST /api/auth/login")
// Metadados da página do Swagger: título, versão, contato e grupos de rotas.
@OpenAPIDefinition(
        info = @Info(
                title = "API Agência Bancária - Aulas 25 a 33",
                version = "1.0.0",
                description = "API didática com Pessoa, TipoConta, ContaBancaria e Usuario. "
                        + "O cadastro de usuário protege a senha com BCrypt e não a devolve. "
                        + "A Aula 33 adiciona login e autenticação JWT HS256. "
                        + "Construída com Spring Boot, Spring Data JPA e PostgreSQL.",
                // Quem aparece no bloco de contato da documentação.
                contact = @Contact(name = "FIAP - Trilha API Spring Boot"),
                // Licença exibida no rodapé do info. Aqui é só uso de aula.
                license = @License(name = "Uso didático")
        ),
        // Cada tag vira um grupo na interface. O name precisa ser igual
        // ao @Tag dos controllers para as rotas caírem no grupo certo.
        tags = {
                @Tag(
                        name = "Pessoas",
                        description = "Cadastro dos titulares das contas"),
                @Tag(
                        name = "Contas bancárias",
                        description = "Abertura, consulta, depósito e saque"),
                @Tag(
                        name = "Usuários",
                        description = "Cadastro e consulta segura de usuários, sem senha"),
                @Tag(
                        name = "Autenticação",
                        description = "Login, emissão do JWT e validação do Bearer Token")
        }
)
public class OpenApiConfig {

    /** Cria a configuração declarada pelas anotações OpenAPI. */
    public OpenApiConfig() {
    }
}
