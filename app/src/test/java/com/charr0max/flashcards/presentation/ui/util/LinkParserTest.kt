package com.charr0max.flashcards.presentation.ui.util

import org.junit.Assert.assertEquals
import org.junit.Test

class LinkParserTest {

    @Test
    fun `extractLinks should return list of URLs`() {
        val text = "Visita [Kotlin Docs](https://kotlinlang.org) y [Jetpack Compose](https://developer.android.com/jetpack/compose)."
        val expected = listOf("https://kotlinlang.org", "https://developer.android.com/jetpack/compose")

        val result = LinkParser.extractLinks(text)

        assertEquals(expected, result)
    }

    @Test
    fun `extractLinks should return empty list if no URLs are found`() {
        val text = "Este texto no tiene links."

        val result = LinkParser.extractLinks(text)

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `cleanMarkdownLinks should remove markdown syntax and keep only links`() {
        val text = "Lee [Kotlin Docs](https://kotlinlang.org) para aprender más."
        val expected = "Lee https://kotlinlang.org para aprender más."

        val result = LinkParser.cleanMarkdownLinks(text)

        assertEquals(expected, result)
    }
}