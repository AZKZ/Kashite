package com.azkz.infrastructure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the mst_tag database table.
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "trn_friend_relation")
public class TrnFriendRelation extends BaseTableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "request_user_id")
	private long requestUserId;

	@Column(name = "acceptance_user_id")
	private long acceptanceUserId;

	private char status;

	public TrnFriendRelation() {
	}

	public TrnFriendRelation(final long requestUserId, final long acceptanceUserId, char status, final String createdUser,
			final String createdProgram) {
		this.setRequestUserId(requestUserId);
		this.setAcceptanceUserId(acceptanceUserId);
		this.setStatus(status);
		this.setCreationDate(new Date());
		this.setCreatedUser(createdUser);
		this.setCreatedProgram(createdProgram);
	}

}
