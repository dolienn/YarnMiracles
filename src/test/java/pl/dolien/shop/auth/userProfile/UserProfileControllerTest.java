package pl.dolien.shop.auth.userProfile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import pl.dolien.shop.auth.registration.dto.RegistrationDTO;
import pl.dolien.shop.user.User;

import static java.time.LocalDate.parse;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserProfileControllerTest {

    @InjectMocks
    private UserProfileController userProfileController;

    @Mock
    private UserProfileUpdater userProfileUpdater;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private RegistrationDTO testRegistrationDTO;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
        initializeTestData();
    }

    @Test
    void shouldUpdateUserProfile() throws Exception {
        when(userProfileUpdater.updateUserProfile(any(RegistrationDTO.class), any(Authentication.class)))
                .thenReturn(testUser);

        performUserProfileUpdateRequest(testRegistrationDTO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testUser.getId()))
                .andExpect(jsonPath("$.firstname").value(testUser.getFirstname()))
                .andExpect(jsonPath("$.lastname").value(testUser.getLastname()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));

        verify(userProfileUpdater, times(1)).updateUserProfile(any(RegistrationDTO.class), any(Authentication.class));
    }

    private void initializeTestData() {
        testUser = User.builder()
                .id(1)
                .email("test@example.com")
                .firstname("John")
                .lastname("Doe")
                .password("password123")
                .dateOfBirth(parse("2020-01-01"))
                .build();

        testRegistrationDTO = RegistrationDTO.builder()
                .firstname("NewJohn")
                .lastname("NewDoe")
                .email("test@example.com")
                .password("newPassword123")
                .dateOfBirth(parse("2020-01-01"))
                .build();
    }

    private ResultActions performUserProfileUpdateRequest(RegistrationDTO registrationDTO) throws Exception {
        return mockMvc.perform(post("/auth/update-user-profile")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDTO))
                .principal(authentication));
    }
}