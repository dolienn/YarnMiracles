package pl.dolien.shop.feedback;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.common.PageResponse;

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

    @GetMapping("/product/{product-id}")
    public PageResponse<FeedbackResponse> findAllFeedbacksByProduct(
            @PathVariable("product-id") Long productId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "5", required = false) int size,
            Authentication connectedUser
    ) {
        return service.findAllFeedbacksByProduct(productId, page, size, connectedUser);
    }
}
