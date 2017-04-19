package cn.com.strongunion.batch.common;

import cn.com.strongunion.batch.common.utils.TaskUtils;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import org.springframework.context.ApplicationContext;

/**
 * 同步的任务工厂类
 *
 */
public class ConcurrentJob extends AbstractSchedulerJob {

    @Override
    public void exec(QrtzScheduleJob scheduleJob, ApplicationContext applicationContext){
        TaskUtils.invokMethod(scheduleJob,applicationContext);
    }

}
