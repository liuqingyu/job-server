package cn.com.strongunion.batch.service.mapper;

import cn.com.strongunion.batch.domain.*;
import cn.com.strongunion.batch.service.dto.QrtzScheduleJobDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity QrtzScheduleJob and its DTO QrtzScheduleJobDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QrtzScheduleJobMapper {

    QrtzScheduleJobDTO qrtzScheduleJobToQrtzScheduleJobDTO(QrtzScheduleJob qrtzScheduleJob);

    List<QrtzScheduleJobDTO> qrtzScheduleJobsToQrtzScheduleJobDTOs(List<QrtzScheduleJob> qrtzScheduleJobs);

    QrtzScheduleJob qrtzScheduleJobDTOToQrtzScheduleJob(QrtzScheduleJobDTO qrtzScheduleJobDTO);

    List<QrtzScheduleJob> qrtzScheduleJobDTOsToQrtzScheduleJobs(List<QrtzScheduleJobDTO> qrtzScheduleJobDTOs);

}
