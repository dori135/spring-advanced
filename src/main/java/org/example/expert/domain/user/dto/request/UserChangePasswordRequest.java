package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.") // 8자 이상
    @Pattern(regexp = ".*\\d.*", message = "새 비밀번호는 숫자를 포함해야 합니다.") // 숫자 포함
    @Pattern(regexp = ".*[A-Z].*", message = "새 비밀번호는 대문자를 포함해야 합니다.") // 대문자 포함
    private String newPassword;
}
