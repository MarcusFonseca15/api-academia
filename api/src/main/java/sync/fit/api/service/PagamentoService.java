package sync.fit.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sync.fit.api.dto.request.PagamentoRequestDTO;
import sync.fit.api.dto.response.PagamentoResponseDTO;
import sync.fit.api.exception.ResourceNotFoundException;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Pagamento;
import sync.fit.api.repository.ClienteRepository;
import sync.fit.api.repository.PagamentoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<PagamentoResponseDTO> findAll() {
        return pagamentoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagamentoResponseDTO findById(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com ID: " + id));
        return toResponseDTO(pagamento);
    }

    @Transactional
    public PagamentoResponseDTO save(PagamentoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cliente não encontrado com ID: " + dto.getClienteId()));

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(dto.getValor());
        pagamento.setDataPagamento(dto.getDataPagamento());
        pagamento.setStatus(dto.getStatus());
        pagamento.setCliente(cliente);

        return toResponseDTO(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public PagamentoResponseDTO update(Long id, PagamentoRequestDTO dto) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento não encontrado com ID: " + id));

        pagamento.setValor(dto.getValor());
        pagamento.setDataPagamento(dto.getDataPagamento());
        pagamento.setStatus(dto.getStatus());

        if (!pagamento.getCliente().getId().equals(dto.getClienteId())) {
            Cliente novoCliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Cliente não encontrado com ID: " + dto.getClienteId()));
            pagamento.setCliente(novoCliente);
        }

        return toResponseDTO(pagamentoRepository.save(pagamento));
    }

    @Transactional
    public void delete(Long id) {
        if (!pagamentoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pagamento não encontrado com ID: " + id);
        }
        pagamentoRepository.deleteById(id);
    }

    private PagamentoResponseDTO toResponseDTO(Pagamento pagamento) {
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(pagamento.getId());
        dto.setValor(pagamento.getValor());
        dto.setDataPagamento(pagamento.getDataPagamento());
        dto.setStatus(pagamento.getStatus());
        dto.setClienteNome(pagamento.getCliente().getNome());
        return dto;
    }
}
