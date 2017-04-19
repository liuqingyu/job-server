package cn.com.strongunion.batch.service.impl;

import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.domain.QrtzSystemOperlog;
import cn.com.strongunion.batch.repository.QrtzSystemOperlogRepository;
import cn.com.strongunion.batch.repository.search.QrtzSystemOperlogSearchRepository;
import cn.com.strongunion.batch.service.QrtzScheduleJobService;
import cn.com.strongunion.batch.service.QrtzSystemOperlogService;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;
import cn.com.strongunion.batch.service.mapper.QrtzScheduleJobMapper;
import cn.com.strongunion.batch.service.mapper.QrtzSystemOperlogMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing QrtzSystemOperlog.
 */
@Service
@Transactional
public class QrtzSystemOperlogServiceImpl implements QrtzSystemOperlogService{

    private final Logger log = LoggerFactory.getLogger(QrtzSystemOperlogServiceImpl.class);

    /**
     *日志记录缓存队列
     */
    private ConcurrentLinkedQueue<QrtzSystemOperlogDTO> queue = new ConcurrentLinkedQueue<QrtzSystemOperlogDTO>();

    @Inject
    private QrtzSystemOperlogRepository qrtzSystemOperlogRepository;

    @Inject
    private QrtzSystemOperlogMapper qrtzSystemOperlogMapper;

    @Inject
    private QrtzScheduleJobMapper qrtzScheduleJobMapper;

    @Inject
    private QrtzSystemOperlogSearchRepository qrtzSystemOperlogSearchRepository;

    @Inject
    private QrtzScheduleJobService qrtzScheduleJobService;

    /**
     * Save a qrtzSystemOperlog.
     *
     * @param qrtzSystemOperlogDTO the entity to save
     * @return the persisted entity
     */
    public QrtzSystemOperlogDTO save(QrtzSystemOperlogDTO qrtzSystemOperlogDTO) {
        log.debug("Request to save QrtzSystemOperlog : {}", qrtzSystemOperlogDTO);
        QrtzSystemOperlog qrtzSystemOperlog = qrtzSystemOperlogMapper.qrtzSystemOperlogDTOToQrtzSystemOperlog(qrtzSystemOperlogDTO);
        qrtzSystemOperlog = qrtzSystemOperlogRepository.save(qrtzSystemOperlog);
        QrtzSystemOperlogDTO result = qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(qrtzSystemOperlog);
        qrtzSystemOperlogSearchRepository.save(qrtzSystemOperlog);
        return result;
    }

    /**
     *  Get all the qrtzSystemOperlogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QrtzSystemOperlogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all QrtzSystemOperlogs");
        Page<QrtzSystemOperlog> result = qrtzSystemOperlogRepository.findAll(pageable);
        return result.map(qrtzSystemOperlog -> qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(qrtzSystemOperlog));
    }

    /**
     *  Get one qrtzSystemOperlog by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public QrtzSystemOperlogDTO findOne(Long id) {
        log.debug("Request to get QrtzSystemOperlog : {}", id);
        QrtzSystemOperlog qrtzSystemOperlog = qrtzSystemOperlogRepository.findOne(id);
        QrtzSystemOperlogDTO qrtzSystemOperlogDTO = qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(qrtzSystemOperlog);
        return qrtzSystemOperlogDTO;
    }

    /**
     *  Delete the  qrtzSystemOperlog by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete QrtzSystemOperlog : {}", id);
        qrtzSystemOperlogRepository.delete(id);
        qrtzSystemOperlogSearchRepository.delete(id);
    }

    /**
     * Search for the qrtzSystemOperlog corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<QrtzSystemOperlogDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of QrtzSystemOperlogs for query {}", query);
        Page<QrtzSystemOperlog> result = qrtzSystemOperlogSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(qrtzSystemOperlog -> qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(qrtzSystemOperlog));
    }

    @Async
    public void asyncLog(QrtzSystemOperlogDTO log) {
        queue.add(log);
        if (queue.size() > 0) {//500
            persistQrtzSystemOperlogDTO();
        }
    }

    /*** {@inheritDoc}*/
    @Scheduled(fixedRate = 30000)
    public void persistQrtzSystemOperlogDTO() {
        while (queue.size() > 0) {
            QrtzSystemOperlogDTO qrtzSystemOperlogDTO = queue.poll();
            save(qrtzSystemOperlogDTO);
        }
    }

    @Override
    public void schedulersSaveLog(QrtzScheduleJob scheduleJob, ZonedDateTime startTime, String exceptionMessage) {
        String userID = "admin";// userID,后期维护到数据字典中。
        String userName = "系统管理员";// userName,后期维护到数据字典中。
        String jobName = scheduleJob.getJobName();// 任务名称
        String trigName = scheduleJob.getJobName();// 计划名称
        String className = scheduleJob.getBeanClass();

        QrtzSystemOperlogDTO operLog = new QrtzSystemOperlogDTO();
        operLog.setStartTime(startTime);

        operLog.setUserId(userID);
        operLog.setUserName(userName);
        operLog.setOperType("BATCH_HANDLE");
        operLog.setJobId(scheduleJob.getId());
        operLog.setTriggerName(trigName);

        operLog.setOperParam("任务名称：" + jobName + ";计划名称：" + trigName + "\n");
        operLog.setBeanClass(className);
        operLog.setMethodName(scheduleJob.getMethodName());
        ZonedDateTime endTime = ZonedDateTime.now();
        operLog.setEndTime(endTime);
        operLog.setDuration(Duration.between(startTime,endTime).getSeconds());
        if (StringUtils.isNotEmpty(exceptionMessage)) {
            operLog.setSuccess("0");
            operLog.setOperResult(exceptionMessage);
            scheduleJob.setBatchStatus("0");
        } else {
            operLog.setSuccess("1");
            operLog.setOperResult("任务执行成功!");
            scheduleJob.setBatchStatus("1");
        }
        scheduleJob.setBatchTime(endTime);
        qrtzScheduleJobService.save(qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(scheduleJob));// 更新当天计划执行时间和状态
        asyncLog(operLog);
    }
}
