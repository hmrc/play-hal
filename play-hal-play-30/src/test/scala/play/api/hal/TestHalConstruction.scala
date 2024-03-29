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

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import play.api.hal.Hal._
import play.api.libs.json.{Json, OWrites}

class TestHalConstruction extends AnyFunSuite with Matchers {

  case class TestData(total: Int, currency: String, status: String)
  implicit val testWrites: OWrites[TestData] = Json.writes[TestData]

  test("A minimal HAL resource is a JSON object") {
    val data = TestData(20, "EUR", "shipped")
    data.asResource.json should equal(Json.toJson(data))
  }

  test("A HAL resource may contain only links") {
    Hal.links(
      HalLink("self", "/orders"),
      HalLink("next", "/orders?page=2"),
      HalLink("find", "/orders{?id}", templated = true)).json should equal(
        Json.parse("""{
                       "_links": {
                       "self": { "href": "/orders" },
                       "next": { "href": "/orders?page=2" },
                       "find": { "href": "/orders{?id}", "templated": true }
                        }
                        }""".stripMargin))
  }

  test("A HAL resource may contain links and state") {
    val data = TestData(20, "EUR", "shipped")
    (data.asResource ++
      HalLink("self", "/orders") ++
      HalLink("next", "/orders?page=2") ++
      HalLink("find", "/orders{?id}", templated = true)).json should equal(
        Json.parse("""{
                       "_links": {
                       "self": { "href": "/orders" },
                       "next": { "href": "/orders?page=2" },
                       "find": { "href": "/orders{?id}", "templated": true }
                        },
                         "total" : 20,
                         "currency" : "EUR",
                         "status": "shipped"
                        }""".stripMargin))
  }

  test("a HAL resource may embed links") {
    val json = TestData(20, "EUR", "shipped")
    Json.toJson(Hal.state(json))
    val selfLink = HalLink("self", "/blog-post").asResource
    val authorLink = HalLink("author", "/people/alan-watts")
    val embeddedAuthorState = Json.obj(
      "name" -> "Alan Watts",
      "born" -> "January 6, 1915",
      "died" -> "November 16, 1973").asResource

    (selfLink ++ Hal.embeddedLink(authorLink, embeddedAuthorState)).json should equal(
      Json.parse("""{
                 "_links": {
                   "self": { "href": "/blog-post" },
                   "author": { "href": "/people/alan-watts" }
                 },
                 "_embedded": {
                   "author": {
                     "_links": { "self": { "href": "/people/alan-watts" } },
                     "name": "Alan Watts",
                     "born": "January 6, 1915",
                     "died": "November 16, 1973"
                   }
                 }
                  }""".stripMargin))
  }

  test("a HAL resource may embed multiple resources") {
    val baseResource =
      Json.obj("currentlyProcessing" -> 14, "shippedToday" -> 20).asResource ++
        HalLink("self", "/orders") ++
        HalLink("next", "/orders?page=2") ++
        HalLink("find", "/orders{?id}", templated = true)

    val resource1 =
      TestData(30, "USD", "shipped").asResource ++
        HalLink("self", "/orders/123") ++
        HalLink("basket", "/baskets/98712") ++
        HalLink("customer", "/customers/7809")

    val resource2 =
      TestData(20, "USD", "processing").asResource ++
        HalLink("self", "/orders/124") ++
        HalLink("basket", "/baskets/97213") ++
        HalLink("customer", "/customers/12369")

    val res = baseResource ++ Hal.embedded("orders", resource1, resource2)

    res.json should equal(Json.parse("""{
      "_links": {
        "self": { "href": "/orders" },
        "next": { "href": "/orders?page=2" },
        "find": { "href": "/orders{?id}", "templated": true }
      },
      "_embedded": {
        "orders": [{
            "_links": {
              "self": { "href": "/orders/123" },
              "basket": { "href": "/baskets/98712" },
              "customer": { "href": "/customers/7809" }
            },
            "total": 30,
            "currency": "USD",
            "status": "shipped"
          },{
            "_links": {
              "self": { "href": "/orders/124" },
              "basket": { "href": "/baskets/97213" },
              "customer": { "href": "/customers/12369" }
            },
            "total": 20,
            "currency": "USD",
            "status": "processing"
        }]
      },
      "currentlyProcessing": 14,
      "shippedToday": 20
      }""".stripMargin))
  }

  test("provide alternative names for composition") {
    val data = TestData(20, "EUR", "shipped")
    data.asResource ++
      HalLink("self", "/orders") ++
      HalLink("next", "/orders?page=2") ++
      HalLink("find", "/orders{?id}", templated = true) should equal(

        data.asResource include
          HalLink("self", "/orders") include
          HalLink("next", "/orders?page=2") include
          HalLink("find", "/orders{?id}", templated = true)
      )
  }

  test("provide support for optional link attributes") {
    Hal.links(
      HalLink("self", "/orders").withDeprecation("http://www.thisisdeprecated.com"),
      HalLink("next", "/orders?page=2").withType("application/json"),
      HalLink("find", "/orders{?id}", templated = true).withHreflang("de")).json should equal(

        Json.parse("""{
        "_links": {
               "self": { "href": "/orders", "deprecation": "http://www.thisisdeprecated.com" },
               "next": { "href": "/orders?page=2", "type": "application/json" },
               "find": { "href": "/orders{?id}", "templated": true, "hreflang": "de" }
             }
        }""".stripMargin)
      )
  }

  test("provide support for arbitrary link attributes") {
    Hal.links(
      HalLink("self", "/orders").withLinkAttributes(Json.obj("isRequired" -> true))
    ).json should equal(

        Json.parse("""{
        "_links": {
               "self": { "href": "/orders", "isRequired": true }
             }
        }""".stripMargin)
      )
  }

  test("provide support for arbitrary link attributes (from seq)") {
    Hal.linksSeq(
      HalLink("self", "/orders").withLinkAttributes(Json.obj("isRequired" -> true)) :: Nil
    ).json should equal(

        Json.parse("""{
        "_links": {
               "self": { "href": "/orders", "isRequired": true }
             }
        }""".stripMargin)
      )
  }


  test("multiple links with the same rel are represented as a JSON array") {
    Hal.links(
      HalLink("self", "/orders"),
      HalLink("ea:admin", "/admins/2").withTitle("Fred"),
      HalLink("ea:admin", "/admins/5").withTitle("Kate")
    ).json should equal(
      Json.parse("""
           |{
           |  "_links": {
           |    "self": {
           |       "href": "/orders"
           |    },
           |    "ea:admin": [{
           |        "href": "/admins/2",
           |        "title": "Fred"
           |      }, {
           |        "href": "/admins/5",
           |        "title": "Kate"
           |    }]
           |  }
           |}""".stripMargin))
  }
}
