package com.gammon.pcms.dao;

import java.util.List;

import com.gammon.pcms.dto.IRocDetailJasperWrapper;
import com.gammon.pcms.dto.RocAndRocDetailWrapper;
import com.gammon.pcms.dto.RocAndRocSubdetailWrapper;
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

	@Query("select a from ROC a where a.projectNo = :projectNo and a.systemStatus='ACTIVE' and a.status='Live'")
	List<ROC> findLiveRocListByJobNo(@Param("projectNo") String projectNo);

	@Query("select distinct a.projectNo from ROC a where a.systemStatus='ACTIVE' and a.status='Live'")
	List<String> findAllJobNumber();

	ROC findByProjectNoAndRocCategoryAndDescriptionAndSystemStatus(String projectNo, String rocCategory, String description, String systemStatus);

	ROC findTopByProjectNoOrderByItemNoDesc(String projectNo);

	@NotFound(action = NotFoundAction.IGNORE)
	@Query(
		value = "select " +
		"d.AMOUNT_BEST as amountBest, " +
		"d.AMOUNT_BEST as amountBestMovement, " +
		"d.AMOUNT_EXPECTED as amountRealistic, " +
		"d.AMOUNT_EXPECTED as amountRealisticMovement, " +
		"d.AMOUNT_WORST as amountWorst, " +
		"d.AMOUNT_WORST as amountWorstMovement, " +
		"r.ROC_CAT as category, " +
		"r.DESCRIPTION as description, " +
		"r.IMPACT as impact, " +
		"r.ITEM_NO as itemNo, " +	
		"d.MONTH as month, " +
		"r.PROJECT_REF as projectRef, " +
		"d.REMARKS as remark, " +
		"d.ID_ROC as rocId, " +
		"d.year as year " +
		"from {h-schema}ROC r " + 
		"left join {h-schema}ROC_DETAIL d on d.ID_ROC = r.ID " +
		"where " + 
		"r.SYSTEM_STATUS='ACTIVE' and d.SYSTEM_STATUS='ACTIVE' and r.STATUS='Live' " +
		"and r.PROJECT_NO = :projectNo " +
		"and d.YEAR = :year " +
		"and d.MONTH = :month " +
		"order by r.ROC_CAT desc, r.ITEM_NO asc",
		nativeQuery = true)
	public List<IRocDetailJasperWrapper> getRocJasperWrapper(@Param("projectNo") String projectNo, @Param("year") int year, @Param("month") int month);
	 
	@NotFound(action = NotFoundAction.IGNORE)
	@Query(
		value = "select " +
		"d.AMOUNT_BEST as amountBest, " +
		"(d.AMOUNT_BEST - pd.AMOUNT_BEST) AS amountBestMovement, " +
		"d.AMOUNT_EXPECTED as amountRealistic, " +
		"(d.AMOUNT_EXPECTED - pd.AMOUNT_EXPECTED) AS amountRealisticMovement, " +
		"d.AMOUNT_WORST as amountWorst, " +
		"(d.AMOUNT_WORST - pd.AMOUNT_WORST) AS amountWorstMovement, " +
		"r.ROC_CAT as category, " +
		"r.DESCRIPTION as description, " +
		"r.IMPACT as impact, " +
		"r.ITEM_NO as itemNo, " +
		"d.MONTH as month, " +
		"r.PROJECT_REF as projectRef, " +
		"d.REMARKS as remark, " +
		"d.ID_ROC as rocId, " +
		"d.year as year " +
		"from {h-schema}ROC r " +
		"left join {h-schema}ROC_DETAIL d on d.ID_ROC = r.ID " +
		"LEFT JOIN  " +
		"(SELECT AMOUNT_BEST, AMOUNT_EXPECTED, AMOUNT_WORST, id_roc, YEAR, MONTH FROM {h-schema}ROC_DETAIL ) pd " +
		"ON pd.ID_ROC = r.id AND pd.YEAR = :prevMonthYear AND pd.MONTH = prevMonth " +
		"where " + 
		"r.SYSTEM_STATUS='ACTIVE' and r.STATUS='Live' " +
		"and r.PROJECT_NO = :projectNo " +
		"and d.YEAR = :year " +
		"and d.MONTH = :month " +
		"order by r.ROC_CAT desc, r.ITEM_NO asc",
		nativeQuery = true)
 	public List<IRocDetailJasperWrapper> getRocJasperWrapper(@Param("projectNo") String projectNo,  @Param("year") int year, @Param("month") int month, @Param("prevMonthYear") int prevMonthYear, @Param("prevMonth") int prevMonth);

	@Query("select new com.gammon.pcms.dto.RocAndRocSubdetailWrapper(a.itemNo, b.id, b.description, b.amountBest, b.amountExpected, b.amountWorst, b.year, b.month, b.hyperlink, b.remarks, b.systemStatus) from ROC a join a.rocSubdetails b " +
			"where a.projectNo=:projectNo and b.year=:year and b.month=:month " +
			"order by a.itemNo, b.createdDate desc")
	List<RocAndRocSubdetailWrapper> getRocSubdetailListAdmin(@Param("projectNo") String projectNo, @Param("year") int year, @Param("month") int month);

	@Query("select new com.gammon.pcms.dto.RocAndRocDetailWrapper(a.itemNo, b.id, b.year, b.month, b.amountBest, b.amountExpected, b.amountWorst, b.remarks, b.status, b.systemStatus) from ROC a join a.rocDetails b " +
			"where a.projectNo=:projectNo and b.year=:year and b.month=:month " +
			"order by a.itemNo, b.createdDate desc")
	List<RocAndRocDetailWrapper> getRocDetailListAdmin(@Param("projectNo") String projectNo, @Param("year") int year, @Param("month") int month);

	@Query("select a from ROC a where a.projectNo=:projectNo order by a.itemNo")
	List<ROC> getRocListAdmin(@Param("projectNo") String projectNo);
}
