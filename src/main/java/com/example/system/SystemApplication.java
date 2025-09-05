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

@SpringBootApplication
public class SystemApplication implements CommandLineRunner{

	@Autowired
	private RoleRepository roleRepository ;

	@Autowired
	private SpecialtyRepository specialtyRepository ;

	@Autowired
	private UserRepository userRepository ;

	public static void main(String[] args) {
		SpringApplication.run(SystemApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// add roles to the database
		for (RoleName roleName : RoleName.values()) {
			Role role = roleRepository.findByRoleName(roleName);
			roleRepository.save(role);
		}

		// add specialties to the database
		for (SpecialtyName specialtyName : SpecialtyName.values()){
			Specialty specialty = specialtyRepository.findBySpecialtyName(specialtyName);
			specialtyRepository.save(specialty);
		}

		try {
			userRepository.findByRole_RoleName(RoleName.OWNER).getFirst();
		} catch(Exception e){
			// add one OWNER to the database
			User owner = new User() ;
			owner.setName("Qasim Batrawi");
			owner.setEmail("qasim123batrawi@gmail.com");
			owner.setUsername("qasimbatrawi_04");
			owner.setPassword("100200300");
			owner.setRole(roleRepository.findByRoleName(RoleName.OWNER));
			userRepository.save(owner) ;
		}


	}

}
