package usst.spm.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jyzxc
 * @since 2024-12-24
 */
@Getter
@Setter
public class Submit implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer paperId;
    private String content;
    private String result;
}
