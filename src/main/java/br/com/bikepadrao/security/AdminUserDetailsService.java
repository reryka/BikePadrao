package br.com.bikepadrao.security;

import br.com.bikepadrao.entity.UsuarioAdmin;
import br.com.bikepadrao.repository.UsuarioAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserDetailsService
        implements UserDetailsService {

    private final UsuarioAdminRepository usuarioAdminRepository;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        UsuarioAdmin usuario =
                usuarioAdminRepository
                        .findByUsername(username)
                        .filter(UsuarioAdmin::getAtivo)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Usuário não encontrado."
                                )
                        );


        return User
                .withUsername(usuario.getUsername())
                .password(usuario.getSenha())
                .roles(usuario.getRole())
                .build();
    }
}