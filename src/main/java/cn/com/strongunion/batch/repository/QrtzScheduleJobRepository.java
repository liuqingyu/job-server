package cn.com.strongunion.batch.repository;

import cn.com.strongunion.batch.domain.QrtzScheduleJob;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the QrtzScheduleJob entity.
 */
@SuppressWarnings("unused")
public interface QrtzScheduleJobRepository extends JpaRepository<QrtzScheduleJob,Long> {
    List<QrtzScheduleJob> findAllByJobNameAndCronExpression(String jobName,String cronExpression);
}
