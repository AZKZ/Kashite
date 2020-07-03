package com.azkz.businesslogic.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.azkz.businesslogic.repository.MstUserRepository;
import com.azkz.businesslogic.repository.TrnFriendRelationRepository;
import com.azkz.businesslogic.repository.ViewFriendRelationListRepository;
import com.azkz.businesslogic.repository.ViewUserFriendListRepository;
import com.azkz.common.kashiteConst;
import com.azkz.infrastructure.entity.TrnFriendRelation;
import com.azkz.infrastructure.entity.ViewFriendRelationList;
import com.azkz.infrastructure.entity.ViewUserFriendList;

import org.springframework.stereotype.Service;

@Service
public class FriendService {

  private final TrnFriendRelationRepository trnFriendRelationRepository;
  private final MstUserRepository mstUserRepository;
  private final ViewFriendRelationListRepository viewFriendRelationListRepository;
  private final ViewUserFriendListRepository viewUserFriendListRepository;

  public FriendService(TrnFriendRelationRepository trnFriendRelationRepository, MstUserRepository mstUserRepository,
      ViewFriendRelationListRepository viewFriendRelationListRepository,
      ViewUserFriendListRepository viewUserFriendListRepository) {
    this.trnFriendRelationRepository = trnFriendRelationRepository;
    this.mstUserRepository = mstUserRepository;
    this.viewFriendRelationListRepository = viewFriendRelationListRepository;
    this.viewUserFriendListRepository = viewUserFriendListRepository;
  }

  /**
   * 承認済みのフレンドを取得する
   *
   * @param userId
   * @return
   */
  public List<ViewUserFriendList> getFriendList(long userId) {

    List<ViewUserFriendList> viewUserFriendList = viewUserFriendListRepository.findByUserId(userId);

    return viewUserFriendList;
  }

  /**
   * 承認対象のデータの件数を取得
   */
  public int getCountAcceptionTarget(long acceptanceUserId) {

    // 承認待ちとなっているデータの件数を取得
    // TODO 保留も含めるか検討
    return viewFriendRelationListRepository.countByAcceptanceUserIdAndStatusCode(acceptanceUserId,
        kashiteConst.FRIEND_RELATION_STATUS_NY_ACCEPT);
  }

  /**
   * 承認対象のフレンドリレーションデータを取得する
   *
   * @param acceptanceUserId
   * @return
   */
  public List<ViewFriendRelationList> getFriendAcceptList(long acceptanceUserId) {

    // ステータス「承認待ち」のデータ取得
    List<ViewFriendRelationList> nyAcceptList = viewFriendRelationListRepository
        .findByAcceptanceUserIdAndStatusCode(acceptanceUserId, kashiteConst.FRIEND_RELATION_STATUS_NY_ACCEPT);

    // ステータス「保留」のデータ取得
    List<ViewFriendRelationList> onHoldList = viewFriendRelationListRepository
        .findByAcceptanceUserIdAndStatusCode(acceptanceUserId, kashiteConst.FRIEND_RELATION_STATUS_ON_HOLD);

    // 2つのリストをマージ
    List<ViewFriendRelationList> viewFriendRelationList = new ArrayList<>();
    viewFriendRelationList.addAll(nyAcceptList);
    viewFriendRelationList.addAll(onHoldList);

    return viewFriendRelationList;
  }

  /**
   * フレンドリクエストのリンクを取得する
   *
   * @param request
   * @param userId
   * @return
   */
  public String getFriendRequestLink(HttpServletRequest request, long userId) {

    // リクエストURLのURI部分を変更して、ベースとなるURLを作成する
    String requestUrl = request.getRequestURL().toString();
    String baseUrl = requestUrl.replaceFirst(request.getRequestURI(), "/friend/request/");

    // 10桁で空白を0埋め
    String path = String.format("%010d", userId);

    // Base64形式でエンコード
    // 直にユーザーIDとかを見せないほうが良いと思うからエンコードしてる
    String encodedPath = Base64.getUrlEncoder().encodeToString(path.getBytes());

    return baseUrl + encodedPath;
  }

