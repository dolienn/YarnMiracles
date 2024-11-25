package pl.dolien.shop.auth.password;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.auth.password.dto.PasswordRequestDTO;
import pl.dolien.shop.user.User;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class PasswordControllerTest {

    @InjectMocks
    private PasswordController passwordController;

    @Mock
    private PasswordChanger passwordChanger;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private PasswordRequestDTO testPasswordRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passwordController).build();
        initializeTestData();
    }

    @Test
    void shouldChangePassword() throws Exception {
        when(passwordChanger.changePassword(any(PasswordRequestDTO.class), any(Authentication.class)))
                .thenReturn(testUser);

        performChangePasswordRequest()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()));

        verify(passwordChanger, times(1)).changePassword(any(PasswordRequestDTO.class), any(Authentication.class));
    }

    private void initializeTestData() {
        testUser = User.builder()
                .id(1)
                .build();

        testPasswordRequestDTO = PasswordRequestDTO.builder()
                .currentPassword("password123")
                .newPassword("newPassword123")
                .build();
    }

    private ResultActions performChangePasswordRequest() throws Exception {
        return mockMvc.perform(post("/auth/change-password")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPasswordRequestDTO))
                .principal(authentication));
    }
}