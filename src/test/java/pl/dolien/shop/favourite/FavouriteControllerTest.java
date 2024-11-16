package pl.dolien.shop.favourite;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.product.dto.ProductDTO;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FavouriteControllerTest {

    private static final Integer TEST_USER_ID = 1;

    @InjectMocks
    private FavouriteController favouriteController;

    @Mock
    private FavouriteService favouriteService;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ProductDTO testProductDTO;
    private PaginationAndSortParams testPaginationAndSortParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(favouriteController).build();

        initializeTestData();
    }

    @Test
    void shouldGetFavouritesByUserId() throws Exception {
        when(favouriteService.getFavourites(anyInt(), any(PaginationAndSortParams.class), any(Authentication.class)))
                .thenReturn(List.of(testProductDTO));

        mockMvc.perform(buildGetFavouritesRequest())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(testProductDTO))));

        verify(favouriteService, times(1))
                .getFavourites(anyInt(), any(PaginationAndSortParams.class), any(Authentication.class));
    }

    @Test
    void shouldAddProductToFavourites() throws Exception {
        mockMvc.perform(buildAddToFavouritesRequest())
                .andExpect(status().isOk());

        verify(favouriteService, times(1)).addToFavourites(anyInt(), anyLong(), any(Authentication.class));
    }

    @Test
    void shouldRemoveProductFromFavourites() throws Exception {
        mockMvc.perform(buildRemoveFromFavouritesRequest())
                .andExpect(status().isOk());

        verify(favouriteService, times(1)).removeFromFavourites(anyInt(), anyLong(), any(Authentication.class));
    }

    private void initializeTestData() {
        testProductDTO = ProductDTO.builder()
                .id(1L)
                .name("Test product")
                .build();

        testPaginationAndSortParams = PaginationAndSortParams.builder()
                .page(0)
                .size(10)
                .sortOrderType("PRICE_ASC")
                .build();
    }

    private MockHttpServletRequestBuilder buildGetFavouritesRequest() throws Exception {
        return get("/users/{userId}/favourites", TEST_USER_ID)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPaginationAndSortParams))
                .principal(authentication);
    }

    private MockHttpServletRequestBuilder buildAddToFavouritesRequest() throws Exception {
        return get("/users/{userId}/favourites/{productId}", TEST_USER_ID, testProductDTO.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProductDTO))
                .principal(authentication);
    }

    private MockHttpServletRequestBuilder buildRemoveFromFavouritesRequest() {
        return delete("/users/{userId}/favourites/{productId}", TEST_USER_ID, testProductDTO.getId())
                .contentType(APPLICATION_JSON)
                .principal(authentication);
    }
}