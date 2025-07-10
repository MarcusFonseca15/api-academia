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
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF para APIs RESTful sem sessões
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)) // Adiciona o EntryPoint
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless para JWT
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos (não exigem autenticação)
                        .requestMatchers("/api/auth/**").permitAll() // Login e registro de usuários (se houver)
                        // URLs para Swagger/OpenAPI (documentação da API)
                        .requestMatchers("/v2/api-docs/**", "/v3/api-docs/**", "/swagger-resources/**",
                                "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").permitAll()

                        // --- Regras de Autorização Baseadas nas Roles ---
                        // Para simplificar a configuração do SecurityFilterChain, podemos definir regras mais genéricas aqui,
                        // e usar @PreAuthorize nos controllers para regras mais detalhadas.

                        // Permite GET /api/funcionarios apenas para ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/funcionarios").hasRole("ADMIN")

                        // Permite POST para criar administradores, instrutores, recepcionistas APENAS para ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/funcionarios/administradores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/funcionarios/instrutores").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/funcionarios/recepcionistas").hasRole("ADMIN")


                        // Exemplo: Todos os endpoints abaixo de /api/admin/ só podem ser acessados por ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Exemplo: Todos os endpoints abaixo de /api/instrutor/ só podem ser acessados por INSTRUTOR
                        // ou Admin (se Admin também puder gerenciar o que o instrutor faz)
                        .requestMatchers("/api/instrutor/**").hasAnyRole("INSTRUTOR", "ADMIN")

                        // Exemplo: Para o restante dos endpoints de /api/funcionarios (GET por ID, PUT, DELETE)
                        // Ajuste as roles conforme sua necessidade para cada método e path
                        .requestMatchers(HttpMethod.GET, "/api/funcionarios/{id}").hasAnyRole("ADMIN", "INSTRUTOR", "RECEPCIONISTA") // Todos podem ver o próprio, admin pode ver todos
                        .requestMatchers(HttpMethod.PUT, "/api/funcionarios/{id}").hasRole("ADMIN") // Apenas ADMIN pode atualizar qualquer funcionário
                        .requestMatchers(HttpMethod.DELETE, "/api/funcionarios/{id}").hasRole("ADMIN") // Apenas ADMIN pode deletar funcionário


                        // Exemplo: Endpoints específicos para Cliente
                        // Você pode ter algo como "/api/meus-dados" para o cliente acessar sem precisar do ID
                        // Mas se usar o ID na URL, o @PreAuthorize é melhor para verificar se é o ID do próprio usuário logado.
                        // .requestMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("CLIENTE", "ADMIN", "INSTRUTOR")
                        // (Prefira @PreAuthorize aqui para lógica de "se for o próprio cliente")

                        // Qualquer outra requisição requer autenticação
                        .anyRequest().authenticated()
                )
                // Adiciona o filtro JWT antes do filtro de autenticação de usuário e senha padrão do Spring Security
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean para o codificador de senhas (BCrypt recomendado)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para o AuthenticationManager, necessário para o processo de login
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}