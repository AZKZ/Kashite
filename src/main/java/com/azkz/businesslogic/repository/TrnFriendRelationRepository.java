package com.azkz.businesslogic.repository;

import com.azkz.infrastructure.entity.TrnFriendRelation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrnFriendRelationRepository extends JpaRepository<TrnFriendRelation, Long> {

	public TrnFriendRelation findByRequestUserIdAndAcceptanceUserId(long requestUserId, long acceptanceUserId);

	public TrnFriendRelation findById(long id);

	public boolean existsByRequestUserIdAndAcceptanceUserIdAndStatus(long requestUserId, long acceptanceUserId,
			char status);
}
