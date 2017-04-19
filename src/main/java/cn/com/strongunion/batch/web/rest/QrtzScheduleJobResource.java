package cn.com.strongunion.batch.web.rest;

import cn.com.strongunion.batch.service.QrtzScheduleJobService;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.web.rest.util.HeaderUtil;
import cn.com.strongunion.batch.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing QrtzScheduleJob.
 */
@RestController
@RequestMapping("/api")
public class QrtzScheduleJobResource {

    private final Logger log = LoggerFactory.getLogger(QrtzScheduleJobResource.class);

    @Inject
    private QrtzScheduleJobService qrtzScheduleJobService;

    /**
     * POST  /qrtz-schedule-jobs : Create a new qrtzScheduleJob.
     *
     * @param qrtzScheduleJobDTO the qrtzScheduleJobDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new qrtzScheduleJobDTO, or with status 400 (Bad Request) if the qrtzScheduleJob has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/qrtz-schedule-jobs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzScheduleJobDTO> createQrtzScheduleJob(@Valid @RequestBody QrtzScheduleJobDTO qrtzScheduleJobDTO) throws URISyntaxException {
        log.debug("REST request to save QrtzScheduleJob : {}", qrtzScheduleJobDTO);
        if (qrtzScheduleJobDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("qrtzScheduleJob", "idexists", "A new qrtzScheduleJob cannot already have an ID")).body(null);
        }
        QrtzScheduleJobDTO result = qrtzScheduleJobService.save(qrtzScheduleJobDTO);
        return ResponseEntity.created(new URI("/api/qrtz-schedule-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("qrtzScheduleJob", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /qrtz-schedule-jobs : Updates an existing qrtzScheduleJob.
     *
     * @param qrtzScheduleJobDTO the qrtzScheduleJobDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated qrtzScheduleJobDTO,
     * or with status 400 (Bad Request) if the qrtzScheduleJobDTO is not valid,
     * or with status 500 (Internal Server Error) if the qrtzScheduleJobDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/qrtz-schedule-jobs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzScheduleJobDTO> updateQrtzScheduleJob(@Valid @RequestBody QrtzScheduleJobDTO qrtzScheduleJobDTO) throws URISyntaxException {
        log.debug("REST request to update QrtzScheduleJob : {}", qrtzScheduleJobDTO);
        if (qrtzScheduleJobDTO.getId() == null) {
            return createQrtzScheduleJob(qrtzScheduleJobDTO);
        }
        QrtzScheduleJobDTO result = qrtzScheduleJobService.save(qrtzScheduleJobDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("qrtzScheduleJob", qrtzScheduleJobDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /qrtz-schedule-jobs : get all the qrtzScheduleJobs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of qrtzScheduleJobs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/qrtz-schedule-jobs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QrtzScheduleJobDTO>> getAllQrtzScheduleJobs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QrtzScheduleJobs");
        Page<QrtzScheduleJobDTO> page = qrtzScheduleJobService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qrtz-schedule-jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * 获取正在运行的任务列表（无分页）
     * @return
     * @throws URISyntaxException
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/running",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QrtzScheduleJobDTO>> getRunningQrtzScheduleJobs()
        throws URISyntaxException {
        log.debug("REST request to get running QrtzScheduleJobs");
        List<QrtzScheduleJobDTO> list = qrtzScheduleJobService.findExecuteJobList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 暂停任务
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/pause/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> pause(@PathVariable Long id) throws Exception {
        log.debug("REST request to parse QrtzScheduleJob : {}",id);
        qrtzScheduleJobService.pauseJob(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityPauseAlert("qrtzScheduleJob", id.toString())).build();
    }

    /**
     * 恢复任务
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/resume/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> resume(@PathVariable Long id) throws Exception {
        log.debug("REST request to parse QrtzScheduleJob : {}",id);
        qrtzScheduleJobService.resumeJob(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityResumeAlert("qrtzScheduleJob", id.toString())).build();
    }

    /**
     * 立即运行一次
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/run/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> runonce(@PathVariable Long id) throws Exception {
        log.debug("REST request to parse QrtzScheduleJob : {}",id);
        qrtzScheduleJobService.runOnce(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityRunAlert("qrtzScheduleJob", id.toString())).build();
    }

    /**
     * GET  /qrtz-schedule-jobs/:id : get the "id" qrtzScheduleJob.
     *
     * @param id the id of the qrtzScheduleJobDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the qrtzScheduleJobDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzScheduleJobDTO> getQrtzScheduleJob(@PathVariable Long id) {
        log.debug("REST request to get QrtzScheduleJob : {}", id);
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobService.findOne(id);
        return Optional.ofNullable(qrtzScheduleJobDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /qrtz-schedule-jobs/:id : delete the "id" qrtzScheduleJob.
     *
     * @param id the id of the qrtzScheduleJobDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/qrtz-schedule-jobs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQrtzScheduleJob(@PathVariable Long id) throws SchedulerException {
        log.debug("REST request to delete QrtzScheduleJob : {}", id);
        qrtzScheduleJobService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("qrtzScheduleJob", id.toString())).build();
    }

    /**
     * SEARCH  /_search/qrtz-schedule-jobs?query=:query : search for the qrtzScheduleJob corresponding
     * to the query.
     *
     * @param query the query of the qrtzScheduleJob search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/qrtz-schedule-jobs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QrtzScheduleJobDTO>> searchQrtzScheduleJobs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of QrtzScheduleJobs for query {}", query);
        Page<QrtzScheduleJobDTO> page = qrtzScheduleJobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/qrtz-schedule-jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
