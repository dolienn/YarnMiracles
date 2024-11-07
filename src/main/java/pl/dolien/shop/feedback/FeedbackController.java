package pl.dolien.shop.feedback;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.dolien.shop.feedback.dto.FeedbackDTO;
import pl.dolien.shop.feedback.dto.FeedbackRequestDTO;
import pl.dolien.shop.feedback.dto.FeedbackResponseDTO;
import pl.dolien.shop.pagination.PageRequestParams;

import java.util.List;

import static pl.dolien.shop.feedback.FeedbackMapper.toFeedbackDTO;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback")
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public FeedbackDTO saveFeedback(
            @Valid @RequestBody FeedbackRequestDTO request,
            Authentication connectedUser
    ) {
        return toFeedbackDTO(service.saveFeedback(request, connectedUser));
    }

    @GetMapping("/products/{productId}")
    public List<FeedbackResponseDTO> getAllFeedbacksByProduct(
            @PathVariable Long productId,
            @ModelAttribute PageRequestParams pageRequestParams,
            Authentication connectedUser
    ) {
        return service.getFeedbacksByProduct(productId, pageRequestParams, connectedUser);
    }
}
