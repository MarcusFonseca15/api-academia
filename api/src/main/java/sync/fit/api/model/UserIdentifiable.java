package sync.fit.api.model;

public interface UserIdentifiable {
    Long getId();
    String getEmail(); // Pode ser útil para logs ou outros propósitos
    // Adicione outros métodos comuns se necessário
}