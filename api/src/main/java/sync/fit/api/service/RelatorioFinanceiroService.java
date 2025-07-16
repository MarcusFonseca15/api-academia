package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO;
import sync.fit.api.repository.ClienteRepository;

import java.util.List;

import sync.fit.api.dto.response.RelatorioSalarioInstrutorDTO;
import sync.fit.api.repository.InstrutorRepository;

import sync.fit.api.dto.response.RelatorioFaturamentoDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano; // Importe a classe Plano

// Importe o PlanoRepository
import sync.fit.api.repository.PlanoRepository; // <--- NOVO IMPORT

import lombok.RequiredArgsConstructor; // Usar RequiredArgsConstructor para injeção de dependências

@Service
@RequiredArgsConstructor
public class RelatorioFinanceiroService {

    private final ClienteRepository clienteRepository;
    private final InstrutorRepository instrutorRepository;
    private final PlanoRepository planoRepository;

    public List<RelatorioClientesPorPlanoDTO> obterRelatorioClientesPorPlano() {
        return clienteRepository.contarClientesPorPlano();
    }

    public List<RelatorioSalarioInstrutorDTO> obterRelatorioSalarioInstrutores() {
        return instrutorRepository.findAll().stream()
                .map(instrutor -> new RelatorioSalarioInstrutorDTO(
                        instrutor.getNome(),
                        instrutor.getSalario()
                )).toList();
    }

    public RelatorioFaturamentoDTO gerarRelatorioFaturamento() {
        List<Cliente> clientes = clienteRepository.findAll();

        int total = clientes.size();
        int mensais = 0, semestrais = 0, anuais = 0;
        double totalFaturado = 0;

        for (Cliente cliente : clientes) {
            Plano plano = cliente.getPlano();
            if (plano != null) {
                totalFaturado += plano.getValor();

                // duracaoMeses = 1 para Mensal
                // duracaoMeses = 6 para Semestral
                // duracaoMeses = 12 para Anual
                switch (plano.getDuracaoMeses()) {
                    case 1 -> mensais++;
                    case 6 -> semestrais++;
                    case 12 -> anuais++;
                    default -> {

                        System.out.println("Aviso: Plano com duração não padrão encontrado: " + plano.getTipo() + " - " + plano.getDuracaoMeses() + " meses");
                    }
                }
            }
        }

        return new RelatorioFaturamentoDTO(total, mensais, semestrais, anuais, totalFaturado);
    }
}