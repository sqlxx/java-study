package ind.sq.study.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sun Qin
 * @since 2020/10/14
 */
public class QuartzJob1 implements Job {
    private static final Logger logger = LoggerFactory.getLogger(QuartzJob1.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("{} is executing, current: {}, prev: {}, next: {}",  context.getJobInstance(),
            context.getScheduledFireTime(), context.getPreviousFireTime(), context.getNextFireTime());
    }
}
