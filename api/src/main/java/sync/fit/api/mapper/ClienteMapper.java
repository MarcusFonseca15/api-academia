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

@Mapper(componentModel = "spring") // Torna o MapStruct um componente Spring, injetável
public interface ClienteMapper {

    // Mapper para converter ClienteRequestDTO para Cliente (para PUT/update)
    @Mapping(target = "id", ignore = true) // ID é gerado pelo banco, não vem do DTO
    @Mapping(target = "plano", source = "planoId", qualifiedByName = "mapPlanoIdToPlano") // Mapeamento manual para Plano
    @Mapping(target = "administrador", source = "administradorId", qualifiedByName = "mapAdministradorIdToAdministrador") // Mapeamento manual para Administrador
    @Mapping(target = "senha", ignore = true) // A senha será tratada no serviço, não mapeada diretamente aqui
    @Mapping(target = "roles", ignore = true) // Roles serão tratadas no serviço, não mapeadas diretamente aqui
    Cliente toEntity(ClienteRequestDTO dto);

    // Mapper para converter ClienteRegisterRequestDTO para Cliente (para POST/create)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plano", source = "planoId", qualifiedByName = "mapPlanoIdToPlano")
    @Mapping(target = "administrador", source = "administradorId", qualifiedByName = "mapAdministradorIdToAdministrador")
    // A senha do ClienteRegisterRequestDTO será criptografada e definida no serviço
    @Mapping(target = "senha", source = "senha") // MapStruct mapeará a senha aqui, mas ela será re-setada pelo encoder no service
    @Mapping(target = "roles", ignore = true)
    Cliente toEntity(ClienteRegisterRequestDTO dto);


    // Mapper para converter Cliente para ClienteResponseDTO
    @Mapping(source = "plano.tipo", target = "planoTipo") // Mapeia o tipo do plano
    @Mapping(source = "administrador.nome", target = "administradorNome") // Mapeia o nome do administrador
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    // Métodos Named para mapear IDs para entidades (serão preenchidos no serviço)
    @Named("mapPlanoIdToPlano")
    default Plano mapPlanoIdToPlano(Long planoId) {
        // Isso é um placeholder. A busca real pelo Plano deve ser feita no serviço.
        // O Mapper apenas indica que existe um mapeamento.
        return null;
    }

    @Named("mapAdministradorIdToAdministrador")
    default Administrador mapAdministradorIdToAdministrador(Long administradorId) {
        // Isso é um placeholder. A busca real pelo Administrador deve ser feita no serviço.
        return null;
    }
}