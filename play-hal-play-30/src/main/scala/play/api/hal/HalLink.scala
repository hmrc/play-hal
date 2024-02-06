/*
 * Copyright 2023 HM Revenue & Customs
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

package play.api.hal

import play.api.libs.json.JsObject

case class HalLink(
  rel         : String,
  href        : String,
  deprecation : Option[String] = None,
  name        : Option[String] = None,
  profile     : Option[String] = None,
  title       : Option[String] = None,
  hreflang    : Option[String] = None,
  `type`      : Option[String] = None,
  linkAttr    : JsObject = Defaults.emptyJson,
  templated   : Boolean = false
) {

  def withLinkAttributes(obj: JsObject): HalLink =
    this.copy(linkAttr = obj)
  def withDeprecation(url: String): HalLink =
    this.copy(deprecation = Some(url))
  def withTitle(title: String): HalLink =
    this.copy(title = Some(title))
  def withHreflang(lang: String): HalLink =
    this.copy(hreflang = Some(lang))
  def withType(mediaType: String): HalLink =
    this.copy(`type` = Some(mediaType))
}

object HalLinks {
  def empty: HalLinks =

    HalLinks(Vector.empty)
}

case class HalLinks(links: Vector[HalLink]) {
  def ++(other: HalLinks): HalLinks =
    HalLinks(links ++ other.links)

  def include(other: HalLinks): HalLinks =
    ++(other)

  def ++(link: HalLink): HalLinks =
    HalLinks(link +: this.links)

  def include(link: HalLink): HalLinks =
    ++(link)
}
