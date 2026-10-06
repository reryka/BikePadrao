package br.com.bikepadrao.controller;

import br.com.bikepadrao.dto.ProdutoDetalheDTO;
import br.com.bikepadrao.service.CategoriaService;
import br.com.bikepadrao.service.MarcaService;
import br.com.bikepadrao.service.ProdutoService;
import br.com.bikepadrao.service.WhatsAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class CatalogoController {

    private final ProdutoService produtoService;
    private final CategoriaService categoriaService;
    private final WhatsAppService whatsAppService;
    private final MarcaService marcaService;


    @GetMapping("/produtos")
    public String produtos(
            @RequestParam(required = false)
            String busca,

            @RequestParam(required = false)
            String categoria,

            @RequestParam(required = false)
            String marca,

            @RequestParam(required = false)
            String ordem,

            Model model
    ) {

        model.addAttribute(
                "produtos",
                produtoService.listarCatalogo(
                        busca,
                        categoria,
                        marca,
                        ordem
                )
        );

        model.addAttribute(
                "categorias",
                categoriaService.listarAtivas()
        );

        model.addAttribute(
                "marcas",
                marcaService.listarAtivas()
        );

        model.addAttribute(
                "busca",
                busca
        );

        model.addAttribute(
                "categoriaSelecionada",
                categoria
        );

        model.addAttribute(
                "marcaSelecionada",
                marca
        );

        model.addAttribute(
                "ordemSelecionada",
                ordem == null || ordem.isBlank()
                        ? "nome"
                        : ordem
        );

        return "produtos";
    }


    @GetMapping("/produto/{slug}")
    public String produto(
            @PathVariable String slug,
            Model model
    ) {

        ProdutoDetalheDTO produto =
                produtoService
                        .buscarPorSlug(slug)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Produto não encontrado"
                                )
                        );


        model.addAttribute(
                "produto",
                produto
        );

        model.addAttribute(
                "whatsappUrl",
                whatsAppService
                        .criarLinkProduto(produto)
        );

        return "produto";
    }
}