package com.finance.manager.seguranca;

import com.finance.manager.servicos.UsuarioServico;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFiltro extends OncePerRequestFilter {

    private final TokenServico tokenServico;
    private final UsuarioServico usuarioServico;

    public SecurityFiltro(TokenServico tokenServico, UsuarioServico usuarioServico) {
        this.tokenServico = tokenServico;
        this.usuarioServico = usuarioServico;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {

        String token = this.recuperarToken(request);

        if(token != null) {
            String sub = tokenServico.validarToken(token);
            if(sub != null) {
                UserDetails user = usuarioServico.loadUserByUsername(sub);
                var auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
            filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) return null;

        return authHeader.substring(7);
    }
}
