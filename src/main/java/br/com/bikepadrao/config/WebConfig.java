package br.com.bikepadrao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String diretorioProdutos;


    public WebConfig(
            @Value("${storage.produtos-dir}")
            String diretorioProdutos
    ) {
        this.diretorioProdutos = diretorioProdutos;
    }


    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        String localizacao = Path
                .of(diretorioProdutos)
                .toAbsolutePath()
                .normalize()
                .toUri()
                .toString();

        registry
                .addResourceHandler(
                        "/uploads/produtos/**"
                )
                .addResourceLocations(localizacao);
    }
}