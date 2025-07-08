package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funcionario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String cargo; // "Instrutor", "Recepcionista", "Administrador"

    @Column(nullable = false)
    private Double salario;
}
