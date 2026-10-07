package br.com.bikepadrao.dto.relatorios;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioMarcaDTO {

        private Long categoriaId;

        private String marca;

        private Long quantidadeProdutos;

}
