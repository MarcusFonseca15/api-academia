package sync.fit.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sync.fit.api.dto.response.PresencaResponse; // Importe
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Presenca;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PresencaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PresencaService {

    @Autowired
    private PresencaRepository presencaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Marcar Presença para um cliente específico
    public PresencaResponse registrarPresenca(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId));

        Presenca novaPresenca = new Presenca(cliente);
        Presenca savedPresenca = presencaRepository.save(novaPresenca);
        return mapToPresencaResponse(savedPresenca);
    }

    // Obter Histórico de Presença de um Cliente
    public List<PresencaResponse> getHistoricoPresencaCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + clienteId));
        return presencaRepository.findByCliente(cliente).stream()
                .map(this::mapToPresencaResponse)
                .collect(Collectors.toList());
    }

    // Obter Frequência Geral
    public List<PresencaResponse> getFrequenciaGeral(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return presencaRepository.findByDataHoraRegistroBetween(dataInicio, dataFim).stream()
                .map(this::mapToPresencaResponse)
                .collect(Collectors.toList());
    }

    // Método auxiliar para mapear Presenca para PresencaResponse
    private PresencaResponse mapToPresencaResponse(Presenca presenca) {
        return new PresencaResponse(
                presenca.getId(),
                presenca.getCliente().getId(),
                presenca.getCliente().getNome(),
                presenca.getDataHoraRegistro()
        );
    }
}