package br.com.bikepadrao.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProdutoFormDTO {

    private Long id;

    @NotBlank(message = "Informe o nome.")
    private String nome;

    @NotBlank(message = "Informe o slug.")
    private String slug;

    @NotBlank(message = "Informe o SKU.")
    private String sku;

    private String descricaoCurta;

    private String descricao;

    @NotNull(message = "Informe o preço.")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
    private BigDecimal preco;

    @DecimalMin(value = "0.00", message = "O preço promocional não pode ser negativo.")
    private BigDecimal precoPromocional;

    @NotNull(message = "Informe o estoque.")
    @Min(value = 0, message = "O estoque não pode ser negativo.")
    private Integer estoque;

    @NotNull(message = "Selecione uma categoria.")
    private Long categoriaId;

    private Long marcaId;

    private Boolean ativo = true;

    private Boolean destaque = false;
}