package pl.dolien.shop.summaryMetrics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface SummaryMetricsRepository extends JpaRepository<SummaryMetrics, Long> {
}
