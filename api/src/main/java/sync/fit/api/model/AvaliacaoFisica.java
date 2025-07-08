package sync.fit.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "avaliacao_fisica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoFisica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double peso;

    private Double altura;

    private Double imc;

    private LocalDate dataAvaliacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public AvaliacaoFisica(Double peso, Double altura, LocalDate dataAvaliacao, Cliente cliente) {
        this.peso = peso;
        this.altura = altura;
        this.dataAvaliacao = dataAvaliacao;
        this.cliente = cliente;
        this.imc = calcularIMC(peso, altura);
    }

    public Double calcularIMC(Double peso, Double altura) {
        if (peso != null && altura != null && altura > 0) {
            return peso / (altura * altura);
        }
        return null;
    }

    // Setters customizados para recalcular IMC automaticamente
    public void setPeso(Double peso) {
        this.peso = peso;
        this.imc = calcularIMC(this.peso, this.altura);
    }

    public void setAltura(Double altura) {
        this.altura = altura;
        this.imc = calcularIMC(this.peso, this.altura);
    }

    public void setDataAvaliacao(LocalDate dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}