package com.pelayo.crudproof.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pelayo.crudproof.model.User;
import com.pelayo.crudproof.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	public UserServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long count() {
		return userRepository.count();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAll() {
		userRepository.deleteAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAll(List<User> users) {
		userRepository.deleteAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return userRepository.existsById(id);
	}



	@Override
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public List<User> findAllById(List<Long> ids) {
		return (List<User>) userRepository.findAllById(ids);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = false)
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	@Transactional(readOnly = false)
	public List<User> saveAll(List<User> users) {
		return (List<User>) userRepository.saveAll(users);
	}

	@Override
	public List<User> selectAll() {
		return userRepository.selectAll();
	}

	@Override
	public int softDelete(Long id) {
		return userRepository.softDelete(id);
	}

	@Override
	public User fetchById(Long id) {
		return userRepository.fetchById(id);
	}

}
