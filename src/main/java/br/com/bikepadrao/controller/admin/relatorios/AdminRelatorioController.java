package br.com.bikepadrao.controller.admin.relatorios;

import br.com.bikepadrao.service.relatorios.RelatorioCsvService;
import br.com.bikepadrao.service.relatorios.RelatorioPdfService;
import br.com.bikepadrao.service.relatorios.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminRelatorioController {

    private final RelatorioService relatorioService;
    private final RelatorioCsvService relatorioCsvService;
    private final RelatorioPdfService relatorioPdfService;

    @GetMapping("/admin/relatorios")
    public String relatorios(Model model) {
        model.addAttribute("resumo", relatorioService.buscarResumo());

        return "admin/relatorios/index";
    }

    @GetMapping("/admin/relatorios/estoque")
    public String estoque(Model model) {
        model.addAttribute("relatorio", relatorioService.buscarEstoque());

        return "admin/relatorios/estoque";
    }

    @GetMapping("/admin/relatorios/categorias")
    public String categorias(Model model) {
        model.addAttribute("categorias", relatorioService.buscarProdutosPorCategoria());
        model.addAttribute("resumo", relatorioService.buscarResumo());

        return "admin/relatorios/categorias";
    }

    @GetMapping("/admin/relatorios/marcas")
    public String marcas(Model model) {
        model.addAttribute("marcas",relatorioService.buscarProdutosPorMarca());
        model.addAttribute("resumo",relatorioService.buscarResumo());

        return "admin/relatorios/marcas";
    }

    @GetMapping("/admin/relatorios/sem-imagem")
    public String semImagem(Model model) {

        model.addAttribute("titulo","Produtos sem imagem");
        model.addAttribute("descricao","Produtos cadastrados que ainda não possuem nenhuma imagem.");
        model.addAttribute("produtos", relatorioService.buscarProdutosSemImagem());
        model.addAttribute("tipoExportacao", "sem-imagem");
        return "admin/relatorios/produtosIncompletos";
    }

    @GetMapping("/admin/relatorios/sem-especificacao")
    public String semEspecificacao(Model model) {

        model.addAttribute("titulo","Produtos sem especificação");
        model.addAttribute("descricao","Produtos cadastrados que ainda não possuem especificações técnicas.");
        model.addAttribute("produtos", relatorioService.buscarProdutosSemEspecificacao());
        model.addAttribute("tipoExportacao", "sem-especificacao");
        return "admin/relatorios/produtosIncompletos";
    }

    @GetMapping("/admin/relatorios/recentes")
    public String recentes(Model model) {

        model.addAttribute("produtos", relatorioService.buscarProdutosRecentes());

        return "admin/relatorios/produtosRecentes";
    }

    @GetMapping("/admin/relatorios/estoque/exportar/csv")
    public ResponseEntity<byte[]> exportarEstoqueCsv() {

        byte[] arquivo = relatorioCsvService.exportarEstoqueCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-estoque.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/categorias/exportar/csv")
    public ResponseEntity<byte[]> exportarCategoriasCsv() {

        byte[] arquivo = relatorioCsvService.exportarCategoriasCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-categorias.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/marcas/exportar/csv")
    public ResponseEntity<byte[]> exportarMarcasCsv() {

        byte[] arquivo = relatorioCsvService.exportarMarcasCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-marcas.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-imagem/exportar/csv")
    public ResponseEntity<byte[]> exportarSemImagemCsv() {

        byte[] arquivo = relatorioCsvService.exportarSemImagemCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-sem-imagem.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-especificacao/exportar/csv")
    public ResponseEntity<byte[]> exportarSemEspecificacaoCsv() {

        byte[] arquivo = relatorioCsvService.exportarSemEspecificacoesCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-sem-especificacao.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/recentes/exportar/csv")
    public ResponseEntity<byte[]> exportarRecentesCsv() {

        byte[] arquivo = relatorioCsvService.exportarRecentesCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-recentes.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/estoque/exportar/pdf")
    public ResponseEntity<byte[]> exportarEstoquePdf() {

        byte[] arquivo = relatorioPdfService.exportarEstoquePdf();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"relatorio-estoque.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/categorias/exportar/pdf")
    public ResponseEntity<byte[]> exportarCategoriasPdf() {

        byte[] arquivo = relatorioPdfService.exportarCategoriasPdf();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"relatorio-categorias.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/marcas/exportar/pdf")
    public ResponseEntity<byte[]> exportarMarcasPdf() {

        byte[] arquivo = relatorioPdfService.exportarMarcasPdf();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"relatorio-marcas.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-imagem/exportar/pdf")
    public ResponseEntity<byte[]> exportarSemImagemPdf() {

        byte[] arquivo =
                relatorioPdfService
                        .exportarSemImagemPdf();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"relatorio-produtos-sem-imagem.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-especificacao/exportar/pdf")
    public ResponseEntity<byte[]> exportarSemEspecificacoesPdf() {

        byte[] arquivo =
                relatorioPdfService
                        .exportarSemEspecificacoesPdf();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"relatorio-produtos-sem-especificacoes.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/recentes/exportar/pdf")
    public ResponseEntity<byte[]> exportarRecentesPdf() {

        byte[] arquivo =
                relatorioPdfService
                        .exportarRecentesPdf();

        return ResponseEntity
                .ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"relatorio-produtos-recentes.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

}