package com.finance.manager.seguranca;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.finance.manager.excecoes.NaoEncontradoException;
import com.finance.manager.usuario.UsuarioServico;
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
import java.util.UUID;

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
            DecodedJWT jwt = tokenServico.validarToken(token);
            String empresaClaim = jwt != null ? jwt.getClaim("empresaId").asString() : null;

            if (empresaClaim != null) {
                try {
                    UsuarioAutenticado user = (UsuarioAutenticado) usuarioServico.loadUserByUsername(jwt.getSubject());
                    user.setEmpresaId(UUID.fromString(empresaClaim));

                    var auth = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (NaoEncontradoException e) {
                }
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
