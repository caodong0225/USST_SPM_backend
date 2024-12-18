package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.Papers;
import usst.spm.mapper.PapersMapper;
import usst.spm.service.IPapersService;

import java.util.List;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@Service
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {
    @Override
    public List<Papers> getPapersById(Integer courseId) {
        return this.lambdaQuery().eq(Papers::getCourseId, courseId).list();
    }

}
