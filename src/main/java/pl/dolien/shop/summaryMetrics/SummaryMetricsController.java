package pl.dolien.shop.summaryMetrics;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/summary-metrics")
@RequiredArgsConstructor
@Tag(name = "Summary-Metrics")
@Validated
public class SummaryMetricsController {

    private final SummaryMetricsService summaryMetricsService;

    @GetMapping
    public SummaryMetrics getSummaryMetrics() {
        return summaryMetricsService.getSummaryMetrics();
    }
}
