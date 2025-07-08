package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.request.AvaliacaoFisicaRequestDTO;
import sync.fit.api.dto.response.AvaliacaoFisicaResponseDTO;
import sync.fit.api.model.AvaliacaoFisica;
import sync.fit.api.model.Cliente;
import sync.fit.api.repository.AvaliacaoFisicaRepository;
import sync.fit.api.repository.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoFisicaService {

    @Autowired
    private AvaliacaoFisicaRepository avaliacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public AvaliacaoFisicaResponseDTO criar(AvaliacaoFisicaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId()).orElseThrow();

        AvaliacaoFisica avaliacao = new AvaliacaoFisica();
        avaliacao.setAltura(dto.getAltura());
        avaliacao.setPeso(dto.getPeso());
        avaliacao.setDataAvaliacao(dto.getDataAvaliacao());
        avaliacao.setCliente(cliente);

        AvaliacaoFisica salva = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(salva);
    }

    public List<AvaliacaoFisicaResponseDTO> listarPorCliente(Long clienteId) {
        return avaliacaoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private AvaliacaoFisicaResponseDTO toResponseDTO(AvaliacaoFisica avaliacao) {
        AvaliacaoFisicaResponseDTO dto = new AvaliacaoFisicaResponseDTO();
        dto.setId(avaliacao.getId());
        dto.setAltura(avaliacao.getAltura());
        dto.setPeso(avaliacao.getPeso());
        dto.setImc(calcularIMC(avaliacao.getPeso(), avaliacao.getAltura()));
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());
        dto.setNomeCliente(avaliacao.getCliente().getNome());
        return dto;
    }

    private double calcularIMC(double peso, double altura) {
        return Math.round((peso / (altura * altura)) * 100.0) / 100.0;
    }
}
