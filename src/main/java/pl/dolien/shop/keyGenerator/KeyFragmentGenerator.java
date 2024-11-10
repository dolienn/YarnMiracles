package pl.dolien.shop.keyGenerator;

public interface KeyFragmentGenerator {
    void appendKeyFragment(StringBuilder keyBuilder, Object param);
}
