package pl.dolien.shop.auth.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.user.User;

import static java.time.LocalDate.parse;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RegistrationControllerTest {

        private final ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    private MockMvc mockMvc;
    private User testUser;
    private RegistrationDTO testRegistrationDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        initializeTestData();
    }

    @Test
    void shouldRegisterUser() throws Exception {
        when(registrationService.registerUser(any(RegistrationDTO.class))).thenReturn(testUser);

        performRegistrationRequest(testRegistrationDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()));

        verify(registrationService, times(1)).registerUser(any(RegistrationDTO.class));
    }

    private void initializeTestData() {
        testUser = User.builder()
                .id(1)
                .build();

        testRegistrationDTO = RegistrationDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("test@example.com")
                .dateOfBirth(parse("2020-01-01"))
                .password("password123")
                .build();
    }

    private ResultActions performRegistrationRequest(RegistrationDTO registrationDTO) throws Exception {
        return mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO)));
    }
}