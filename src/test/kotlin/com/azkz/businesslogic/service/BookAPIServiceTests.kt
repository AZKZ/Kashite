package com.azkz.businesslogic.service

import com.azkz.application.resource.BookInfoForm
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import java.lang.reflect.InvocationTargetException

@SpringBootTest
class BookAPIServiceTests(@Autowired val restTemplate: RestTemplate) {

    lateinit var bookAPIService: BookAPIService

    @BeforeEach
    fun setUp() {
        bookAPIService = BookAPIService(restTemplate)
    }

    @Nested
    inner class GetBookInfoTests {

        @ParameterizedTest
        @ValueSource(strings = ["9784798042770", "978-4798042770", "9-7-8-4-7-9-8-0-4-2-7-7-0"])
        fun `return BookInfoForm instance with values`(isbnCode: String) {

            // 対象メソッド実行
            val bookInfoForm = bookAPIService.getBookInfo(isbnCode)

            // 各項目の値があることを確認
            assertEquals(0L, bookInfoForm.id)
            assertNotNull(bookInfoForm.author)
            assertNotNull(bookInfoForm.title)
            assertNotNull(bookInfoForm.imagePath)
            assertNotNull(bookInfoForm.isbnCode)
        }

        @Test
        fun `return empty BookInfoForm instance other than isbn code`() {

            // 存在しないISBNコード
            val isbnCode = "1111111111111"

            // 対象メソッド実行
            val bookInfoForm = bookAPIService.getBookInfo(isbnCode)

            // 戻り値がNullではないことを確認
            assertNotNull(bookInfoForm)

            // ISBNコード以外の項目がnullもしくは初期値であることを確認
            assertEquals(0L, bookInfoForm.id)
            assertNull(bookInfoForm.author)
            assertNull(bookInfoForm.title)
            assertNull(bookInfoForm.imagePath)

            // ISBNコードが意図した値であることを確認
            assertEquals(isbnCode, bookInfoForm.isbnCode)
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = [" ", "", "\t", "\n"])
        fun `return empty BookInfoForm instance`(isbnCode: String?) {

            // 対象メソッド実行
            val bookInfoForm = bookAPIService.getBookInfo(isbnCode)

            // 戻り値がNullではないことを確認
            assertNotNull(bookInfoForm)

            // 項目がnullもしくは初期値であることを確認
            assertEquals(0L, bookInfoForm.id)
            assertNull(bookInfoForm.author)
            assertNull(bookInfoForm.title)
            assertNull(bookInfoForm.imagePath)
            assertNull(bookInfoForm.isbnCode)
        }
    }

    @Nested
    inner class GetOpenbdBookInfoTests {

        @ParameterizedTest
        @ValueSource(strings = ["9784798042770", "978-4798042770", "9-7-8-4-7-9-8-0-4-2-7-7-0"])
        fun `return BookInfoForm instance with values`(isbnCode: String) {

            val inputBookInfoForm = BookInfoForm()

            // リフレクションを使ってprivateメソッドを取得
            val method = BookAPIService::class.java.getDeclaredMethod("getOpenbdBookInfo", String::class.java, BookInfoForm::class.java)

            // 取得したprivateメソッドにアクセスできるようにする
            method.trySetAccessible()

            // 対象メソッド実行
            val outputBookInfoForm: BookInfoForm = method.invoke(bookAPIService, isbnCode, inputBookInfoForm) as BookInfoForm

            // 意図した値であることを確認
            assertEquals(0L, outputBookInfoForm.id)
            assertEquals("川場隆／著", outputBookInfoForm.author)
            assertEquals("新わかりやすいJava 入門編", outputBookInfoForm.title)
            assertEquals("https://cover.openbd.jp/9784798042770.jpg", outputBookInfoForm.imagePath)
            assertEquals("9784798042770", outputBookInfoForm.isbnCode)
        }

        @Test
        fun `return empty BookInfoForm instance other than isbn code`() {

            // 存在しないISBNコード
            val isbnCode = "1111111111111"

            // 引数用の空インスタンス
            val inputBookInfoForm = BookInfoForm()

            // リフレクションを使ってprivateメソッドを取得
            val method = BookAPIService::class.java.getDeclaredMethod("getOpenbdBookInfo", String::class.java, BookInfoForm::class.java)

            // 取得したprivateメソッドにアクセスできるようにする
            method.trySetAccessible()

            // 対象メソッド実行
            val outputBookInfoForm: BookInfoForm = method.invoke(bookAPIService, isbnCode, inputBookInfoForm) as BookInfoForm

            // 戻り値がNullではないことを確認
            assertNotNull(outputBookInfoForm)

            // ISBNコード以外の項目がnullもしくは初期値であることを確認
            assertEquals(0L, outputBookInfoForm.id)
            assertNull(outputBookInfoForm.author)
            assertNull(outputBookInfoForm.title)
            assertNull(outputBookInfoForm.imagePath)

            // ISBNコードが意図した値であることを確認
            assertEquals(isbnCode, outputBookInfoForm.isbnCode)
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = [" ", "", "\t", "\n"])
        fun `throw IllegalArgumentException with message due to isbn code`(isbnCode: String?){
            // 引数用の空インスタンス
            val inputBookInfoForm = BookInfoForm()

            // リフレクションを使ってprivateメソッドを取得
            val method = BookAPIService::class.java.getDeclaredMethod("getOpenbdBookInfo", String::class.java, BookInfoForm::class.java)

            // 取得したprivateメソッドにアクセスできるようにする
            method.trySetAccessible()

            // リフレクション使用時の例外はInvocationTargetExceptionでラッピングされるため
            // catch節の中で詳細を取得し、assertする
            try {
                // 対象メソッド実行
                method.invoke(bookAPIService, isbnCode, inputBookInfoForm)

                //例外が発生しなかった場合はテスト失敗とする
                assertTrue(false)
            }catch (e: InvocationTargetException){
                // 原因をExceptionとして取得
                val exception = e.cause as Exception

                // 意図した例外であることを確認
                assertTrue(exception is IllegalArgumentException)
                // 意図したエラーメッセージであることを確認
                assertEquals("パラメータが不正です", exception.message)
            }
        }

        @Test
        fun `throw IllegalArgumentException with message due to bookInfoForm is null`(){

            val isbnCode = "1111111111111"

            // 引数のbookInfoFormをnullにする
            val inputBookInfoForm = null

            // リフレクションを使ってprivateメソッドを取得
            val method = BookAPIService::class.java.getDeclaredMethod("getOpenbdBookInfo", String::class.java, BookInfoForm::class.java)

            // 取得したprivateメソッドにアクセスできるようにする
            method.trySetAccessible()

            // リフレクション使用時の例外はInvocationTargetExceptionでラッピングされるため
            // catch節の中で詳細を取得し、assertする
            try {
                // 対象メソッド実行
                method.invoke(bookAPIService, isbnCode, inputBookInfoForm)

                //例外が発生しなかった場合はテスト失敗とする
                assertTrue(false)
            }catch (e: InvocationTargetException){
                // 原因をExceptionとして取得
                val exception = e.cause as Exception

                // 意図した例外であることを確認
                assertTrue(exception is IllegalArgumentException)
                // 意図したエラーメッセージであることを確認
                assertEquals("パラメータが不正です", exception.message)
            }
        }
    }

}
