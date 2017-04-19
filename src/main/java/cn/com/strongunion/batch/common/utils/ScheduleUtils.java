package cn.com.strongunion.batch.common.utils;

import cn.com.strongunion.batch.common.ConcurrentJob;
import cn.com.strongunion.batch.common.Constant;
import cn.com.strongunion.batch.common.NonConcurrentJob;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.web.rest.errors.CustomParameterizedException;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 定时任务辅助类
 *
 * Created by liyd on 12/19/14.
 */
public class ScheduleUtils {

	/** 日志对象 */
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleUtils.class);

	/***
	 * 判断任务是否存在 *
	 * @param jobName
	 * @return
	 * @throws SchedulerException
	 */
	public static boolean isJobExists(Scheduler scheduler, String jobName, String jobGroup) {
	    boolean isJobExist = false;
		if (scheduler == null) {
			return false;
		}
		if (StringUtils.isEmpty(jobGroup)) {
			jobGroup = jobName;
		}
		JobKey key = new JobKey(jobName, jobGroup);
        try {
            isJobExist = scheduler.checkExists(key);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CustomParameterizedException("检验任务是否存在发生异常！");
        }
        return isJobExist;
    }

	/**
	 * 获取触发器key
	 *
	 * @param jobName
	 * @param jobGroup
	 * @return
	 */
	public static TriggerKey getTriggerKey(String jobName, String jobGroup) {

		return TriggerKey.triggerKey(jobName, jobGroup);
	}

	/**
	 * 获取表达式触发器
	 *
	 * @param scheduler the scheduler
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @return cron trigger
	 */
	public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) {

		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger instanceof CronTrigger) {
                return (CronTrigger) scheduler.getTrigger(triggerKey);
            }
		} catch (SchedulerException e) {
			LOG.error("获取定时任务CronTrigger出现异常", e);
			throw new CustomParameterizedException("获取定时任务触发器出现异常！");
		}
		return null;
	}

    /**
     * 获取第一个触发器
     * @param scheduler
     * @param jobName
     * @param jobGroup
     * @return
     */
	public static Trigger getFirstTrigger(Scheduler scheduler, String jobName, String jobGroup) {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        List<? extends Trigger> triggers = null;
        Trigger trigger = null;
        try {
            triggers = scheduler.getTriggersOfJob(jobKey);

            if (triggers != null && triggers.size() > 0) {
                trigger = triggers.iterator().next();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CustomParameterizedException("获取定时任务触发器出现异常！");
        }
        return trigger;
    }

    /**
     * 获取触发器状态
     * @param scheduler
     * @param trigger
     * @return
     */
    public static Trigger.TriggerState getTriggerState(Scheduler scheduler,Trigger trigger) {
        Trigger.TriggerState triggerState = null;
        try {
            triggerState = scheduler.getTriggerState(trigger.getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CustomParameterizedException("获取定时任务触发器出现异常！");
        }
        return triggerState;
    }

	/**
	 * 创建任务
	 *
	 * @param scheduler the scheduler
	 * @param scheduleJob the schedule job
	 */
	public static void createScheduleJob(Scheduler scheduler, QrtzScheduleJob scheduleJob) {
		boolean isSync = false;
		if ("1".equals(scheduleJob.getIsConcurrent())) {
			isSync = true;
		}

		String jobName = scheduleJob.getJobName();
        String jobGroup = scheduleJob.getJobGroup();
        String cronExpression = scheduleJob.getCronExpression();
        // 同步或异步
        Class<? extends Job> jobClass = isSync ? ConcurrentJob.class : NonConcurrentJob.class;

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, scheduleJob);

        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

        if (scheduleJob.getStartDate() != null) {
            Date start = Date.from(scheduleJob.getStartDate().toInstant());
            trigger = trigger.getTriggerBuilder().startAt(start).build();
        }

        if (scheduleJob.getEndDate() != null) {
            Date end = Date.from(scheduleJob.getEndDate().toInstant());
            trigger = trigger.getTriggerBuilder().endAt(end).build();
        }

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error("创建定时任务失败", e);
            throw new CustomParameterizedException("创建定时任务失败！");
        }
	}

	/**
	 * 创建定时任务
	 *
	 * @param scheduler the scheduler
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @param cronExpression the cron expression
	 * @param isSync the is sync
	 * @param param the param
	 */
	public static void createScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression, boolean isSync, Object param) {
		// 同步或异步
		Class<? extends Job> jobClass = isSync ? ConcurrentJob.class : NonConcurrentJob.class;

		// 构建job信息
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

		// 放入参数，运行时的方法可以获取
		jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, param);

		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			LOG.error("创建定时任务失败", e);
			throw new CustomParameterizedException("创建定时任务失败！");
		}
	}

	/**
	 * 运行一次任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void runOnce(Scheduler scheduler, String jobName, String jobGroup) {
		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("运行一次定时任务失败", e);
			throw new CustomParameterizedException("运行一次定时任务失败！");
		}
	}

	/**
	 * 暂停任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.pauseJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("暂停定时任务失败", e);
			throw new CustomParameterizedException("暂停定时任务失败！");
		}
	}

	/**
	 * 恢复任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) {

		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
		try {
			scheduler.resumeJob(jobKey);
		} catch (SchedulerException e) {
			LOG.error("暂停定时任务失败", e);
			throw new CustomParameterizedException("暂停定时任务失败！");
		}
	}

	/**
	 * 获取jobKey
	 *
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @return the job key
	 */
	public static JobKey getJobKey(String jobName, String jobGroup) {

		return JobKey.jobKey(jobName, jobGroup);
	}

	/**
	 * 更新定时任务
	 *
	 * @param scheduler the scheduler
	 * @param scheduleJob the schedule job
	 */
	public static void updateScheduleJob(Scheduler scheduler, QrtzScheduleJob scheduleJob) {
		boolean isSync = false;
		if ("1".equals(scheduleJob.getIsConcurrent())) {
			isSync = true;
		}
		updateScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup(), scheduleJob.getCronExpression(), isSync, scheduleJob);
	}

	/**
	 * 更新定时任务
	 *
	 * @param scheduler the scheduler
	 * @param jobName the job name
	 * @param jobGroup the job group
	 * @param cronExpression the cron expression
	 * @param isSync the is sync
	 * @param param the param
	 */
	public static void updateScheduleJob(Scheduler scheduler, String jobName, String jobGroup, String cronExpression, boolean isSync, Object param) {

		// 同步或异步
		// Class<? extends Job> jobClass = isSync ? JobSyncFactory.class : JobFactory.class;

		try {
			// JobDetail jobDetail = scheduler.getJobDetail(getJobKey(jobName, jobGroup));

			// jobDetail = jobDetail.getJobBuilder().ofType(jobClass).build();

			// 更新参数 实际测试中发现无法更新
			// JobDataMap jobDataMap = jobDetail.getJobDataMap();
			// jobDataMap.put(ScheduleJobVo.JOB_PARAM_KEY, param);
			// jobDetail.getJobBuilder().usingJobData(jobDataMap);

			TriggerKey triggerKey = ScheduleUtils.getTriggerKey(jobName, jobGroup);

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (null == trigger) {
				// 同步或异步
				Class<? extends Job> jobClass = isSync ? ConcurrentJob.class : NonConcurrentJob.class;
				JobDetail jobDetail = scheduler.getJobDetail(getJobKey(jobName, jobGroup));

				jobDetail = jobDetail.getJobBuilder().ofType(jobClass).build();

				 jobDetail.getJobDataMap().put(Constant.JOB_PARAM_KEY, param);

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();

				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}

		} catch (SchedulerException e) {
			LOG.error("更新定时任务失败", e);
			throw new CustomParameterizedException("更新定时任务失败！");
		}
	}

	/**
	 * 删除定时任务
	 *
	 * @param scheduler
	 * @param jobName
	 * @param jobGroup
	 */
	public static void deleteScheduleJob(Scheduler scheduler, String jobName, String jobGroup) {
		try {
			scheduler.deleteJob(getJobKey(jobName, jobGroup));
		} catch (SchedulerException e) {
			LOG.error("删除定时任务失败", e);
			throw new CustomParameterizedException("删除定时任务失败！");
		}
	}
}
