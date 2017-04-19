package cn.com.strongunion.batch.web.rest;

import cn.com.strongunion.batch.JobserverApp;
import cn.com.strongunion.batch.domain.QrtzSystemOperlog;
import cn.com.strongunion.batch.repository.QrtzSystemOperlogRepository;
import cn.com.strongunion.batch.service.QrtzSystemOperlogService;
import cn.com.strongunion.batch.repository.search.QrtzSystemOperlogSearchRepository;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;
import cn.com.strongunion.batch.service.mapper.QrtzSystemOperlogMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the QrtzSystemOperlogResource REST controller.
 *
 * @see QrtzSystemOperlogResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobserverApp.class)
public class QrtzSystemOperlogResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_OPER_PARAM = "AAAAA";
    private static final String UPDATED_OPER_PARAM = "BBBBB";
    private static final String DEFAULT_OPER_RESULT = "AAAAA";
    private static final String UPDATED_OPER_RESULT = "BBBBB";
    private static final String DEFAULT_OPER_TYPE = "AAAAA";
    private static final String UPDATED_OPER_TYPE = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_TIME_STR = dateTimeFormatter.format(DEFAULT_START_TIME);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_TIME_STR = dateTimeFormatter.format(DEFAULT_END_TIME);

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;
    private static final String DEFAULT_SUCCESS = "AAAAA";
    private static final String UPDATED_SUCCESS = "BBBBB";
    private static final String DEFAULT_TRIGGER_NAME = "AAAAA";
    private static final String UPDATED_TRIGGER_NAME = "BBBBB";
    private static final String DEFAULT_USER_ID = "AAAAA";
    private static final String UPDATED_USER_ID = "BBBBB";
    private static final String DEFAULT_USER_NAME = "AAAAA";
    private static final String UPDATED_USER_NAME = "BBBBB";
    private static final String DEFAULT_BEAN_CLASS = "AAAAA";
    private static final String UPDATED_BEAN_CLASS = "BBBBB";
    private static final String DEFAULT_METHOD_NAME = "AAAAA";
    private static final String UPDATED_METHOD_NAME = "BBBBB";

    @Inject
    private QrtzSystemOperlogRepository qrtzSystemOperlogRepository;

    @Inject
    private QrtzSystemOperlogMapper qrtzSystemOperlogMapper;

    @Inject
    private QrtzSystemOperlogService qrtzSystemOperlogService;

    @Inject
    private QrtzSystemOperlogSearchRepository qrtzSystemOperlogSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQrtzSystemOperlogMockMvc;

    private QrtzSystemOperlog qrtzSystemOperlog;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QrtzSystemOperlogResource qrtzSystemOperlogResource = new QrtzSystemOperlogResource();
        ReflectionTestUtils.setField(qrtzSystemOperlogResource, "qrtzSystemOperlogService", qrtzSystemOperlogService);
        this.restQrtzSystemOperlogMockMvc = MockMvcBuilders.standaloneSetup(qrtzSystemOperlogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrtzSystemOperlog createEntity(EntityManager em) {
        QrtzSystemOperlog qrtzSystemOperlog = new QrtzSystemOperlog();
        qrtzSystemOperlog = new QrtzSystemOperlog()
                .operParam(DEFAULT_OPER_PARAM)
                .operResult(DEFAULT_OPER_RESULT)
                .operType(DEFAULT_OPER_TYPE)
                .startTime(DEFAULT_START_TIME)
                .endTime(DEFAULT_END_TIME)
                .duration(DEFAULT_DURATION)
                .success(DEFAULT_SUCCESS)
                .triggerName(DEFAULT_TRIGGER_NAME)
                .userId(DEFAULT_USER_ID)
                .userName(DEFAULT_USER_NAME)
                .beanClass(DEFAULT_BEAN_CLASS)
                .methodName(DEFAULT_METHOD_NAME);
        return qrtzSystemOperlog;
    }

    @Before
    public void initTest() {
        qrtzSystemOperlogSearchRepository.deleteAll();
        qrtzSystemOperlog = createEntity(em);
    }

    @Test
    @Transactional
    public void createQrtzSystemOperlog() throws Exception {
        int databaseSizeBeforeCreate = qrtzSystemOperlogRepository.findAll().size();

        // Create the QrtzSystemOperlog
        QrtzSystemOperlogDTO qrtzSystemOperlogDTO = qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(qrtzSystemOperlog);

        restQrtzSystemOperlogMockMvc.perform(post("/api/qrtz-system-operlogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzSystemOperlogDTO)))
                .andExpect(status().isCreated());

        // Validate the QrtzSystemOperlog in the database
        List<QrtzSystemOperlog> qrtzSystemOperlogs = qrtzSystemOperlogRepository.findAll();
        assertThat(qrtzSystemOperlogs).hasSize(databaseSizeBeforeCreate + 1);
        QrtzSystemOperlog testQrtzSystemOperlog = qrtzSystemOperlogs.get(qrtzSystemOperlogs.size() - 1);
        assertThat(testQrtzSystemOperlog.getOperParam()).isEqualTo(DEFAULT_OPER_PARAM);
        assertThat(testQrtzSystemOperlog.getOperResult()).isEqualTo(DEFAULT_OPER_RESULT);
        assertThat(testQrtzSystemOperlog.getOperType()).isEqualTo(DEFAULT_OPER_TYPE);
        assertThat(testQrtzSystemOperlog.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testQrtzSystemOperlog.getEndTime()).isEqualTo(DEFAULT_END_TIME);
        assertThat(testQrtzSystemOperlog.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testQrtzSystemOperlog.getSuccess()).isEqualTo(DEFAULT_SUCCESS);
        assertThat(testQrtzSystemOperlog.getTriggerName()).isEqualTo(DEFAULT_TRIGGER_NAME);
        assertThat(testQrtzSystemOperlog.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testQrtzSystemOperlog.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testQrtzSystemOperlog.getBeanClass()).isEqualTo(DEFAULT_BEAN_CLASS);
        assertThat(testQrtzSystemOperlog.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);

        // Validate the QrtzSystemOperlog in ElasticSearch
        QrtzSystemOperlog qrtzSystemOperlogEs = qrtzSystemOperlogSearchRepository.findOne(testQrtzSystemOperlog.getId());
        assertThat(qrtzSystemOperlogEs).isEqualToComparingFieldByField(testQrtzSystemOperlog);
    }

    @Test
    @Transactional
    public void getAllQrtzSystemOperlogs() throws Exception {
        // Initialize the database
        qrtzSystemOperlogRepository.saveAndFlush(qrtzSystemOperlog);

        // Get all the qrtzSystemOperlogs
        restQrtzSystemOperlogMockMvc.perform(get("/api/qrtz-system-operlogs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(qrtzSystemOperlog.getId().intValue())))
                .andExpect(jsonPath("$.[*].operParam").value(hasItem(DEFAULT_OPER_PARAM.toString())))
                .andExpect(jsonPath("$.[*].operResult").value(hasItem(DEFAULT_OPER_RESULT.toString())))
                .andExpect(jsonPath("$.[*].operType").value(hasItem(DEFAULT_OPER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
                .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
                .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.toString())))
                .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
                .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
                .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
                .andExpect(jsonPath("$.[*].beanClass").value(hasItem(DEFAULT_BEAN_CLASS.toString())))
                .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME.toString())));
    }

    @Test
    @Transactional
    public void getQrtzSystemOperlog() throws Exception {
        // Initialize the database
        qrtzSystemOperlogRepository.saveAndFlush(qrtzSystemOperlog);

        // Get the qrtzSystemOperlog
        restQrtzSystemOperlogMockMvc.perform(get("/api/qrtz-system-operlogs/{id}", qrtzSystemOperlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qrtzSystemOperlog.getId().intValue()))
            .andExpect(jsonPath("$.operParam").value(DEFAULT_OPER_PARAM.toString()))
            .andExpect(jsonPath("$.operResult").value(DEFAULT_OPER_RESULT.toString()))
            .andExpect(jsonPath("$.operType").value(DEFAULT_OPER_TYPE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME_STR))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME_STR))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.success").value(DEFAULT_SUCCESS.toString()))
            .andExpect(jsonPath("$.triggerName").value(DEFAULT_TRIGGER_NAME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.beanClass").value(DEFAULT_BEAN_CLASS.toString()))
            .andExpect(jsonPath("$.methodName").value(DEFAULT_METHOD_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQrtzSystemOperlog() throws Exception {
        // Get the qrtzSystemOperlog
        restQrtzSystemOperlogMockMvc.perform(get("/api/qrtz-system-operlogs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQrtzSystemOperlog() throws Exception {
        // Initialize the database
        qrtzSystemOperlogRepository.saveAndFlush(qrtzSystemOperlog);
        qrtzSystemOperlogSearchRepository.save(qrtzSystemOperlog);
        int databaseSizeBeforeUpdate = qrtzSystemOperlogRepository.findAll().size();

        // Update the qrtzSystemOperlog
        QrtzSystemOperlog updatedQrtzSystemOperlog = qrtzSystemOperlogRepository.findOne(qrtzSystemOperlog.getId());
        updatedQrtzSystemOperlog
                .operParam(UPDATED_OPER_PARAM)
                .operResult(UPDATED_OPER_RESULT)
                .operType(UPDATED_OPER_TYPE)
                .startTime(UPDATED_START_TIME)
                .endTime(UPDATED_END_TIME)
                .duration(UPDATED_DURATION)
                .success(UPDATED_SUCCESS)
                .triggerName(UPDATED_TRIGGER_NAME)
                .userId(UPDATED_USER_ID)
                .userName(UPDATED_USER_NAME)
                .beanClass(UPDATED_BEAN_CLASS)
                .methodName(UPDATED_METHOD_NAME);
        QrtzSystemOperlogDTO qrtzSystemOperlogDTO = qrtzSystemOperlogMapper.qrtzSystemOperlogToQrtzSystemOperlogDTO(updatedQrtzSystemOperlog);

        restQrtzSystemOperlogMockMvc.perform(put("/api/qrtz-system-operlogs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzSystemOperlogDTO)))
                .andExpect(status().isOk());

        // Validate the QrtzSystemOperlog in the database
        List<QrtzSystemOperlog> qrtzSystemOperlogs = qrtzSystemOperlogRepository.findAll();
        assertThat(qrtzSystemOperlogs).hasSize(databaseSizeBeforeUpdate);
        QrtzSystemOperlog testQrtzSystemOperlog = qrtzSystemOperlogs.get(qrtzSystemOperlogs.size() - 1);
        assertThat(testQrtzSystemOperlog.getOperParam()).isEqualTo(UPDATED_OPER_PARAM);
        assertThat(testQrtzSystemOperlog.getOperResult()).isEqualTo(UPDATED_OPER_RESULT);
        assertThat(testQrtzSystemOperlog.getOperType()).isEqualTo(UPDATED_OPER_TYPE);
        assertThat(testQrtzSystemOperlog.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testQrtzSystemOperlog.getEndTime()).isEqualTo(UPDATED_END_TIME);
        assertThat(testQrtzSystemOperlog.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testQrtzSystemOperlog.getSuccess()).isEqualTo(UPDATED_SUCCESS);
        assertThat(testQrtzSystemOperlog.getTriggerName()).isEqualTo(UPDATED_TRIGGER_NAME);
        assertThat(testQrtzSystemOperlog.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testQrtzSystemOperlog.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testQrtzSystemOperlog.getBeanClass()).isEqualTo(UPDATED_BEAN_CLASS);
        assertThat(testQrtzSystemOperlog.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);

        // Validate the QrtzSystemOperlog in ElasticSearch
        QrtzSystemOperlog qrtzSystemOperlogEs = qrtzSystemOperlogSearchRepository.findOne(testQrtzSystemOperlog.getId());
        assertThat(qrtzSystemOperlogEs).isEqualToComparingFieldByField(testQrtzSystemOperlog);
    }

    @Test
    @Transactional
    public void deleteQrtzSystemOperlog() throws Exception {
        // Initialize the database
        qrtzSystemOperlogRepository.saveAndFlush(qrtzSystemOperlog);
        qrtzSystemOperlogSearchRepository.save(qrtzSystemOperlog);
        int databaseSizeBeforeDelete = qrtzSystemOperlogRepository.findAll().size();

        // Get the qrtzSystemOperlog
        restQrtzSystemOperlogMockMvc.perform(delete("/api/qrtz-system-operlogs/{id}", qrtzSystemOperlog.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean qrtzSystemOperlogExistsInEs = qrtzSystemOperlogSearchRepository.exists(qrtzSystemOperlog.getId());
        assertThat(qrtzSystemOperlogExistsInEs).isFalse();

        // Validate the database is empty
        List<QrtzSystemOperlog> qrtzSystemOperlogs = qrtzSystemOperlogRepository.findAll();
        assertThat(qrtzSystemOperlogs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchQrtzSystemOperlog() throws Exception {
        // Initialize the database
        qrtzSystemOperlogRepository.saveAndFlush(qrtzSystemOperlog);
        qrtzSystemOperlogSearchRepository.save(qrtzSystemOperlog);

        // Search the qrtzSystemOperlog
        restQrtzSystemOperlogMockMvc.perform(get("/api/_search/qrtz-system-operlogs?query=id:" + qrtzSystemOperlog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrtzSystemOperlog.getId().intValue())))
            .andExpect(jsonPath("$.[*].operParam").value(hasItem(DEFAULT_OPER_PARAM.toString())))
            .andExpect(jsonPath("$.[*].operResult").value(hasItem(DEFAULT_OPER_RESULT.toString())))
            .andExpect(jsonPath("$.[*].operType").value(hasItem(DEFAULT_OPER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME_STR)))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME_STR)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].success").value(hasItem(DEFAULT_SUCCESS.toString())))
            .andExpect(jsonPath("$.[*].triggerName").value(hasItem(DEFAULT_TRIGGER_NAME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].beanClass").value(hasItem(DEFAULT_BEAN_CLASS.toString())))
            .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME.toString())));
    }
}
