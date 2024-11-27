package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import usst.spm.entity.Papers;
import usst.spm.mapper.PapersMapper;
import usst.spm.service.IPapersService;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
public class PapersServiceImpl extends ServiceImpl<PapersMapper, Papers> implements IPapersService {
    @Override
    public boolean changePaperStatus(Integer paperId, String status) {
        Papers paper = getById(paperId);
        if (paper == null) {
            return false;
        }
        paper.setStatus(status);
        return updateById(paper);
    }
}
