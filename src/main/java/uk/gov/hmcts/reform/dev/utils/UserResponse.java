package uk.gov.hmcts.reform.dev.utils;

import uk.gov.hmcts.reform.dev.models.User;

public record UserResponse(Long id, String email, String name, String role) {
    public static UserResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}
