/*
 * Copyright (c) 2008, 2009, 2010, 2011 Josh Suereth, Steven Blundy, Josh Cough, Mark Harrah, Stuart Roebuck, Tony Sloane, Vesa Vilhonen, Jason Zaugg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2022 47 Degrees Open Source <https://www.47deg.com>
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

package com.typesafe.sbt.sbtghpages

import sbt.*
import sbt.Keys.*

trait GhpagesKeys {
  lazy val ghpagesCommitOptions = settingKey[Seq[String]]("commit options")
  lazy val ghpagesRepository =
    settingKey[File]("sandbox environment where git project ghpages branch is checked out.")
  lazy val ghpagesBranch = settingKey[String](
    "Name of the git branch in which to store ghpages content. Defaults to gh-pages."
  )
  lazy val ghpagesNoJekyll = settingKey[Boolean](
    "If this flag is set, ghpages will automatically generate a .nojekyll file to prevent github from running jekyll on pushed sites."
  )
  lazy val ghpagesUpdatedRepository =
    taskKey[File]("Updates the local ghpages branch on the sandbox repository.")
  // Note:  These are *only* here in the event someone wants to completely bypass the sbt-site plugin.
  lazy val ghpagesPrivateMappings = ghpagesSynchLocal / mappings
  lazy val ghpagesSynchLocal =
    taskKey[File]("Copies the locally generated site into the local ghpages repository.")
  lazy val ghpagesCleanSite = taskKey[Unit]("Cleans the staged repository for ghpages branch.")
  lazy val ghpagesPushSite = taskKey[Unit](
    "Pushes a generated site into the ghpages branch.  Will not clean the branch unless you run clean-site first."
  )
}
