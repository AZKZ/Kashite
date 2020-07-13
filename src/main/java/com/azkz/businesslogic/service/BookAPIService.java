package com.azkz.businesslogic.service;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.azkz.application.resource.BookInfoForm;

@Service
public class BookAPIService {

	private final RestTemplate restTemplate;

	// 国立国会図書館 WebAPI
	private static final String NDL_URL = "https://iss.ndl.go.jp/api/sru?operation=searchRetrieve&query=isbn={isbn}&recordSchema=dcndl_simple";

	// OpenBD WebAPI
	private static final String OPENBD_URL = "https://api.openbd.jp/v1/get?isbn={isbn}&pretty";

	@Autowired
	public BookAPIService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public BookInfoForm getBookInfo(String isbnCode) {

		BookInfoForm bookInfoForm = new BookInfoForm();

		if (StringUtils.isBlank(isbnCode)) {
			return bookInfoForm;
		}

		bookInfoForm = getOpenbdBookInfo(isbnCode, bookInfoForm);

		return bookInfoForm;
	}

	/**
	 * 国立国会図書館 APIから書籍情報を取得する
	 *
	 * @param isbnCode
	 * @param bookInfoForm
	 * @return bookInfoForm (取得できなかったら空)
	 */
	private BookInfoForm getNdlBookInfo(String isbnCode, BookInfoForm bookInfoForm) {
		try {
			String xml = restTemplate.getForObject(NDL_URL, String.class, isbnCode);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(new InputSource(new StringReader(xml)));

			String numberOfRecords = document.getElementsByTagName("numberOfRecords").item(0).getTextContent();

			if (numberOfRecords.equals("0")) {
				return bookInfoForm;
			}

			String title = document.getElementsByTagName("dc:title").item(0).getTextContent();
			String creator = document.getElementsByTagName("dc:creator").item(0).getTextContent();

			bookInfoForm.setTitle(title);
			bookInfoForm.setAuthor(creator);
			bookInfoForm.setIsbnCode(isbnCode);

		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			return bookInfoForm;
		}
		return bookInfoForm;
	}

	/**
	 * OpenBD APIから書籍情報を取得する
	 *
	 * @param isbnCode
	 * @param bookInfoForm
	 * @return bookInfoForm (取得できなかったら空)
	 */
	private BookInfoForm getOpenbdBookInfo(String isbnCode, BookInfoForm bookInfoForm) {

		if(StringUtils.isBlank(isbnCode) || bookInfoForm == null){
			throw new IllegalArgumentException("パラメータが不正です");
		}

		// オブジェクト配列としてOpenBDからデータを取得
		ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(OPENBD_URL, Object[].class, isbnCode);

		// レスポンスのbodyを取得
		Object[] books = responseEntity.getBody();

		// 1冊分の情報を取得
		LinkedHashMap book = (LinkedHashMap) books[0];

		// 書籍情報が取得できなければ、ISBNコードだけ入れて返す
		if (book == null) {
			bookInfoForm.setIsbnCode(isbnCode);
			return bookInfoForm;
		}

		// サマリー項目を取得
		LinkedHashMap summary = (LinkedHashMap) book.get("summary");

		bookInfoForm.setTitle(summary.get("title").toString());
		bookInfoForm.setAuthor(summary.get("author").toString());
		bookInfoForm.setIsbnCode(summary.get("isbn").toString());
		bookInfoForm.setImagePath(summary.get("cover").toString());

		return bookInfoForm;
	}

}
