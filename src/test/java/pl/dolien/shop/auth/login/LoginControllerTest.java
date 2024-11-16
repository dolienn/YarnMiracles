package pl.dolien.shop.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.auth.login.dto.LoginRequestDTO;
import pl.dolien.shop.auth.login.dto.LoginResponseDTO;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private LoginRequestDTO testLoginRequestDTO;
    private LoginResponseDTO testLoginResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        initializeTestData();
    }

    @Test
    void shouldLogin() throws Exception {
        when(loginService.login(any(LoginRequestDTO.class))).thenReturn(testLoginResponseDTO);

        performLoginRequest(testLoginRequestDTO)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testLoginResponseDTO)));

        verify(loginService, times(1)).login(any(LoginRequestDTO.class));
    }

    private void initializeTestData() {
        testLoginRequestDTO = LoginRequestDTO.builder(  )
                .email("test@example.com")
                .password("password123")
                .build();

        testLoginResponseDTO = LoginResponseDTO.builder()
                .token("test-jwt-token")
                .build();
    }

    private ResultActions performLoginRequest(LoginRequestDTO loginRequest) throws Exception {
        return mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)));
    }
}