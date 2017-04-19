package cn.com.strongunion.batch.service.impl;

import cn.com.strongunion.batch.common.Constant;
import cn.com.strongunion.batch.common.utils.ScheduleUtils;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.repository.QrtzScheduleJobRepository;
import cn.com.strongunion.batch.repository.QrtzSystemOperlogRepository;
import cn.com.strongunion.batch.repository.search.QrtzScheduleJobSearchRepository;
import cn.com.strongunion.batch.service.QrtzScheduleJobService;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.service.mapper.QrtzScheduleJobMapper;
import cn.com.strongunion.batch.web.rest.errors.CustomParameterizedException;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing QrtzScheduleJob.
 */
@Service
@Transactional
public class QrtzScheduleJobServiceImpl implements QrtzScheduleJobService {

    private final Logger log = LoggerFactory.getLogger(QrtzScheduleJobServiceImpl.class);

    @Inject
    private QrtzScheduleJobRepository qrtzScheduleJobRepository;

    @Inject
    private QrtzScheduleJobMapper qrtzScheduleJobMapper;

    @Inject
    private QrtzScheduleJobSearchRepository qrtzScheduleJobSearchRepository;

    @Inject
    private QrtzSystemOperlogRepository qrtzSystemOperlogRepository;

    @Inject
    private Scheduler scheduler;

    /**
     * Save a qrtzScheduleJob.
     *
     * @param qrtzScheduleJobDTO the entity to save
     * @return the persisted entity
     */
    public QrtzScheduleJobDTO save(QrtzScheduleJobDTO qrtzScheduleJobDTO) {
        log.debug("Request to save QrtzScheduleJob : {}", qrtzScheduleJobDTO);
        QrtzScheduleJob qrtzScheduleJob = qrtzScheduleJobMapper.qrtzScheduleJobDTOToQrtzScheduleJob(qrtzScheduleJobDTO);

        if (qrtzScheduleJob.getId() == null) {
            if (ScheduleUtils.isJobExists(scheduler, qrtzScheduleJob.getJobName(), qrtzScheduleJob.getJobGroup())) {
                throw new CustomParameterizedException("任务已存在!");
            }

            // 查询数据库中 此任务名称和表达式是否已存在
            List<QrtzScheduleJob> jobs = qrtzScheduleJobRepository.findAllByJobNameAndCronExpression(qrtzScheduleJob.getJobName(), qrtzScheduleJob.getCronExpression());

            // 做验证 计划名称和时间间隔不能重复
            if (jobs != null && jobs.size() > 0) {
                throw new CustomParameterizedException("任务已存在！");
            }
        } else {
            ScheduleUtils.deleteScheduleJob(scheduler, qrtzScheduleJob.getJobName(), qrtzScheduleJob.getJobGroup());
        }
        if (Constant.TRIGGER_TYPE_CRON.equals(qrtzScheduleJob.getTriggerType()) && StringUtils.isBlank(qrtzScheduleJob.getCronExpression())) {
            throw new CustomParameterizedException("任务计划时间表达式不能为空！");
        }

        ScheduleUtils.createScheduleJob(scheduler, qrtzScheduleJob);
        qrtzScheduleJob = qrtzScheduleJobRepository.save(qrtzScheduleJob);
        QrtzScheduleJobDTO result = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);
        qrtzScheduleJobSearchRepository.save(qrtzScheduleJob);
        return result;
    }

    /**
     * Get all the qrtzScheduleJobs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QrtzScheduleJobDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QrtzScheduleJobs");
        Page<QrtzScheduleJob> result = qrtzScheduleJobRepository.findAll(pageable);
        for (QrtzScheduleJob vo : result) {

            Trigger trigger = ScheduleUtils.getFirstTrigger(scheduler, vo.getJobName(), vo.getJobGroup());

            if (trigger != null) {
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    vo.setCronExpression(cronExpression);
                }
                Trigger.TriggerState triggerState = ScheduleUtils.getTriggerState(scheduler, trigger);
                if (triggerState != null) {
                    vo.setJobStatus(triggerState.name());
                }
            }
        }
        return result.map(qrtzScheduleJob -> qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob));
    }

    /**
     * 获取正在运行的任务列表
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<QrtzScheduleJobDTO> findExecuteJobList() {
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            List<QrtzScheduleJob> jobList = new ArrayList<QrtzScheduleJob>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                QrtzScheduleJob job = new QrtzScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription(jobDetail.getDescription());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
            Collections.sort(jobList, new Comparator<QrtzScheduleJob>() {
                @Override
                public int compare(QrtzScheduleJob o1, QrtzScheduleJob o2) {
                    return o1.getJobName().compareTo(o2.getJobName());
                }
            });
            return qrtzScheduleJobMapper.qrtzScheduleJobsToQrtzScheduleJobDTOs(jobList);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new CustomParameterizedException("获取执行任务列表异常！");
        }
    }

    /**
     * Get one qrtzScheduleJob by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public QrtzScheduleJobDTO findOne(Long id) {
        log.debug("Request to get QrtzScheduleJob : {}", id);
        QrtzScheduleJob qrtzScheduleJob = qrtzScheduleJobRepository.findOne(id);
        Trigger trigger = ScheduleUtils.getFirstTrigger(scheduler, qrtzScheduleJob.getJobName(), qrtzScheduleJob.getJobGroup());

        if (trigger != null) {
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                qrtzScheduleJob.setCronExpression(cronExpression);
            }
            Trigger.TriggerState triggerState = ScheduleUtils.getTriggerState(scheduler, trigger);
            if (triggerState != null) {
                qrtzScheduleJob.setJobStatus(triggerState.name());
            }
        }
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);
        return qrtzScheduleJobDTO;
    }

    /**
     * Delete the  qrtzScheduleJob by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QrtzScheduleJob : {}", id);
        // 查询Job信息
        QrtzScheduleJob scheduleJob = qrtzScheduleJobRepository.findOne(id);
        // 删除运行的任务
        ScheduleUtils.deleteScheduleJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
        qrtzSystemOperlogRepository.deleteByJobId(id);
        qrtzScheduleJobRepository.delete(id);
        qrtzScheduleJobSearchRepository.delete(id);
    }

    /**
     * 立即运行一次
     *
     * @param id
     */
    public void runOnce(Long id) {
        QrtzScheduleJob scheduleJob = qrtzScheduleJobRepository.findOne(id);
        ScheduleUtils.runOnce(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    /**
     * 暂停任务
     *
     * @param id
     */
    @Transactional
    public void pauseJob(Long id) {
        QrtzScheduleJob scheduleJob = qrtzScheduleJobRepository.findOne(id);
        ScheduleUtils.pauseJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    /**
     * 恢复任务
     *
     * @param id
     */
    @Transactional
    public void resumeJob(Long id) {
        QrtzScheduleJob scheduleJob = qrtzScheduleJobRepository.findOne(id);
        ScheduleUtils.resumeJob(scheduler, scheduleJob.getJobName(), scheduleJob.getJobGroup());
    }

    /**
     * Search for the qrtzScheduleJob corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QrtzScheduleJobDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of QrtzScheduleJobs for query {}", query);
        Page<QrtzScheduleJob> result = qrtzScheduleJobSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(qrtzScheduleJob -> qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob));
    }
}
