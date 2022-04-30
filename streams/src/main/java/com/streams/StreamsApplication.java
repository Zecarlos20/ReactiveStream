package com.streams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

import java.util.Locale;

@SpringBootApplication
public class StreamsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(StreamsApplication.class, args);
    }

    private static final Logger log = LoggerFactory.getLogger(StreamsApplication.class);

    @Override
    public void run(String... args) throws Exception {
        Flux<String> nombres = Flux.just("zecarlos", "Jose", "Luis");
        Flux<Usuario> var = nombres.map(nombre -> new Usuario(nombre.toUpperCase(), null))
                .filter(nombre -> nombre.getNombre().equalsIgnoreCase("ZECARLOS"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException("Nombres no pueden estar vacios");
                    }
                    System.out.println(usuario.getNombre());
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });
        //nombres.subscribe();
        var.subscribe(e -> log.info(e.toString()),
                error -> log.error(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecucion del observable con exito!");
                    }
                });


    }
}
