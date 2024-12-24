package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreateQuestionDTO;
import usst.spm.dto.UpdateQuestionDTO;
import usst.spm.entity.Questions;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IQuestionsService;

import java.util.List;

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
    @Operation(summary = "创建问题", description = "创建问题")
    public BaseResponse createQuestion(@RequestBody CreateQuestionDTO createQuestionDTO) {
        Questions questions = new Questions();
        questions.setQuestionType(createQuestionDTO.getQuestionType());
        questions.setQuestionName(createQuestionDTO.getQuestion());
        questions.setQuestionLevel(createQuestionDTO.getQuestionLevel());
        questions.setQuestionOptions(createQuestionDTO.getOptions());
        questions.setCourseId(createQuestionDTO.getCourseId());
        questions.setExplanation(createQuestionDTO.getExplanation());
        questions.setAnswers(createQuestionDTO.getAnswers());
        if(!questionsService.insertQuestion(questions)){
            return BaseResponse.makeResponse(400, "创建失败");
        }
        return BaseResponse.makeResponse(200, "创建成功");
    }

    @DeleteMapping("/delete/{questionId}")
    @PreAuthorize("@QuestionsExpression.canEditQuestions(#questionId)")
    @Operation(summary = "删除问题", description = "删除问题")
    public BaseResponse deleteQuestion(@PathVariable Integer questionId) {
        if(!questionsService.deleteQuestionById(questionId)){
            return BaseResponse.makeResponse(400, "删除失败");
        }
        return BaseResponse.makeResponse(200, "删除成功");
    }

    @PutMapping("/update")
    @PreAuthorize("@QuestionsExpression.canEditQuestions(#updateQuestionDTO.id)")
    @Operation(summary = "更新问题", description = "更新问题")
    public BaseResponse updateQuestion(@RequestBody UpdateQuestionDTO updateQuestionDTO) {
        Questions questions = questionsService.getById(updateQuestionDTO.getId());
        questions.setQuestionName(updateQuestionDTO.getQuestion());
        questions.setQuestionType(updateQuestionDTO.getQuestionType());
        questions.setQuestionLevel(updateQuestionDTO.getQuestionLevel());
        questions.setQuestionOptions(updateQuestionDTO.getOptions());
        questions.setExplanation(updateQuestionDTO.getExplanation());
        questions.setAnswers(updateQuestionDTO.getAnswers());
        if(!questionsService.updateById(questions)){
            return BaseResponse.makeResponse(400, "更新失败");
        }
        return BaseResponse.makeResponse(200, "更新成功");
    }
    @GetMapping("/preview/{courseId}/list")
    @PreAuthorize("@CourseExpression.canEditCourse(#courseId)")
    @Operation(summary = "预览问题", description = "预览问题")
    public GeneralDataResponse<List<Questions>> previewQuestions(@PathVariable Integer courseId) {
        return new GeneralDataResponse<>(questionsService.getQuestionsByCourseId(courseId));
    }
    @GetMapping("/preview/paper/{paperId}")
    @PreAuthorize("@PaperExpression.canAccessPaper(#paperId)")
    @Operation(summary = "预览试卷问题", description = "预览试卷问题")
    public GeneralDataResponse<List<Questions>> previewQuestionsByPaperId(@PathVariable Integer paperId) {
        return new GeneralDataResponse<>(questionsService.getQuestionsByPaperId(paperId));
    }
}
