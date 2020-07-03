/**
 * Base64からバイナリーデータにする
 * 今後使う可能性があるので残しておく
 *
 * @param {String} base64
 */
function base64ToArrayBuffer(base64) {
  var binary_string = window.atob(base64);
  var len = binary_string.length;
  var bytes = new Uint8Array(len);
  for (var i = 0; i < len; i++) {
    bytes[i] = binary_string.charCodeAt(i);
  }
  return bytes.buffer;
}

/**
* 代替画像を表示する
* @param title:書籍のタイトル
* @param element：代替画像を表示する要素
*/
function dispSubImage(title, element) {
  element.onError = null;
  element.src = "http://placehold.jp/13/cccccc/000000/180x240.png?text=" + title;
}
