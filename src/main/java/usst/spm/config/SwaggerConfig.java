package usst.spm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Configuration for SPM Application
 * Provides API documentation for public and private endpoints.
 *
 * @author jyzxc
 * @since 2024-10-26
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customSwagger() {
        return new OpenAPI()
                .info(new Info()
                        .title("SPM API Documentation")
                        .version("1.0.0")
                        .description("API documentation for spm."));

    }
}
