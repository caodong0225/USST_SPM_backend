package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.PaperQuestions;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-23
 */
public interface IPaperQuestionsService extends IService<PaperQuestions> {
    List<PaperQuestions> getPaperQuestionsByPaperId(Integer paperId);
    Long getQuestionsNumByPaperId(Integer paperId);
    boolean addPaperQuestion(Integer paperId, Integer questionId);
    boolean deletePaperQuestion(Integer paperId, Integer questionId);
}
