package br.edu.fiap.desafiobancario.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {

    /**
     * Converte o segredo Base64 da propriedade em uma chave HMAC.
     *
     * <p>HS256 exige uma chave com pelo menos 256 bits. A aplicação recusa uma
     * chave menor para evitar uma configuração silenciosamente fraca.</p>
     */
    @Bean
    SecretKey jwtSecretKey(@Value("${security.jwt.secret}") String secretBase64) {
        byte[] secret = Base64.getDecoder().decode(secretBase64);
        if (secret.length < 32) {
            throw new IllegalArgumentException(
                    "JWT_SECRET deve possuir pelo menos 32 bytes após o Base64.");
        }
        return new SecretKeySpec(secret, "HmacSHA256");
    }

    /** Cria o componente que monta a assinatura do token com HS256. */
    @Bean
    JwtEncoder jwtEncoder(SecretKey jwtSecretKey) {
        return NimbusJwtEncoder.withSecretKey(jwtSecretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }


    /**
     * Cria o componente que valida assinatura, expiração e emissor.
     *
     * <p>O decoder recebe a mesma chave do encoder. Se alguém alterar uma
     * claim, usar outra chave, trocar o emissor ou enviar um token expirado, o
     * Spring Security encerra a requisição antes do controller.</p>
     */
    @Bean
    JwtDecoder jwtDecoder(
            SecretKey jwtSecretKey,
//            COFRE DE SENHAS
            @Value("${security.jwt.issuer}") String issuer) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        return decoder;
    }

}
