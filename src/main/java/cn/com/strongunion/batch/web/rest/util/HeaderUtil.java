package cn.com.strongunion.batch.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public class HeaderUtil {

    private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-jobserverApp-alert", message);
        headers.add("X-jobserverApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".created", param);
    }

    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".deleted", param);
    }

    public static HttpHeaders createEntityPauseAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".paused", param);
    }

    public static HttpHeaders createEntityResumeAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".resumed", param);
    }

    public static HttpHeaders createEntityRunAlert(String entityName, String param) {
        return createAlert("jobserverApp." + entityName + ".runned", param);
    }

    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        log.error("Entity creation failed, {}", defaultMessage);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-jobserverApp-error", "error." + errorKey);
        headers.add("X-jobserverApp-params", entityName);
        return headers;
    }
}
