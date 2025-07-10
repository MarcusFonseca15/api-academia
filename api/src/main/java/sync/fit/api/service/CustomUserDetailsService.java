package sync.fit.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sync.fit.api.repository.FuncionarioRepository;
import sync.fit.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final FuncionarioRepository funcionarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Tenta encontrar o usuário como funcionário (Administrador, Instrutor, Recepcionista)
        return funcionarioRepository.findByEmail(email)
                .map(funcionario -> (UserDetails) funcionario) // Se encontrar, retorna como UserDetails
                // Se não encontrar como funcionário, tenta encontrar como cliente
                .or(() -> clienteRepository.findByEmail(email).map(cliente -> (UserDetails) cliente))
                // Se não encontrar em nenhum dos dois, lança exceção
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}