package sync.fit.api.repository;

import sync.fit.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Método para buscar uma Role pelo nome (ex: "ROLE_CLIENTE")
    Optional<Role> findByName(String name);
}