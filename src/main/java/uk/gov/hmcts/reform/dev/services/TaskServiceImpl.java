package uk.gov.hmcts.reform.dev.services;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.models.User;
import uk.gov.hmcts.reform.dev.repository.TaskRepository;
import uk.gov.hmcts.reform.dev.repository.UserRepository;
import uk.gov.hmcts.reform.dev.utils.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.utils.TaskResponse;
import uk.gov.hmcts.reform.dev.utils.TaskStatus;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {

        Long assigneeId = request.assigneeId();
        Long creatorId = request.creatorId();

        User assignee = assigneeId != null ? userRepository.findById(assigneeId).orElse(null) : null;
        User creator = creatorId != null ? userRepository.findById(creatorId).orElse(null) : null;

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDueDateTime(request.dueDateTime());
        task.setAssignee(assignee);
        task.setCreatedBy(creator);
        task.setModifiedBy(creator);

        Task createdTask = taskRepository.save(task);
        return TaskResponse.fromEntity(createdTask);

    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findActiveById(id)
            .orElseThrow(() -> new EntityNotFoundException("Active task not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAllActive();
    }

    @Override
    public Task updateTaskStatus(Long id, TaskStatus status, Long modifierId) {
        Task task = getTaskById(id);
        task.setStatus(status);
        if (modifierId != null) {
            userRepository.findById(modifierId).ifPresent(task::setModifiedBy);
        }
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id, Long modifierId) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        LocalDateTime now = LocalDateTime.now();
        int updated = modifierId != null
            ? taskRepository.markAsDeleted(id, modifierId, now)
            : taskRepository.markAsDeletedWithoutModifier(id, now);
        if (updated == 0) {
            throw new EntityNotFoundException("Could not delete task with id: " + id);
        }
    }
}
