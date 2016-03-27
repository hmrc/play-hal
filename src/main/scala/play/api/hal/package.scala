package play.api

import play.api.libs.json._

/**
 * Scala model of the JSON Hypertext Application Language according to https://tools.ietf.org/html/draft-kelly-json-hal-06
 */
package object hal {

  object Defaults {
    val emptyJson = Json.parse("{}").as[JsObject]
  }

  implicit val halLinkWrites = new Writes[HalLinks] {

    def writes(hal: HalLinks): JsValue = {

      val halLinks = hal.links.map { link =>
        val href = linkToJson(link)

        val links = if (link.templated) href + ("templated" -> JsBoolean(true)) else href
        link.rel -> links
      }
      Json.obj("_links" -> JsObject(halLinks))
    }

    def linkToJson(link: HalLink): JsObject = {
      JsObject(List("href" -> JsString(link.href)) ++
        optAttribute("deprecation", link.deprecation) ++
        optAttribute("name", link.name) ++
        optAttribute("profile", link.profile) ++
        optAttribute("title", link.title) ++
        optAttribute("type", link.`type`) ++
        optAttribute("hreflang", link.hreflang).toList) ++ link.linkAttr
    }

    def optAttribute(s: String, option: Option[String]) =
      option.map(value => (s, JsString(value)))
  }

  implicit val halResourceWrites: Writes[HalResource] = new Writes[HalResource] {
    def writes(hal: HalResource): JsValue = {
      val embedded = toEmbeddedJson(hal)
      val resource = if (hal.links.links.isEmpty) hal.state
      else Json.toJson(hal.links).as[JsObject] ++ hal.state
      if (embedded.fields.isEmpty) resource
      else resource + ("_embedded" -> embedded)
    }

    def toEmbeddedJson(hal: HalResource): JsObject = {
      hal.embedded match {
        case Vector((k, Vector(elem))) => Json.obj((k, Json.toJson(elem)))
        case e if e.isEmpty => JsObject(Nil)
        case e => JsObject(e.map {
          case (link, resources) =>
            link -> Json.toJson(resources.map(r => Json.toJson(r)))
        })
      }
    }
  }
}
