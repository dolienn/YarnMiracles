package pl.dolien.shop.keyGenerator;

import org.springframework.stereotype.Component;

@Component
public class DefaultKeyFragmentGenerator implements KeyFragmentGenerator {

    @Override
    public void appendKeyFragment(StringBuilder keyBuilder, Object param) {
        keyBuilder.append("_").append(param.toString());
    }
}

