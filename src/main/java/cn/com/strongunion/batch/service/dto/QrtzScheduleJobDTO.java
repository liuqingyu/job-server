package cn.com.strongunion.batch.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the QrtzScheduleJob entity.
 */
public class QrtzScheduleJobDTO implements Serializable {

    private Long id;

    @NotNull
    private String jobName;

    @NotNull
    private String jobGroup;

    @NotNull
    private String triggerType;

    @NotNull
    private String cronExpression;

    @NotNull
    private String beanClass;

    @NotNull
    private String methodName;

    private String parameterJson;

    @NotNull
    private String isConcurrent;

    private String batchStatus;

    private ZonedDateTime batchTime;

    @NotNull
    private String description;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private ZonedDateTime createTime;

    private String createBy;

    private ZonedDateTime updateTime;

    private String updateBy;

    private String jobStatus;

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }
    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }
    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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
    public String getParameterJson() {
        return parameterJson;
    }

    public void setParameterJson(String parameterJson) {
        this.parameterJson = parameterJson;
    }
    public String getIsConcurrent() {
        return isConcurrent;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }
    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }
    public ZonedDateTime getBatchTime() {
        return batchTime;
    }

    public void setBatchTime(ZonedDateTime batchTime) {
        this.batchTime = batchTime;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }
    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }
    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QrtzScheduleJobDTO qrtzScheduleJobDTO = (QrtzScheduleJobDTO) o;

        if ( ! Objects.equals(id, qrtzScheduleJobDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrtzScheduleJobDTO{" +
            "id=" + id +
            ", jobName='" + jobName + "'" +
            ", jobGroup='" + jobGroup + "'" +
            ", triggerType='" + triggerType + "'" +
            ", cronExpression='" + cronExpression + "'" +
            ", beanClass='" + beanClass + "'" +
            ", methodName='" + methodName + "'" +
            ", parameterJson='" + parameterJson + "'" +
            ", isConcurrent='" + isConcurrent + "'" +
            ", batchStatus='" + batchStatus + "'" +
            ", batchTime='" + batchTime + "'" +
            ", description='" + description + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            ", createTime='" + createTime + "'" +
            ", createBy='" + createBy + "'" +
            ", updateTime='" + updateTime + "'" +
            ", updateBy='" + updateBy + "'" +
            '}';
    }
}
