package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "plano")
@Data // ✅ Gera getters, setters, equals, hashCode, toString
@NoArgsConstructor // ✅ Construtor padrão
@AllArgsConstructor // ✅ Construtor com todos os parâmetros
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(nullable = false)
    private double valor;

    private int duracaoMeses;
}
