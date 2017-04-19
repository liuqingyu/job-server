package cn.com.strongunion.batch.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the QrtzSystemOperlog entity.
 */
public class QrtzSystemOperlogDTO implements Serializable {

    private Long id;

    private String operParam;

    private String operResult;

    private String operType;

    private ZonedDateTime startTime;

    private ZonedDateTime endTime;

    private Long duration;

    private String success;

    private String triggerName;

    private String userId;

    private String userName;

    private String beanClass;

    private String methodName;


    private Long jobId;

    private String jobName;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOperParam() {
        return operParam;
    }

    public void setOperParam(String operParam) {
        this.operParam = operParam;
    }
    public String getOperResult() {
        return operResult;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }
    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }
    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long qrtzScheduleJobId) {
        this.jobId = qrtzScheduleJobId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QrtzSystemOperlogDTO qrtzSystemOperlogDTO = (QrtzSystemOperlogDTO) o;

        if ( ! Objects.equals(id, qrtzSystemOperlogDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrtzSystemOperlogDTO{" +
            "id=" + id +
            ", operParam='" + operParam + "'" +
            ", operResult='" + operResult + "'" +
            ", operType='" + operType + "'" +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", duration='" + duration + "'" +
            ", success='" + success + "'" +
            ", triggerName='" + triggerName + "'" +
            ", userId='" + userId + "'" +
            ", userName='" + userName + "'" +
            ", beanClass='" + beanClass + "'" +
            ", methodName='" + methodName + "'" +
            '}';
    }
}
