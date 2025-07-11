package sync.fit.api.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sync.fit.api.service.PagamentoService;

@Component
@RequiredArgsConstructor
public class NotificacaoScheduler {

    private final PagamentoService pagamentoService;

    // Executa todo dia às 08:00
    @Scheduled(cron = "0 0 8 * * ?")
    public void verificarPagamentosVencendo() {
        pagamentoService.notificarPagamentosVencendo();
    }
}
