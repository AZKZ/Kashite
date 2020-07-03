package com.azkz.businesslogic.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.azkz.application.resource.BookInfoForm;
import com.azkz.application.resource.FriendBookDTO;
import com.azkz.businesslogic.repository.MstBookRepository;
import com.azkz.businesslogic.repository.TrnUserBookRepository;
import com.azkz.businesslogic.repository.ViewUserBookListRepository;
import com.azkz.common.ModifyModeEnum;
import com.azkz.infrastructure.entity.MstBook;
import com.azkz.infrastructure.entity.TrnUserBook;
import com.azkz.infrastructure.entity.ViewUserBookList;
import com.azkz.infrastructure.entity.ViewUserFriendList;

@Service
public class BookMngService {

	private final MstBookRepository mstBookRepository;
	private final TrnUserBookRepository trnUserBookRepository;
	private final ViewUserBookListRepository viewUserBookListRepository;

	public BookMngService(MstBookRepository mstBookRepository, TrnUserBookRepository trnUserBookRepository,
			ViewUserBookListRepository viewUserBookListRepository) {
		this.mstBookRepository = mstBookRepository;
		this.trnUserBookRepository = trnUserBookRepository;
		this.viewUserBookListRepository = viewUserBookListRepository;
	}

	public boolean add(BookInfoForm bookInfoForm, long userId) {

		MstBook mstBook = mstBookRepository.findByIsbnCodeOrTitle(bookInfoForm.getIsbnCode(), bookInfoForm.getTitle());

		if (mstBook == null) {
			addMst(bookInfoForm);
			mstBook = mstBookRepository.findByIsbnCodeOrTitle(bookInfoForm.getIsbnCode(), bookInfoForm.getTitle());
		}

		addUserBook(mstBook, userId);

		return false;
	}

	public boolean addMst(BookInfoForm bookInfoForm) {

		mstBookRepository.saveAndFlush(new MstBook(bookInfoForm.getAuthor(), bookInfoForm.getIsbnCode(),
				bookInfoForm.getTitle(), null, null, bookInfoForm.getImagePath(), "azegami", "addbook"));

		return false;
	}

	public boolean addUserBook(MstBook mstBook, long userId) {

		trnUserBookRepository.saveAndFlush(new TrnUserBook(mstBook.getId(), userId, '1', new Date(), "azegami", "addbook"));

		return false;
	}

	public List<ViewUserBookList> getUserBookList(long userId) {

		// List<ViewUserBookList> list =
		// viewUserBookListRepository.findByUserIdAndStatusCode(userId, '1');
		List<ViewUserBookList> list = viewUserBookListRepository.findByUserId(userId);

		if (list == null) {
			// TODO 0件時の処理
		}

		return list;

	}

	public int modifyUserBook(List<Long> tubIdList, long userId, ModifyModeEnum modifyModeEnum, char statusCode) {
		int modifyCnt = 0;

		for (long tubid : tubIdList) {

			TrnUserBook trnUserBook = trnUserBookRepository.findById(tubid);

			// レコードのユーザーIDと引数のユーザーIDが一致した場合は更新
			// 他人の書籍を削除・更新できないようにする
			if (trnUserBook.getUserId() == userId) {

				switch (modifyModeEnum) {

					case DELETE:
						trnUserBookRepository.deleteById(tubid);
						modifyCnt++;
						break;

					case UPDATE:
						trnUserBook.setStatus(statusCode);
						trnUserBookRepository.save(trnUserBook);
						modifyCnt++;
						break;

				}

			}

		}

		return modifyCnt;
	}

	/**
	 * フレンドの書籍一覧を取得する
	 *
	 * @param viewFriendRelationList
	 * @return
	 */
	public List<FriendBookDTO> getFriendBookList(List<ViewUserFriendList> viewUserFriendList) {

		// 初期化
		List<FriendBookDTO> friendBookDTOList = new ArrayList<>();
		List<ViewUserBookList> viewUserBookList = null;

		// フレンドの数だけ繰り返し
		for (ViewUserFriendList friend : viewUserFriendList) {
			// フレンドの書籍一覧を取得
			viewUserBookList = viewUserBookListRepository.findByUserId(friend.getFriendId());

			// DTOのインスタンスを作成し、DTOリストに追加する
			FriendBookDTO friendBookDTO = new FriendBookDTO(friend.getFriendId(), friend.getFriendName(), viewUserBookList);
			friendBookDTOList.add(friendBookDTO);
		}

		return friendBookDTOList;
	}

}
