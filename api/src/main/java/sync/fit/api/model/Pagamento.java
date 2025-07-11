package sync.fit.api.model;

import jakarta.persistence.*;
import sync.fit.api.model.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal valor;

    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    private LocalDate dataVencimento;

    private StatusPagamento status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Pagamento(Long id, BigDecimal valor, LocalDate dataPagamento, LocalDate dataVencimento,
            StatusPagamento status, Cliente cliente) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = dataVencimento;
        this.status = status;
        this.cliente = cliente;
    }

    public Pagamento() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
