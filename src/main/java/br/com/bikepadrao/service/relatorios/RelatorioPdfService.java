package br.com.bikepadrao.service.relatorios;

import br.com.bikepadrao.dto.relatorios.*;
import lombok.RequiredArgsConstructor;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class RelatorioPdfService {

    private final RelatorioService relatorioService;


    public byte[] exportarEstoquePdf() {

        RelatorioEstoqueResumoDTO relatorio = relatorioService.buscarEstoque();
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        Document documento =
                new Document(
                        PageSize.A4.rotate(),
                        30,
                        30,
                        30,
                        30
                );
        try {
            PdfWriter.getInstance(documento, saida);
            documento.open();
            adicionarCabecalho(documento);
            adicionarTabelaEstoque(documento, relatorio);
            adicionarTotal(documento, relatorio.getValorTotalEstoque());

            documento.close();

            return saida.toByteArray();
        } catch (Exception e) {
            if (documento.isOpen()) {
                documento.close();
            }

            throw new IllegalStateException(
                    "Não foi possível gerar o relatório PDF de estoque.",
                    e
            );
        }
    }

    public byte[] exportarCategoriasPdf() {

        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        Document documento =
                new Document(
                        PageSize.A4,
                        40,
                        40,
                        40,
                        40
                );

        try {
            PdfWriter.getInstance(documento, saida);

            documento.open();

            adicionarCabecalhoGenerico(documento, "Relatório de Produtos por Categoria");

            PdfPTable tabela = new PdfPTable(2);

            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{4f, 2f});
            tabela.setHeaderRows(1);

            adicionarCabecalhoTabela(tabela, "Categoria");
            adicionarCabecalhoTabela(tabela, "Quantidade de produtos");

            for (RelatorioCategoriaDTO categoria : relatorioService.buscarProdutosPorCategoria()) {
                adicionarCelula(tabela, categoria.getCategoria());
                adicionarCelulaCentralizada(tabela, String.valueOf(categoria.getQuantidadeProdutos()));
            }

            documento.add(tabela);
            documento.close();

            return saida.toByteArray();
        } catch (Exception e) {
            if (documento.isOpen()) {
                documento.close();
            }

            throw new IllegalStateException(
                    "Não foi possível gerar o relatório PDF por categoria.",
                    e
            );
        }
    }

    public byte[] exportarMarcasPdf() {

        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        Document documento =
                new Document(
                        PageSize.A4,
                        40,
                        40,
                        40,
                        40
                );

        try {
            PdfWriter.getInstance(documento, saida);

            documento.open();

            adicionarCabecalhoGenerico(documento, "Relatório de Produtos por Marca");

            PdfPTable tabela = new PdfPTable(2);

            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{4f, 2f});
            tabela.setHeaderRows(1);

            adicionarCabecalhoTabela(tabela, "Marca");
            adicionarCabecalhoTabela(tabela, "Quantidade de produtos");

            for (RelatorioMarcaDTO marca : relatorioService.buscarProdutosPorMarca()) {
                adicionarCelula(tabela, marca.getMarca());
                adicionarCelulaCentralizada(tabela, String.valueOf(marca.getQuantidadeProdutos()));
            }

            documento.add(tabela);
            documento.close();

            return saida.toByteArray();
        } catch (Exception e) {
            if (documento.isOpen()) {
                documento.close();
            }

            throw new IllegalStateException(
                    "Não foi possível gerar o relatório PDF por marca.",
                    e
            );
        }
    }

    public byte[] exportarSemImagemPdf() {

        return exportarProdutosIncompletosPdf(
                "Relatório de Produtos sem Imagem",
                relatorioService.buscarProdutosSemImagem()
        );
    }

    public byte[] exportarSemEspecificacoesPdf() {

        return exportarProdutosIncompletosPdf(
                "Relatório de Produtos sem Especificações",
                relatorioService.buscarProdutosSemEspecificacao()
        );
    }

    public byte[] exportarRecentesPdf() {

        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        Document documento =
                new Document(
                        PageSize.A4.rotate(),
                        30,
                        30,
                        30,
                        30
                );

        try {

            PdfWriter.getInstance(documento, saida);
            documento.open();
            adicionarCabecalhoGenerico(documento, "Relatório de Produtos Recentes");

            PdfPTable tabela = new PdfPTable(6);
            tabela.setWidthPercentage(100);
            tabela.setWidths(
                    new float[]{
                            1.8f,
                            3f,
                            1.6f,
                            2.2f,
                            2f,
                            1.3f
                    }
            );

            tabela.setHeaderRows(1);
            adicionarCabecalhoTabela(tabela, "Data");
            adicionarCabecalhoTabela(tabela, "Produto");
            adicionarCabecalhoTabela(tabela, "SKU");
            adicionarCabecalhoTabela(tabela, "Categoria");
            adicionarCabecalhoTabela(tabela, "Marca");
            adicionarCabecalhoTabela(tabela, "Status");

            DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (RelatorioProdutoRecenteDTO produto : relatorioService.buscarProdutosRecentes()) {
                adicionarCelula(tabela, produto.getDataCriacao() != null ? produto.getDataCriacao().format(formatoData) : "");
                adicionarCelula(tabela, produto.getNome());
                adicionarCelula(tabela, produto.getSku());
                adicionarCelula(tabela, produto.getCategoria());
                adicionarCelula(tabela, produto.getMarca() != null ? produto.getMarca() : "-");
                adicionarCelulaCentralizada(tabela, Boolean.TRUE.equals(produto.getAtivo()) ? "Ativo" : "Inativo");
            }
            documento.add(tabela);
            documento.close();
            return saida.toByteArray();

        } catch (Exception e) {
            if (documento.isOpen()) {
                documento.close();
            }
            throw new IllegalStateException(
                    "Não foi possível gerar o relatório PDF de produtos recentes.",
                    e
            );
        }
    }

    private byte[] exportarProdutosIncompletosPdf(String tituloRelatorio, List<RelatorioProdutoIncompletoDTO> produtos) {

        ByteArrayOutputStream saida = new ByteArrayOutputStream();

        Document documento =
                new Document(
                        PageSize.A4,
                        40,
                        40,
                        40,
                        40
                );

        try {
            PdfWriter.getInstance(documento, saida);
            documento.open();
            adicionarCabecalhoGenerico(documento, tituloRelatorio);

            PdfPTable tabela = new PdfPTable(5);
            tabela.setWidthPercentage(100);
            tabela.setWidths(new float[]{3f, 1.7f, 2.2f, 2f, 1.3f});
            tabela.setHeaderRows(1);
            adicionarCabecalhoTabela(tabela, "Produto");
            adicionarCabecalhoTabela(tabela, "SKU");
            adicionarCabecalhoTabela(tabela, "Categoria");
            adicionarCabecalhoTabela(tabela, "Marca");
            adicionarCabecalhoTabela(tabela, "Status");

            for (RelatorioProdutoIncompletoDTO produto : produtos) {
                adicionarCelula(tabela, produto.getNome());
                adicionarCelula(tabela, produto.getSku());
                adicionarCelula(tabela, produto.getCategoria());
                adicionarCelula(tabela, produto.getMarca() != null ? produto.getMarca() : "-");
                adicionarCelulaCentralizada(tabela, Boolean.TRUE.equals(produto.getAtivo()) ? "Ativo" : "Inativo");
            }

            documento.add(tabela);
            documento.close();
            return saida.toByteArray();

        } catch (Exception e) {
            if (documento.isOpen()) {
                documento.close();
            }

            throw new IllegalStateException(
                    "Não foi possível gerar o relatório PDF.",
                    e
            );
        }
    }

    private void adicionarCabecalho(Document documento) throws Exception {

        Font fonteTitulo =
                new Font(
                        Font.HELVETICA,
                        18,
                        Font.BOLD
                );

        Font fonteSubtitulo =
                new Font(
                        Font.HELVETICA,
                        9,
                        Font.NORMAL
                );

        Paragraph titulo =
                new Paragraph(
                        "BIKE PADRÃO",
                        fonteTitulo
                );

        titulo.setAlignment(Element.ALIGN_CENTER);

        Paragraph subtitulo =
                new Paragraph(
                        "Relatório de Estoque",
                        fonteSubtitulo
                );

        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(20);

        documento.add(titulo);
        documento.add(subtitulo);
    }

    private void adicionarCabecalhoGenerico(Document documento, String nomeRelatorio) throws Exception {

        Font fonteTitulo =
                new Font(
                        Font.HELVETICA,
                        18,
                        Font.BOLD
                );

        Font fonteSubtitulo =
                new Font(
                        Font.HELVETICA,
                        10,
                        Font.NORMAL
                );


        Paragraph titulo = new Paragraph("BIKE PADRÃO", fonteTitulo);
        Paragraph subtitulo = new Paragraph(nomeRelatorio, fonteSubtitulo);

        titulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(20);

        documento.add(titulo);
        documento.add(subtitulo);
    }


    private void adicionarTabelaEstoque(Document documento, RelatorioEstoqueResumoDTO relatorio) throws Exception {

        PdfPTable tabela = new PdfPTable(8);

        tabela.setWidthPercentage(100);
        tabela.setWidths(
                new float[]{
                        3.2f,
                        1.5f,
                        2.0f,
                        1.8f,
                        1.0f,
                        1.5f,
                        1.8f,
                        1.5f
                }
        );
        tabela.setHeaderRows(1);

        adicionarCabecalhoTabela(tabela, "Produto");
        adicionarCabecalhoTabela(tabela, "SKU");
        adicionarCabecalhoTabela(tabela, "Categoria");
        adicionarCabecalhoTabela(tabela, "Marca");
        adicionarCabecalhoTabela(tabela, "Estoque");
        adicionarCabecalhoTabela(tabela, "Preço");
        adicionarCabecalhoTabela(tabela, "Valor estoque");
        adicionarCabecalhoTabela(tabela, "Status");

        for (RelatorioEstoqueDTO produto : relatorio.getProdutos()) {
            adicionarCelula(tabela, produto.getNome());
            adicionarCelula(tabela, produto.getSku());
            adicionarCelula(tabela, produto.getCategoria());
            adicionarCelula(tabela, produto.getMarca() != null ? produto.getMarca() : "-");
            adicionarCelulaCentralizada(tabela, String.valueOf(produto.getEstoque()));
            adicionarCelulaDireita(tabela, formatarMoeda(produto.getPreco()));
            adicionarCelulaDireita(tabela, formatarMoeda(produto.getValorEstoque()));
            adicionarCelulaCentralizada(tabela, formatarStatus(produto.getStatusEstoque()));
        }

        documento.add(tabela);
    }

    private void adicionarCabecalhoTabela(
            PdfPTable tabela,
            String texto
    ) {

        Font fonte =
                new Font(
                        Font.HELVETICA,
                        8,
                        Font.BOLD
                );


        PdfPCell celula =
                new PdfPCell(
                        new Phrase(
                                texto,
                                fonte
                        )
                );


        celula.setPadding(7);

        celula.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );


        tabela.addCell(celula);
    }


    private void adicionarCelula(
            PdfPTable tabela,
            String texto
    ) {

        PdfPCell celula =
                criarCelula(texto);

        tabela.addCell(celula);
    }


    private void adicionarCelulaCentralizada(
            PdfPTable tabela,
            String texto
    ) {

        PdfPCell celula =
                criarCelula(texto);

        celula.setHorizontalAlignment(
                Element.ALIGN_CENTER
        );

        tabela.addCell(celula);
    }


    private void adicionarCelulaDireita(
            PdfPTable tabela,
            String texto
    ) {

        PdfPCell celula =
                criarCelula(texto);

        celula.setHorizontalAlignment(
                Element.ALIGN_RIGHT
        );

        tabela.addCell(celula);
    }


    private PdfPCell criarCelula(
            String texto
    ) {

        Font fonte =
                new Font(
                        Font.HELVETICA,
                        7,
                        Font.NORMAL
                );


        PdfPCell celula =
                new PdfPCell(
                        new Phrase(
                                texto != null
                                        ? texto
                                        : "",
                                fonte
                        )
                );


        celula.setPadding(6);

        celula.setVerticalAlignment(
                Element.ALIGN_MIDDLE
        );


        return celula;
    }


    private void adicionarTotal(
            Document documento,
            BigDecimal valorTotal
    ) throws Exception {

        Font fonte =
                new Font(
                        Font.HELVETICA,
                        11,
                        Font.BOLD
                );


        Paragraph total =
                new Paragraph(
                        "Valor total estimado do estoque: "
                                + formatarMoeda(valorTotal),
                        fonte
                );


        total.setAlignment(
                Element.ALIGN_RIGHT
        );

        total.setSpacingBefore(18);


        documento.add(total);
    }


    private String formatarMoeda(
            BigDecimal valor
    ) {

        if (valor == null) {
            return "R$ 0,00";
        }


        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale(
                                "pt",
                                "BR"
                        )
                );


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
}