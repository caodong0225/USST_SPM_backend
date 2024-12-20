package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.Questions;
import usst.spm.mapper.QuestionsMapper;
import usst.spm.service.IQuestionsService;

/**
* @author jyzxc
* @description 针对表【questions(问题列表)】的数据库操作Service实现
* @createDate 2024-11-25 23:31:40
*/
@Service
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions>
    implements IQuestionsService {

    @Override
    public boolean insertQuestion(Questions questions) {
        return this.save(questions);
    }

    @Override
    public boolean deleteQuestionById(Integer questionId) {
        return this.baseMapper.deleteById(questionId) > 0;
    }
}




