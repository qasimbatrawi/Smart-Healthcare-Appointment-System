package com.example.system.controller;

import com.example.system.dto.UserDTO;
import com.example.system.entity.User;
import com.example.system.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService ;

    @GetMapping
    public User getOwner(){
        return ownerService.getOwner() ;
    }

    @PatchMapping
    public ResponseEntity<User> updateOwner(@RequestBody UserDTO newOwnerDetails){
        User user = ownerService.updateOwner(newOwnerDetails);
        return ResponseEntity.ok(user) ;
    }

    @GetMapping("/all_admins")
    public List<User> getAllAdmins(){
        return ownerService.getAllAdmins() ;
    }

    @GetMapping("/admin/{username}")
    public ResponseEntity<Object> getAdminByUsername(@PathVariable String username){
        try {
            User user = ownerService.getAdminByUsername(username);
            return ResponseEntity.ok(user) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @PatchMapping("/admin/{username}")
    public ResponseEntity<Object> updateAdminByUsername(@PathVariable String username , @RequestBody UserDTO newAdminDetails){
        try {
            User user = ownerService.updateAdminByUsername(username, newAdminDetails);
            return ResponseEntity.ok(user) ;
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }

    @DeleteMapping("/admin/{username}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable String username){
        try{
            ownerService.deleteAdminByUsername(username) ;
            return ResponseEntity.noContent().build() ;
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage()) ;
        }
    }
}
