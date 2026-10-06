package br.com.bikepadrao.controller.admin;

import br.com.bikepadrao.dto.MarcaFormDTO;
import br.com.bikepadrao.service.MarcaAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/marcas")
@RequiredArgsConstructor
public class AdminMarcaController {

    private final MarcaAdminService marcaAdminService;


    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "marcas",
                marcaAdminService.listarTodas()
        );

        return "admin/marcas/lista";
    }


    @GetMapping("/nova")
    public String nova(Model model) {

        model.addAttribute(
                "marca",
                new MarcaFormDTO()
        );

        model.addAttribute(
                "edicao",
                false
        );

        return "admin/marcas/form";
    }


    @PostMapping
    public String criar(
            @Valid
            @ModelAttribute("marca")
            MarcaFormDTO marca,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "edicao",
                    false
            );

            return "admin/marcas/form";
        }


        try {

            marcaAdminService.criar(marca);

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Marca criada com sucesso."
            );

            return "redirect:/admin/marcas";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "edicao",
                    false
            );

            return "admin/marcas/form";
        }
    }


    @GetMapping("/{id}/editar")
    public String editar(
            @PathVariable Long id,
            Model model
    ) {

        model.addAttribute(
                "marca",
                marcaAdminService.buscarForm(id)
        );

        model.addAttribute(
                "edicao",
                true
        );

        return "admin/marcas/form";
    }


    @PostMapping("/{id}")
    public String atualizar(
            @PathVariable Long id,

            @Valid
            @ModelAttribute("marca")
            MarcaFormDTO marca,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes
    ) {

        marca.setId(id);


        if (bindingResult.hasErrors()) {

            model.addAttribute(
                    "edicao",
                    true
            );

            return "admin/marcas/form";
        }


        try {

            marcaAdminService.atualizar(
                    id,
                    marca
            );

            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    "Marca atualizada."
            );

            return "redirect:/admin/marcas";

        } catch (IllegalArgumentException e) {

            model.addAttribute(
                    "erro",
                    e.getMessage()
            );

            model.addAttribute(
                    "edicao",
                    true
            );

            return "admin/marcas/form";
        }
    }


    @PostMapping("/{id}/status")
    public String alternarStatus(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {

        marcaAdminService
                .alternarStatus(id);

        redirectAttributes.addFlashAttribute(
                "sucesso",
                "Status da marca alterado."
        );

        return "redirect:/admin/marcas";
    }
}