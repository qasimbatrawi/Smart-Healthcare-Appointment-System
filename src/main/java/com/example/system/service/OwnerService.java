package com.example.system.service;

import com.example.system.Enum.RoleName;
import com.example.system.dto.UserDTO;
import com.example.system.entity.User;
import com.example.system.exception.BadRequestException;
import com.example.system.exception.ResourceNotFoundException;
import com.example.system.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final RoleRepository roleRepository ;
    private final UserRepository userRepository ;

    public User getOwner(){
        return userRepository.findByRole_RoleName(RoleName.OWNER).get(0) ;
    }

    public User updateOwner(UserDTO newOwnerDetails){

        User owner = userRepository.findByRole_RoleName(RoleName.OWNER).get(0) ;

        String newUsername = newOwnerDetails.getUsername() ;
        String newEmail = newOwnerDetails.getEmail() ;
        String newName = newOwnerDetails.getName() ;
        String newPassword = newOwnerDetails.getPassword() ;

        if (newUsername == null || newEmail == null || newName == null || newPassword == null){
            throw new ResourceNotFoundException("Fields must not be empty.") ;
        }

        owner.setUsername(newUsername) ;
        owner.setEmail(newEmail) ;
        owner.setName(newName) ;
        owner.setPassword(newPassword) ;

        try {
            return userRepository.save(owner);
        } catch (Exception e){
            throw new BadRequestException("Invalid email format. Email or username is used.");
        }
    }

    public List<User> getAllAdmins(){
        return userRepository.findByRole_RoleName(RoleName.ADMIN) ;
    }

    public User getAdminByUsername(String username){
        User user = userRepository.findByUsernameAndRole_RoleName(username, RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found")) ;

        return user ;
    }

    public User updateAdminByUsername(String username , UserDTO newUserDetails){

        User user = userRepository.findByUsernameAndRole_RoleName(username, RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found")) ;

        String newUsername = newUserDetails.getUsername() ;
        String newEmail = newUserDetails.getEmail() ;
        String newName = newUserDetails.getName() ;
        String newPassword = newUserDetails.getPassword() ;

        if (newUsername == null || newEmail == null || newName == null || newPassword == null){
            throw new BadRequestException("Fields must not be empty.") ;
        }

        user.setUsername(newUsername) ;
        user.setEmail(newEmail) ;
        user.setName(newName) ;
        user.setPassword(newPassword) ;

        try {
            return userRepository.save(user);
        } catch (Exception e){
            throw new BadRequestException("Invalid email format. Email or username is used.");
        }
    }

    public void deleteAdminByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
        userRepository.delete(user);
    }
}
