package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProdutoDetalheDTO {

    private Long id;

    private String nome;

    private String slug;

    private String sku;

    private String descricaoCurta;

    private String descricao;

    private BigDecimal preco;

    private BigDecimal precoPromocional;

    private Integer estoque;

    private String categoria;

    private String categoriaSlug;

    private String marca;

    private String marcaSlug;

    private List<ProdutoImagemDTO> imagens;

    private List<ProdutoEspecificacaoDTO> especificacoes;
}