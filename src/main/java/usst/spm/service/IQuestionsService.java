package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Questions;

import java.util.List;

/**
* @author jyzxc
* @description 针对表【questions(问题列表)】的数据库操作Service
* @createDate 2024-11-25 23:31:40
*/
public interface IQuestionsService extends IService<Questions> {
    boolean insertQuestion(Questions questions);
    boolean deleteQuestionById(Integer questionId);
    List<Questions> getQuestionsByCourseId(Integer courseId);
    List<Questions> getQuestionsByPaperId(Integer paperId);
}
