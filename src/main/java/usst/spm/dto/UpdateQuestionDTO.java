package usst.spm.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@Getter
@Setter
public class UpdateQuestionDTO {
    private Integer id;
    private String questionType;
    private String question;
    private String questionLevel;
    private String options;
    private Integer courseId;
}
