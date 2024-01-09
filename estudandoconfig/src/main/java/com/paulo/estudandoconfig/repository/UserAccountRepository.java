package com.paulo.estudandoconfig.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.paulo.estudandoconfig.model.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	
	Optional<UserAccount> findByUserName(String userName);

}
