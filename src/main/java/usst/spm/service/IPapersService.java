package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Papers;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
public interface IPapersService extends IService<Papers> {
    boolean changePaperStatus(Integer paperId, String status);
}
