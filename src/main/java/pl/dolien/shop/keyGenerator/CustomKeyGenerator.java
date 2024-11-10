package pl.dolien.shop.keyGenerator;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("customKeyGenerator")
@RequiredArgsConstructor
public class CustomKeyGenerator implements KeyGenerator {

    private final KeyFragmentGeneratorFactory generatorFactory;

    @Override
    @Nonnull
    public Object generate(@Nonnull Object target,
                           @Nonnull Method method,
                           @Nonnull Object... params) {
        StringBuilder keyBuilder = buildBaseKey(method);

        for (Object param : params) {
            appendKeyFragment(keyBuilder, param);
        }

        return keyBuilder.toString();
    }

    private StringBuilder buildBaseKey(Method method) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(method.getName());
        return keyBuilder;
    }

    private void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        KeyFragmentGenerator generator = generatorFactory.getGenerator(param);
        generator.appendKeyFragment(keyBuilder, param);
    }
}

