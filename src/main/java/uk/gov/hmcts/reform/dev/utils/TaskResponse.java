package uk.gov.hmcts.reform.dev.utils;

import java.time.LocalDateTime;

import uk.gov.hmcts.reform.dev.models.Task;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDateTime dueDateTime,
    UserResponse assignee,
    UserResponse createdBy,
    UserResponse modifiedBy,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    boolean deletedFlag
) {
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getDueDateTime(),
            UserResponse.fromEntity(task.getAssignee()),
            UserResponse.fromEntity(task.getCreatedBy()),
            UserResponse.fromEntity(task.getModifiedBy()),
            task.getCreatedAt(),
            task.getModifiedAt(),
            task.isDeletedFlag()
        );
    }
}
