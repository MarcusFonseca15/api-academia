package sync.fit.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import sync.fit.api.dto.response.ClienteResponseDTO;
import sync.fit.api.dto.response.InstrutorResponseDTO;
import sync.fit.api.model.Cliente;
import sync.fit.api.model.Instrutor;
import sync.fit.api.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = { ClienteMapper.class })
public interface InstrutorMapper {

    @Mapping(source = "cargo.nomeCargo", target = "cargoNome")
    @Mapping(source = "cargo.id", target = "cargoId")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRoles")
    InstrutorResponseDTO toResponseDTO(Instrutor instrutor);

    List<InstrutorResponseDTO> toResponseDTOList(List<Instrutor> instrutores);

    List<ClienteResponseDTO> toClienteResponseDTOList(List<Cliente> clientes);

    @Named("mapRoles")
    static Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
