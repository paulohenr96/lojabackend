package com.paulo.estudandoconfig.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paulo.estudandoconfig.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	
	Optional<Role> findByName(String name);
}
