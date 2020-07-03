package com.azkz.businesslogic

import com.azkz.businesslogic.service.BookAPIService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate

class BookAPIServiceTests{

    val bookAPIService: BookAPIService = BookAPIService(RestTemplate())

    @Nested
    inner class GetBookInfoTests{

        @Test
        fun `invalid ISBNCode`(){
            val isbnCode = "aaaaaaaa"
            val bookInfoForm = bookAPIService.getBookInfo(isbnCode)

            // Nullではないことを確認
            Assertions.assertTrue(bookInfoForm != null)


        }

    }




}