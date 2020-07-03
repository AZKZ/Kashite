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
@Table(name = "view_user_friend_list")
public class ViewUserFriendList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tfr_id")
	private long tfrId;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "friend_id")
	private long friendId;

	@Column(name = "friend_name")
	private String friendName;

	public ViewUserFriendList() {
	}

}
