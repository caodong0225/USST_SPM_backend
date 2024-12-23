package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreatePaperDTO;
import usst.spm.entity.Papers;
import usst.spm.entity.UserLogin;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IPaperQuestionsService;
import usst.spm.service.IPapersService;
import usst.spm.vo.PapersVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@RestController
@RequestMapping("/paper")
@Tag(name = "试卷问题接口", description = "创建试卷的接口")
public class PapersController {
    @Resource
    IPapersService papersService;
    @Resource
    IPaperQuestionsService paperQuestionsService;

    @PostMapping("/create")
    @Operation(summary = "创建试卷", description = "创建试卷")
    @PreAuthorize("@AuthExpression.isTeacher()")
    public BaseResponse createPaper(@RequestBody CreatePaperDTO createPaperDTO) {
        // do something
        if (createPaperDTO.getStartTime() == null || createPaperDTO.getEndTime() == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为空");
        }
        // 如果开始时间在结束时间之后
        if (createPaperDTO.getStartTime().isAfter(createPaperDTO.getEndTime())) {
            throw new IllegalArgumentException("开始时间不能在结束时间之后");
        }
        Papers papers = new Papers();
        papers.setPaperName(createPaperDTO.getPaperName());
        papers.setPaperDesc(createPaperDTO.getPaperDescription());
        papers.setPaperStartTime(createPaperDTO.getStartTime());
        papers.setPaperEndTime(createPaperDTO.getEndTime());
        LocalDateTime now = LocalDateTime.now();
        papers.setStatus(now.isBefore(createPaperDTO.getStartTime()) ? "upcoming" : now.isAfter(createPaperDTO.getEndTime()) ? "ended" : "ongoing");
        papers.setCourseId(createPaperDTO.getCourseId());
        if (!papersService.save(papers)) {
            return BaseResponse.makeResponse(400, "创建失败");
        }
        return BaseResponse.makeResponse(200, "创建成功");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除试卷", description = "删除试卷")
    @PreAuthorize("@PaperExpression.canEditPaper(#id)")
    public BaseResponse deletePaper(@PathVariable("id") Long id) {
        // do something
        if (!papersService.removeById(id)) {
            return BaseResponse.makeResponse(400, "删除失败");
        }
        return BaseResponse.makeResponse(200, "删除成功");
    }

    @GetMapping("/{id}/list")
    @Operation(summary = "获取试卷列表", description = "获取试卷列表")
    @PreAuthorize("@CourseExpression.canAccessCourse(#id)")
    public GeneralDataResponse<List<PapersVO>> listPaper(@PathVariable("id") Integer id) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // do something
        List<Papers> papers = papersService.getPapersById(id);
        if(!user.isAdmin()){
            papers.removeIf(paper -> !paper.getVisible());
        }
        List<PapersVO> papersVOList = new ArrayList<>();
        for (Papers paper : papers) {
            PapersVO papersVO = new PapersVO();
            papersVO.setPapers(paper);
            papersVO.setQuestionsNum(paperQuestionsService.getQuestionsNumByPaperId(paper.getId()));
            papersVOList.add(papersVO);
        }
        return new GeneralDataResponse<>(200, "获取成功", papersVOList);
    }
}
