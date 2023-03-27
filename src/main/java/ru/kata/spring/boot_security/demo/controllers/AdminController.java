package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> adminPage() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            roleService.getRoles();
            return ResponseEntity.ok(users);
        }
    }

//    @GetMapping("/new")
//    public String getUserCreateForm(@ModelAttribute("user") User user,Model model) {
//
//        model.addAttribute("roles", roleService.getRoles());
//        userService.getAllUsers();
//
//        return "new";
//    }

    @PostMapping("/new")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);

        if (createdUser == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

//    @GetMapping("/edit/{id}")
//    public String getUserEditForm(@PathVariable("id") Long id, Model model) {
//        User userById = userService.getUserById(id);
//        model.addAttribute("user", userById);
//        model.addAttribute("roles", roleService.getRoles());
//        return "edit";
//    }

    @GetMapping("/user")
    public ResponseEntity<User> userInfo(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/edit/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        User updateUser = userService.updateUser(id, user);

        if (updateUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        User deleteUser = userService.removeUser(id);

        if (deleteUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deleteUser);
    }
}
