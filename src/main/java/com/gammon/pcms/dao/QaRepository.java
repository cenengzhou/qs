package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gammon.pcms.model.Qa;

public interface QaRepository extends JpaRepository<Qa, Long>{
	List<Qa> findByIdTableAndNameTableAndFieldLike(Long idTable, String nameTable, String field);
}
