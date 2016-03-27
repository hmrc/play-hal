package play.api.hal

import play.api.libs.json.JsObject

case class HalResource(links: HalLinks, state: JsObject, embedded: Vector[(String, Vector[HalResource])] = Vector.empty) {
  def ++(other: HalResource): HalResource = {
    val d = state ++ other.state
    val l = links ++ other.links
    val e = embedded ++ other.embedded
    HalResource(l, d, e)
  }

  def include(other: HalResource) = ++(other)

  def ++(link: HalLink): HalResource = {
    this.copy(links = links ++ link)
  }

  def include(link: HalLink) = ++(link)
}
