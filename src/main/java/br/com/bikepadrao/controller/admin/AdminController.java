package br.com.bikepadrao.controller.admin;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/login")
    public String login() {

        return "admin/login";
    }


    @GetMapping("/admin")
    public String dashboard(
            Authentication authentication,
            Model model
    ) {

        model.addAttribute(
                "usuario",
                authentication.getName()
        );

        return "admin/dashboard";
    }
}