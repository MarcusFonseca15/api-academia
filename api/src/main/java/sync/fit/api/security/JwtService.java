package sync.fit.api.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    // Injeta a chave secreta do application.properties
    @Value("${application.jwt.secret}")
    private String SECRET_KEY;

    // Injeta o tempo de expiração do token em milissegundos do application.properties
    @Value("${application.jwt.expiration-ms}")
    private long JWT_EXPIRATION_MS;

    // Extrai o nome de usuário (email) do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrai a data de expiração do token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Método genérico para extrair um claim específico do token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extrai todos os claims (corpo) do token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verifica se o token expirou
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Gera um token JWT para um UserDetails, incluindo as roles/autoridades como um claim.
     * O token é assinado com a chave secreta e tem um tempo de expiração.
     * @param userDetails Os detalhes do usuário para quem o token é gerado.
     * @return O token JWT gerado.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Adiciona as roles/autoridades do usuário como um claim 'roles' no token
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Pega a string da autoridade
                .collect(Collectors.toList()));
        return Jwts.builder()
                .setClaims(extraClaims) // Inclui os claims personalizados
                .setSubject(userDetails.getUsername()) // Define o "subject" do token (o username/email)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS)) // Data de expiração
                .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256) // Assina com a chave e algoritmo HS256
                .compact(); // Constrói e compacta o token
    }

    /**
     * Valida um token JWT contra os detalhes de um usuário.
     * Verifica se o username no token corresponde ao username do UserDetails e se o token não expirou.
     * @param token O token JWT a ser validado.
     * @param userDetails Os detalhes do usuário a serem comparados.
     * @return true se o token é válido, false caso contrário.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Obtém a chave de assinatura decodificada da Base64
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}