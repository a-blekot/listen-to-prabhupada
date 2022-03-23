package com.anadi.prabhupadalectures

import com.anadi.prabhupadalectures.network.api.pageNumber
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class PageNumberTest(
    private val nextPageUrl: String?,
    private val expected: Int?
) {
    @Test
    fun `page parsed correctly`() {
        val actual = nextPageUrl?.pageNumber
        Assert.assertEquals(expected, actual)
    }

    companion object {
        private val URL_PAGE_NULL: String? = null

        private const val URL_PAGE_2 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=2&quotes__scripture=3"
        private const val URL_PAGE_20 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=20&quotes__scripture=3"
        private const val URL_PAGE_5000 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=5000&quotes__scripture=3"

        private const val URL_PAGE_6 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=6"
        private const val URL_PAGE_60 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=60"
        private const val URL_PAGE_6000 = "http://ydl3.da.net.ua/api/v1/archive/file/?categories=1&page=6000"

        private const val URL_PAGE_7 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=7"
        private const val URL_PAGE_70 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=70"
        private const val URL_PAGE_7000 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=7000"

        private const val URL_PAGE_8 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=8&quotes__scripture=3"
        private const val URL_PAGE_80 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=80&quotes__scripture=3"
        private const val URL_PAGE_8000 = "http://ydl3.da.net.ua/api/v1/archive/file/?page=8000&quotes__scripture=3"

        @JvmStatic
        @Parameterized.Parameters
        fun data() =
            arrayListOf(
                arrayOf(URL_PAGE_NULL, null),

                arrayOf(URL_PAGE_2, 2),
                arrayOf(URL_PAGE_20, 20),
                arrayOf(URL_PAGE_5000, 5000),

                arrayOf(URL_PAGE_6, 6),
                arrayOf(URL_PAGE_60, 60),
                arrayOf(URL_PAGE_6000, 6000),

                arrayOf(URL_PAGE_7, 7),
                arrayOf(URL_PAGE_70, 70),
                arrayOf(URL_PAGE_7000, 7000),

                arrayOf(URL_PAGE_8, 8),
                arrayOf(URL_PAGE_80, 80),
                arrayOf(URL_PAGE_8000, 8000),
            )
    }
}