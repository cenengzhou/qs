package com.gammon.pcms.dao;

import java.util.List;

import com.gammon.pcms.dto.IRocDetailJasperWrapper;
import com.gammon.pcms.model.ROC;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RocRepository extends JpaRepository<ROC, Long>{

	@Query("select a from ROC a where a.id = :id " )
	ROC getByID(@Param("id") Long id);

	@Query("select a from ROC a where a.projectNo = :projectNo and a.systemStatus='ACTIVE' order by a.rocCategory desc, a.createdDate asc")
//	@Query("select a from ROC a where a.projectNo = :projectNo and a.systemStatus='ACTIVE' order by a.createdDate desc")
	List<ROC> findByJobNo(@Param("projectNo") String projectNo);

	ROC findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(String projectNo, String rocCategory, String description, String systemStatus);

	ROC findTopByProjectNoOrderByItemNoDesc(String projectNo);

	@NotFound(action = NotFoundAction.IGNORE)
	@Query(
		value = "select " +
		"d.AMOUNT_BEST as amountBest, " +
		"d.AMOUNT_EXPECTED as amountRealistic, " +
		"d.AMOUNT_WORST as amountWorst, " +
		"r.ROC_CAT as category, " +
		"r.DESCRIPTION as description, " +
		"r.PROJECT_REF as projectRef, " +
		"d.REMARKS as remark, " +
		"r.ITEM_NO as rocId " +	
		"from {h-schema}ROC r " + 
		"left join {h-schema}ROC_DETAIL d on d.ID_ROC = r.ID " +
		"where r.PROJECT_NO = :projectNo " +
		"and d.YEAR = :year " +
		"and d.MONTH = :month " +
		"order by r.ROC_CAT desc, r.ITEM_NO asc",
		nativeQuery = true)
 	public List<IRocDetailJasperWrapper> getRocJasperWrapper(@Param("projectNo") String projectNo,  @Param("year") int year, @Param("month") int month);
}
