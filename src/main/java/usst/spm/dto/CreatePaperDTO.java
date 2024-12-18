package usst.spm.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@Setter
@Getter
public class CreatePaperDTO {
    private String paperName;
    private String paperDescription;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer courseId;
}
