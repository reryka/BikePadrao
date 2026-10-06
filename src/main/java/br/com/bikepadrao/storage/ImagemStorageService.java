package br.com.bikepadrao.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImagemStorageService {

    String salvar(MultipartFile arquivo);

    void excluir(String url);
}