package pl.dolien.shop.auth.activation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ActivationControllerTest {

    private static final String TEST_TOKEN = "123456";

    @InjectMocks
    private ActivationController activationController;

    @Mock
    private ActivationService activationService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(activationController).build();
    }

    @Test
    void shouldActivateUserByToken() throws Exception {
        performActivationRequest()
                .andExpect(status().isOk());

        verify(activationService, times(1)).activateUser(TEST_TOKEN);
    }

    private ResultActions performActivationRequest() throws Exception {
        return mockMvc.perform(get("/auth/activate")
                .contentType(APPLICATION_JSON)
                .param("token", TEST_TOKEN));
    }
}