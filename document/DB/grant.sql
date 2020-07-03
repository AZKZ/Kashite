
--ロール作成
--CREATE ROLE kashite_r WITH LOGIN PASSWORD '*任意のパスワード*'; --読み取りのみ
--CREATE ROLE kashite_rw WITH LOGIN PASSWORD '*任意のパスワード*'; --読み書きのみ

--ロール間の継承
GRANT kashite_r TO kashite_rw;

--スキーマへのアクセス権限を設定
GRANT USAGE ON SCHEMA kashite TO kashite_r;
GRANT USAGE ON SCHEMA kashite TO kashite_rw;

--各テーブルのアクセス権限を設定
--GRANT SELECT ON テーブル名orシーケンス名 TO kashite_r;
--GRANT INSERT, UPDATE, DELETE ON テーブル名 TO kashite_rw;
--GRANT UPDATE, USAGE ON シーケンス TO kashite_rw;
 GRANT SELECT ON kashite.map_tag               TO kashite_r;
 GRANT SELECT ON kashite.map_tag_id_seq        TO kashite_r;
 GRANT SELECT ON kashite.mst_book              TO kashite_r;
 GRANT SELECT ON kashite.mst_book_id_seq       TO kashite_r;
 GRANT SELECT ON kashite.mst_tag               TO kashite_r;
 GRANT SELECT ON kashite.mst_tag_id_seq        TO kashite_r;
 GRANT SELECT ON kashite.mst_user              TO kashite_r;
 GRANT SELECT ON kashite.mst_user_id_seq       TO kashite_r;
 GRANT SELECT ON kashite.trn_user_book         TO kashite_r;
 GRANT SELECT ON kashite.trn_user_book_id_seq  TO kashite_r;
 GRANT SELECT ON kashite.view_tag_map_list     TO kashite_r;
 GRANT SELECT ON kashite.view_user_book_list   TO kashite_r;
 GRANT SELECT ON kashite.mst_user_book_status   TO kashite_r;
 GRANT SELECT ON kashite.mst_friend_relation_status   TO kashite_r;
 GRANT SELECT ON kashite.trn_friend_relation   TO kashite_r;
 GRANT SELECT ON kashite.view_friend_reletion_list   TO kashite_r;
 GRANT SELECT ON kashite.view_user_friend_list   TO kashite_r;
 GRANT INSERT, UPDATE, DELETE ON kashite.map_tag               TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.map_tag_id_seq        TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.mst_book              TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.mst_book_id_seq       TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.mst_tag               TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.mst_tag_id_seq        TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.mst_user              TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.mst_user_id_seq       TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.trn_user_book         TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.trn_user_book_id_seq  TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.view_tag_map_list     TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.view_user_book_list   TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.view_user_friend_list   TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.mst_user_book_status   TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.mst_friend_relation_status   TO kashite_rw;
 GRANT INSERT, UPDATE, DELETE ON kashite.trn_friend_relation   TO kashite_rw;
 GRANT UPDATE, USAGE ON kashite.trn_friend_relation_id_seq  TO kashite_rw;
