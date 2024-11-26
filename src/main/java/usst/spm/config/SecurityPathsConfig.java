package usst.spm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "security.paths")
public class SecurityPathsConfig {
    private List<String> publicRoutes;
    private List<String> authenticated;
}
