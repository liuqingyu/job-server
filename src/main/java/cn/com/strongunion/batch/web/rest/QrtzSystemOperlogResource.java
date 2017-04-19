package cn.com.strongunion.batch.web.rest;

import com.codahale.metrics.annotation.Timed;
import cn.com.strongunion.batch.service.QrtzSystemOperlogService;
import cn.com.strongunion.batch.web.rest.util.HeaderUtil;
import cn.com.strongunion.batch.web.rest.util.PaginationUtil;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing QrtzSystemOperlog.
 */
@RestController
@RequestMapping("/api")
public class QrtzSystemOperlogResource {

    private final Logger log = LoggerFactory.getLogger(QrtzSystemOperlogResource.class);
        
    @Inject
    private QrtzSystemOperlogService qrtzSystemOperlogService;

    /**
     * POST  /qrtz-system-operlogs : Create a new qrtzSystemOperlog.
     *
     * @param qrtzSystemOperlogDTO the qrtzSystemOperlogDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new qrtzSystemOperlogDTO, or with status 400 (Bad Request) if the qrtzSystemOperlog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/qrtz-system-operlogs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzSystemOperlogDTO> createQrtzSystemOperlog(@RequestBody QrtzSystemOperlogDTO qrtzSystemOperlogDTO) throws URISyntaxException {
        log.debug("REST request to save QrtzSystemOperlog : {}", qrtzSystemOperlogDTO);
        if (qrtzSystemOperlogDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("qrtzSystemOperlog", "idexists", "A new qrtzSystemOperlog cannot already have an ID")).body(null);
        }
        QrtzSystemOperlogDTO result = qrtzSystemOperlogService.save(qrtzSystemOperlogDTO);
        return ResponseEntity.created(new URI("/api/qrtz-system-operlogs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("qrtzSystemOperlog", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /qrtz-system-operlogs : Updates an existing qrtzSystemOperlog.
     *
     * @param qrtzSystemOperlogDTO the qrtzSystemOperlogDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated qrtzSystemOperlogDTO,
     * or with status 400 (Bad Request) if the qrtzSystemOperlogDTO is not valid,
     * or with status 500 (Internal Server Error) if the qrtzSystemOperlogDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/qrtz-system-operlogs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzSystemOperlogDTO> updateQrtzSystemOperlog(@RequestBody QrtzSystemOperlogDTO qrtzSystemOperlogDTO) throws URISyntaxException {
        log.debug("REST request to update QrtzSystemOperlog : {}", qrtzSystemOperlogDTO);
        if (qrtzSystemOperlogDTO.getId() == null) {
            return createQrtzSystemOperlog(qrtzSystemOperlogDTO);
        }
        QrtzSystemOperlogDTO result = qrtzSystemOperlogService.save(qrtzSystemOperlogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("qrtzSystemOperlog", qrtzSystemOperlogDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /qrtz-system-operlogs : get all the qrtzSystemOperlogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of qrtzSystemOperlogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/qrtz-system-operlogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QrtzSystemOperlogDTO>> getAllQrtzSystemOperlogs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of QrtzSystemOperlogs");
        Page<QrtzSystemOperlogDTO> page = qrtzSystemOperlogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/qrtz-system-operlogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /qrtz-system-operlogs/:id : get the "id" qrtzSystemOperlog.
     *
     * @param id the id of the qrtzSystemOperlogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the qrtzSystemOperlogDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/qrtz-system-operlogs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QrtzSystemOperlogDTO> getQrtzSystemOperlog(@PathVariable Long id) {
        log.debug("REST request to get QrtzSystemOperlog : {}", id);
        QrtzSystemOperlogDTO qrtzSystemOperlogDTO = qrtzSystemOperlogService.findOne(id);
        return Optional.ofNullable(qrtzSystemOperlogDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /qrtz-system-operlogs/:id : delete the "id" qrtzSystemOperlog.
     *
     * @param id the id of the qrtzSystemOperlogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/qrtz-system-operlogs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteQrtzSystemOperlog(@PathVariable Long id) {
        log.debug("REST request to delete QrtzSystemOperlog : {}", id);
        qrtzSystemOperlogService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("qrtzSystemOperlog", id.toString())).build();
    }

    /**
     * SEARCH  /_search/qrtz-system-operlogs?query=:query : search for the qrtzSystemOperlog corresponding
     * to the query.
     *
     * @param query the query of the qrtzSystemOperlog search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/qrtz-system-operlogs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<QrtzSystemOperlogDTO>> searchQrtzSystemOperlogs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of QrtzSystemOperlogs for query {}", query);
        Page<QrtzSystemOperlogDTO> page = qrtzSystemOperlogService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/qrtz-system-operlogs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
