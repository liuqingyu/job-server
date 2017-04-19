package cn.com.strongunion.batch.common;

import cn.com.strongunion.batch.common.utils.TaskUtils;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.ApplicationContext;


/**
 * 任务工厂类,非同步
 *
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class NonConcurrentJob extends AbstractSchedulerJob {

    @Override
    public void exec(QrtzScheduleJob scheduleJob, ApplicationContext applicationContext) {
        TaskUtils.invokMethod(scheduleJob,applicationContext);
    }
}
