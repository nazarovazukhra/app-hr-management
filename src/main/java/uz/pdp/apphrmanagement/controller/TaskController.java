package uz.pdp.apphrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.TaskDto;
import uz.pdp.apphrmanagement.service.TaskService;

import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    public HttpEntity<?> createTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.createTask(taskDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping
    public HttpEntity<?> editTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.editTask(taskDto);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);
    }

    @GetMapping
    public HttpEntity<?> getAll() {
        Object result = taskService.get();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        Object result = taskService.getById(id);
        return ResponseEntity.ok(result);
    }
    // yana task ni createdByUser va resIdUser larini edit qiluvchi method yozish kerak

    @PostMapping("/changeTaskStatus")
    public HttpEntity<?> changeTaskStatusInProgress(@RequestParam String name, @RequestParam UUID id, @RequestParam Integer number) {
        ApiResponse apiResponse = taskService.changeTaskStatus(name, id, number);
        return ResponseEntity.status(apiResponse.getSuccess() ? 202 : 409).body(apiResponse);
    }
}
