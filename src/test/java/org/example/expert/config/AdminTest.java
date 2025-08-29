package org.example.expert.config;

import org.example.expert.domain.comment.controller.CommentAdminController;
import org.example.expert.domain.comment.service.CommentAdminService;
import org.example.expert.domain.user.controller.UserAdminController;
import org.example.expert.domain.user.service.UserAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminTest {

    private MockMvc mockMvc;
    private AdminInterceptor adminInterceptor;
    private CommentAdminService commentAdminService;
    private UserAdminService userAdminService;

    // 기본 세팅
    @BeforeEach
    void setUp() {
        adminInterceptor = new AdminInterceptor();

        commentAdminService = Mockito.mock(CommentAdminService.class);
        userAdminService = Mockito.mock(UserAdminService.class);

        CommentAdminController commentController = new CommentAdminController(commentAdminService);
        UserAdminController userController = new UserAdminController(userAdminService);

        mockMvc = MockMvcBuilders.standaloneSetup(commentController, userController)
                .addInterceptors(adminInterceptor)
                .build();
    }

    // deleteComment 관련 테스트
    @Test
    void deleteComment_admin_권한이_없으면_접근_불가능() throws Exception {
        mockMvc.perform(delete("/admin/comments/1")
                        .requestAttr("userId", 123L)
                        .requestAttr("userRole", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComment_admin_권한이_있으면_접근_가능() throws Exception {
        mockMvc.perform(delete("/admin/comments/1")
                        .requestAttr("userId", 1L)
                        .requestAttr("userRole", "ADMIN"))
                .andExpect(status().isOk());

        Mockito.verify(commentAdminService).deleteComment(1L);
    }


    //
    @Test
    void changeUserRole_admin_권한이_없으면_접근_불가능() throws Exception {
        mockMvc.perform(patch("/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userRole\":\"ADMIN\"}")
                        .requestAttr("userId", 123L)
                        .requestAttr("userRole", "USER"))
                .andExpect(status().isForbidden());
    }

    @Test
    void changeUserRole_admin_권한_있으면_접근_가능() throws Exception {
        mockMvc.perform(patch("/admin/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userRole\":\"USER\"}")
                        .requestAttr("userId", 1L)
                        .requestAttr("userRole", "ADMIN"))
                .andExpect(status().isOk());

        Mockito.verify(userAdminService).changeUserRole(Mockito.eq(1L), Mockito.any());
    }
}
