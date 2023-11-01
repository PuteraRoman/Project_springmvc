package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.State;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final ToDoService toDoService;
    private final TaskService taskService;
    private final StateService stateService;

    @Autowired
    public TaskController(ToDoService toDoService, TaskService taskService, StateService stateService) {
        this.toDoService = toDoService;
        this.taskService = taskService;
        this.stateService = stateService;
    }

    @GetMapping("create/create/todos/{todo_id}")
    public String createForm(@PathVariable("todo_id") Long toDoId, Model model, TaskDto taskDto) {
        ToDo toDo = toDoService.readById(toDoId);
        model.addAttribute("toDo", toDo);
        model.addAttribute("taskDto", taskDto != null ? taskDto : new TaskDto());
        return "create-task";
    }

    @PostMapping("/create/todos/{todo_id}")
    public String createSubmit(@Valid @ModelAttribute("taskDto") TaskDto taskDto, BindingResult result, @PathVariable("todo_id") Long todoId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("taskDto", taskDto);
            model.addAttribute("toDo", toDoService.readById(todoId));
            return "create-task";
        }

        ToDo toDo = toDoService.readById(todoId);
        State state = stateService.readById(5L);

        Task task = TaskTransformer.convertToEntity(taskDto, toDo, state);
        taskService.create(task);
        toDo.getTasks().add(task);
        toDoService.create(toDo);

        return "todo-tasks";
    }


    @GetMapping("/{task_id}/update/todos/{todo_id}")
    public String updateForm(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId, Model model) {
        var todo = toDoService.readById(todoId);
        var tasks = taskService.getByTodoId(todoId);
        Task task = tasks.stream().filter(e -> e.getId() == taskId).findFirst().orElseGet(null);
        var states = stateService.getAll();
        var priorities = Arrays.asList(Priority.values());
        TaskDto taskDto = TaskTransformer.convertToDto(task);
        model.addAttribute("todo", todo);
        model.addAttribute("taskDto", taskDto);
        model.addAttribute("states", states);
        model.addAttribute("priorities", priorities);

        return "update-task";
    }

    @PostMapping("/{task_id}/update/todos/{todo_id}")
    public String updateSubmit(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId, @Valid @ModelAttribute("taskDto") TaskDto taskDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("taskDto", taskDto);
            model.addAttribute("todo", toDoService.readById(todoId));
            var states = stateService.getAll();
            var priorities = Arrays.asList(Priority.values());
            model.addAttribute("states", states);
            model.addAttribute("priorities", priorities);
            return "update-task";
        }

        var task = taskService.readById(taskId);
        var state = stateService.readById(taskDto.getStateId());
        task.setName(taskDto.getName());
        task.setState(state);
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        taskService.update(task);
        return "redirect:/todos/" + todoId + "/tasks";
    }

//    @GetMapping("/{task_id}/delete/todos/{todo_id}")
//    public String deleteConfirmation(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId) {
//
//        return "delete_confirmation";
//    }

    @PostMapping("/{task_id}/delete/todos/{todo_id}")
    public String delete(@PathVariable("task_id") Long taskId, @PathVariable("todo_id") Long todoId) {

        return "redirect:/tasks/" + todoId;
    }
}