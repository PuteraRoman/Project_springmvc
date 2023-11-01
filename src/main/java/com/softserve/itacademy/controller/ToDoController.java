package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
public class ToDoController {


    private final ToDoService toDoService;
    private final UserService userService;

    public ToDoController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }

    @GetMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, Model model) {
        model.addAttribute("todo", new ToDo());
        return "create-todo";
    }

    @PostMapping("/create/users/{owner_id}")
    public String create(@PathVariable("owner_id") long ownerId, @ModelAttribute ToDo todo) {
        User owner = userService.readById(ownerId);
        todo.setOwner(owner);
        toDoService.create(todo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{id}/tasks")
    public String read(@PathVariable("id") long id, Model model) {
        ToDo todo = toDoService.readById(id);
        model.addAttribute("todo", todo);
        return "todo-tasks";
    }

    @GetMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("todo_id") long todoId, Model model) {
        ToDo todo = toDoService.readById(todoId);
        model.addAttribute("todo", todo);
        return "update-todo";
    }

    @PostMapping("/{todo_id}/update/users/{owner_id}")
    public String update(@PathVariable("owner_id") long ownerId, @ModelAttribute ToDo todo) {
        toDoService.update(todo);
        return "redirect:/todos/all/users/" + ownerId;
    }

    @GetMapping("/{todo_id}/delete/users/{owner_id}")
    public String delete(@PathVariable("owner_id") long ownerId, @PathVariable("todo_id") long todoId) {
        toDoService.delete(todoId);
        return "redirect:/todos/all/users/" + ownerId;

    }

    @GetMapping("/all/users/{user_id}")
    public String getAll(@PathVariable("user_id") long userId, Model model) {
        User user = userService.readById(userId);
        model.addAttribute("user", user);
        model.addAttribute("todos", toDoService.getByUserId(userId));
        return "todo-lists";
    }

    @GetMapping("/{id}/add")
    public String addCollaborator(@PathVariable("id") long id, @RequestParam("collaboratorId") long collaboratorId) {
        ToDo todo = toDoService.readById(id);
        User collaborator = userService.readById(collaboratorId);

        todo.getCollaborators().add(collaborator);
        toDoService.update(todo);

        return "redirect:/todos/all/users/" + todo.getOwner().getId();
    }

    @GetMapping("/{id}/remove")
    public String removeCollaborator(@PathVariable("id") long id, @RequestParam("collaboratorId") long collaboratorId) {
        ToDo todo = toDoService.readById(id);
        User collaborator = userService.readById(collaboratorId);

        todo.getCollaborators().remove(collaborator);
        toDoService.update(todo);

        return "redirect:/todos/all/users/" + todo.getOwner().getId();
    }
}