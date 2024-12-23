package usst.spm.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jyzxc
 */
@Getter
@Setter
public class PaperQuestions implements Serializable {
    private Integer id;
    private Integer paperId;
    private Integer questionId;
}
