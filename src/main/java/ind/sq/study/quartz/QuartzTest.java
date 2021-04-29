package ind.sq.study.quartz;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sun Qin
 * @since 2020/10/14
 */
public class QuartzTest {

    private static final Logger logger = LoggerFactory.getLogger(QuartzTest.class);

    public static void main(String[] args) throws SchedulerException {
        var scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        logger.info("Quartz scheduler started. ");


    }
}
