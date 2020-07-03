package com.azkz.infrastructure.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * The persistent class for the view_user_book_list database table.
 *
 */
@Entity
@Data
@Table(name = "view_friend_reletion_list")
public class ViewFriendRelationList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tfr_id")
	private long tfrId;

	@Column(name = "request_user_id")
	private long requestUserId;

	@Column(name = "request_user_name")
	private String requestUserName;

	@Column(name = "acceptance_user_id")
	private long acceptanceUserId;

	@Column(name = "acceptance_user_name")
	private String acceptanceUserName;

	@Column(name = "status_code")
	private char statusCode;

	@Column(name = "status_name")
	private String statusName;

	public ViewFriendRelationList() {
	}

}
