package uk.gov.hmcts.reform.dev.services;

import java.util.List;

import uk.gov.hmcts.reform.dev.models.Task;
import uk.gov.hmcts.reform.dev.utils.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.utils.TaskResponse;
import uk.gov.hmcts.reform.dev.utils.TaskStatus;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);

    Task getTaskById(Long id);

    List<Task> getAllTasks();

    Task updateTaskStatus(Long id, TaskStatus status, Long modifierId);

    void deleteTask(Long id, Long modifierId);
}