  /**
   * 未申請のフレンドリレーション取得する
   *
   * @param requestUserId           リクエストユーザID
   * @param encodedAcceptanceUserId エンコードされた承認ユーザID
   * @return ステータス「未申請」のフレンドリレーションデータ
   */
  public ViewFriendRelationList getNYRequestRelation(long requestUserId, String encodedAcceptanceUserId) {

    // エンコードされたユーザIDをデコードする
    String decodedPath = new String(Base64.getUrlDecoder().decode(encodedAcceptanceUserId));

    // 数値に変換
    long acceptanceUserId = Long.parseLong(decodedPath);

    // リクエスト側と同一ユーザーでないことを確認
    if (requestUserId == acceptanceUserId) {
      throw new IllegalArgumentException("リクエスト元とリクエスト先が同一ユーザー");
    }

    // acceptanceUserIdのユーザマスタ存在チェック
    if (!mstUserRepository.existsById(acceptanceUserId)) {
      throw new IllegalArgumentException("リクエスト先のユーザーがマスタに存在しません");
    }

    // 既にフレンドになっているかチェック
    if (viewUserFriendListRepository.existsByUserIdAndFriendId(requestUserId, acceptanceUserId)) {
      throw new IllegalArgumentException("既にフレンドになっています");
    }

    // 既存のフレンドリレーションデータの取得
    TrnFriendRelation trnFriendRelation = trnFriendRelationRepository
        .findByRequestUserIdAndAcceptanceUserId(requestUserId, acceptanceUserId);

    if (trnFriendRelation == null) {

      // 既存データがない場合はステータス「未申請」で新規作成
      trnFriendRelationRepository.saveAndFlush(new TrnFriendRelation(requestUserId, acceptanceUserId,
          kashiteConst.FRIEND_RELATION_STATUS_NY_REQUEST, String.valueOf(requestUserId), "createFriendRequest"));

    } else {

      // 既存レコードがあれば、ステータスを取得
      char status = trnFriendRelation.getStatus();

      // 「申請キャンセル」or 「拒否」の場合は
      if (kashiteConst.FRIEND_RELATION_STATUS_REQUEST_CANCEL == status
          || kashiteConst.FRIEND_RELATION_STATUS_REFUSE == status) {

        // ステータス「未申請」に更新
        trnFriendRelation.setStatus(kashiteConst.FRIEND_RELATION_STATUS_NY_REQUEST);
        trnFriendRelationRepository.saveAndFlush(trnFriendRelation);
      }

    }

    // 未申請のフレンドリレーションデータを取得
    ViewFriendRelationList viewFriendRelationList = viewFriendRelationListRepository
        .findByRequestUserIdAndAcceptanceUserIdAndStatusCode(requestUserId, acceptanceUserId,
            kashiteConst.FRIEND_RELATION_STATUS_NY_REQUEST);

    // ここで未申請のデータが取れない場合は既にリクエストしているのでエラーとする
    if (viewFriendRelationList == null) {
      throw new IllegalArgumentException("既にリクエストしています");
    }

    return viewFriendRelationList;
  }

  /**
   * フレンドリクエストをする（ステータスを承認待ちにする）
   *
   * @param tfrId         フレンドリレーションID
   * @param requestUserId リクエストユーザーID
   * @return 正常：true 異常：例外
   */
  public boolean requestFriend(long tfrId, long requestUserId) {

    // 対象のフレンドリレーションデータを取得
    TrnFriendRelation trnFriendRelation = trnFriendRelationRepository.findById(tfrId);

    // バリデーションに該当したら例外を発生
    if (trnFriendRelation == null) {
      throw new IllegalArgumentException("対象データなし");
    } else if (trnFriendRelation.getRequestUserId() != requestUserId) {
      throw new IllegalArgumentException("リクエストユーザの不一致");
    } else if (trnFriendRelation.getStatus() != kashiteConst.FRIEND_RELATION_STATUS_NY_REQUEST) {
      throw new IllegalArgumentException("対象データのステータスが不正");
    }

    // ステータス「承認待ち」に更新
    trnFriendRelation.setStatus(kashiteConst.FRIEND_RELATION_STATUS_NY_ACCEPT);
    trnFriendRelationRepository.saveAndFlush(trnFriendRelation);

    return true;

  }

  /**
   * フレンドリクエストに回答する（承認or拒否）
   *
   * @param tfrId            フレンドリレーションID
   * @param acceptanceUserId 承認ユーザーID
   * @return 正常：true 異常：例外
   */
  public boolean replyFriendRequest(long tfrId, long acceptanceUserId, char status) {

    // 対象のフレンドリレーションデータを取得
    TrnFriendRelation trnFriendRelation = trnFriendRelationRepository.findById(tfrId);

    // バリデーションに該当したら例外を発生
    // リレーションデータの存在チェック
    if (trnFriendRelation == null) {
      throw new IllegalArgumentException("対象データなし");

      // 承認ユーザーの本人チェック
    } else if (trnFriendRelation.getAcceptanceUserId() != acceptanceUserId) {
      throw new IllegalArgumentException("承認ユーザーの不一致");

      // リレーションデータのステータスチェック
    } else if (trnFriendRelation.getStatus() != kashiteConst.FRIEND_RELATION_STATUS_NY_ACCEPT) {
      throw new IllegalArgumentException("対象データのステータスが不正");

      // 引数のstatusの値チェック
    } else if (status != kashiteConst.FRIEND_RELATION_STATUS_ACCEPTED
        && status != kashiteConst.FRIEND_RELATION_STATUS_REFUSE) {
      throw new IllegalArgumentException("引数のステータスが不正");

      // 既にフレンドになっているかチェック
    } else if (viewUserFriendListRepository.existsByUserIdAndFriendId(acceptanceUserId,
        trnFriendRelation.getRequestUserId())) {
      throw new IllegalArgumentException("既にフレンドになっています");
    }

    // ステータス「承認済み」もしくは「拒否」に更新
    trnFriendRelation.setStatus(status);
    trnFriendRelationRepository.saveAndFlush(trnFriendRelation);

    // ステータス「承認済み」の場合
    // 申請と承認の関係が逆のリレーションデータを削除する
    if (status == kashiteConst.FRIEND_RELATION_STATUS_ACCEPTED) {

      // 逆関係のリレーションデータを取得
      TrnFriendRelation reverseRelation = trnFriendRelationRepository.findByRequestUserIdAndAcceptanceUserId(
          trnFriendRelation.getAcceptanceUserId(), trnFriendRelation.getRequestUserId());

      // NULLでなかったら削除
      if (reverseRelation != null) {
        deleteFriendRelationById(reverseRelation.getId());
      }
    }

    return true;

  }

  /**
   * ID指定でフレンドリレーションを削除する
   *
   * @param tfrId
   * @return
   */
  public boolean deleteFriendRelationById(long tfrId) {

    trnFriendRelationRepository.deleteById(tfrId);

    return true;
  }

}
