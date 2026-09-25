package br.edu.fiap.desafiobancario.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

/**
 * Configura os metadados exibidos no Swagger UI.
 *
 * <p>A classe fica em {@code config} porque cuida de uma integração da
 * aplicação e não participa das regras bancárias.</p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Desafio Bancário Correção",
                version = "1.0.0",
                description = "API bancária para implementar entidades, repositories, services e controllers.",
                contact = @Contact(name = "FIAP - Trilha API Spring Boot"),
                license = @License(name = "Uso didático")
        ),
        tags = {
                @Tag(name = "Pessoas", description = "Cadastro dos titulares"),
                @Tag(name = "Contas bancárias", description = "Abertura, consulta e movimentação de contas")
        }
)
public class OpenApiConfig {

    /** Cria a configuração declarada pelas anotações OpenAPI. */
    public OpenApiConfig() {
    }
}
