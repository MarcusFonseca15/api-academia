package sync.fit.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.request.ClienteRequestDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Instrutor;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plano", source = "planoId", qualifiedByName = "mapPlanoIdToPlano")
    @Mapping(target = "administrador", source = "administradorId", qualifiedByName = "mapAdministradorIdToAdministrador")
    @Mapping(target = "instrutor", source = "instrutorId", qualifiedByName = "mapInstrutorIdToInstrutor")
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plano", source = "planoId", qualifiedByName = "mapPlanoIdToPlano")
    @Mapping(target = "administrador", source = "administradorId", qualifiedByName = "mapAdministradorIdToAdministrador")
    @Mapping(target = "instrutor", source = "instrutorId", qualifiedByName = "mapInstrutorIdToInstrutor")
    @Mapping(target = "senha", source = "senha")
    @Mapping(target = "roles", ignore = true)
    Cliente toEntity(ClienteRegisterRequestDTO dto);

    @Mapping(source = "plano.tipo", target = "planoTipo")
    @Mapping(source = "administrador.nome", target = "administradorNome")
    @Mapping(source = "instrutor.nome", target = "instrutorNome")
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    @Named("mapPlanoIdToPlano")
    default Plano mapPlanoIdToPlano(Long planoId) {
        // ATENÇÃO: Em uma aplicação real, você injetaria o PlanoRepository aqui
        // e faria a busca real. Ex: planoRepository.findById(planoId).orElse(null);
        return null;
    }

    @Named("mapAdministradorIdToAdministrador")
    default Administrador mapAdministradorIdToAdministrador(Long administradorId) {
        // ATENÇÃO: Em uma aplicação real, você injetaria o AdministradorRepository aqui
        // e faria a busca real.
        return null;
    }

    @Named("mapInstrutorIdToInstrutor")
    default Instrutor mapInstrutorIdToInstrutor(Long instrutorId) {
        // ATENÇÃO: Em uma aplicação real, você injetaria o InstrutorRepository aqui
        // e faria a busca real.
        return null;
    }
}