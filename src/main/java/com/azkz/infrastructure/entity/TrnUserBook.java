package com.azkz.infrastructure.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The persistent class for the trn_user_book database table.
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "trn_user_book")
public class TrnUserBook extends BaseTableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "book_id")
	private long bookId;

	@Column(name = "user_id")
	private long userId;

	private char status;

	@Temporal(TemporalType.DATE)
	@Column(name = "regist_date")
	private Date registDate;

	public TrnUserBook(final long bookId, final long userId, final char status, final Date registDate,
			final String createdUser, final String createdProgram) {
		this.setBookId(bookId);
		this.setUserId(userId);
		this.setRegistDate(registDate);
		this.setStatus(status);
		this.setCreationDate(new Date());
		this.setCreatedUser(createdUser);
		this.setCreatedProgram(createdProgram);
	}

	public TrnUserBook() {
	}

}
