package com.azkz.businesslogic.repository;

import java.util.List;

import com.azkz.infrastructure.entity.ViewUserFriendList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewUserFriendListRepository extends JpaRepository<ViewUserFriendList, Long> {

	public List<ViewUserFriendList> findByUserId(long userId);

	public boolean existsByUserIdAndFriendId(long userId, long friendId);

}
