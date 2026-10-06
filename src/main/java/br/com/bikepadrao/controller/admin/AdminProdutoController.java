package br.com.bikepadrao.controller.admin;

import br.com.bikepadrao.dto.ProdutoFormDTO;
import br.com.bikepadrao.entity.Produto;
import br.com.bikepadrao.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/produtos")
@RequiredArgsConstructor
public class AdminProdutoController {

    private final ProdutoAdminService produtoAdminService;

    private final ProdutoImagemService produtoImagemService;

    private final CategoriaService categoriaService;

    private final MarcaService marcaService;

    private final ProdutoEspecificacaoService produtoEspecificacaoService;


    @GetMapping
    public String listar(
            @RequestParam(
                    required = false,
                    defaultValue = ""
            )
            String busca,

            @RequestParam(required = false)
            Long categoriaId,

            @RequestParam(required = false)
            Long marcaId,

            @RequestParam(required = false)
            Boolean ativo,

            @RequestParam(required = false)
            Boolean destaque,

            Model model
    ) {

        model.addAttribute(
                "produtos",
                produtoAdminService.listar(
                        busca,
                        categoriaId,
                        marcaId,
                        ativo,
                        destaque
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
                categoriaId
        );

        model.addAttribute(
                "marcaSelecionada",
                marcaId
        );

        model.addAttribute(
                "ativoSelecionado",
                ativo
        );

        model.addAttribute(
                "destaqueSelecionado",
                destaque
        );

        return "admin/produtos/lista";
    }


    @GetMapping("/novo")
    public String novo(Model model) {

        prepararFormulario(
                model,
                new ProdutoFormDTO(),
                false
        );

        return "admin/produtos/form";
    }


    @PostMapping
    public String criar(
            @Valid
            @ModelAttribute("produto")
            ProdutoFormDTO produto,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {

            prepararFormulario(
                    model,
                    produto,
                    false
            );

            return "admin/produtos/form";
        }


        try {

            Produto criado =
                    produtoAdminService.criar(produto);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Produto criado com sucesso."
            );

            return "redirect:/admin/produtos/"
                    + criado.getId()
                    + "/editar";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            prepararFormulario(
                    model,
                    produto,
                    false
            );

            return "admin/produtos/form";
        }
    }


    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model
    ) {

        ProdutoFormDTO produto =
                produtoAdminService.buscarForm(id);

        prepararFormulario(
                model,
                produto,
                true
        );

        model.addAttribute(
                "imagens",
                produtoImagemService
                        .listarPorProduto(id)
        );

        model.addAttribute(
                "especificacoes",
                produtoEspecificacaoService
                        .listarPorProduto(id)
        );

        return "admin/produtos/form";
    }


    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,

            @Valid
            @ModelAttribute("produto")
            ProdutoFormDTO produto,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        produto.setId(id);


        if (bindingResult.hasErrors()) {

            prepararFormulario(
                    model,
                    produto,
                    true
            );

            model.addAttribute(
                    "imagens",
                    produtoImagemService
                            .listarPorProduto(id)
            );

            model.addAttribute(
                    "especificacoes",
                    produtoEspecificacaoService
                            .listarPorProduto(id)
            );

            return "admin/produtos/form";
        }


        try {

            produtoAdminService.atualizar(
                    id,
                    produto
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Produto atualizado com sucesso."
            );

            return "redirect:/admin/produtos/"
                    + id
                    + "/editar";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            prepararFormulario(
                    model,
                    produto,
                    true
            );

            model.addAttribute(
                    "imagens",
                    produtoImagemService
                            .listarPorProduto(id)
            );

            return "admin/produtos/form";
        }
    }


    @PostMapping("/{id}/status")
    public String alternarStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        produtoAdminService.alternarStatus(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Status alterado."
        );

        return "redirect:/admin/produtos";
    }

    @PostMapping("/{id}/duplicar")
    public String duplicar(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        try {

            Produto copia =
                    produtoAdminService.duplicar(id);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Produto duplicado com sucesso. Revise os dados antes de ativá-lo."
            );

            return "redirect:/admin/produtos/"
                    + copia.getId()
                    + "/editar";

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );

            return "redirect:/admin/produtos";
        }
    }

    @PostMapping("/lote")
    public String executarAcaoEmLote(
            @RequestParam(
                    required = false,
                    name = "produtoIds"
            )
            List<Long> produtoIds,

            @RequestParam
            String acao,

            RedirectAttributes redirectAttributes
    ) {

        try {

            if (produtoIds == null || produtoIds.isEmpty()) {
                throw new IllegalArgumentException(
                        "Selecione pelo menos um produto."
                );
            }

            switch (acao) {

                case "ATIVAR" -> {

                    produtoAdminService.alterarStatusEmLote(
                            produtoIds,
                            true
                    );

                    redirectAttributes.addFlashAttribute(
                            "sucesso",
                            produtoIds.size()
                                    + " produto(s) ativado(s) com sucesso."
                    );
                }

                case "DESATIVAR" -> {

                    produtoAdminService.alterarStatusEmLote(
                            produtoIds,
                            false
                    );

                    redirectAttributes.addFlashAttribute(
                            "sucesso",
                            produtoIds.size()
                                    + " produto(s) desativado(s) com sucesso."
                    );
                }

                case "DESTACAR" -> {

                    produtoAdminService.alterarDestaqueEmLote(
                            produtoIds,
                            true
                    );

                    redirectAttributes.addFlashAttribute(
                            "sucesso",
                            produtoIds.size()
                                    + " produto(s) adicionado(s) aos destaques."
                    );
                }

                case "REMOVER_DESTAQUE" -> {

                    produtoAdminService.alterarDestaqueEmLote(
                            produtoIds,
                            false
                    );

                    redirectAttributes.addFlashAttribute(
                            "sucesso",
                            produtoIds.size()
                                    + " produto(s) removido(s) dos destaques."
                    );
                }

                case "EXCLUIR" -> {

                    int quantidade =
                            produtoIds.size();


                    produtoAdminService.excluirEmLote(
                            produtoIds
                    );


                    redirectAttributes.addFlashAttribute(
                            "sucesso",
                            quantidade
                                    + " produto(s) excluído(s) com sucesso."
                    );
                }

                default ->
                        throw new IllegalArgumentException(
                                "Ação em lote inválida."
                        );
            }

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }

        return "redirect:/admin/produtos";
    }

    @PostMapping("/{id}/imagens")
    public String adicionarImagem(
            @PathVariable Long id,

            @RequestParam("arquivo")
            MultipartFile arquivo,

            @RequestParam(
                    defaultValue = "false"
            )
            boolean principal,

            RedirectAttributes redirectAttributes
    ) {

        try {

            produtoImagemService.adicionar(
                    id,
                    arquivo,
                    principal
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Imagem adicionada."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }


        return "redirect:/admin/produtos/"
                + id
                + "/editar";
    }


    @PostMapping(
            "/{produtoId}/imagens/{imagemId}/excluir"
    )
    public String excluirImagem(
            @PathVariable Long produtoId,
            @PathVariable Long imagemId,
            RedirectAttributes redirectAttributes
    ) {

        produtoImagemService.excluir(
                produtoId,
                imagemId
        );

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Imagem excluída."
        );

        return "redirect:/admin/produtos/"
                + produtoId
                + "/editar";
    }


    private void prepararFormulario(
            Model model,
            ProdutoFormDTO produto,
            boolean edicao
    ) {

        model.addAttribute(
                "produto",
                produto
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
                "edicao",
                edicao
        );
    }

    @PostMapping("/{id}/especificacoes")
    public String adicionarEspecificacao(
            @PathVariable Long id,

            @RequestParam String nome,

            @RequestParam String valor,

            RedirectAttributes redirectAttributes
    ) {

        try {

            produtoEspecificacaoService.adicionar(
                    id,
                    nome,
                    valor
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Especificação adicionada."
            );

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    e.getMessage()
            );
        }


        return "redirect:/admin/produtos/"
                + id
                + "/editar";
    }

    @PostMapping(
            "/{produtoId}/especificacoes/{especificacaoId}/excluir"
    )
    public String excluirEspecificacao(
            @PathVariable Long produtoId,

            @PathVariable Long especificacaoId,

            RedirectAttributes redirectAttributes
    ) {

        produtoEspecificacaoService.excluir(
                produtoId,
                especificacaoId
        );


        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Especificação excluída."
        );


        return "redirect:/admin/produtos/"
                + produtoId
                + "/editar";
    }
}