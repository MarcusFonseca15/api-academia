package sync.fit.api.dto.response;

import java.time.LocalDateTime;

public record PresencaResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        LocalDateTime dataHoraRegistro
) {}