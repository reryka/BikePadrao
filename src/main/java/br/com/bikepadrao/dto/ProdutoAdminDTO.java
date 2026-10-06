package br.com.bikepadrao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProdutoAdminDTO {

    private Long id;
    private String nome;
    private String sku;
    private BigDecimal preco;
    private Integer estoque;
    private Boolean ativo;
    private Boolean destaque;
}