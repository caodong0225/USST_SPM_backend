package usst.spm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import usst.spm.service.ICoursesService;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@RequestMapping("/course")
@Tag(name = "课程管理接口")
@Validated
@RequiredArgsConstructor
public class CoursesController {
    private ICoursesService coursesService;


}
