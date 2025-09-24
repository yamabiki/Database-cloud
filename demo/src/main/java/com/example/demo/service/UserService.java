package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User findById(@NotNull Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
	}

	public User create(@Valid User user) {
		// Ignore client-sent id/createdAt to avoid insert errors
		user.setId(null);
		user.setCreatedAt(null);
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email already in use");
		}
		return userRepository.save(user);
	}

	public User update(@NotNull Long id, @Valid User update) {
		User existing = findById(id);
		if (!existing.getEmail().equals(update.getEmail()) && userRepository.existsByEmail(update.getEmail())) {
			throw new IllegalArgumentException("Email already in use");
		}
		existing.setName(update.getName());
		existing.setEmail(update.getEmail());
		existing.setAge(update.getAge());
		existing.setActive(update.getActive());
		return userRepository.save(existing);
	}

	public void delete(@NotNull Long id) {
		if (!userRepository.existsById(id)) {
			throw new EntityNotFoundException("User not found: " + id);
		}
		userRepository.deleteById(id);
	}
}


