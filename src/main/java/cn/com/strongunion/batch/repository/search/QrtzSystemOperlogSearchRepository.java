package cn.com.strongunion.batch.repository.search;

import cn.com.strongunion.batch.domain.QrtzSystemOperlog;
import cn.com.strongunion.batch.service.dto.QrtzSystemOperlogDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the QrtzSystemOperlog entity.
 */
public interface QrtzSystemOperlogSearchRepository extends ElasticsearchRepository<QrtzSystemOperlog, Long> {
}
