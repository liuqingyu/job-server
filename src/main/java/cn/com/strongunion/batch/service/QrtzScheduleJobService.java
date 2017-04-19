package cn.com.strongunion.batch.service;

import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing QrtzScheduleJob.
 */
public interface QrtzScheduleJobService {

    /**
     * Save a qrtzScheduleJob.
     *
     * @param qrtzScheduleJobDTO the entity to save
     * @return the persisted entity
     */
    QrtzScheduleJobDTO save(QrtzScheduleJobDTO qrtzScheduleJobDTO);

    /**
     *  Get all the qrtzScheduleJobs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrtzScheduleJobDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" qrtzScheduleJob.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QrtzScheduleJobDTO findOne(Long id);

    /**
     *  Delete the "id" qrtzScheduleJob.
     *
     *  @param id the id of the entity
     */
    void delete(Long id) throws SchedulerException;

    /**
     * Search for the qrtzScheduleJob corresponding to the query.
     *
     *  @param query the query of the search
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QrtzScheduleJobDTO> search(String query, Pageable pageable);

    /**
     * 暂停任务
     * @param id
     */
    void pauseJob(Long id);

    /**
     * 恢复任务
     * @param id
     */
    void resumeJob(Long id);

    void runOnce(Long id);

    List<QrtzScheduleJobDTO> findExecuteJobList();
}
