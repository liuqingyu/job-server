package cn.com.strongunion.batch.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.SessionListener;
import com.hazelcast.web.spring.SpringAwareWebFilter;
import cn.com.strongunion.batch.web.filter.CachingHttpHeadersFilter;

import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.*;
import org.springframework.boot.context.embedded.tomcat.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import javax.inject.Inject;
import javax.servlet.*;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Inject
    private Environment env;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Autowired(required = false)
    private MetricRegistry metricRegistry;

    // Hazelcast instance is injected to force its initialization before the Servlet filter uses it.
    @Inject
    private HazelcastInstance hazelcastInstance;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        }
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initClusteredHttpSessionFilter(servletContext, disps);
        initMetrics(servletContext, disps);
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            initCachingHttpHeadersFilter(servletContext, disps);
        }
        log.info("Web application fully configured");
    }

    /**
     * Initializes the Clustered Http Session filter
     */
    private void initClusteredHttpSessionFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering Clustered Http Session Filter");
        servletContext.addListener(new SessionListener());

        FilterRegistration.Dynamic hazelcastWebFilter = servletContext.addFilter("hazelcastWebFilter", new SpringAwareWebFilter());
        Map<String, String> parameters = new HashMap<>();
        parameters.put("instance-name", "jobserver");
        // Name of the distributed map storing your web session objects
        parameters.put("map-name", "clustered-http-sessions");

        // How is your load-balancer configured?
        // Setting "sticky-session" to "true" means all requests of a session
        // are routed to the node where the session is first created.
        // This is excellent for performance.
        // If "sticky-session" is set to "false", then when a session is updated
        // on a node, entries for this session on all other nodes are invalidated.
        // You have to know how your load-balancer is configured before
        // setting this parameter. Default is true.
        parameters.put("sticky-session", "true");

        // Name of session id cookie
        parameters.put("cookie-name", "hazelcast.sessionId");

        // Are you debugging? Default is false.
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            parameters.put("debug", "false");
        } else {
            parameters.put("debug", "true");
        }

        // Do you want to shutdown HazelcastInstance during
        // web application undeploy process?
        // Default is true.
        parameters.put("shutdown-on-destroy", "true");

        hazelcastWebFilter.setInitParameters(parameters);
        hazelcastWebFilter.addMappingForUrlPatterns(disps, true, "/*");
        hazelcastWebFilter.setAsyncSupported(true);
    }

    /**
     * Customize the Tomcat engine: Mime types, the document root, the cache.
     */
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        // IE issue, see https://github.com/jhipster/generator-jhipster/pull/711
        mappings.add("html", "text/html;charset=utf-8");
        // CloudFoundry issue, see https://github.com/cloudfoundry/gorouter/issues/64
        mappings.add("json", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);
        customizeTomcat(container);
        // When running in an IDE or with ./mvnw spring-boot:run, set location of the static web assets.
        setLocationForStaticAssets(container);
    }

    /**
     * Customize Tomcat configuration.
     */
    private void customizeTomcat(ConfigurableEmbeddedServletContainer container) {
        if (container instanceof TomcatEmbeddedServletContainerFactory) {
            TomcatEmbeddedServletContainerFactory tomcatFactory = (TomcatEmbeddedServletContainerFactory) container;
            tomcatFactory.addContextCustomizers((TomcatContextCustomizer) context -> {
                // See https://github.com/jhipster/generator-jhipster/issues/3995
                StandardRoot resources = new StandardRoot();
                resources.setCacheMaxSize(40960);
                resources.setCacheObjectMaxSize(2048);
                context.setResources(resources);
            });
        }
    }

    private void setLocationForStaticAssets(ConfigurableEmbeddedServletContainer container) {
        File root;
        String prefixPath = resolvePathPrefix();
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            root = new File(prefixPath + "target/www/");
        } else {
            root = new File(prefixPath + "src/main/webapp/");
        }
        if (root.exists() && root.isDirectory()) {
            container.setDocumentRoot(root);
        }
    }

    /**
     *  Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath = this.getClass().getResource("").getPath();
        String rootPath = Paths.get(".").toUri().normalize().getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("target/");
        if(extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    /**
     * Initializes the caching HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext,
                                              EnumSet<DispatcherType> disps) {
        log.debug("Registering Caching HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
            servletContext.addFilter("cachingHttpHeadersFilter",
                new CachingHttpHeadersFilter(jHipsterProperties));

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/content/*");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "/app/*");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * Initializes Metrics.
     */
    private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Initializing Metrics registries");
        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
            metricRegistry);
        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
            metricRegistry);

        log.debug("Registering Metrics Filter");
        FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
            new InstrumentedFilter());

        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
        metricsFilter.setAsyncSupported(true);

        log.debug("Registering Metrics Servlet");
        ServletRegistration.Dynamic metricsAdminServlet =
            servletContext.addServlet("metricsServlet", new MetricsServlet());

        metricsAdminServlet.addMapping("/management/jhipster/metrics/*");
        metricsAdminServlet.setAsyncSupported(true);
        metricsAdminServlet.setLoadOnStartup(2);
    }

    @Bean
    @ConditionalOnProperty(name = "jhipster.cors.allowed-origins")
    public CorsFilter corsFilter() {
        log.debug("Registering CORS filter");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = jHipsterProperties.getCors();
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/v2/api-docs", config);
        source.registerCorsConfiguration("/oauth/**", config);
        return new CorsFilter(source);
    }
}
