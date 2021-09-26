/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2021 Elior "Mallowigi" Boukhobza
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
package com.mallowigi.search.parsers

import java.util.regex.Pattern

object ColorParserFactory {
  private const val COLOR_METHOD = "Color."
  private const val COLOR_UIRESOURCE_METHOD = "ColorUIResource."
  private const val RGB = "rgb"
  private const val HSL = "hsl"
  private const val OX = "0x"
  private const val COLOR = "new Color"
  private const val COLOR_UI_RESOURCE = "new ColorUIResource"
  private val NO_HEX_PATTERN = Pattern.compile("(\\b[a-fA-F0-9]{3,8}\\b)")
  const val HASH: String = "#"

  fun getParser(text: String): ColorParser {
    //    } else if (text.startsWith(COLOR) || text.startsWith(COLOR_UI_RESOURCE)) {
    //      return new ColorCtorParser();
    //      } else if (text.startsWith(COLOR_METHOD)) {
    //        return new ColorMethodParser(COLOR_METHOD);
    //      } else if (text.startsWith(COLOR_UIRESOURCE_METHOD)) {
    //        return new ColorMethodParser(COLOR_UIRESOURCE_METHOD);
    return when {
      text.startsWith(HASH) && text.length > 1 -> HexColorParser(HASH)
      text.startsWith(RGB) -> RGBColorParser()
      text.startsWith(HSL) -> HSLColorParser()
      text.startsWith(OX) -> HexColorParser(OX)
      NO_HEX_PATTERN.matcher(text).find() -> HexColorParser("")
      else -> SVGColorParser()
    }
  }
}