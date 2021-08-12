package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocRepository extends JpaRepository<ROC, Long>{

	@Query("select a from ROC a where a.id = :id " )
	ROC getByID(@Param("id") Long id);

	@Query("select a from ROC a where a.projectNo = :projectNo")
	List<ROC> findByJobNo(@Param("projectNo") String projectNo);

	Long deleteByProjectNoAndId(String projectNo, Long id);


}
