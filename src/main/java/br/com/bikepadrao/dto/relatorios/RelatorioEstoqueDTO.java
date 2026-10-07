package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RelatorioEstoqueDTO {

    private Long id;

    private String nome;

    private String sku;

    private String categoria;

    private String marca;

    private Integer estoque;

    private BigDecimal preco;

    private Boolean ativo;


    public BigDecimal getValorEstoque() {

        if (preco == null || estoque == null) {
            return BigDecimal.ZERO;
        }

        return preco.multiply(
                BigDecimal.valueOf(estoque)
        );
    }


    public String getStatusEstoque() {

        if (estoque == null || estoque <= 0) {
            return "SEM_ESTOQUE";
        }

        if (estoque <= 5) {
            return "ESTOQUE_BAIXO";
        }

        return "NORMAL";
    }
}