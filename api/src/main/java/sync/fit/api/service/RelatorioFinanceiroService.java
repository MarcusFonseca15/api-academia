package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.response.RelatorioClientesPorPlanoDTO;
import sync.fit.api.repository.ClienteRepository;

import java.util.List;

import sync.fit.api.dto.response.RelatorioSalarioInstrutorDTO;
import sync.fit.api.repository.InstrutorRepository;

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
}