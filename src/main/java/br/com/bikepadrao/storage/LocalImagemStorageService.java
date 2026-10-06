package br.com.bikepadrao.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalImagemStorageService implements ImagemStorageService {

    private final Path diretorio;


    public LocalImagemStorageService(
            @Value("${storage.produtos-dir}")
            String diretorio
    ) {
        this.diretorio = Path
                .of(diretorio)
                .toAbsolutePath()
                .normalize();
    }


    @PostConstruct
    public void inicializar() {

        try {
            Files.createDirectories(diretorio);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível criar o diretório de imagens.",
                    e
            );
        }
    }


    @Override
    public String salvar(MultipartFile arquivo) {

        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException(
                    "O arquivo de imagem está vazio."
            );
        }

        String extensao = obterExtensao(
                arquivo.getContentType()
        );

        String nomeArquivo =
                UUID.randomUUID() + extensao;

        Path destino = diretorio
                .resolve(nomeArquivo)
                .normalize();

        if (!destino.getParent().equals(diretorio)) {
            throw new IllegalArgumentException(
                    "Caminho de arquivo inválido."
            );
        }

        try (
                InputStream inputStream =
                        arquivo.getInputStream()
        ) {

            Files.copy(
                    inputStream,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Não foi possível salvar a imagem.",
                    e
            );
        }

        return "/uploads/produtos/" + nomeArquivo;
    }


    @Override
    public void excluir(String url) {

        if (url == null || url.isBlank()) {
            return;
        }

        String nomeArquivo =
                Path.of(url).getFileName().toString();

        Path arquivo = diretorio
                .resolve(nomeArquivo)
                .normalize();

        if (!arquivo.getParent().equals(diretorio)) {
            return;
        }

        try {
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Não foi possível excluir a imagem.",
                    e
            );
        }
    }


    private String obterExtensao(
            String contentType
    ) {

        if (contentType == null) {
            throw new IllegalArgumentException(
                    "Tipo de arquivo inválido."
            );
        }

        return switch (contentType) {

            case "image/jpeg" -> ".jpg";

            case "image/png" -> ".png";

            case "image/webp" -> ".webp";

            default ->
                    throw new IllegalArgumentException(
                            "Formato de imagem não permitido."
                    );
        };
    }
}