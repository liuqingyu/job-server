package cn.com.strongunion.batch.common;

import cn.com.strongunion.batch.common.utils.SpringContextHolder;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.service.QrtzSystemOperlogService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.ZonedDateTime;


public abstract class AbstractSchedulerJob extends QuartzJobBean {

    /* 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(AbstractSchedulerJob.class);

    /**
     * 执行作业
     * @param scheduleJob
     * @param applicationContext
     */
    public abstract void exec(QrtzScheduleJob scheduleJob, ApplicationContext applicationContext);

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String expMessage = null;
        // 记录日志开始时间
        ZonedDateTime startTime = ZonedDateTime.now();
        QrtzScheduleJob scheduleJob = (QrtzScheduleJob) context.getMergedJobDataMap().get(Constant.JOB_PARAM_KEY);
        logger.info("jobName:" + scheduleJob.getJobName() + "，jobGroup:" + scheduleJob.getJobGroup());
        try {
            exec(scheduleJob, SpringContextHolder.getApplicationContext());
        } catch (Exception e) {
            expMessage = e.getMessage();
            e.printStackTrace();
            logger.error("执行任务错误信息:" + expMessage);
        }
        QrtzSystemOperlogService operlogService = SpringContextHolder.getBean(QrtzSystemOperlogService.class);
        operlogService.schedulersSaveLog(scheduleJob, startTime, expMessage);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

