package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioResumoDTO {

    private long totalProdutos;

    private long produtosAtivos;

    private long produtosInativos;

    private long estoqueBaixo;

    private long semEstoque;

    private long totalCategorias;

    private long totalMarcas;
}