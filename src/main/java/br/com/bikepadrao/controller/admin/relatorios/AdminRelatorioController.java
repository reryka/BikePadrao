package br.com.bikepadrao.controller.admin.relatorios;

import br.com.bikepadrao.service.relatorios.RelatorioExportacaoService;
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
    private final RelatorioExportacaoService relatorioExportacaoService;


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

        byte[] arquivo = relatorioExportacaoService.exportarEstoqueCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-estoque.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/categorias/exportar/csv")
    public ResponseEntity<byte[]> exportarCategoriasCsv() {

        byte[] arquivo = relatorioExportacaoService.exportarCategoriasCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-categorias.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/marcas/exportar/csv")
    public ResponseEntity<byte[]> exportarMarcasCsv() {

        byte[] arquivo = relatorioExportacaoService.exportarMarcasCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-marcas.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-imagem/exportar/csv")
    public ResponseEntity<byte[]> exportarSemImagemCsv() {

        byte[] arquivo = relatorioExportacaoService.exportarSemImagemCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-sem-imagem.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/sem-especificacao/exportar/csv")
    public ResponseEntity<byte[]> exportarSemEspecificacaoCsv() {

        byte[] arquivo = relatorioExportacaoService.exportarSemEspecificacoesCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-sem-especificacao.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

    @GetMapping("/admin/relatorios/recentes/exportar/csv")
    public ResponseEntity<byte[]> exportarRecentesCsv() {

        byte[] arquivo = relatorioExportacaoService.exportarRecentesCsv();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"relatorio-produtos-recentes.csv\"")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(arquivo);
    }

}