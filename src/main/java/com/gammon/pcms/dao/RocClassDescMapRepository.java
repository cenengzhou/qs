package com.gammon.pcms.dao;

import com.gammon.pcms.model.ROC_CLASS_DESC_MAP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RocClassDescMapRepository extends JpaRepository<ROC_CLASS_DESC_MAP, Long>{



}
