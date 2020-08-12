package com.pelayo.crudproof.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pelayo.crudproof.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	@Modifying
	@Query("update user u set u.deletedAt = current_date where u.id = :id and u.deletedAt is null")
	int softDelete(@Param("id") Long id);

	@Query("select u from user u where u.deletedAt is null ")
	List<User> selectAll();

	@Query("select u from user u where u.id = :id and u.deletedAt is null ")
	User fetchById(@Param("id") Long id);


}
