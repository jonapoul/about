package com.jonapoul.libraries.data

import com.jonapoul.libraries.data.json.LicenseModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.assertEquals

@RunWith(Parameterized::class)
internal class LicenseModelTest(private val expectedOutput: String, private val input: String) {
    @Test
    fun `Clean up license name`() {
        /* Given */
        val model = LicenseModel(
            license = input,
            licenseUrl = "URL IS UNTOUCHED IN THIS TEST"
        )

        /* When */
        model.cleanUpLicense()

        /* Then */
        assertEquals(expected = expectedOutput, actual = model.license)
    }

    companion object {
        private const val APACHE_2_0 = "Apache 2.0"
        private const val MIT = "MIT"
        private const val OTHER = "This doesn't match either pattern!"

        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(APACHE_2_0, APACHE_2_0),
            arrayOf(APACHE_2_0, "Apache             2.0"),
            arrayOf(APACHE_2_0, "Apachesomething2.0"),
            arrayOf(APACHE_2_0, "Apache Commons 2.0"),
            arrayOf(APACHE_2_0, "Apache Open Source License 2.0"),
            arrayOf("Apache 3.0", "Apache 3.0"),
            arrayOf("Apache 1.0", "Apache 1.0"),
            arrayOf(MIT, MIT),
            arrayOf(MIT, "MIT License"),
            arrayOf(OTHER, OTHER),
        )
    }
}
