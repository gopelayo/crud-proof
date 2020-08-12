package com.pelayo.crudproof.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.pelayo.crudproof.exception.UserNotFoundException;
import com.pelayo.crudproof.model.User;
import com.pelayo.crudproof.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private UserService userService;

	@GetMapping
	public List<User> findAll() {
		return userService.selectAll();
	}

	@DeleteMapping
	public void deleteAll() {
		userService.deleteAll();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {
		if (!userService.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			userService.deleteById(id);
			return ResponseEntity.accepted().build();
		}
	}

	@DeleteMapping("/soft/{id}")
	public ResponseEntity<?> softDeleteById(@PathVariable("id") Long id) {
		if (userService.fetchById(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			userService.softDelete(id);
			return ResponseEntity.accepted().build();
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") Long id) {
		if (!userService.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} else {
			return ResponseEntity.ok(userService.findById(id).orElseGet(null));
		}
	}

	@PostMapping
	public List<User> saveAll(@RequestBody List<User> users) {
		return userService.saveAll(users);
	}

	@PutMapping(path = "/{id}", consumes = "application/json")
	public ResponseEntity<User> putUser(@PathVariable Long id, @RequestBody User data) {
		try {

			User user = userService.findById(id).map(entity -> {
				entity.setFirstName(data.getFirstName());
				entity.setLastName(data.getLastName());
				entity.setUsername(data.getUsername());
				entity.setPassword(data.getPassword());
				return userService.save(entity);
			}).orElseGet(() -> {
				return null;
			});

			if (user == null) {
				throw new UserNotFoundException();
			} else {
				return ResponseEntity.ok(user);
			}
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
	public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody JsonPatch patch) {
		try {
			User user = userService.findById(id).orElseThrow(UserNotFoundException::new);
			User userPatched = applyPatchToCustomer(patch, user);
			userPatched = userService.save(userPatched);
			return ResponseEntity.ok(userPatched);
		} catch (JsonPatchException | JsonProcessingException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	private User applyPatchToCustomer(JsonPatch patch, User targetUser)
			throws JsonPatchException, JsonProcessingException {
		JsonNode patched = patch.apply(objectMapper.convertValue(targetUser, JsonNode.class));
		return objectMapper.treeToValue(patched, User.class);
	}

}
