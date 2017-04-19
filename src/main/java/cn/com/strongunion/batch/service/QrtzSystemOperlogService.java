package cn.com.strongunion.batch.service;

import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;

/**
 * Service Interface for managing QrtzSystemOperlog.
 */
public interface QrtzSystemOperlogService {

    /**
     * Save a qrtzSystemOperlog.
     *
     * @param qrtzSystemOperlogDTO the entity to save
     * @return the persisted entity
     */
    QrtzSystemOperlogDTO save(QrtzSystemOperlogDTO qrtzSystemOperlogDTO);

    /**
     *  Get all the qrtzSystemOperlogs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrtzSystemOperlogDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" qrtzSystemOperlog.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QrtzSystemOperlogDTO findOne(Long id);

    /**
     *  Delete the "id" qrtzSystemOperlog.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the qrtzSystemOperlog corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrtzSystemOperlogDTO> search(String query, Pageable pageable);

    /**
     * 异步将日志保存到队列中，当队列大小超过500时，持久化日志到数据库中。
     * @param log 操作日志
     */
    void asyncLog(QrtzSystemOperlogDTO log);

    /**
     * 将队列中的操作日志持久化，30s定时保存
     */
    void persistQrtzSystemOperlogDTO();

    /**
     * 保存日志
     * @param scheduleJob
     * @param startTime
     * @param expMessage
     */
    void schedulersSaveLog(QrtzScheduleJob scheduleJob, ZonedDateTime startTime, String expMessage);
}
