package sync.fit.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sync.fit.api.security.JwtAuthenticationFilter;
import sync.fit.api.security.JwtAuthenticationEntryPoint;
import sync.fit.api.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize

                        // Apenas o endpoint de LOGIN é público (POST /api/auth/login)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                        // As URLs do Swagger/OpenAPI são públicas
                        .requestMatchers("/v2/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()

                        // Todas as OUTRAS rotas (incluindo /api/auth/register/* e outras)
                        // exigirão AUTENTICAÇÃO via JWT.
                        // O @PreAuthorize nos Controllers fará a verificação de role após a autenticação.

                        // Exemplo de outras regras de autorização baseadas em role (APÓS AUTENTICAÇÃO)
                        .requestMatchers(HttpMethod.GET, "/api/funcionarios").hasRole("ADMIN")

                        // Rotas para criar novos tipos de funcionários (exigem AUTHENTICAÇÃO + ADMIN role)

                        // Todos os endpoints abaixo de /api/admin/ só podem ser acessados por ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Todos os endpoints abaixo de /api/instrutor/ só podem ser acessados por INSTRUTOR ou Admin
                        .requestMatchers("/api/instrutor/**").hasAnyRole("INSTRUTOR", "ADMIN")

                        // Quaisquer outras requisições (incluindo TODAS as /api/auth/register/*)
                        // devem ser AUTENTICADAS.
                        .anyRequest().authenticated()
                )
                // Adiciona o filtro JWT antes do filtro de autenticação de usuário e senha padrão do Spring Security
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}