package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.AddPaperQuestionDTO;
import usst.spm.entity.PaperQuestions;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IPaperQuestionsService;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@RestController
@RequestMapping("/paper")
@Tag(name = "测试问题接口", description = "试卷添加对应的题目")
public class PaperQuestionsController {
    @Resource
    IPaperQuestionsService paperQuestionsService;
    @GetMapping("/{paperId}/questions/list")
    @PreAuthorize("@PaperExpression.canAccessPaper(#paperId)")
    @Operation(summary = "获取试卷的题目", description = "获取试卷的题目")
    public GeneralDataResponse<List<PaperQuestions>> getPaperQuestionsByPaperId(@PathVariable("paperId") Integer paperId) {
        return new GeneralDataResponse<>(paperQuestionsService.getPaperQuestionsByPaperId(paperId));
    }
    @PostMapping("/{paperId}/questions/add")
    @PreAuthorize("@PaperExpression.canEditPaper(#paperId)")
    @Operation(summary = "添加试卷的题目", description = "添加试卷的题目")
    public BaseResponse addPaperQuestion(@PathVariable("paperId") Integer paperId, @RequestBody AddPaperQuestionDTO addPaperQuestionDTO) {
        if (!paperQuestionsService.addPaperQuestion(paperId, addPaperQuestionDTO.getQuestionId())) {
            return BaseResponse.makeResponse(400, "添加失败");
        }
        return BaseResponse.makeResponse(200, "添加成功");
    }
    @DeleteMapping("/{paperId}/questions/delete/{questionId}")
    @PreAuthorize("@PaperExpression.canEditPaper(#paperId)")
    @Operation(summary = "删除试卷的题目", description = "删除试卷的题目")
    public BaseResponse deletePaperQuestion(@PathVariable("paperId") Integer paperId, @PathVariable("questionId") Integer questionId) {
        // do something
        if(!paperQuestionsService.deletePaperQuestion(paperId, questionId)) {
            return BaseResponse.makeResponse(400, "删除失败");
        }
        return BaseResponse.makeResponse(200, "删除成功");
    }
}
