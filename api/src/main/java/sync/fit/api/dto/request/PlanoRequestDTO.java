package sync.fit.api.dto.request;

public class PlanoRequestDTO {
    private String tipo;
    private double valor;
    private int duracaoMeses; // mensal = 1, semestral = 6, etc

    public PlanoRequestDTO() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(int duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }
}
