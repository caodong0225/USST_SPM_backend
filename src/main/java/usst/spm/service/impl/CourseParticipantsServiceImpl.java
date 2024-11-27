package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.CourseParticipants;
import usst.spm.mapper.CourseParticipantsMapper;
import usst.spm.service.ICourseParticipantsService;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Service
public class CourseParticipantsServiceImpl extends ServiceImpl<CourseParticipantsMapper, CourseParticipants> implements ICourseParticipantsService {
}
