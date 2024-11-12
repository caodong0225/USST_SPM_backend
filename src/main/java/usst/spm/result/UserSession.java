package usst.spm.result;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Getter
@Setter
public class UserSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private String sessionId;
    private String loginIp;
    private String loginUserAgent;
    private Long loginTime;
}