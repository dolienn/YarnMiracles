package pl.dolien.shop.keyGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dolien.shop.exception.GeneratorNotFoundException;
import pl.dolien.shop.pagination.PaginationAndSortParams;
import pl.dolien.shop.pagination.PaginationParams;

import java.util.List;

@Component
@RequiredArgsConstructor
public class KeyFragmentGeneratorFactory {

    private final List<KeyFragmentGenerator> generators;

    public KeyFragmentGenerator getGenerator(Object param) {
        return generators.stream()
                .filter(generator -> isMatchingGenerator(generator, param))
                .findFirst()
                .orElseGet(this::getDefaultGenerator);
    }

    private boolean isMatchingGenerator(KeyFragmentGenerator generator, Object param) {
        return (generator instanceof PaginationAndSortParamsKeyFragmentGenerator && param instanceof PaginationAndSortParams) ||
                (generator instanceof PaginationParamsKeyFragmentGenerator && param instanceof PaginationParams) ||
                (generator instanceof AuthenticationKeyFragmentGenerator && param instanceof Authentication);
    }

    private KeyFragmentGenerator getDefaultGenerator() {
        return generators.stream()
                .filter(generator -> generator instanceof DefaultKeyFragmentGenerator)
                .findFirst()
                .orElseThrow(() -> new GeneratorNotFoundException("No suitable generator found"));
    }
}
