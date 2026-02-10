package com.example.MobileAppBackend.controller;

import com.example.MobileAppBackend.dto.create.CreateUserRequest;
import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/username/{name}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String name){
        System.out.println("getUser");
        return ResponseEntity.ok(userService.getUserByUsername(name));
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest userDto){
        System.out.println("User controller: " + userDto.toString());
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@PathVariable String id, @RequestBody CreateUserRequest userDto){
        System.out.println("Edit: " + userDto.toString());
        return ResponseEntity.ok(userService.editUser(id, userDto));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteUser(@PathVariable String name){
        this.userService.deleteUser(name);
        return ResponseEntity.ok().build();
    }








}
