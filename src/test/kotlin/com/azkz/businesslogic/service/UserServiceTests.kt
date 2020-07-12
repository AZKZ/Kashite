package com.azkz.businesslogic.service

import com.azkz.businesslogic.repository.MstUserRepository
import com.azkz.common.KashiteConst
import com.azkz.infrastructure.entity.MstUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.NullAndEmptySource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.security.oauth2.core.oidc.user.OidcUser

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTests @Autowired constructor(val entityManager: TestEntityManager, val mstUserRepository: MstUserRepository) {

    lateinit var userService: UserService

    // @Nestedにすると、TestEntityManagerで以下の例外が発生しテストできない
    // java.lang.IllegalStateException: No transactional EntityManager found

    @BeforeEach
    fun setup() {
        userService = UserService(mstUserRepository)
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = [" ", "", "\t", "\n"])
    fun `when initialLogin then User is registered with no name`(name: String?) {

        val subject = "nonametest"
        val issuer = "https://accounts.google.com"

        // 引数のnameを返すMockを作成
        val principal = mockk<OidcUser>()
        every { principal.subject } returns subject
        every { principal.issuer.toString() } returns issuer
        every { principal.attributes["name"]} returns name

        // 対象メソッド実行
        userService.initialLogin(principal)

        // 実行後に対象ユーザーを検索
        val mstUser = mstUserRepository.findBySubjectAndIssuer(subject, issuer)

        // nameが"no name"で登録されていることを確認
        assertEquals("no name", mstUser.name)
    }

    @Test
    fun `when initialLogin then User is registered`() {

        // 存在しないsubjectとissuer（nameはあり）
        val subject = "aaaaaaaaaaaaaaaaaaaaa"
        val issuer = "https://accounts.google.com"
        val name = "test taro"

        val principal = mockk<OidcUser>()
        every { principal.subject } returns subject
        every { principal.issuer.toString() } returns issuer
        every { principal.attributes["name"]} returns name

        // 対象メソッド実行
        userService.initialLogin(principal)

        // 実行後に対象ユーザーを検索
        val mstUser = mstUserRepository.findBySubjectAndIssuer(subject, issuer)

        // 意図した値でユーザーが登録されていることを確認
        assertEquals(subject, mstUser.subject)
        assertEquals(issuer, mstUser.issuer)
        assertEquals(name, mstUser.name)
        assertEquals(KashiteConst.ENABLE_FLG_ENABLE, mstUser.enableFlg)
    }

    @Test
    fun `when initialLogin then User is not registered (already exists)`() {

        val subject = "asdfgh"
        val issuer = "https://accounts.google.com"

        // 上記の値を使って、テストデータを作成
        // nameは"name before"としている
        entityManager.persist(MstUser(subject, issuer, KashiteConst.ENABLE_FLG_ENABLE, "name before", "developer", "test program"))

        // テストメソッドの引数では、name afterという名前を使う
        val name = "name after"

        val principal = mockk<OidcUser>()
        every { principal.subject } returns subject
        every { principal.issuer.toString() } returns issuer
        every { principal.attributes["name"]} returns name

        // 対象メソッド実行
        userService.initialLogin(principal)

        // 実行後に対象ユーザーを検索
        val mstUser = mstUserRepository.findBySubjectAndIssuer(subject, issuer)

        // DBに登録されているユーザーのnameが変更されていないことを確認
        assertNotSame(name, mstUser.name)
    }


    @Test
    fun `when getUserId then return UserId`() {

        val subject = "asdfgh"
        val issuer = "https://accounts.google.com"

        // 上記の値を使ってテストデータを作成
        entityManager.persist(MstUser(subject, issuer, KashiteConst.ENABLE_FLG_ENABLE, "", "developer", "test program"))

        val principal = mockk<OidcUser>()
        every { principal.subject } returns subject
        every { principal.issuer.toString() } returns issuer

        // 対象メソッド実行
        val userId = userService.getUserId(principal)

        // UserIDが取得できていることの確認
        assertNotNull(userId)
    }

    @Test
    fun `when getUserId then throw Exception`() {

        // 存在しないsubjectとissuer
        val subject = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
        val issuer = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

        val principal = mockk<OidcUser>()
        every { principal.subject } returns subject
        every { principal.issuer.toString() } returns issuer

        // 対象メソッド実行時に例外が発生する
        val exception = assertThrows<IllegalArgumentException> { userService.getUserId(principal) }

        // 意図したエラーメッセージであることを確認
        assertEquals("対象のユーザーが存在しません", exception.message)
    }
}