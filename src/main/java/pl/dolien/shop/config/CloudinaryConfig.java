package pl.dolien.shop.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dn4yf36q2");
        config.put("api_key", "175833256642875");
        config.put("api_secret", "cv9XZ_7mBmo0DMZnnozXSHeVpSc");
        return new Cloudinary(config);
    }
}
