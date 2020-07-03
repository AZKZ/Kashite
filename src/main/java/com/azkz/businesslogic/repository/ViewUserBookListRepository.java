package com.azkz.businesslogic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azkz.infrastructure.entity.ViewUserBookList;

@Repository
public interface ViewUserBookListRepository extends JpaRepository<ViewUserBookList, Long> {

	public List<ViewUserBookList> findByUserIdAndStatusCode(long userId, char statusCode);

	public List<ViewUserBookList> findByUserId(long userId);

}
