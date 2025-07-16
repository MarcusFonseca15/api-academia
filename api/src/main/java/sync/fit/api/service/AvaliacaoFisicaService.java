package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sync.fit.api.dto.request.AvaliacaoFisicaRequestDTO;
import sync.fit.api.dto.response.AvaliacaoFisicaResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
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

    @Transactional
    public AvaliacaoFisicaResponseDTO criar(AvaliacaoFisicaRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));

        AvaliacaoFisica avaliacao = new AvaliacaoFisica();
        avaliacao.setAltura(dto.getAltura());
        avaliacao.setPeso(dto.getPeso());
        avaliacao.setDataAvaliacao(dto.getDataAvaliacao());
        avaliacao.setCliente(cliente);

        AvaliacaoFisica salva = avaliacaoRepository.save(avaliacao);
        return toResponseDTO(salva);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoFisicaResponseDTO> listarPorCliente(Long clienteId) {
        // Verifica se o cliente existe antes de tentar listar suas avaliações
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId);
        }
        return avaliacaoRepository.findByClienteId(clienteId)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public AvaliacaoFisicaResponseDTO buscarPorId(Long id) {
        AvaliacaoFisica avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação Física não encontrada com ID: " + id));
        return toResponseDTO(avaliacao);
    }


    @Transactional
    public void deletar(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avaliação Física não encontrada com ID: " + id);
        }
        avaliacaoRepository.deleteById(id);
    }


    private AvaliacaoFisicaResponseDTO toResponseDTO(AvaliacaoFisica avaliacao) {
        AvaliacaoFisicaResponseDTO dto = new AvaliacaoFisicaResponseDTO();
        dto.setId(avaliacao.getId());
        dto.setAltura(avaliacao.getAltura());
        dto.setPeso(avaliacao.getPeso());
        dto.setImc(calcularIMC(avaliacao.getPeso(), avaliacao.getAltura()));
        dto.setDataAvaliacao(avaliacao.getDataAvaliacao());

        if (avaliacao.getCliente() != null) {
            dto.setNomeCliente(avaliacao.getCliente().getNome());
        } else {
            dto.setNomeCliente("Cliente Desconhecido"); // Fallback
        }
        return dto;
    }

    private double calcularIMC(double peso, double altura) {
        if (altura <= 0) {
            return 0.0;
        }
        return Math.round((peso / (altura * altura)) * 100.0) / 100.0;
    }
}