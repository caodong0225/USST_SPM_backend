package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Submit;

import java.util.List;

/**
 * @author jyzxc
 * @since 02==2024-12-24
 */
public interface ISubmitService extends IService<Submit> {
    List<Submit> getSubmitListByUserId(Integer userId, Integer paperId);
}
