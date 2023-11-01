package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        userService.create(user);
        return "todos-user";
    }


    @GetMapping("/{id}/read")
    public String readUser(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/{id}/update")
    public String showUpdateUserForm(@PathVariable long id, Model model) {
        User user = userService.readById(id);
        model.addAttribute("user", user);
        return "update-user";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/users";
    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAll());
        return "home";
    }
}