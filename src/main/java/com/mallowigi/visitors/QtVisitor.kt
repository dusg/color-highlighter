/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2023 Elior "Mallowigi" Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.mallowigi.visitors

import com.intellij.codeInsight.daemon.impl.HighlightVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.mallowigi.config.home.ColorHighlighterConfig
import com.mallowigi.search.ColorSearchEngine
import com.mallowigi.search.parsers.ColorParser
import com.mallowigi.search.parsers.QColorCtorParser

class QtVisitor : ColorVisitor() {
  private val allowedTypes = listOf(
    "INTEGER_CONSTANT",
    "STRING_TEMPLATE",
    "CALL_EXPRESSION",
    "REFERENCE_EXPRESSION",
    "DECLARATION",
    "CPP_NEW_EXPRESSION"
  )
  private val prefixes: Set<Pair<Regex, ()->ColorParser>> = setOf(
    Pair("^(const )?QColor \\w+\\s*=\\s*[({]".toRegex()) { return@Pair QColorCtorParser(); },
    Pair("^(const )?QColor\\s*\\w+\\s*[{(]".toRegex()) { return@Pair QColorCtorParser(); },
    Pair("^QColor\\s*\\(".toRegex()) { return@Pair QColorCtorParser(); },
    Pair("^new QColor\\s*\\(".toRegex()) { return@Pair QColorCtorParser(); },
//    Pair("^(const )?auto \\w+ = QColor\\s*\\(".toRegex()) { return@Pair ColorCtorParser(); },
//    Pair("^(const )?QColor \\w+ = QColor\\s*\\(".toRegex()) { return@Pair ColorCtorParser(); },

  )

  private val config = ColorHighlighterConfig.instance

  override fun clone(): HighlightVisitor = QtVisitor()

  override fun getParser(text: String): ColorParser {
//    return prefixes.find { text.startsWith(it.first) }?.second?.let { it() }!!
    return prefixes.find { it.first.containsMatchIn(text) }?.second?.let { it() }!!
//    when {
//      text.startsWith(ColorPrefixes.QT_COLOR.text) -> ColorCtorParser()
//      text.startsWith(ColorPrefixes.QT_NEW_COLOR.text) -> ColorCtorParser()
//      else -> throw IllegalArgumentException("Cannot find a parser for the text: $text")
//    }
  }

  override fun shouldParseText(text: String): Boolean {
    return prefixes.any { it.first.containsMatchIn(text) }
//    return prefixes.any { text.startsWith(it.first) }
  }

  override fun suitableForFile(file: PsiFile): Boolean {
    val ext: Set<String> = setOf(
      "cpp",
      "hpp",
      "h",
      "cc",
      "cxx"
    )
    return ext.contains(file.virtualFile.extension)
  }

  override fun visit(element: PsiElement) {
    val type = PsiUtilCore.getElementType(element).toString()
    if (type !in allowedTypes) return

    val value = element.text
    val color = ColorSearchEngine.getColor(value, this)
    color?.let { highlight(element, it) }
  }

}
