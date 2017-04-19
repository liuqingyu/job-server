package cn.com.strongunion.batch.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A QrtzSystemOperlog.
 */
@Entity
@Table(name = "qrtz_system_operlog")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "qrtzsystemoperlog")
public class QrtzSystemOperlog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "oper_param")
    private String operParam;

    @Column(name = "oper_result")
    private String operResult;

    @Column(name = "oper_type")
    private String operType;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime endTime;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "success")
    private String success;

    @Column(name = "trigger_name")
    private String triggerName;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "bean_class")
    private String beanClass;

    @Column(name = "method_name")
    private String methodName;

    @ManyToOne
    private QrtzScheduleJob job;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperParam() {
        return operParam;
    }

    public QrtzSystemOperlog operParam(String operParam) {
        this.operParam = operParam;
        return this;
    }

    public void setOperParam(String operParam) {
        this.operParam = operParam;
    }

    public String getOperResult() {
        return operResult;
    }

    public QrtzSystemOperlog operResult(String operResult) {
        this.operResult = operResult;
        return this;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public String getOperType() {
        return operType;
    }

    public QrtzSystemOperlog operType(String operType) {
        this.operType = operType;
        return this;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public QrtzSystemOperlog startTime(ZonedDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public QrtzSystemOperlog endTime(ZonedDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public QrtzSystemOperlog duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getSuccess() {
        return success;
    }

    public QrtzSystemOperlog success(String success) {
        this.success = success;
        return this;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public QrtzSystemOperlog triggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getUserId() {
        return userId;
    }

    public QrtzSystemOperlog userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public QrtzSystemOperlog userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBeanClass() {
        return beanClass;
    }

    public QrtzSystemOperlog beanClass(String beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public QrtzSystemOperlog methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public QrtzScheduleJob getJob() {
        return job;
    }

    public QrtzSystemOperlog job(QrtzScheduleJob qrtzScheduleJob) {
        this.job = qrtzScheduleJob;
        return this;
    }

    public void setJob(QrtzScheduleJob qrtzScheduleJob) {
        this.job = qrtzScheduleJob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzSystemOperlog qrtzSystemOperlog = (QrtzSystemOperlog) o;
        if(qrtzSystemOperlog.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, qrtzSystemOperlog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QrtzSystemOperlog{" +
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
