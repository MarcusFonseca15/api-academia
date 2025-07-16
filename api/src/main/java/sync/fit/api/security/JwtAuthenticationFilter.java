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
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // Lista de caminhos que o JWT filter deve IGNORAR TOTALMENTE.
    // Estes caminhos são permitidos pelo SecurityConfig sem autenticação.
    // IMPORTANTE: /api/auth/login é adicionado aqui porque não que o filtro JWT
    // tente extrair ou validar um token para ele, já que é uma rota pública.
    private static final List<String> PATHS_TO_SKIP_JWT_FILTER = Arrays.asList(
            "/api/auth/login", // Apenas o login. POST requests para aqui não serão verificadas pelo JWT Filter.
            "/v3/api-docs",
            "/swagger-ui",
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
        final String requestMethod = request.getMethod(); // Pegue o método também para o login


        // Se a requisição for para /api/auth/login e for um POST, ou para uma das rotas do Swagger,
        // o filtro JWT não deve faz nada, apenas passar a requisição adiante.
        // O SecurityConfig já lidará com o 'permitAll()'.
        boolean shouldSkipJwtFilter = false;
        if (requestUri.equals("/api/auth/login") && requestMethod.equals("POST")) {
            shouldSkipJwtFilter = true;
        } else {
            for (String skipPath : PATHS_TO_SKIP_JWT_FILTER) {
                if (requestUri.startsWith(skipPath)) {
                    shouldSkipJwtFilter = true;
                    break;
                }
            }
        }

        if (shouldSkipJwtFilter) {
            filterChain.doFilter(request, response);
            return; // Encerrar o processamento deste filtro
        }


        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Se não houver cabeçalho de autorização ou não for um token Bearer,
        // a requisição não está autenticada via JWT.
        // O Spring Security decidirá se a rota exige autenticação ou não (401 se protegida).
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token JWT (removendo o prefixo "Bearer ")
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // Se o email foi extraído e o usuário ainda NÃO está autenticado no contexto de segurança
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Valida o token com os detalhes do usuário carregados
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
        // Continua a cadeia de filtros.
        filterChain.doFilter(request, response);
    }
}