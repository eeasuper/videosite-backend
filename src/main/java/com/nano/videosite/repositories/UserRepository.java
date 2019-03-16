package com.nano.videosite.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.User;

public interface UserRepository extends CrudRepository<User, Long>{
	public boolean existsByUsername(String username);
}
