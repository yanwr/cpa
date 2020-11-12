package br.com.softplan.controllers;


import br.com.softplan.exceptions.DataNotFoundException;
import br.com.softplan.models.User;
import br.com.softplan.models.dto.UserDTO;
import br.com.softplan.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/users")
@Api(tags = "User Micro-Services Controller", value = "UserController", description = "Controller for User Micro-Services")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder crypto;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    @ApiOperation(value = "Retrieves a list of all users", notes = "This endpoint retrieves all users (Admin Only)")
    public ResponseEntity<List<UserDTO>> index() throws Exception {
        List<User> userList = userService.index();
        List<UserDTO> userDTOList = userList.stream().map(UserDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(userDTOList);
    }

    @PreAuthorize("hasAnyRole('TRIADOR')")
    @GetMapping(value = "/finishers")
    @ApiOperation(value = "Retrieves a list of all finishers users",
            notes = "This endpoint retrieves all finishers users (Triator Only)")
    public ResponseEntity<List<UserDTO>> showFinishers() {
        List<User> userList = userService.findFinishers();
        List<UserDTO> userDTOList = userList.stream().map(UserDTO::new).collect(Collectors.toList());
        if (userDTOList.isEmpty()) {
            throw new DataNotFoundException("List of finishers users not found.");
        }
        return ResponseEntity.ok().body(userDTOList);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Retrieves an specific user by unique identifier",
            notes = "This endpoint retrieves an user by unique identifier (Admin Only)")
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
        User currentUser = userService.show(id);
        UserDTO currentUserDTO = new UserDTO(currentUser);
        return ResponseEntity.ok().body(currentUserDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    @ApiOperation(value = "Create a new user", notes = "This endpoint creates a new user (Admin Only)")
    public ResponseEntity<UserDTO> store(@Validated @RequestBody User newUser) {
        newUser.setPassword(crypto.encode(newUser.getPassword()));
        UserDTO newUserSaved = new UserDTO(userService.store(newUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserSaved);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{id}")
    @ApiOperation(value = "Update an user by unique identifier",
            notes = "This endpoint update data from user by unique identifier (Admin Only)")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO updateUser) {
        User user = userService.show(id);
        User userUpdate = userService.update(id, new User().update(updateUser, user));
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(userUpdate));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete an user by unique identifier",
            notes = "This endpoint deletes an user by unique identifier (Admin Only)")
    public ResponseEntity<HttpStatus> destroy(@PathVariable Long id) {
        if (userService.destroy(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
