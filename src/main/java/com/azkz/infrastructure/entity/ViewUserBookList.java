package com.azkz.infrastructure.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

/**
 * The persistent class for the view_user_book_list database table.
 *
 */
@Entity
@Data
@Table(name = "view_user_book_list")
public class ViewUserBookList implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "tub_id")
	private long tubId;

	private String author;

	@Column(name = "status_name")
	private String statusName;

	@Column(name = "status_code")
	private char statusCode;

	@Temporal(TemporalType.DATE)
	@Column(name = "regist_date")
	private Date registDate;

	private String title;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "isbn_code")
	private String isbnCode;

	@Column(name = "image_path")
	private String imagePath;

	@Column(name = "user_name")
	private String userName;

	public ViewUserBookList() {
	}

}
