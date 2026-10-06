package br.com.bikepadrao.config;

import br.com.bikepadrao.entity.UsuarioAdmin;
import br.com.bikepadrao.repository.UsuarioAdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrapConfig {

    @Bean
    CommandLineRunner criarAdministradorInicial(
            UsuarioAdminRepository usuarioAdminRepository,
            PasswordEncoder passwordEncoder,

            @Value("${admin.username}")
            String username,

            @Value("${admin.password}")
            String password
    ) {

        return args -> {

            if (
                    usuarioAdminRepository
                            .existsByUsername(username)
            ) {
                return;
            }


            UsuarioAdmin admin =
                    new UsuarioAdmin();

            admin.setUsername(username);

            admin.setSenha(
                    passwordEncoder.encode(password)
            );

            admin.setRole("ADMIN");

            admin.setAtivo(true);


            usuarioAdminRepository.save(admin);


            System.out.println(
                    "Administrador inicial criado: "
                            + username
            );
        };
    }
}