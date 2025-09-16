package com.example.system;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.Role;
import com.example.system.entity.Specialty;
import com.example.system.entity.User;
import com.example.system.repository.RoleRepository;
import com.example.system.repository.SpecialtyRepository;
import com.example.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class SystemApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository ;

	@Autowired
	private SpecialtyRepository specialtyRepository ;

	@Autowired
	private UserRepository userRepository ;

	@Autowired
	private PasswordEncoder passwordEncoder ;

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// add roles to the database
		for (RoleName roleName : RoleName.values()) {
			Role role = roleRepository.findByRoleName(roleName);
			if (role == null) {
				role = new Role();
				role.setRoleName(roleName);
				roleRepository.save(role);
			}
		}

		// add specialties to the database
		for (SpecialtyName specialtyName : SpecialtyName.values()){
			Specialty specialty = specialtyRepository.findBySpecialtyName(specialtyName);
			if (specialty == null) {
				specialty = new Specialty();
				specialty.setSpecialtyName(specialtyName);
				specialtyRepository.save(specialty);
			}
		}

		List<User> owners = userRepository.findByRole_RoleName(RoleName.OWNER);
		if (owners.isEmpty()) {
			User owner = new User();
			owner.setName("Qasim Batrawi");
			owner.setEmail("qasim123batrawi@gmail.com");
			owner.setUsername(System.getenv("OWNER_USERNAME"));
			owner.setPassword(passwordEncoder.encode(System.getenv("OWNER_PASSWORD")));
			owner.setRole(roleRepository.findByRoleName(RoleName.OWNER));
			userRepository.save(owner);
		}
	}

}
