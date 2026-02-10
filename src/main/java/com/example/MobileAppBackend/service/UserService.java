package com.example.MobileAppBackend.service;


import com.example.MobileAppBackend.dto.create.CreateUserRequest;
import com.example.MobileAppBackend.model.User;
import com.example.MobileAppBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public User createUser(CreateUserRequest userDto){

        if(userRepository.existsUserByEmail(userDto.getEmail())) {
            log.warn("User with email {} already exists", userDto.getEmail());
            throw new RuntimeException("User with this email already exists");
        }
        User user = modelMapper.map(userDto, User.class);
        log.debug("Created user {}", user);
        return this.userRepository.save(user);
    }

    public User editUser(String user_id, CreateUserRequest userDto){

        User existingUser = userRepository.findById(user_id)
                .orElseThrow(() ->{
                    log.warn("User with id {} does not exist", user_id);
                    return new RuntimeException("User not found");
                });

        User user = modelMapper.map(userDto, User.class);

        if(!existingUser.getId().equals(getCurrentUserId())){
            log.warn("You are not allowed to edit this user");
            throw new RuntimeException("You are not allowed to edit this user ");
        }
        System.out.println(userDto.toString());
        Optional.ofNullable(user.getUsername()).ifPresent(existingUser::setUsername);
        Optional.ofNullable(user.getEmail()).ifPresent(existingUser::setEmail);
        Optional.ofNullable(user.getPassword()).ifPresent(existingUser::setPassword);
        Optional.ofNullable(user.getAvatar()).ifPresent(existingUser::setAvatar);
        Optional.ofNullable(user.getPreferred_tags()).ifPresent(existingUser::setPreferred_tags);
        Optional.ofNullable(user.getPreferred_cuisine()).ifPresent(existingUser::setPreferred_cuisine);
        Optional.ofNullable(user.getAllergies()).ifPresent(existingUser::setAllergies);
        Optional.ofNullable(user.getFavorites()).ifPresent(existingUser::setFavorites);
        log.debug("User body {}", user);
        log.info("User updated successfully");
        return userRepository.save(existingUser);
    }

    public User getUserById(String id){
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public User getUserByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }
    public List<User> findAll(){
        return this.userRepository.findAll();
    }

    public void deleteUser(String user_id){
       Optional<User> optionalUser = userRepository.findById(user_id);
       User user = optionalUser.get();
       if(!user.getId().equals(getCurrentUserId())){
           log.warn("You are not allowed to delete this user");
            throw new RuntimeException("You are not allowed to remove this user ");
       }
       optionalUser.ifPresent(userRepository::delete);
       log.info("User deleted successfully");
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }



}
