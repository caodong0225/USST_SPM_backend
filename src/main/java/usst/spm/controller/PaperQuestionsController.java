package usst.spm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@RestController
@RequestMapping("/paper")
@Tag(name = "试卷问题接口", description = "试卷添加对应的题目")
public class PaperQuestionsController {

}
