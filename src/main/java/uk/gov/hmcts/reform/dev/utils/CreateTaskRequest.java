package uk.gov.hmcts.reform.dev.utils;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateTaskRequest(
    @NotBlank(message = "Title is mandatory") String title,
    String description,
    @NotNull(message = "Status is mandatory") TaskStatus status,
    @NotNull(message = "Due date/time is mandatory") @FutureOrPresent LocalDateTime dueDateTime,
    Long assigneeId,
    Long creatorId

) {
}
