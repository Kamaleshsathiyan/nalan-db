package com.nalanbatters.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nalanbatters.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

}
