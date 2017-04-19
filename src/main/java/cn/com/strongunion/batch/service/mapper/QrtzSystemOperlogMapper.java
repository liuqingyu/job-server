package cn.com.strongunion.batch.service.mapper;

import cn.com.strongunion.batch.domain.*;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity QrtzSystemOperlog and its DTO QrtzSystemOperlogDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QrtzSystemOperlogMapper {

    @Mapping(source = "job.id", target = "jobId")
    QrtzSystemOperlogDTO qrtzSystemOperlogToQrtzSystemOperlogDTO(QrtzSystemOperlog qrtzSystemOperlog);

    List<QrtzSystemOperlogDTO> qrtzSystemOperlogsToQrtzSystemOperlogDTOs(List<QrtzSystemOperlog> qrtzSystemOperlogs);

    @Mapping(source = "jobId", target = "job")
    QrtzSystemOperlog qrtzSystemOperlogDTOToQrtzSystemOperlog(QrtzSystemOperlogDTO qrtzSystemOperlogDTO);

    List<QrtzSystemOperlog> qrtzSystemOperlogDTOsToQrtzSystemOperlogs(List<QrtzSystemOperlogDTO> qrtzSystemOperlogDTOs);

    default QrtzScheduleJob qrtzScheduleJobFromId(Long id) {
        if (id == null) {
            return null;
        }
        QrtzScheduleJob qrtzScheduleJob = new QrtzScheduleJob();
        qrtzScheduleJob.setId(id);
        return qrtzScheduleJob;
    }
}
