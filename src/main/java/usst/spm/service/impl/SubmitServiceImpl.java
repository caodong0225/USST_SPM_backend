package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.Submit;
import usst.spm.mapper.SubmitMapper;
import usst.spm.service.ISubmitService;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-12-24
 */
@Service
public class SubmitServiceImpl extends ServiceImpl<SubmitMapper, Submit> implements ISubmitService {
    @Override
    public List<Submit> getSubmitListByUserId(Integer userId, Integer paperId) {
        return this.lambdaQuery().eq(Submit::getUserId, userId).eq(Submit::getPaperId, paperId).list();
    }
}
