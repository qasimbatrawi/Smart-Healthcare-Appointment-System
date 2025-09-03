package com.example.system;

import com.example.system.Enum.RoleName;
import com.example.system.Enum.SpecialtyName;
import com.example.system.entity.Role;
import com.example.system.entity.Specialty;
import com.example.system.repository.RoleRepository;
import com.example.system.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SystemApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository ;

	@Autowired
	private SpecialtyRepository specialtyRepository ;

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

	@Override
	public void run(String... args) {
		for (RoleName roleName : RoleName.values()) {
			Role role = roleRepository.findByRoleName(roleName);
			roleRepository.save(role);
		}

		for (SpecialtyName specialtyName : SpecialtyName.values()){
			Specialty specialty = specialtyRepository.findBySpecialtyName(specialtyName);
			specialtyRepository.save(specialty);
		}
	}

}
