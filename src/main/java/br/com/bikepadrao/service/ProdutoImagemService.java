package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.ProdutoImagemDTO;
import br.com.bikepadrao.entity.Produto;
import br.com.bikepadrao.entity.ProdutoImagem;
import br.com.bikepadrao.repository.ProdutoImagemRepository;
import br.com.bikepadrao.repository.ProdutoRepository;
import br.com.bikepadrao.storage.ImagemStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoImagemService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoImagemRepository produtoImagemRepository;

    private final ImagemStorageService imagemStorageService;


    @Transactional
    public ProdutoImagem adicionar(
            Long produtoId,
            MultipartFile arquivo,
            boolean principal
    ) {

        Produto produto = produtoRepository
                .findById(produtoId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Produto não encontrado."
                        )
                );


        String url =
                imagemStorageService.salvar(arquivo);


        try {

            if (principal) {

                produto.getImagens()
                        .forEach(imagem ->
                                imagem.setPrincipal(false)
                        );
            }


            int proximaOrdem =
                    produto.getImagens()
                            .stream()
                            .mapToInt(
                                    ProdutoImagem::getOrdem
                            )
                            .max()
                            .orElse(-1)
                            + 1;


            ProdutoImagem imagem =
                    new ProdutoImagem();

            imagem.setProduto(produto);

            imagem.setUrl(url);

            imagem.setOrdem(proximaOrdem);

            imagem.setPrincipal(
                    principal
                            || produto.getImagens().isEmpty()
            );


            return produtoImagemRepository.save(
                    imagem
            );

        } catch (RuntimeException e) {

            imagemStorageService.excluir(url);

            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<ProdutoImagemDTO> listarPorProduto(
            Long produtoId
    ) {

        return produtoImagemRepository
                .findByProdutoIdOrderByOrdemAsc(
                        produtoId
                )
                .stream()
                .map(imagem ->
                        new ProdutoImagemDTO(
                                imagem.getId(),
                                imagem.getUrl(),
                                imagem.getOrdem(),
                                imagem.getPrincipal()
                        )
                )
                .toList();
    }

    @Transactional
    public void mover(
            Long produtoId,
            Long imagemId,
            String direcao
    ) {

        List<ProdutoImagem> imagens =
                produtoImagemRepository
                        .findByProdutoIdOrderByOrdemAsc(
                                produtoId
                        );


        int indiceAtual = -1;


        for (int i = 0; i < imagens.size(); i++) {

            if (
                    imagens
                            .get(i)
                            .getId()
                            .equals(imagemId)
            ) {

                indiceAtual = i;
                break;
            }
        }


        if (indiceAtual == -1) {

            throw new IllegalArgumentException(
                    "Imagem não encontrada para este produto."
            );
        }


        int indiceDestino;


        switch (direcao) {

            case "cima" ->
                    indiceDestino =
                            indiceAtual - 1;

            case "baixo" ->
                    indiceDestino =
                            indiceAtual + 1;

            default ->
                    throw new IllegalArgumentException(
                            "Direção inválida."
                    );
        }


        /*
         * Se já estiver na primeira ou última posição,
         * não há nada para mover.
         */

        if (
                indiceDestino < 0 ||
                        indiceDestino >= imagens.size()
        ) {

            return;
        }


        ProdutoImagem atual =
                imagens.get(indiceAtual);

        ProdutoImagem destino =
                imagens.get(indiceDestino);


        int ordemAtual =
                atual.getOrdem();

        int ordemDestino =
                destino.getOrdem();


        atual.setOrdem(
                ordemDestino
        );

        destino.setOrdem(
                ordemAtual
        );


        produtoImagemRepository.save(atual);
        produtoImagemRepository.save(destino);
    }
    
    @Transactional
    public void excluir(
            Long produtoId,
            Long imagemId
    ) {

        ProdutoImagem imagem =
                produtoImagemRepository
                        .findById(imagemId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Imagem não encontrada."
                                )
                        );


        if (
                !imagem
                        .getProduto()
                        .getId()
                        .equals(produtoId)
        ) {

            throw new IllegalArgumentException(
                    "Imagem inválida."
            );
        }


        String url = imagem.getUrl();

        boolean eraPrincipal =
                Boolean.TRUE.equals(
                        imagem.getPrincipal()
                );


        produtoImagemRepository.delete(imagem);

        produtoImagemRepository.flush();


        if (eraPrincipal) {

            produtoImagemRepository
                    .findByProdutoIdOrderByOrdemAsc(
                            produtoId
                    )
                    .stream()
                    .findFirst()
                    .ifPresent(proxima ->
                            proxima.setPrincipal(true)
                    );
        }


        imagemStorageService.excluir(url);
    }
}