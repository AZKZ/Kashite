package com.azkz.businesslogic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azkz.infrastructure.entity.MstBook;

@Repository
public interface MstBookRepository  extends JpaRepository<MstBook, Long>  {

	public MstBook findByIsbnCodeOrTitle(String isbnCode,String title);

}
