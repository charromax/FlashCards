package com.charr0max.flashcards.presentation.ui.util

import java.util.regex.Pattern

object LinkParser {
    private val markdownLinkPattern = Pattern.compile("\\[.*?]\\((https?://[^)]+)\\)")

    fun extractLinks(text: String): List<String> {
        val urls = mutableListOf<String>()
        val matcher = markdownLinkPattern.matcher(text)
        while (matcher.find()) {
            urls.add(matcher.group(1) ?: "")
        }
        return urls
    }

    fun cleanMarkdownLinks(text: String): String {
        return markdownLinkPattern.matcher(text).replaceAll("$1")
    }
}