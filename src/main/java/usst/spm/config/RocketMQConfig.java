package usst.spm.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Configuration
public class RocketMQConfig {

    @Bean
    public RocketMQTemplate rocketTemplate() {
        return new RocketMQTemplate();
    }
}

