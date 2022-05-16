package com.gammon.pcms.dao;

import com.gammon.pcms.model.ConsultancyAgreement;
import com.gammon.qs.domain.Subcontract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultancyAgreementRepository extends JpaRepository<ConsultancyAgreement, Long>{

    @Query("SELECT c FROM ConsultancyAgreement c WHERE c.idSubcontract = :idSubcontract and c.systemStatus='ACTIVE'")
    ConsultancyAgreement findBySubcontractId(@Param("idSubcontract") Subcontract idSubcontract);
}
