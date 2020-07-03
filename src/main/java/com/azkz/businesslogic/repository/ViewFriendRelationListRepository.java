package com.azkz.businesslogic.repository;

import java.util.List;

import com.azkz.infrastructure.entity.ViewFriendRelationList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewFriendRelationListRepository extends JpaRepository<ViewFriendRelationList, Long> {

	public ViewFriendRelationList findByRequestUserIdAndAcceptanceUserIdAndStatusCode(long requestUserId,
			long acceptanceUserId, char statusCode);

	public List<ViewFriendRelationList> findByRequestUserIdAndStatusCode(long requestUserId, char statusCode);

	public List<ViewFriendRelationList> findByAcceptanceUserIdAndStatusCode(long acceptanceUserId, char statusCode);

	public int countByAcceptanceUserIdAndStatusCode(long acceptanceUserId, char statusCode);

}
