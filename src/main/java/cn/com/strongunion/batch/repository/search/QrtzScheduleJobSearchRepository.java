package cn.com.strongunion.batch.repository.search;

import cn.com.strongunion.batch.domain.QrtzScheduleJob;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the QrtzScheduleJob entity.
 */
public interface QrtzScheduleJobSearchRepository extends ElasticsearchRepository<QrtzScheduleJob, Long> {
}
