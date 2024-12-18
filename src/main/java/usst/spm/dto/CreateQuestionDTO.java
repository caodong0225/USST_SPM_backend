package usst.spm.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * @author jyzxc
 * @since 2024-12-18
 */
@Getter
@Setter
public class CreateQuestionDTO {
    private Integer courseId;
    private String questionType;
    private String question;
    private String questionLevel;
    private String options;
}
