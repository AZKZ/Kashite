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
 * The persistent class for the mst_book database table.
 *
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "mst_book")
public class MstBook extends BaseTableEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String author;

	@Column(name = "isbn_code")
	private String isbnCode;

	private String title;

	@Column(name = "published_year")
	private String publishedYear;

	private Integer tags;

	@Column(name = "image_path")
	private String imagePath;

	public MstBook(String author, String isbnCode, String title, String publishedYear, Integer tags, String imagePath,
			String createdUser, String createdProgram) {
		this.setAuthor(author);
		;
		this.setIsbnCode(isbnCode);
		;
		this.setTitle(title);
		this.setPublishedYear(publishedYear);
		this.setTags(tags);
		this.setImagePath(imagePath);
		this.setCreationDate(new Date());
		this.setCreatedUser(createdUser);
		this.setCreatedProgram(createdProgram);
	}

	public MstBook() {

	}
}
