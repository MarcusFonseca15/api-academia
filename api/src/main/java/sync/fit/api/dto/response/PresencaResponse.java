package sync.fit.api.dto.response;

import java.time.LocalDateTime;

public record PresencaResponse(
        Long id,
        Long clienteId,
        String clienteNome, // Opcional, para facilitar a visualização
        LocalDateTime dataHoraRegistro
) {}