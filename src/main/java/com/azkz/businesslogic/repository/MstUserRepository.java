package com.azkz.businesslogic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.azkz.infrastructure.entity.MstUser;

@Repository
public interface MstUserRepository extends JpaRepository<MstUser, Long> {

	public boolean existsBySubjectAndIssuer(String subject, String issuer);

	public MstUser findBySubjectAndIssuer(String subject, String issuer);
}
