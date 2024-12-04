package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.UserExts;
import usst.spm.mapper.UserExtsMapper;
import usst.spm.service.IUserExtsService;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Service
public class UserExtsServiceImpl extends ServiceImpl<UserExtsMapper, UserExts> implements IUserExtsService {

    @Override
    public String getPicture(Integer userId) {
        UserExts userExts = this.lambdaQuery()
                .eq(UserExts::getUserId, userId)
                .eq(UserExts::getKey, "pic")
                .one();
        if (userExts == null) {
            return null;
        }
        return userExts.getValue();
    }
}