package pl.dolien.shop.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import pl.dolien.shop.product.Product;
import pl.dolien.shop.product.ProductRepository;
import pl.dolien.shop.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService service;

    @Mock
    private FeedbackRepository repository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FeedbackMapper mapper;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void shouldSuccessfullySaveAStudent() {
//        FeedbackRequest request = new FeedbackRequest(
//                5D,
//                "What an amazing product!",
//                2L
//        );
//
//        Feedback feedback = new Feedback(
//                1,
//                5D,
//                "What an amazing product!",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                2,
//                null
//        );
//
//        User user = new User();
//        user.setId(2);
//
//        when(mapper.toFeedback(request)).thenReturn(feedback);
//        when(repository.save(feedback)).thenReturn(feedback);
//        when(authentication.getPrincipal()).thenReturn(user);
//
//        Integer savedFeedbackId = service.save(request, authentication);
//
//        assertEquals(feedback.getId(), savedFeedbackId);
//        verify(mapper, times(1)).toFeedback(request);
//        verify(repository, times(1)).save(feedback);
//    }

    @Test
    public void shouldThrowExceptionWhenRequestIsNull() {
        Feedback feedback = new Feedback(
                1,
                5D,
                "What an amazing product!",
                Product.builder().id(4L).build(),
                LocalDateTime.now(),
                null,
                2,
                null
        );

        User user = new User();
        user.setId(1);

        when(mapper.toFeedback(null)).thenReturn(feedback);
        when(repository.save(feedback)).thenReturn(feedback);
        when(authentication.getPrincipal()).thenReturn(user);

        var exp = assertThrows(NullPointerException.class, () -> service.save(null, authentication));
        assertEquals("Feedback request should not be null", exp.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenMapperReturnsNull() {
        FeedbackRequest request = new FeedbackRequest(
                5D,
                "What an amazing product!",
                2L
        );

        Feedback feedback = new Feedback(
                1,
                5D,
                "What an amazing product!",
                Product.builder().id(4L).build(),
                LocalDateTime.now(),
                null,
                2,
                null
        );

        when(mapper.toFeedback(request)).thenReturn(null);
        when(repository.save(feedback)).thenReturn(feedback);

        User user = new User();
        user.setId(1);

        when(authentication.getPrincipal()).thenReturn(user);

        assertThrows(NullPointerException.class, () -> service.save(request, authentication));
    }

    @Test
    public void shouldThrowExceptionWhenRepositoryReturnsNull() {
        FeedbackRequest request = new FeedbackRequest(
                5D,
                "What an amazing product!",
                2L
        );

        Feedback feedback = new Feedback(
                1,
                5D,
                "What an amazing product!",
                Product.builder().id(4L).build(),
                LocalDateTime.now(),
                null,
                2,
                1
        );

        when(mapper.toFeedback(request)).thenReturn(feedback);
        when(repository.save(feedback)).thenReturn(null);

        User user = new User();
        user.setId(1);

        when(authentication.getPrincipal()).thenReturn(user);

        assertThrows(NullPointerException.class, () -> service.save(request, authentication));
    }

    @Test
    public void shouldThrowExceptionWhenAuthenticationPrincipalIsNull() {
        FeedbackRequest request = new FeedbackRequest(
                5D,
                "What an amazing product!",
                2L
        );

        Feedback feedback = new Feedback(
                1,
                5D,
                "What an amazing product!",
                Product.builder().id(4L).build(),
                LocalDateTime.now(),
                null,
                2,
                1
        );

        when(mapper.toFeedback(request)).thenReturn(feedback);
        when(repository.save(feedback)).thenReturn(feedback);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> service.save(request, authentication));
    }

//    @Test
//    public void shouldReturnAllFeedbacksByProduct() {
//        Long productId = 2L;
//        int page = 0;
//        int size = 5;
//
//        User user = new User();
//        user.setId(1);
//        when(authentication.getPrincipal()).thenReturn(user);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(Product.builder().id(productId).build()));
//
//        Feedback feedback1 = new Feedback(
//                1,
//                5D,
//                "What an amazing product!",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        Feedback feedback2 = new Feedback(
//                2,
//                3D,
//                "Nah. It's not bad but.. you know, should be better",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);
//        Page<Feedback> feedbackPage = new PageImpl<>(feedbackList, PageRequest.of(page, size), feedbackList.size());
//
//        when(repository.findAllByProductId(productId, PageRequest.of(page, size))).thenReturn(feedbackPage);
//
//        FeedbackResponse feedbackResponse1 = new FeedbackResponse(
//                feedback1.getNote(),
//                feedback1.getComment(),
//                feedback1.getCreatedDate(),
//                feedback1.getCreatedBy(),
//                Objects.equals(feedback1.getCreatedBy(), user.getId())
//        );
//
//        FeedbackResponse feedbackResponse2 = new FeedbackResponse(
//                feedback2.getNote(),
//                feedback2.getComment(),
//                feedback2.getCreatedDate(),
//                feedback2.getCreatedBy(),
//                Objects.equals(feedback2.getCreatedBy(), user.getId())
//        );
//
//        when(mapper.toFeedbackResponse(feedback1, user.getId())).thenReturn(feedbackResponse1);
//        when(mapper.toFeedbackResponse(feedback2, user.getId())).thenReturn(feedbackResponse2);
//
//        Page<FeedbackResponse> result = service.getAllFeedbacksByProduct(productId, new PageRequestParams(), authentication);
//
//        assertEquals(feedbackPage.getSize(), result.getSize());
//        verify(repository, times(1)).findAllByProductId(productId, PageRequest.of(page, size));
//        assertThat(result.getContent()).containsExactly(feedbackResponse1, feedbackResponse2);
//        assertThat(result.getNumber()).isEqualTo(page);
//        assertThat(result.getSize()).isEqualTo(size);
//        assertThat(result.getTotalElements()).isEqualTo(feedbackList.size());
//        assertThat(result.getTotalPages()).isEqualTo(1);
//        assertThat(result.isFirst()).isTrue();
//        assertThat(result.isLast()).isTrue();
//    }

//    @Test
//    public void shouldThrowExceptionWhenProductIdIs0() {
//        Long productId = 0L;
//        int page = 0;
//        int size = 5;
//
//        User user = new User();
//        user.setId(1);
//        when(authentication.getPrincipal()).thenReturn(user);
//
//        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(Product.builder().id(productId).build()));
//
//        Feedback feedback1 = new Feedback(
//                1,
//                5D,
//                "What an amazing product!",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        Feedback feedback2 = new Feedback(
//                2,
//                3D,
//                "Nah. It's not bad but.. you know, should be better",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);
//        Page<Feedback> feedbackPage = new PageImpl<>(feedbackList, PageRequest.of(page, size), feedbackList.size());
//
//        when(repository.findAllByProductId(productId, PageRequest.of(page, size))).thenReturn(feedbackPage);
//
//        FeedbackResponse feedbackResponse1 = new FeedbackResponse(
//                feedback1.getNote(),
//                feedback1.getComment(),
//                feedback1.getCreatedDate(),
//                feedback1.getCreatedBy(),
//                Objects.equals(feedback1.getCreatedBy(), user.getId())
//        );
//
//        FeedbackResponse feedbackResponse2 = new FeedbackResponse(
//                feedback2.getNote(),
//                feedback2.getComment(),
//                feedback2.getCreatedDate(),
//                feedback2.getCreatedBy(),
//                Objects.equals(feedback2.getCreatedBy(), user.getId())
//        );
//
//        when(mapper.toFeedbackResponse(feedback1, user.getId())).thenReturn(feedbackResponse1);
//        when(mapper.toFeedbackResponse(feedback2, user.getId())).thenReturn(feedbackResponse2);
//
//        var exp = assertThrows(IllegalArgumentException.class, () -> service.findAllFeedbacksByProduct(productId, page, size, authentication));
//        assertEquals("Invalid product ID", exp.getMessage());
//    }

//    @Test
//    public void shouldThrowExceptionWhenProductIsNull() {
//        Long productId = 2L;
//        int page = 0;
//        int size = 5;
//
//        User user = new User();
//        user.setId(1);
//        when(authentication.getPrincipal()).thenReturn(user);
//
//        when(productRepository.findById(productId)).thenReturn(null);
//
//        Feedback feedback1 = new Feedback(
//                1,
//                5D,
//                "What an amazing product!",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        Feedback feedback2 = new Feedback(
//                2,
//                3D,
//                "Nah. It's not bad but.. you know, should be better",
//                Product.builder().id(4L).build(),
//                LocalDateTime.now(),
//                null,
//                user.getId(),
//                1
//        );
//
//        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);
//        Page<Feedback> feedbackPage = new PageImpl<>(feedbackList, PageRequest.of(page, size), feedbackList.size());
//
//        when(repository.findAllByProductId(productId, PageRequest.of(page, size))).thenReturn(feedbackPage);
//
//        FeedbackResponse feedbackResponse1 = new FeedbackResponse(
//                feedback1.getNote(),
//                feedback1.getComment(),
//                feedback1.getCreatedDate(),
//                feedback1.getCreatedBy(),
//                Objects.equals(feedback1.getCreatedBy(), user.getId())
//        );
//
//        FeedbackResponse feedbackResponse2 = new FeedbackResponse(
//                feedback2.getNote(),
//                feedback2.getComment(),
//                feedback2.getCreatedDate(),
//                feedback2.getCreatedBy(),
//                Objects.equals(feedback2.getCreatedBy(), user.getId())
//        );
//
//        when(mapper.toFeedbackResponse(feedback1, user.getId())).thenReturn(feedbackResponse1);
//        when(mapper.toFeedbackResponse(feedback2, user.getId())).thenReturn(feedbackResponse2);
//
//        var exp = assertThrows(NullPointerException.class, () -> service.findAllFeedbacksByProduct(productId, page, size, authentication));
//        assertEquals("Cannot invoke \"java.util.Optional.isEmpty()\" because the return value of \"pl.dolien.shop.product.ProductRepository.findById(Object)\" is null", exp.getMessage());
//    }
}