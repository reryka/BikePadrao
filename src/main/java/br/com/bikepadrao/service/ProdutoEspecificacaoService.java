package br.com.bikepadrao.service;

import br.com.bikepadrao.dto.ProdutoEspecificacaoDTO;
import br.com.bikepadrao.entity.Produto;
import br.com.bikepadrao.entity.ProdutoEspecificacao;
import br.com.bikepadrao.repository.ProdutoEspecificacaoRepository;
import br.com.bikepadrao.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoEspecificacaoService {

    private final ProdutoRepository produtoRepository;

    private final ProdutoEspecificacaoRepository
            produtoEspecificacaoRepository;


    @Transactional(readOnly = true)
    public List<ProdutoEspecificacaoDTO>
    listarPorProduto(Long produtoId) {

        return produtoEspecificacaoRepository
                .findByProdutoIdOrderByOrdemAsc(
                        produtoId
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
    }


    @Transactional
    public void adicionar(
            Long produtoId,
            String nome,
            String valor
    ) {

        if (nome == null || nome.isBlank()) {

            throw new IllegalArgumentException(
                    "Informe o nome da especificação."
            );
        }


        if (valor == null || valor.isBlank()) {

            throw new IllegalArgumentException(
                    "Informe o valor da especificação."
            );
        }


        Produto produto =
                produtoRepository
                        .findById(produtoId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Produto não encontrado."
                                )
                        );


        List<ProdutoEspecificacao> atuais =
                produtoEspecificacaoRepository
                        .findByProdutoIdOrderByOrdemAsc(
                                produtoId
                        );


        int proximaOrdem =
                atuais
                        .stream()
                        .mapToInt(
                                ProdutoEspecificacao::getOrdem
                        )
                        .max()
                        .orElse(-1)
                        + 1;


        ProdutoEspecificacao especificacao =
                new ProdutoEspecificacao();

        especificacao.setProduto(produto);

        especificacao.setNome(
                nome.trim()
        );

        especificacao.setValor(
                valor.trim()
        );

        especificacao.setOrdem(
                proximaOrdem
        );


        produtoEspecificacaoRepository
                .save(especificacao);
    }


    @Transactional
    public void excluir(
            Long produtoId,
            Long especificacaoId
    ) {

        ProdutoEspecificacao especificacao =
                produtoEspecificacaoRepository
                        .findById(especificacaoId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Especificação não encontrada."
                                )
                        );


        if (
                !especificacao
                        .getProduto()
                        .getId()
                        .equals(produtoId)
        ) {

            throw new IllegalArgumentException(
                    "Especificação inválida."
            );
        }


        produtoEspecificacaoRepository
                .delete(especificacao);
    }
}