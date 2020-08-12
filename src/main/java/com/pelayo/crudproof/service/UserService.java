package com.pelayo.crudproof.service;

import java.util.List;
import java.util.Optional;

import com.pelayo.crudproof.model.User;

public interface UserService {

	long count();

	void delete(User user);

	void deleteAll();

	void deleteAll(List<User> users);

	void deleteById(Long id);

	boolean existsById(Long id);

	List<User> findAll();

	List<User> findAllById(List<Long> ids);

	Optional<User> findById(Long id);

	User save(User user);

	List<User> saveAll(List<User> users);

	List<User> selectAll();

	int softDelete(Long id);

	User fetchById(Long id);
}
