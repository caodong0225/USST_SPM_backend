package usst.spm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@Getter
@Setter
@Schema(name = "UpdateExtraRequestDTO", description = "更新拓展信息的请求")
public class UpdateExtraRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "value不能为空")
    @Schema(name = "value", description = "拓展信息的值")
    private String value;
}
