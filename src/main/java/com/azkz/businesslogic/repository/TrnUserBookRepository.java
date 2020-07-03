package com.azkz.businesslogic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azkz.infrastructure.entity.TrnUserBook;

@Repository
public interface TrnUserBookRepository extends JpaRepository<TrnUserBook, Long> {

	public List<TrnUserBook> findByUserIdAndStatus(long userId, String status);

	public TrnUserBook findById(long id);
}
