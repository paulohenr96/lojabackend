package com.paulo.estudandoconfig.mapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.model.Role;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.RoleRepository;

@Component
public class UserAccountMapper {

    private  ModelMapper mapper;
    private  RoleRepository roleRepo;

    public UserAccountMapper(ModelMapper mapper, RoleRepository roleRepo) {
        this.mapper = mapper;
        this.roleRepo = roleRepo;
    }

    private Set<Role> rolesNamesToObjects(List<String> names) {
        return names.stream()
                .map(name -> roleRepo.findByName(name))
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Function<UserAccountDTO,UserAccount> toEntity=(user)-> {
        UserAccount account = mapper.map(user, UserAccount.class);
        account.setRoles(rolesNamesToObjects(user.getRolesName()));
        return account;
    };

    public UserAccountDTO toDTO(UserAccount user) {
        UserAccountDTO dto = mapper.map(user, UserAccountDTO.class);
        dto.setRolesName(user.getRoles().stream().map(Role::getName).toList());
        return dto;
    }
}
