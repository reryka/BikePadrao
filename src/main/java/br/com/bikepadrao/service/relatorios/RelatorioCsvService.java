package br.com.bikepadrao.service.relatorios;

import br.com.bikepadrao.dto.relatorios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RelatorioCsvService {

    private final RelatorioService relatorioService;


    public byte[] exportarEstoqueCsv() {

        RelatorioEstoqueResumoDTO relatorio =
                relatorioService.buscarEstoque();


        StringBuilder csv = new StringBuilder();

        /*
         * BOM UTF-8.
         * Evita problemas com acentos ao abrir no Excel.
         */
        csv.append('\uFEFF');


        csv.append(
                "Produto;SKU;Categoria;Marca;Estoque;Preço;Valor em estoque;Status\n"
        );


        for (RelatorioEstoqueDTO produto :
                relatorio.getProdutos()) {

            csv.append(valorCsv(produto.getNome()))
                    .append(";")

                    .append(valorCsv(produto.getSku()))
                    .append(";")

                    .append(valorCsv(produto.getCategoria()))
                    .append(";")

                    .append(valorCsv(
                            produto.getMarca() != null
                                    ? produto.getMarca()
                                    : ""
                    ))
                    .append(";")

                    .append(produto.getEstoque())
                    .append(";")

                    .append(formatarMoeda(produto.getPreco()))
                    .append(";")

                    .append(formatarMoeda(
                            produto.getValorEstoque()
                    ))
                    .append(";")

                    .append(formatarStatus(
                            produto.getStatusEstoque()
                    ))

                    .append("\n");
        }


        csv.append("\n");

        csv.append(
                ";;;;;;Valor total do estoque;"
        );

        csv.append(
                formatarMoeda(
                        relatorio.getValorTotalEstoque()
                )
        );

        csv.append("\n");


        return csv
                .toString()
                .getBytes(StandardCharsets.UTF_8);
    }


    private String formatarMoeda(
            BigDecimal valor
    ) {

        if (valor == null) {
            return "0,00";
        }


        NumberFormat formato =
                NumberFormat.getNumberInstance(
                        new Locale("pt", "BR")
                );

        formato.setMinimumFractionDigits(2);

        formato.setMaximumFractionDigits(2);


        return formato.format(valor);
    }


    private String formatarStatus(
            String status
    ) {

        if (status == null) {
            return "";
        }


        return switch (status) {

            case "SEM_ESTOQUE" ->
                    "Sem estoque";

            case "ESTOQUE_BAIXO" ->
                    "Estoque baixo";

            case "NORMAL" ->
                    "Normal";

            default ->
                    status;
        };
    }


    private String valorCsv(String valor) {

        if (valor == null)
            return "";

        String valorTratado = valor.replace("\"", "\"\"");

        return "\"" + valorTratado + "\"";
    }

    public byte[] exportarCategoriasCsv() {

        StringBuilder csv = new StringBuilder();

        csv.append('\uFEFF');
        csv.append("Categoria;Quantidade de produtos\n");

        for (RelatorioCategoriaDTO categoria : relatorioService.buscarProdutosPorCategoria()) {
            csv.append(valorCsv(categoria.getCategoria()));
            csv.append(";");
            csv.append(categoria.getQuantidadeProdutos());
            csv.append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportarMarcasCsv() {

        StringBuilder csv = new StringBuilder();

        csv.append('\uFEFF');
        csv.append("Marca;Quantidade de produtos\n");

        for (RelatorioMarcaDTO marca : relatorioService.buscarProdutosPorMarca()) {
            csv.append(valorCsv(marca.getMarca()));
            csv.append(";");
            csv.append(marca.getQuantidadeProdutos());
            csv.append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportarSemImagemCsv() {
        return exportarProdutosIncompletosCsv(relatorioService.buscarProdutosSemImagem());
    }


    public byte[] exportarSemEspecificacoesCsv() {
        return exportarProdutosIncompletosCsv(relatorioService.buscarProdutosSemEspecificacao());
    }


    private byte[] exportarProdutosIncompletosCsv(List<RelatorioProdutoIncompletoDTO> produtos) {

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("Produto;SKU;Categoria;Marca;Status\n");

        for (RelatorioProdutoIncompletoDTO produto : produtos) {
            csv.append(valorCsv(produto.getNome()))
                    .append(";")
                    .append(valorCsv(produto.getSku()))
                    .append(";")
                    .append(valorCsv(produto.getCategoria()))
                    .append(";")
                    .append(valorCsv(produto.getMarca() != null ? produto.getMarca() : ""))
                    .append(";")
                    .append(Boolean.TRUE.equals(produto.getAtivo()) ? "Ativo" : "Inativo")
                    .append("\n");
        }
        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportarRecentesCsv() {

        StringBuilder csv = new StringBuilder();
        csv.append('\uFEFF');
        csv.append("Data de cadastro;Produto;SKU;Categoria;Marca;Status\n");

        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (RelatorioProdutoRecenteDTO produto : relatorioService.buscarProdutosRecentes()) {
            csv.append(produto.getDataCriacao() != null ? produto.getDataCriacao().format(formatoData) : "")
                    .append(";")
                    .append(valorCsv(produto.getNome()))
                    .append(";")
                    .append(valorCsv(produto.getSku()))
                    .append(";")
                    .append(valorCsv(produto.getCategoria()))
                    .append(";")
                    .append(valorCsv(produto.getMarca() != null ? produto.getMarca() : ""))
                    .append(";")
                    .append(Boolean.TRUE.equals(produto.getAtivo()) ? "Ativo" : "Inativo")
                    .append("\n");
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }
}