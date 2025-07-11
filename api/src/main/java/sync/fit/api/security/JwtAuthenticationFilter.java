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
import sync.fit.api.service.CustomUserDetailsService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List; // Adicionado para usar List

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // Lista de caminhos que devem ser PUBLICOS e IGNORADOS pelo filtro JWT
    // Use Arrays.asList para criar uma lista imutável
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/auth", // Para cobrir /api/auth/**
            "/v3/api-docs", // Para cobrir /v3/api-docs e /v3/api-docs/swagger-config
            "/swagger-ui", // Para cobrir /swagger-ui/index.html e outros recursos
            "/swagger-resources",
            "/webjars",
            "/swagger-ui.html"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String requestUri = request.getRequestURI();

        // 1. Verifica se a requisição é para um caminho público que não precisa de autenticação JWT
        // Percorremos a lista de caminhos públicos
        for (String publicPath : PUBLIC_PATHS) {
            // Usamos startsWith para verificar se a URI da requisição começa com algum dos caminhos públicos
            if (requestUri.startsWith(publicPath)) {
                filterChain.doFilter(request, response); // Permite a requisição passar sem validação JWT
                return; // Encerra o processamento deste filtro
            }
        }

        // Se a requisição NÃO for para um caminho público, então tentamos autenticá-la com JWT
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Verifica se o cabeçalho Authorization existe e começa com "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Se não tiver cabeçalho JWT e não for um caminho público,
            // a requisição não está autenticada por JWT.
            // O Spring Security ainda pode bloqueá-la se não for um 'permitAll()' em outro lugar.
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token JWT (removendo o prefixo "Bearer ")
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 4. Se o email foi extraído e o usuário ainda NÃO está autenticado no contexto de segurança
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Valida o token com os detalhes do usuário carregados
            if (jwtService.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 6. Continua a cadeia de filtros.
        filterChain.doFilter(request, response);
    }
}