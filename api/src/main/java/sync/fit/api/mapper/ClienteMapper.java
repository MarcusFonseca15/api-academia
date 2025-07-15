package sync.fit.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget; // Importar para o update
import org.mapstruct.Named;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
// import sync.fit.api.model.Administrador; // REMOVIDO
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Instrutor;

// ATENÇÃO: Se você pretende que o mapper faça a busca de entidades por ID,
// você precisará injetar os repositórios aqui. Exemplo:
// import org.springframework.beans.factory.annotation.Autowired;
// import sync.fit.api.repository.PlanoRepository;
// import sync.fit.api.repository.InstrutorRepository;
// ...
// @Autowired
// private PlanoRepository planoRepository;
// @Autowired
// private InstrutorRepository instrutorRepository;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class); // Não precisa se componentModel="spring"

    // Mapeamento de ClienteRegisterRequestDTO para Cliente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true) // Senha é criptografada e setada no service
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "presencas", ignore = true)
    @Mapping(target = "plano", ignore = true) // Setado manualmente no service
    @Mapping(target = "instrutor", ignore = true) // Setado manualmente no service
    // REMOVIDO: @Mapping(target = "administrador", ignore = true)
    Cliente toEntity(ClienteRegisterRequestDTO dto);

    // Mapeamento de Cliente para ClienteResponseDTO
    @Mapping(source = "plano.tipo", target = "planoTipo")
    // REMOVIDO: @Mapping(source = "administrador.nome", target = "administradorNome")
    @Mapping(source = "instrutor.nome", target = "instrutorNome")
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    // Mapeamento de ClienteRequestDTO para Cliente (para atualização)
    // Use @MappingTarget para atualizar uma entidade existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true) // O email não deve ser alterado aqui se você já faz a validação no service
    @Mapping(target = "senha", ignore = true) // Senha é tratada separadamente ou é opcional na atualização
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "presencas", ignore = true)
    @Mapping(target = "plano", ignore = true) // Setado manualmente no service
    @Mapping(target = "instrutor", ignore = true) // Setado manualmente no service
    // REMOVIDO: @Mapping(target = "administrador", ignore = true)
    void updateClienteFromDto(ClienteRequestDTO dto, @MappingTarget Cliente cliente);

    // REMOVIDO: Métodos @Named para Administrador
    // @Named("mapAdministradorIdToAdministrador")
    // default Administrador mapAdministradorIdToAdministrador(Long administradorId) { return null; }

    // Manter os métodos @Named para Plano e Instrutor, mas eles devem injetar os repositórios
    // se o mapper for responsável por buscar essas entidades por ID.
    // Como você já está buscando no service, pode remover esses @Named e os "source" nos mappings
    // ou fazer a injeção no mapper. Se o service está fazendo a busca, a injeção no mapper não é estritamente necessária
    // para estes mappings (desde que você defina ignore = true para os targets "plano" e "instrutor" no toEntity).
    // Vou manter como está, mas reforço a necessidade de injetar repositórios para um comportamento real.

    @Named("mapPlanoIdToPlano")
    default Plano mapPlanoIdToPlano(Long planoId) {
        // ATENÇÃO: Para uma funcionalidade real, você DEVE INJETAR O PLANOREPOSITORY AQUI
        // e buscar o plano. Ex:
        // @Autowired
        // private PlanoRepository planoRepository;
        // return planoRepository.findById(planoId).orElse(null);
        return null; // Retornando null para evitar erro de compilação.
    }

    @Named("mapInstrutorIdToInstrutor")
    default Instrutor mapInstrutorIdToInstrutor(Long instrutorId) {
        // ATENÇÃO: Para uma funcionalidade real, você DEVE INJETAR O INSTRUTORREPOSITORY AQUI
        // e buscar o instrutor. Ex:
        // @Autowired
        // private InstrutorRepository instrutorRepository;
        // return instrutorRepository.findById(instrutorId).orElse(null);
        return null; // Retornando null para evitar erro de compilação.
    }
}