package com.paulo.estudandoconfig.mapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.paulo.estudandoconfig.dto.UserAccountDTO;
import com.paulo.estudandoconfig.model.Role;
import com.paulo.estudandoconfig.model.UserAccount;
import com.paulo.estudandoconfig.repository.RoleRepository;
@Component
public class ProductMapper {
	@Autowired
	private static ModelMapper mapper;
	@Autowired
	private static RoleRepository roleRepo;

	static private Function<List<String>, Set<Role>> rolesNamesToObjects = names -> names.stream()
			.map(name -> roleRepo.findByName(name)).map(Optional::get).collect(Collectors.toSet());

	static public Function<UserAccountDTO, UserAccount> toEntity = (user) -> {
		UserAccount account = mapper.map(user, UserAccount.class);
		account.setRoles(rolesNamesToObjects.apply(user.getRolesName()));
		return account;
	};
	
	
	static public UserAccountDTO toDTO(UserAccount user) {
		UserAccountDTO dto = new ModelMapper().map(user, UserAccountDTO.class);
		dto.setRolesName(user.getRoles().stream().map(Role::getName).toList());

		return dto;
	}
}
