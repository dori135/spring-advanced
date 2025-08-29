package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {

    @InjectMocks
    private UserAdminService userAdminService; // InjectMocks 쓰면 final 사용할 수 없음

    @Mock
    private UserRepository userRepository;

    @Test
    void changeUserRole_유저가_없을_경우_예외가_발생한다() {
        // given
        long userId = 123L;
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("ADMIN");

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> userAdminService.changeUserRole(userId, userRoleChangeRequest)
        );
    }

    @Test
    void UserRole가_정상적으로_변경된다() {
        // given
        long userId = 123L;
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("ADMIN");
        User mockUser = new User("b@b.com", "password", UserRole.USER);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(mockUser));

        // when
        userAdminService.changeUserRole(userId, userRoleChangeRequest);

        // then
        assertEquals(UserRole.ADMIN, mockUser.getUserRole());

    }
}
