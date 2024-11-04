package pl.dolien.shop.feedback;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.pagination.PageRequestParams;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public Integer saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ) {
        return service.save(request, connectedUser);
    }

    @GetMapping("/products/{productId}")
    public Page<FeedbackResponse> getAllFeedbacksByProduct(
            @PathVariable Long productId,
            @ModelAttribute PageRequestParams pageRequestParams,
            Authentication connectedUser
    ) {
        return service.getAllFeedbacksByProduct(productId, pageRequestParams, connectedUser);
    }
}
