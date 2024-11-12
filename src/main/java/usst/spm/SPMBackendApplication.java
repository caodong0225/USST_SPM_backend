package usst.spm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author jyzxc
 * @since 2024-10-26
 */
@SpringBootApplication
@MapperScan("usst.spm.mapper")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
@EnableCaching
public class SPMBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SPMBackendApplication.class, args);
    }

}
