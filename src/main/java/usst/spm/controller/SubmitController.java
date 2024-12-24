package usst.spm.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreateSubmitDTO;
import usst.spm.entity.*;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IPaperQuestionsService;
import usst.spm.service.IQuestionsService;
import usst.spm.service.ISubmitService;
import usst.spm.vo.PaperSubmitResultVO;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-24
 */
@RequestMapping("/submit")
@Tag(name = "试卷提交")
@RestController
public class SubmitController {
    @Resource
    IQuestionsService questionsService;
    @Resource
    IPaperQuestionsService paperQuestionsService;
    @Resource
    ISubmitService submitService;
    @PostMapping("/{paperId}")
    @PreAuthorize("@PaperExpression.canAccessPaper(#paperId)")
    @Operation(summary = "提交试卷答案")
    public BaseResponse submitPaper(@PathVariable Integer paperId,
                                    @RequestBody List<CreateSubmitDTO> createSubmitDTOList) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<PaperQuestions> paperQuestions = paperQuestionsService.getPaperQuestionsByPaperId(paperId);
        PaperSubmitResultVO paperSubmitResultVO = new PaperSubmitResultVO();
        paperSubmitResultVO.setWrongNum(0);
        paperSubmitResultVO.setCorrectNum(0);
        for(CreateSubmitDTO createSubmitDTO : createSubmitDTOList) {
            if(paperQuestions.stream().noneMatch(paperQuestion -> paperQuestion.getQuestionId().equals(createSubmitDTO.getQuestionId()))) {
                throw new IllegalArgumentException("试卷中不存在题目ID：" + createSubmitDTO.getQuestionId());
            }
            Questions question = questionsService.getById(createSubmitDTO.getQuestionId());
            if(question == null) {
                throw new IllegalArgumentException("题目ID：" + createSubmitDTO.getQuestionId() + "不存在");
            }
            if(question.getAnswers().equals(createSubmitDTO.getAnswer())) {
                paperSubmitResultVO.setCorrectNum(paperSubmitResultVO.getCorrectNum() + 1);
            }else{
                paperSubmitResultVO.setWrongNum(paperSubmitResultVO.getWrongNum() + 1);
            }
        }
        Submit submit = new Submit();
        submit.setPaperId(paperId);
        submit.setUserId((Integer) user.getUserId());
        // 使用 Jackson 将 createSubmitDTOList 序列化为 JSON 字符串
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String serializedContent = objectMapper.writeValueAsString(createSubmitDTOList);
            submit.setContent(serializedContent);
        } catch (JsonProcessingException e) {
            return new BaseResponse(500, "序列化提交内容失败");
        }
        try {
            String serializedResult = objectMapper.writeValueAsString(paperSubmitResultVO);
            submit.setResult(serializedResult);
        } catch (JsonProcessingException e) {
            return new BaseResponse(500, "序列化提交结果失败");
        }
        if(!submitService.save(submit)){
            return new BaseResponse(500, "提交失败");
        }
        return new BaseResponse(200, "提交成功");
    }
    @GetMapping("/{paperId}/list")
    @Operation(summary = "获取提交列表记录")
    public GeneralDataResponse<List<Submit>> getSubmitList(@PathVariable Integer paperId) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Submit> submitList = submitService.getSubmitListByUserId((Integer) user.getUserId(),paperId);
        return new GeneralDataResponse<>(200, "获取成功", submitList);
    }
}
