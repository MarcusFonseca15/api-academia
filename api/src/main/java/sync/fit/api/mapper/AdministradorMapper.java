package sync.fit.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import sync.fit.api.dto.response.AdministradorResponseDTO;
import sync.fit.api.model.Administrador;
import sync.fit.api.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdministradorMapper {

    @Mapping(source = "cargo.nomeCargo", target = "cargoNome")
    @Mapping(source = "cargo.id", target = "cargoId")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapRoles")
    AdministradorResponseDTO toResponseDTO(Administrador administrador);

    List<AdministradorResponseDTO> toResponseDTOList(List<Administrador> administradores);

    @Named("mapRoles")
    static Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
