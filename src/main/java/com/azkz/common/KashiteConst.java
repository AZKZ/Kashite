package com.azkz.common;

public class KashiteConst {

  /** フレンドリレーションステータス 未申請 */
  public static final char FRIEND_RELATION_STATUS_NY_REQUEST = '1';

  /** フレンドリレーションステータス 承認待ち */
  public static final char FRIEND_RELATION_STATUS_NY_ACCEPT = '2';

  /** フレンドリレーションステータス 承認済み */
  public static final char FRIEND_RELATION_STATUS_ACCEPTED = '3';

  /** フレンドリレーションステータス 保留 */
  public static final char FRIEND_RELATION_STATUS_ON_HOLD = '4';

  /** フレンドリレーションステータス 申請キャンセル */
  public static final char FRIEND_RELATION_STATUS_REQUEST_CANCEL = '5';

  /** フレンドリレーションステータス 拒否 */
  public static final char FRIEND_RELATION_STATUS_REFUSE = '6';

  /** 有効フラグ 有効 */
  public static final char ENABLE_FLG_ENABLE = '1';

  /** 有効フラグ 無効 */
  public static final char ENABLE_FLG_DISABLE = '0';
}
