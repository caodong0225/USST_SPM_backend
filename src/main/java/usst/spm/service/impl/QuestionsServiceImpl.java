package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import usst.spm.entity.PaperQuestions;
import usst.spm.entity.Questions;
import usst.spm.mapper.QuestionsMapper;
import usst.spm.service.IPaperQuestionsService;
import usst.spm.service.IQuestionsService;

import java.util.ArrayList;
import java.util.List;

/**
* @author jyzxc
* @description 针对表【questions(问题列表)】的数据库操作Service实现
* @createDate 2024-11-25 23:31:40
*/
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions>
    implements IQuestionsService {
    @Resource
    IPaperQuestionsService paperQuestionsService;
    @Override
    public boolean insertQuestion(Questions questions) {
        return this.save(questions);
    }

    @Override
    public boolean deleteQuestionById(Integer questionId) {
        return this.baseMapper.deleteById(questionId) > 0;
    }

    @Override
    public List<Questions> getQuestionsByCourseId(Integer courseId) {
        return this.lambdaQuery().eq(Questions::getCourseId, courseId).list();
    }

    @Override
    public List<Questions> getQuestionsByPaperId(Integer paperId) {
        List<PaperQuestions> paperQuestions = paperQuestionsService.getPaperQuestionsByPaperId(paperId);
        List<Questions> questions = new ArrayList<>();
        for (PaperQuestions paperQuestion : paperQuestions) {
            Questions question = this.getById(paperQuestion.getQuestionId());
            // 屏蔽答案和解析等关键信息
            question.setAnswers(null);
            question.setExplanation(null);
            questions.add(question);
        }
        return questions;
    }
}




