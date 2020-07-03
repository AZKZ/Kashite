package com.azkz.application.resource;

import lombok.Data;

@Data
public class BookInfoForm {

	long id;
	String title;
	String author;
	String isbnCode;
	String imagePath;

}
