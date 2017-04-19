package cn.com.strongunion.batch.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A QrtzScheduleJob.
 */
@Entity
@Table(name = "qrtz_schedule_job")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "qrtzschedulejob")
public class QrtzScheduleJob implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "job_name", nullable = false)
    private String jobName;

    @NotNull
    @Column(name = "job_group", nullable = false)
    private String jobGroup;

    @NotNull
    @Column(name = "trigger_type", nullable = false)
    private String triggerType;

    @NotNull
    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @NotNull
    @Column(name = "bean_class", nullable = false)
    private String beanClass;

    @NotNull
    @Column(name = "method_name", nullable = false)
    private String methodName;

    @Column(name = "parameter_json")
    private String parameterJson;

    @NotNull
    @Column(name = "is_concurrent", nullable = false)
    private String isConcurrent;

    @Column(name = "batch_status")
    private String batchStatus;

    @Column(name = "batch_time")
    private ZonedDateTime batchTime;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Column(name = "create_time")
    private ZonedDateTime createTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_time")
    private ZonedDateTime updateTime;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "job_status")
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

    public QrtzScheduleJob jobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public QrtzScheduleJob jobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public QrtzScheduleJob triggerType(String triggerType) {
        this.triggerType = triggerType;
        return this;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public QrtzScheduleJob cronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public QrtzScheduleJob beanClass(String beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public QrtzScheduleJob methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameterJson() {
        return parameterJson;
    }

    public QrtzScheduleJob parameterJson(String parameterJson) {
        this.parameterJson = parameterJson;
        return this;
    }

    public void setParameterJson(String parameterJson) {
        this.parameterJson = parameterJson;
    }

    public String getIsConcurrent() {
        return isConcurrent;
    }

    public QrtzScheduleJob isConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
        return this;
    }

    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public QrtzScheduleJob batchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
        return this;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public ZonedDateTime getBatchTime() {
        return batchTime;
    }

    public QrtzScheduleJob batchTime(ZonedDateTime batchTime) {
        this.batchTime = batchTime;
        return this;
    }

    public void setBatchTime(ZonedDateTime batchTime) {
        this.batchTime = batchTime;
    }

    public String getDescription() {
        return description;
    }

    public QrtzScheduleJob description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public QrtzScheduleJob startDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public QrtzScheduleJob endDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public QrtzScheduleJob createTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public QrtzScheduleJob createBy(String createBy) {
        this.createBy = createBy;
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public QrtzScheduleJob updateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public QrtzScheduleJob updateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
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
        QrtzScheduleJob qrtzScheduleJob = (QrtzScheduleJob) o;
        if(qrtzScheduleJob.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, qrtzScheduleJob.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrtzScheduleJob{" +
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
