/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2022 Elior "Mallowigi" Boukhobza
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
package com.mallowigi.colors

import com.intellij.util.xmlb.annotations.Property
import java.io.Serializable

/**
 * Represents a Single color: name + code
 *
 * @property name
 * @property code
 */
@Suppress("Detekt:DataClassShouldBeImmutable", "Detekt:DataClassContainsFunctions") // Need to disable this for now
data class SingleColor internal constructor(
  @field:Property var name: String = "",
  @field:Property var code: String = "",
) : Serializable {
  val isEmpty: Boolean
    get() = name.isEmpty() || code.isEmpty()

  val colorInt: Int
    get() = Integer.valueOf(code, 16)

  fun apply(other: SingleColor) {
    name = other.name
    code = other.code
  }

  override fun toString(): String = "$name: $code"
}
