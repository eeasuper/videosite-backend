package com.nano.videosite.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.User;

public interface UserRepository extends CrudRepository<User, Long>{
	public boolean existsByUsername(String username);
	public boolean existsByEmail(String email);
	public Optional<User> findByUsername(String username);
}
