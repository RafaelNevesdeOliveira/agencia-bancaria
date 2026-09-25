package br.edu.fiap.desafiobancario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada do desafio bancário.
 *
 * <p>O Spring Boot procura componentes a partir deste pacote. Por isso, as
 * futuras camadas {@code controller}, {@code service}, {@code repository} e
 * {@code entity} devem permanecer abaixo de {@code br.edu.fiap.desafiobancario}.</p>
 */
@SpringBootApplication
public class DesafioBancarioCorrecaoApplication {

    /** Inicia o contexto Spring e o servidor HTTP embutido. */
    public static void main(String[] args) {
        SpringApplication.run(DesafioBancarioCorrecaoApplication.class, args);
    }
}

