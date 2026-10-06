package br.com.bikepadrao.controller;

import br.com.bikepadrao.service.CategoriaService;
import br.com.bikepadrao.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
                "categorias",
                categoriaService.listarAtivas()
        );

        model.addAttribute(
                "produtosDestaque",
                produtoService.listarDestaques()
        );

        return "index";
    }
}