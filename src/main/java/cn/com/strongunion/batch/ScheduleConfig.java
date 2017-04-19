package cn.com.strongunion.batch;

import org.elasticsearch.common.Strings;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @Copyright: Copyright (c) 2016,${year},
 * @Description: ${todo}
 */

@Configuration
@PropertySource("classpath:quartz.properties")
public class ScheduleConfig implements EnvironmentAware {

/*    *//**
     * config Scheduler
     * @return
     *//*
    @Bean(name = "uniScheduler")
    public Scheduler getScheduler() {
        Scheduler scheduler = null;

        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return scheduler;
    }*/

    static final Logger LOG = LoggerFactory.getLogger(ScheduleConfig.class);

    private RelaxedPropertyResolver propertyResolver;

    private Environment env;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        String schedulerName = propertyResolver.getProperty("scheduler.instanceName");
        if(Strings.isNullOrEmpty(schedulerName)){
            throw new ApplicationContextException("Scheduler name needs to be specified as a property org.quartz.scheduler.name");
        }
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(transactionManager);
        schedulerFactoryBean.setOverwriteExistingJobs(true);

        schedulerFactoryBean.setSchedulerName(schedulerName);

        // Custom job factory of spring with DI support for @Autowired
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);

        return schedulerFactoryBean;
    }

    @Bean
    public Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
        Properties properties;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
            LOG.info("Value of the property org.quartz.jobStore.driverDelegateClass {}", properties.getProperty("org.quartz.jobStore.driverDelegateClass"));
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to load quartz.properties", e);
        }
        return properties;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        this.propertyResolver = new RelaxedPropertyResolver(env, "org.quartz.");
    }
}
