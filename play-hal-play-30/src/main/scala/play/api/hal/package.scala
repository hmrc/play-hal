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

package play.api

import play.api.libs.json._

/**
 * Scala model of the JSON Hypertext Application Language according to https://tools.ietf.org/html/draft-kelly-json-hal-06
 */
package object hal {

  object Defaults {
    val emptyJson: JsObject = Json.parse("{}").as[JsObject]
  }

  implicit val halLinkWrites: Writes[HalLinks] = new Writes[HalLinks] {

    def writes(hal: HalLinks): JsValue = {

      val halLinks =
        hal.links.groupBy(_.rel).map {
          case (rel, links) =>
            rel -> links.map {
              link =>
                val href = linkToJson(link)

                if (link.templated) href + ("templated" -> JsBoolean(true)) else href
            }
        } map {
          case (rel, links) if links.size == 1 =>
            rel -> links.head
          case (rel, links) =>
            rel -> JsArray(links)
        }

      Json.obj("_links" -> JsObject(halLinks.toSeq))
    }

    def linkToJson(link: HalLink): JsObject =
      JsObject(
        List("href" -> JsString(link.href)) ++
        optAttribute("deprecation", link.deprecation) ++
        optAttribute("name", link.name) ++
        optAttribute("profile", link.profile) ++
        optAttribute("title", link.title) ++
        optAttribute("type", link.`type`) ++
        optAttribute("hreflang", link.hreflang).toList
      ) ++ link.linkAttr

    def optAttribute(s: String, option: Option[String]): Option[(String, JsString)] =
      option.map(value => (s, JsString(value)))
  }

  implicit val halResourceWrites: Writes[HalResource] = (hal: HalResource) => {
    val resource =
      if (hal.links.links.isEmpty)
        hal.state
      else
        Json.toJson(hal.links).as[JsObject] ++ hal.state

    if (toEmbeddedJson(hal).fields.isEmpty)
      resource
    else
      resource + ("_embedded" -> toEmbeddedJson(hal))
  }
  private def toEmbeddedJson(hal: HalResource): JsObject =
    hal.embedded match {
      case Vector((k, Vector(elem))) =>
        Json.obj((k, Json.toJson(elem)))
      case e if e.isEmpty =>
        JsObject(Nil)
      case e =>
        JsObject(e.map {
          case (link, resources) =>
            link -> Json.toJson(resources.map(r => Json.toJson(r)))
        })
    }
}
