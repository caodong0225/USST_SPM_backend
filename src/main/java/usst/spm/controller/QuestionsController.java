package usst.spm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreateQuestionDTO;
import usst.spm.dto.UpdateQuestionDTO;
import usst.spm.entity.Questions;
import usst.spm.result.BaseResponse;
import usst.spm.service.IQuestionsService;

/**
 * @author jyzxc
 * @since 2024-12-10
 */
@RestController
@RequestMapping("/question")
@Tag(name = "问题接口", description = "问题相关接口")
public class QuestionsController {
    @Resource
    private IQuestionsService questionsService;
    @PostMapping("/create")
    @PreAuthorize("@CourseExpression.canAccessCourse(#createQuestionDTO.courseId)")
    public BaseResponse createQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        Questions questions = new Questions();
        questions.setQuestionType(createQuestionDTO.getQuestionType());
        questions.setQuestionName(createQuestionDTO.getQuestion());
        questions.setQuestionLevel(createQuestionDTO.getQuestionLevel());
        questions.setQuestionOptions(createQuestionDTO.getOptions());
        questions.setCourseId(createQuestionDTO.getCourseId());
        if(!questionsService.insertQuestion(questions)){
            return BaseResponse.makeResponse(400, "创建失败");
        }
        return BaseResponse.makeResponse(200, "创建成功");
    }

    @DeleteMapping("/delete/{questionId}")
    @PreAuthorize("@QuestionsExpression.canEditQuestions(#questionId)")
    public BaseResponse deleteQuestion(@PathVariable Integer questionId) {
        if(!questionsService.deleteQuestionById(questionId)){
            return BaseResponse.makeResponse(400, "删除失败");
        }
        return BaseResponse.makeResponse(200, "删除成功");
    }

    @PutMapping("/update")
    @PreAuthorize("@QuestionsExpression.canEditQuestions(#updateQuestionDTO.id)")
    public BaseResponse updateQuestion(@RequestBody UpdateQuestionDTO updateQuestionDTO) {
        Questions questions = questionsService.getById(updateQuestionDTO.getId());
        questions.setQuestionName(updateQuestionDTO.getQuestion());
        questions.setQuestionType(updateQuestionDTO.getQuestionType());
        questions.setQuestionLevel(updateQuestionDTO.getQuestionLevel());
        questions.setQuestionOptions(updateQuestionDTO.getOptions());
        if(!questionsService.updateById(questions)){
            return BaseResponse.makeResponse(400, "更新失败");
        }
        return BaseResponse.makeResponse(200, "更新成功");
    }
}
