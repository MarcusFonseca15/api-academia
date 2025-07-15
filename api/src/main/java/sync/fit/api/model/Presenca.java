package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "presenca")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDateTime dataHoraRegistro;

    // Construtor para facilitar a criação de uma nova presença
    public Presenca(Cliente cliente) {
        this.cliente = cliente;
        this.dataHoraRegistro = LocalDateTime.now();
    }
}