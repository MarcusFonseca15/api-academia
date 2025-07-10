package sync.fit.api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sync.fit.api.service.CustomUserDetailsService; // Seu serviço customizado de UserDetails

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Pega o cabeçalho Authorization
        final String jwt;
        final String userEmail;

        // 1. Verifica se o cabeçalho Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Se não, continua a cadeia de filtros (requisição não autenticada por JWT)
            return;
        }

        // 2. Extrai o token JWT (removendo o prefixo "Bearer ")
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // Extrai o email (subject) do token

        // 3. Se o email foi extraído e o usuário ainda NÃO está autenticado no contexto de segurança
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário pelo email usando o CustomUserDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 4. Valida o token com os detalhes do usuário carregados
            if (jwtService.validateToken(jwt, userDetails)) {
                // Se o token é válido, cria um objeto de autenticação
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, // O principal (UserDetails carregado)
                        null,        // Credenciais (já verificadas pelo JWT, então null)
                        userDetails.getAuthorities() // As autoridades/roles do usuário
                );
                // Define detalhes adicionais da requisição (como IP do cliente)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Define o objeto de autenticação no SecurityContextHolder, indicando que o usuário está autenticado
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 5. Continua a cadeia de filtros. A requisição agora está autenticada se o JWT for válido.
        filterChain.doFilter(request, response);
    }
}