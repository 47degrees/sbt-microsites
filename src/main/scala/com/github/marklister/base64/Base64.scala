/*
 * Copyright 2016-2023 47 Degrees Open Source <https://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.marklister.base64

/**
 * Base64 encoder
 * @author
 *   Mark Lister This software is distributed under the 2-Clause BSD license. See the LICENSE file
 *   in the root of the repository.
 *
 * Copyright (c) 2014 - 2015 Mark Lister
 *
 * The repo for this Base64 encoder lives at https://github.com/marklister/base64 Please send your
 * issues, suggestions and pull requests there.
 */
object Base64 {

  case class B64Scheme(
      encodeTable: Array[Char],
      strictPadding: Boolean = true,
      postEncode: String => String = identity,
      preDecode: String => String = identity
  ) {}

  val base64: B64Scheme = B64Scheme(
    (('A' to 'Z') ++ ('a' to 'z') ++ ('0' to '9') ++ Seq('+', '/')).toArray
  )

  implicit class SeqEncoder(s: Seq[Byte]) {
    def toBase64(implicit scheme: B64Scheme = base64): String = Encoder(s.toArray).toBase64
  }

  implicit class Encoder(b: Array[Byte]) {
    private[this] val r       = new java.lang.StringBuilder((b.length + 3) * 4 / 3)
    private lazy val pad: Int = (3 - b.length % 3) % 3

    def toBase64(implicit scheme: B64Scheme = base64): String = {
      def sixBits(x: Byte, y: Byte, z: Byte): Unit = {
        val zz = (x & 0xff) << 16 | (y & 0xff) << 8 | (z & 0xff)
        r append scheme.encodeTable(zz >> 18)
        r append scheme.encodeTable(zz >> 12 & 0x3f)
        r append scheme.encodeTable(zz >> 6 & 0x3f)
        r append scheme.encodeTable(zz & 0x3f)
        ()
      }
      for (p <- 0 until b.length - 2 by 3)
        sixBits(b(p), b(p + 1), b(p + 2))
      pad match {
        case 0 =>
        case 1 => sixBits(b(b.length - 2), b(b.length - 1), 0)
        case 2 => sixBits(b(b.length - 1), 0, 0)
      }
      r setLength (r.length - pad)
      r append "=" * pad
      scheme.postEncode(r.toString)
    }
  }

}
