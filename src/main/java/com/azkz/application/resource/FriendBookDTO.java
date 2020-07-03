package com.azkz.application.resource;

import java.util.List;

import com.azkz.infrastructure.entity.ViewUserBookList;

import lombok.Data;

@Data
public class FriendBookDTO {

	long friendId;
	String friendName;
	List<ViewUserBookList> viewUserBookList;

	public FriendBookDTO(long friendId, String friendName, List<ViewUserBookList> viewUserBookList) {
		this.friendId = friendId;
		this.friendName = friendName;
		this.viewUserBookList = viewUserBookList;
	}

}
