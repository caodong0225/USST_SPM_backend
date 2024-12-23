package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.PaperQuestions;
import usst.spm.mapper.PaperQuestionsMapper;
import usst.spm.service.IPaperQuestionsService;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-23
 */
@Service
public class PaperQuestionsServiceImpl extends ServiceImpl<PaperQuestionsMapper, PaperQuestions> implements IPaperQuestionsService {

    @Override
    public List<PaperQuestions> getPaperQuestionsByPaperId(Integer paperId) {
        return this.lambdaQuery().eq(PaperQuestions::getPaperId, paperId).list();
    }

    @Override
    public Long getQuestionsNumByPaperId(Integer paperId) {
        return this.lambdaQuery().eq(PaperQuestions::getPaperId, paperId).count();
    }

    @Override
    public boolean addPaperQuestion(Integer paperId, Integer questionId) {
        PaperQuestions paperQuestions = new PaperQuestions();
        paperQuestions.setPaperId(paperId);
        paperQuestions.setQuestionId(questionId);
        return this.save(paperQuestions);
    }

    @Override
    public boolean deletePaperQuestion(Integer paperId, Integer questionId) {
        PaperQuestions paperQuestions = this.lambdaQuery().eq(PaperQuestions::getPaperId, paperId).eq(PaperQuestions::getQuestionId, questionId).one();
        if (paperQuestions == null) {
            return false;
        }
        return this.removeById(paperQuestions.getId());
    }
}
