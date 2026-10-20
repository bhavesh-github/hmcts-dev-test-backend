package uk.gov.hmcts.reform.dev.utils;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
    @NotNull(message = "Status is mandatory") TaskStatus status
) {
}
