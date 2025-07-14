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

@Service
public class RelatorioFinanceiroService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<RelatorioClientesPorPlanoDTO> obterRelatorioClientesPorPlano() {
        return clienteRepository.contarClientesPorPlano();
    }

    @Autowired
    private InstrutorRepository instrutorRepository;

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
            int planoId = Math.toIntExact(cliente.getPlano().getId());
            switch (planoId) {
                case 1 -> { // mensal
                    mensais++;
                    totalFaturado += 100;
                }
                case 2 -> { // semestral
                    semestrais++;
                    totalFaturado += 500;
                }
                case 3 -> { // anual
                    anuais++;
                    totalFaturado += 900;
                }
            }
        }

        return new RelatorioFaturamentoDTO(total, mensais, anuais, semestrais, totalFaturado);
    }
}