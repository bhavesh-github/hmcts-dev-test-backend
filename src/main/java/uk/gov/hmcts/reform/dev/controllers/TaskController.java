package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.services.TaskService;
import uk.gov.hmcts.reform.dev.utils.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.utils.UpdateTaskStatusRequest;
import uk.gov.hmcts.reform.dev.utils.TaskResponse;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(TaskResponse.fromEntity(taskService.getTaskById(id)));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> list = taskService.getAllTasks().stream()
            .map(TaskResponse::fromEntity)
            .toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
        @PathVariable Long id,
        @Valid @RequestBody UpdateTaskStatusRequest request,
        @RequestParam(required = false) Long modifierId
    ) {
        Task updated = taskService.updateTaskStatus(id, request.status(), modifierId);
        return ResponseEntity.ok(TaskResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
        @PathVariable Long id,
        @RequestParam(required = false) Long modifierId
    ) {
        taskService.deleteTask(id, modifierId);
        return ResponseEntity.noContent().build();
    }
}
