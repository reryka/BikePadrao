package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class RelatorioEstoqueResumoDTO {

    private List<RelatorioEstoqueDTO> produtos;

    private BigDecimal valorTotalEstoque;
}