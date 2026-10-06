package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProdutoCardDTO {

    private Long id;

    private String nome;

    private String slug;

    private String sku;

    private String descricaoCurta;

    private BigDecimal preco;

    private BigDecimal precoPromocional;

    private Integer estoque;

    private String categoria;

    private String marca;

    private String imagemPrincipal;
}