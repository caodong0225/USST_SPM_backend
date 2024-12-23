package usst.spm.vo;

import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.PaperQuestions;
import usst.spm.entity.Papers;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-23
 */
@Getter
@Setter
public class PapersVO {
    Papers papers;
    List<PaperQuestions> paperQuestions;
    Integer questionsNum;
}
