package usst.spm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jyzxc
 * @since 2024-10-26
 */
@SpringBootApplication
@MapperScan("usst.spm.mapper")
public class SPMBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SPMBackendApplication.class, args);
    }

}
