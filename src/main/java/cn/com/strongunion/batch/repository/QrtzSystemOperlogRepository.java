package cn.com.strongunion.batch.repository;

import cn.com.strongunion.batch.domain.QrtzSystemOperlog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the QrtzSystemOperlog entity.
 */
@SuppressWarnings("unused")
public interface QrtzSystemOperlogRepository extends JpaRepository<QrtzSystemOperlog,Long> {

    void deleteByJobId(Long id);
}
