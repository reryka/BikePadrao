package br.com.bikepadrao.controller.admin;

import br.com.bikepadrao.dto.CategoriaFormDTO;
import br.com.bikepadrao.service.CategoriaAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
@RequiredArgsConstructor
public class AdminCategoriaController {

    private final CategoriaAdminService categoriaAdminService;


    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "categorias",
                categoriaAdminService.listarTodas()
        );

        return "admin/categorias/lista";
    }


    @GetMapping("/nova")
    public String nova(Model model) {

        model.addAttribute(
                "categoria",
                new CategoriaFormDTO()
        );

        model.addAttribute(
                "edicao",
                false
        );

        return "admin/categorias/form";
    }


    @PostMapping
    public String criar(
            @Valid
            @ModelAttribute("categoria")
            CategoriaFormDTO categoria,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "edicao",
                    false
            );

            return "admin/categorias/form";
        }


        try {

            categoriaAdminService.criar(
                    categoria
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Categoria criada com sucesso."
            );

            return "redirect:/admin/categorias";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "edicao",
                    false
            );

            return "admin/categorias/form";
        }
    }


    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "categoria",
                categoriaAdminService
                        .buscarForm(id)
        );

        model.addAttribute(
                "edicao",
                true
        );

        return "admin/categorias/form";
    }


    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,

            @Valid
            @ModelAttribute("categoria")
            CategoriaFormDTO categoria,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        categoria.setId(id);


        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "edicao",
                    true
            );

            return "admin/categorias/form";
        }


        try {

            categoriaAdminService.atualizar(
                    id,
                    categoria
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Categoria atualizada."
            );

            return "redirect:/admin/categorias";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "edicao",
                    true
            );

            return "admin/categorias/form";
        }
    }


    @PostMapping("/{id}/status")
    public String alternarStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        categoriaAdminService
                .alternarStatus(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Status da categoria alterado."
        );

        return "redirect:/admin/categorias";
    }
}