package usst.spm.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Component
public class PaperStartJob  implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }
}
