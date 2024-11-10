package pl.dolien.shop.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

@Component
public class ObjectMapperConfig {

    public ObjectMapper configureObjectMapper(boolean isFeedback) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();

        if (isFeedback) {
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        } else {
            objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        }

        return objectMapper;
    }
}
