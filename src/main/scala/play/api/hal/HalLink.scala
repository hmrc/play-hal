package play.api.hal

import play.api.libs.json.JsObject

case class HalLink(rel: String, href: String,
                   deprecation: Option[String] = None, name: Option[String] = None, profile: Option[String] = None,
                   title: Option[String] = None, hreflang: Option[String] = None, `type`: Option[String] = None,
                   linkAttr: JsObject = Defaults.emptyJson, templated: Boolean = false) {

  def withLinkAttributes(obj: JsObject) = this.copy(linkAttr = obj)
  def withDeprecation(url: String) = this.copy(deprecation = Some(url))
  def withName(name: String) = this.copy(name = Some(name))
  def withProfile(profile: String) = this.copy(profile = Some(profile))
  def withTitle(title: String) = this.copy(title = Some(title))
  def withHreflang(lang: String) = this.copy(hreflang = Some(lang))
  def withType(mediaType: String) = this.copy(`type` = Some(mediaType))
}

object HalLinks {
  def empty = HalLinks(Vector.empty)
}

case class HalLinks(links: Vector[HalLink]) {
  def ++(other: HalLinks) = {
    HalLinks(links ++ other.links)
  }

  def include(other: HalLinks) = ++(other)

  def ++(link: HalLink) = HalLinks(link +: this.links)

  def include(link: HalLink) = ++(link)
}
