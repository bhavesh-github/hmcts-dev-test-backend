package uk.gov.hmcts.reform.dev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.hmcts.reform.dev.controllers.TaskController;
import uk.gov.hmcts.reform.dev.models.User;
import uk.gov.hmcts.reform.dev.services.TaskService;
import uk.gov.hmcts.reform.dev.utils.CreateTaskRequest;
import uk.gov.hmcts.reform.dev.utils.TaskResponse;
import uk.gov.hmcts.reform.dev.utils.TaskStatus;
import uk.gov.hmcts.reform.dev.utils.UserResponse;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TaskControllerUnitTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User getUser() {
        return new User(1L, "user@testh.uk",
                        "TestUser", "ADMIN");
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        LocalDateTime current = LocalDateTime.now();
        CreateTaskRequest request = new CreateTaskRequest("Submit HMCTS Report",
                                                          "Complete the draft layout backend test",
                                                          TaskStatus.NEW,
                                                          current,
                                                          getUser().getId(),
                                                          getUser().getId());

        UserResponse userResponse = UserResponse.fromEntity(getUser());
        TaskResponse expectedResponse = new TaskResponse(1L,
                                                         "Submit HMCTS Report",
                                                         "Complete the draft layout backend test",
                                                         TaskStatus.NEW,
                                                         current,
                                                         userResponse,
                                                         userResponse,
                                                         userResponse,
                                                         current,
                                                         current,
                                                         false
        );

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<TaskResponse> response = taskController.createTask(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Submit HMCTS Report", response.getBody().title());
        assertEquals(TaskStatus.NEW, response.getBody().status());
    }
}
