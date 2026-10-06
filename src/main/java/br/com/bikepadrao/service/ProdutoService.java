package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.ProdutoCardDTO;
import br.com.bikepadrao.dto.ProdutoDetalheDTO;
import br.com.bikepadrao.dto.ProdutoEspecificacaoDTO;
import br.com.bikepadrao.entity.Produto;
import br.com.bikepadrao.repository.ProdutoEspecificacaoRepository;
import br.com.bikepadrao.repository.ProdutoImagemRepository;
import br.com.bikepadrao.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.bikepadrao.dto.ProdutoImagemDTO;
import br.com.bikepadrao.entity.ProdutoImagem;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoImagemRepository produtoImagemRepository;
    private final ProdutoEspecificacaoRepository produtoEspecificacaoRepository;

    public List<ProdutoCardDTO> listarCatalogo(
            String busca,
            String categoria,
            String marca
    ) {

        busca = normalizarBusca(busca);
        categoria = normalizarFiltro(categoria);
        marca = normalizarFiltro(marca);

        List<Produto> produtos =
                produtoRepository.buscarCatalogo(
                        busca,
                        categoria,
                        marca
                );

        return paraCards(produtos);
    }


    public List<ProdutoCardDTO> listarDestaques() {

        List<Produto> produtos =
                produtoRepository.buscarDestaques();

        return paraCards(produtos);
    }

    private List<ProdutoCardDTO> paraCards(
            List<Produto> produtos
    ) {

        if (produtos.isEmpty()) {
            return List.of();
        }

        List<Long> ids = produtos
                .stream()
                .map(Produto::getId)
                .toList();


        Map<Long, String> imagensPrincipais =
                produtoImagemRepository
                        .buscarPrincipaisPorProdutoIds(ids)
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        imagem ->
                                                imagem
                                                        .getProduto()
                                                        .getId(),

                                        ProdutoImagem::getUrl,

                                        (primeira, segunda) ->
                                                primeira
                                )
                        );


        return produtos
                .stream()
                .map(produto ->
                        paraCardDTO(
                                produto,
                                imagensPrincipais
                                        .get(produto.getId())
                        )
                )
                .toList();
    }


    public Optional<ProdutoDetalheDTO> buscarPorSlug(
            String slug
    ) {

        return produtoRepository
                .buscarDetalhePorSlug(slug)
                .map(this::paraDetalheDTO);
    }


    private ProdutoCardDTO paraCardDTO(
            Produto produto,
            String imagemPrincipal
    ) {

        return new ProdutoCardDTO(
                produto.getId(),
                produto.getNome(),
                produto.getSlug(),
                produto.getSku(),
                produto.getDescricaoCurta(),
                produto.getPreco(),
                produto.getPrecoPromocional(),
                produto.getEstoque(),

                produto.getCategoria().getNome(),

                produto.getMarca() != null
                        ? produto.getMarca().getNome()
                        : null,

                imagemPrincipal
        );
    }


    private ProdutoDetalheDTO paraDetalheDTO(
            Produto produto
    ) {

        List<ProdutoImagemDTO> imagens =
                produtoImagemRepository
                        .findByProdutoIdOrderByOrdemAsc(
                                produto.getId()
                        )
                        .stream()
                        .sorted(
                                Comparator
                                        .comparing(
                                                ProdutoImagem::getPrincipal,
                                                Comparator.reverseOrder()
                                        )
                                        .thenComparing(
                                                ProdutoImagem::getOrdem
                                        )
                        )
                        .map(imagem ->
                                new ProdutoImagemDTO(
                                        imagem.getId(),
                                        imagem.getUrl(),
                                        imagem.getOrdem(),
                                        imagem.getPrincipal()
                                )
                        )
                        .toList();


        List<ProdutoEspecificacaoDTO> especificacoes =
                produtoEspecificacaoRepository
                        .findByProdutoIdOrderByOrdemAsc(
                                produto.getId()
                        )
                        .stream()
                        .map(especificacao ->
                                new ProdutoEspecificacaoDTO(
                                        especificacao.getId(),
                                        especificacao.getNome(),
                                        especificacao.getValor(),
                                        especificacao.getOrdem()
                                )
                        )
                        .toList();


        return new ProdutoDetalheDTO(
                produto.getId(),
                produto.getNome(),
                produto.getSlug(),
                produto.getSku(),
                produto.getDescricaoCurta(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getPrecoPromocional(),
                produto.getEstoque(),

                produto.getCategoria().getNome(),

                produto.getMarca() != null
                        ? produto.getMarca().getNome()
                        : null,

                imagens,

                especificacoes
        );
    }

    private String normalizarBusca(String busca) {

        if (busca == null || busca.isBlank()) {
            return "";
        }

        return busca
                .trim()
                .toLowerCase();
    }


    private String normalizarFiltro(String filtro) {

        if (filtro == null || filtro.isBlank()) {
            return "";
        }

        return filtro.trim();
    }
}