package sync.fit.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import sync.fit.api.dto.request.ClienteRegisterRequestDTO;
import sync.fit.api.dto.request.ClienteAdminUpdateDTO;
import sync.fit.api.dto.response.ClienteResponseDTO;

import sync.fit.api.model.Cliente;
import sync.fit.api.model.Plano;
import sync.fit.api.model.Instrutor;


@Mapper(componentModel = "spring")
public interface ClienteMapper {

    // Mapeamento de ClienteRegisterRequestDTO para Cliente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "presencas", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "instrutor", ignore = true)

    Cliente toEntity(ClienteRegisterRequestDTO dto);

    // Mapeamento de Cliente para ClienteResponseDTO
    @Mapping(source = "plano.tipo", target = "planoTipo")

    @Mapping(source = "instrutor.nome", target = "instrutorNome")
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    // Mapeamento de ClienteRequestDTO para Cliente (para atualização)
    // Use @MappingTarget para atualizar uma entidade existente
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "presencas", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "instrutor", ignore = true)

    void updateClienteFromDto(ClienteAdminUpdateDTO dto, @MappingTarget Cliente cliente);

    @Named("mapPlanoIdToPlano")
    default Plano mapPlanoIdToPlano(Long planoId) {
        return null; // Retornando null para evitar erro de compilação.
    }

    @Named("mapInstrutorIdToInstrutor")
    default Instrutor mapInstrutorIdToInstrutor(Long instrutorId) {
        return null; // Retornando null para evitar erro de compilação.
    }
}