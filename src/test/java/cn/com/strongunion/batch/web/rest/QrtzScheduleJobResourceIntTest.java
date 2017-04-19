package cn.com.strongunion.batch.web.rest;

import cn.com.strongunion.batch.JobserverApp;
import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import cn.com.strongunion.batch.repository.QrtzScheduleJobRepository;
import cn.com.strongunion.batch.service.QrtzScheduleJobService;
import cn.com.strongunion.batch.repository.search.QrtzScheduleJobSearchRepository;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;
import cn.com.strongunion.batch.service.mapper.QrtzScheduleJobMapper;

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
 * Test class for the QrtzScheduleJobResource REST controller.
 *
 * @see QrtzScheduleJobResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JobserverApp.class)
public class QrtzScheduleJobResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));
    private static final String DEFAULT_JOB_NAME = "AAAAA";
    private static final String UPDATED_JOB_NAME = "BBBBB";
    private static final String DEFAULT_JOB_GROUP = "AAAAA";
    private static final String UPDATED_JOB_GROUP = "BBBBB";
    private static final String DEFAULT_TRIGGER_TYPE = "AAAAA";
    private static final String UPDATED_TRIGGER_TYPE = "BBBBB";
    private static final String DEFAULT_CRON_EXPRESSION = "AAAAA";
    private static final String UPDATED_CRON_EXPRESSION = "BBBBB";
    private static final String DEFAULT_BEAN_CLASS = "AAAAA";
    private static final String UPDATED_BEAN_CLASS = "BBBBB";
    private static final String DEFAULT_METHOD_NAME = "AAAAA";
    private static final String UPDATED_METHOD_NAME = "BBBBB";
    private static final String DEFAULT_PARAMETER_JSON = "AAAAA";
    private static final String UPDATED_PARAMETER_JSON = "BBBBB";
    private static final String DEFAULT_IS_CONCURRENT = "AAAAA";
    private static final String UPDATED_IS_CONCURRENT = "BBBBB";
    private static final String DEFAULT_BATCH_STATUS = "AAAAA";
    private static final String UPDATED_BATCH_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_BATCH_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_BATCH_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BATCH_TIME_STR = dateTimeFormatter.format(DEFAULT_BATCH_TIME);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_END_DATE_STR = dateTimeFormatter.format(DEFAULT_END_DATE);

    private static final ZonedDateTime DEFAULT_CREATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATE_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATE_TIME);
    private static final String DEFAULT_CREATE_BY = "AAAAA";
    private static final String UPDATED_CREATE_BY = "BBBBB";

    private static final ZonedDateTime DEFAULT_UPDATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATE_TIME_STR = dateTimeFormatter.format(DEFAULT_UPDATE_TIME);
    private static final String DEFAULT_UPDATE_BY = "AAAAA";
    private static final String UPDATED_UPDATE_BY = "BBBBB";

    @Inject
    private QrtzScheduleJobRepository qrtzScheduleJobRepository;

    @Inject
    private QrtzScheduleJobMapper qrtzScheduleJobMapper;

    @Inject
    private QrtzScheduleJobService qrtzScheduleJobService;

    @Inject
    private QrtzScheduleJobSearchRepository qrtzScheduleJobSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQrtzScheduleJobMockMvc;

    private QrtzScheduleJob qrtzScheduleJob;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QrtzScheduleJobResource qrtzScheduleJobResource = new QrtzScheduleJobResource();
        ReflectionTestUtils.setField(qrtzScheduleJobResource, "qrtzScheduleJobService", qrtzScheduleJobService);
        this.restQrtzScheduleJobMockMvc = MockMvcBuilders.standaloneSetup(qrtzScheduleJobResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QrtzScheduleJob createEntity(EntityManager em) {
        QrtzScheduleJob qrtzScheduleJob = new QrtzScheduleJob();
        qrtzScheduleJob = new QrtzScheduleJob()
                .jobName(DEFAULT_JOB_NAME)
                .jobGroup(DEFAULT_JOB_GROUP)
                .triggerType(DEFAULT_TRIGGER_TYPE)
                .cronExpression(DEFAULT_CRON_EXPRESSION)
                .beanClass(DEFAULT_BEAN_CLASS)
                .methodName(DEFAULT_METHOD_NAME)
                .parameterJson(DEFAULT_PARAMETER_JSON)
                .isConcurrent(DEFAULT_IS_CONCURRENT)
                .batchStatus(DEFAULT_BATCH_STATUS)
                .batchTime(DEFAULT_BATCH_TIME)
                .description(DEFAULT_DESCRIPTION)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .createTime(DEFAULT_CREATE_TIME)
                .createBy(DEFAULT_CREATE_BY)
                .updateTime(DEFAULT_UPDATE_TIME)
                .updateBy(DEFAULT_UPDATE_BY);
        return qrtzScheduleJob;
    }

    @Before
    public void initTest() {
        qrtzScheduleJobSearchRepository.deleteAll();
        qrtzScheduleJob = createEntity(em);
    }

    @Test
    @Transactional
    public void createQrtzScheduleJob() throws Exception {
        int databaseSizeBeforeCreate = qrtzScheduleJobRepository.findAll().size();

        // Create the QrtzScheduleJob
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isCreated());

        // Validate the QrtzScheduleJob in the database
        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeCreate + 1);
        QrtzScheduleJob testQrtzScheduleJob = qrtzScheduleJobs.get(qrtzScheduleJobs.size() - 1);
        assertThat(testQrtzScheduleJob.getJobName()).isEqualTo(DEFAULT_JOB_NAME);
        assertThat(testQrtzScheduleJob.getJobGroup()).isEqualTo(DEFAULT_JOB_GROUP);
        assertThat(testQrtzScheduleJob.getTriggerType()).isEqualTo(DEFAULT_TRIGGER_TYPE);
        assertThat(testQrtzScheduleJob.getCronExpression()).isEqualTo(DEFAULT_CRON_EXPRESSION);
        assertThat(testQrtzScheduleJob.getBeanClass()).isEqualTo(DEFAULT_BEAN_CLASS);
        assertThat(testQrtzScheduleJob.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
        assertThat(testQrtzScheduleJob.getParameterJson()).isEqualTo(DEFAULT_PARAMETER_JSON);
        assertThat(testQrtzScheduleJob.getIsConcurrent()).isEqualTo(DEFAULT_IS_CONCURRENT);
        assertThat(testQrtzScheduleJob.getBatchStatus()).isEqualTo(DEFAULT_BATCH_STATUS);
        assertThat(testQrtzScheduleJob.getBatchTime()).isEqualTo(DEFAULT_BATCH_TIME);
        assertThat(testQrtzScheduleJob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testQrtzScheduleJob.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testQrtzScheduleJob.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testQrtzScheduleJob.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testQrtzScheduleJob.getCreateBy()).isEqualTo(DEFAULT_CREATE_BY);
        assertThat(testQrtzScheduleJob.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testQrtzScheduleJob.getUpdateBy()).isEqualTo(DEFAULT_UPDATE_BY);

        // Validate the QrtzScheduleJob in ElasticSearch
        QrtzScheduleJob qrtzScheduleJobEs = qrtzScheduleJobSearchRepository.findOne(testQrtzScheduleJob.getId());
        assertThat(qrtzScheduleJobEs).isEqualToComparingFieldByField(testQrtzScheduleJob);
    }

    @Test
    @Transactional
    public void checkJobNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setJobName(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkJobGroupIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setJobGroup(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTriggerTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setTriggerType(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCronExpressionIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setCronExpression(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBeanClassIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setBeanClass(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMethodNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setMethodName(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIsConcurrentIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setIsConcurrent(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBatchStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setBatchStatus(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = qrtzScheduleJobRepository.findAll().size();
        // set the field null
        qrtzScheduleJob.setDescription(null);

        // Create the QrtzScheduleJob, which fails.
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(qrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(post("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isBadRequest());

        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQrtzScheduleJobs() throws Exception {
        // Initialize the database
        qrtzScheduleJobRepository.saveAndFlush(qrtzScheduleJob);

        // Get all the qrtzScheduleJobs
        restQrtzScheduleJobMockMvc.perform(get("/api/qrtz-schedule-jobs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(qrtzScheduleJob.getId().intValue())))
                .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
                .andExpect(jsonPath("$.[*].jobGroup").value(hasItem(DEFAULT_JOB_GROUP.toString())))
                .andExpect(jsonPath("$.[*].triggerType").value(hasItem(DEFAULT_TRIGGER_TYPE.toString())))
                .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION.toString())))
                .andExpect(jsonPath("$.[*].beanClass").value(hasItem(DEFAULT_BEAN_CLASS.toString())))
                .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME.toString())))
                .andExpect(jsonPath("$.[*].parameterJson").value(hasItem(DEFAULT_PARAMETER_JSON.toString())))
                .andExpect(jsonPath("$.[*].isConcurrent").value(hasItem(DEFAULT_IS_CONCURRENT.toString())))
                .andExpect(jsonPath("$.[*].batchStatus").value(hasItem(DEFAULT_BATCH_STATUS.toString())))
                .andExpect(jsonPath("$.[*].batchTime").value(hasItem(DEFAULT_BATCH_TIME_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
                .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
                .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME_STR)))
                .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())));
    }

    @Test
    @Transactional
    public void getQrtzScheduleJob() throws Exception {
        // Initialize the database
        qrtzScheduleJobRepository.saveAndFlush(qrtzScheduleJob);

        // Get the qrtzScheduleJob
        restQrtzScheduleJobMockMvc.perform(get("/api/qrtz-schedule-jobs/{id}", qrtzScheduleJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qrtzScheduleJob.getId().intValue()))
            .andExpect(jsonPath("$.jobName").value(DEFAULT_JOB_NAME.toString()))
            .andExpect(jsonPath("$.jobGroup").value(DEFAULT_JOB_GROUP.toString()))
            .andExpect(jsonPath("$.triggerType").value(DEFAULT_TRIGGER_TYPE.toString()))
            .andExpect(jsonPath("$.cronExpression").value(DEFAULT_CRON_EXPRESSION.toString()))
            .andExpect(jsonPath("$.beanClass").value(DEFAULT_BEAN_CLASS.toString()))
            .andExpect(jsonPath("$.methodName").value(DEFAULT_METHOD_NAME.toString()))
            .andExpect(jsonPath("$.parameterJson").value(DEFAULT_PARAMETER_JSON.toString()))
            .andExpect(jsonPath("$.isConcurrent").value(DEFAULT_IS_CONCURRENT.toString()))
            .andExpect(jsonPath("$.batchStatus").value(DEFAULT_BATCH_STATUS.toString()))
            .andExpect(jsonPath("$.batchTime").value(DEFAULT_BATCH_TIME_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE_STR))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME_STR))
            .andExpect(jsonPath("$.createBy").value(DEFAULT_CREATE_BY.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME_STR))
            .andExpect(jsonPath("$.updateBy").value(DEFAULT_UPDATE_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQrtzScheduleJob() throws Exception {
        // Get the qrtzScheduleJob
        restQrtzScheduleJobMockMvc.perform(get("/api/qrtz-schedule-jobs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQrtzScheduleJob() throws Exception {
        // Initialize the database
        qrtzScheduleJobRepository.saveAndFlush(qrtzScheduleJob);
        qrtzScheduleJobSearchRepository.save(qrtzScheduleJob);
        int databaseSizeBeforeUpdate = qrtzScheduleJobRepository.findAll().size();

        // Update the qrtzScheduleJob
        QrtzScheduleJob updatedQrtzScheduleJob = qrtzScheduleJobRepository.findOne(qrtzScheduleJob.getId());
        updatedQrtzScheduleJob
                .jobName(UPDATED_JOB_NAME)
                .jobGroup(UPDATED_JOB_GROUP)
                .triggerType(UPDATED_TRIGGER_TYPE)
                .cronExpression(UPDATED_CRON_EXPRESSION)
                .beanClass(UPDATED_BEAN_CLASS)
                .methodName(UPDATED_METHOD_NAME)
                .parameterJson(UPDATED_PARAMETER_JSON)
                .isConcurrent(UPDATED_IS_CONCURRENT)
                .batchStatus(UPDATED_BATCH_STATUS)
                .batchTime(UPDATED_BATCH_TIME)
                .description(UPDATED_DESCRIPTION)
                .startDate(UPDATED_START_DATE)
                .endDate(UPDATED_END_DATE)
                .createTime(UPDATED_CREATE_TIME)
                .createBy(UPDATED_CREATE_BY)
                .updateTime(UPDATED_UPDATE_TIME)
                .updateBy(UPDATED_UPDATE_BY);
        QrtzScheduleJobDTO qrtzScheduleJobDTO = qrtzScheduleJobMapper.qrtzScheduleJobToQrtzScheduleJobDTO(updatedQrtzScheduleJob);

        restQrtzScheduleJobMockMvc.perform(put("/api/qrtz-schedule-jobs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(qrtzScheduleJobDTO)))
                .andExpect(status().isOk());

        // Validate the QrtzScheduleJob in the database
        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeUpdate);
        QrtzScheduleJob testQrtzScheduleJob = qrtzScheduleJobs.get(qrtzScheduleJobs.size() - 1);
        assertThat(testQrtzScheduleJob.getJobName()).isEqualTo(UPDATED_JOB_NAME);
        assertThat(testQrtzScheduleJob.getJobGroup()).isEqualTo(UPDATED_JOB_GROUP);
        assertThat(testQrtzScheduleJob.getTriggerType()).isEqualTo(UPDATED_TRIGGER_TYPE);
        assertThat(testQrtzScheduleJob.getCronExpression()).isEqualTo(UPDATED_CRON_EXPRESSION);
        assertThat(testQrtzScheduleJob.getBeanClass()).isEqualTo(UPDATED_BEAN_CLASS);
        assertThat(testQrtzScheduleJob.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
        assertThat(testQrtzScheduleJob.getParameterJson()).isEqualTo(UPDATED_PARAMETER_JSON);
        assertThat(testQrtzScheduleJob.getIsConcurrent()).isEqualTo(UPDATED_IS_CONCURRENT);
        assertThat(testQrtzScheduleJob.getBatchStatus()).isEqualTo(UPDATED_BATCH_STATUS);
        assertThat(testQrtzScheduleJob.getBatchTime()).isEqualTo(UPDATED_BATCH_TIME);
        assertThat(testQrtzScheduleJob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testQrtzScheduleJob.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testQrtzScheduleJob.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testQrtzScheduleJob.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testQrtzScheduleJob.getCreateBy()).isEqualTo(UPDATED_CREATE_BY);
        assertThat(testQrtzScheduleJob.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testQrtzScheduleJob.getUpdateBy()).isEqualTo(UPDATED_UPDATE_BY);

        // Validate the QrtzScheduleJob in ElasticSearch
        QrtzScheduleJob qrtzScheduleJobEs = qrtzScheduleJobSearchRepository.findOne(testQrtzScheduleJob.getId());
        assertThat(qrtzScheduleJobEs).isEqualToComparingFieldByField(testQrtzScheduleJob);
    }

    @Test
    @Transactional
    public void deleteQrtzScheduleJob() throws Exception {
        // Initialize the database
        qrtzScheduleJobRepository.saveAndFlush(qrtzScheduleJob);
        qrtzScheduleJobSearchRepository.save(qrtzScheduleJob);
        int databaseSizeBeforeDelete = qrtzScheduleJobRepository.findAll().size();

        // Get the qrtzScheduleJob
        restQrtzScheduleJobMockMvc.perform(delete("/api/qrtz-schedule-jobs/{id}", qrtzScheduleJob.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean qrtzScheduleJobExistsInEs = qrtzScheduleJobSearchRepository.exists(qrtzScheduleJob.getId());
        assertThat(qrtzScheduleJobExistsInEs).isFalse();

        // Validate the database is empty
        List<QrtzScheduleJob> qrtzScheduleJobs = qrtzScheduleJobRepository.findAll();
        assertThat(qrtzScheduleJobs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchQrtzScheduleJob() throws Exception {
        // Initialize the database
        qrtzScheduleJobRepository.saveAndFlush(qrtzScheduleJob);
        qrtzScheduleJobSearchRepository.save(qrtzScheduleJob);

        // Search the qrtzScheduleJob
        restQrtzScheduleJobMockMvc.perform(get("/api/_search/qrtz-schedule-jobs?query=id:" + qrtzScheduleJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qrtzScheduleJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobName").value(hasItem(DEFAULT_JOB_NAME.toString())))
            .andExpect(jsonPath("$.[*].jobGroup").value(hasItem(DEFAULT_JOB_GROUP.toString())))
            .andExpect(jsonPath("$.[*].triggerType").value(hasItem(DEFAULT_TRIGGER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cronExpression").value(hasItem(DEFAULT_CRON_EXPRESSION.toString())))
            .andExpect(jsonPath("$.[*].beanClass").value(hasItem(DEFAULT_BEAN_CLASS.toString())))
            .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME.toString())))
            .andExpect(jsonPath("$.[*].parameterJson").value(hasItem(DEFAULT_PARAMETER_JSON.toString())))
            .andExpect(jsonPath("$.[*].isConcurrent").value(hasItem(DEFAULT_IS_CONCURRENT.toString())))
            .andExpect(jsonPath("$.[*].batchStatus").value(hasItem(DEFAULT_BATCH_STATUS.toString())))
            .andExpect(jsonPath("$.[*].batchTime").value(hasItem(DEFAULT_BATCH_TIME_STR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE_STR)))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME_STR)))
            .andExpect(jsonPath("$.[*].createBy").value(hasItem(DEFAULT_CREATE_BY.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME_STR)))
            .andExpect(jsonPath("$.[*].updateBy").value(hasItem(DEFAULT_UPDATE_BY.toString())));
    }
}
