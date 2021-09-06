/*
 * Copyright 2021 HM Revenue & Customs
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

package play.api.mvc.hal

import com.google.inject.Inject
import play.api.test.Injecting
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.{HeaderNames, Status}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents, Result}
import play.api.test.{DefaultAwaitTimeout, FakeRequest, ResultExtractors}

import scala.concurrent.Future

class ControllerTest extends AnyFunSuite
  with Matchers
  with ResultExtractors
  with HeaderNames
  with Status
  with DefaultAwaitTimeout
  with GuiceOneAppPerSuite
  with Injecting {

  class TestController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with HalWriteController

  val mockControllerComponents: ControllerComponents = inject[ControllerComponents]

  test("A HAL Resource should be writeable") {
    val controller = new TestController(mockControllerComponents)
    val result: Future[Result] = controller.hal().apply(FakeRequest())
    val bodyText: String = contentAsString(result)
    contentType(result) should equal(Some("application/hal+json"))
    (Json.parse(bodyText) \ "foo").as[String] should equal("bar")
  }

  test("A Resource can be retrived as JSON") {
    val controller = new TestController(mockControllerComponents)
    val result: Future[Result] = controller.halOrJson.apply(FakeRequest().withHeaders("Accept" -> "application/json"))
    contentType(result) should equal(Some("application/json"))
  }

  test("A Resource can be retrived as HAL") {
    val controller = new TestController(mockControllerComponents)
    val result: Future[Result] = controller.halOrJson.apply(FakeRequest().withHeaders("Accept" -> "application/hal+json"))
    contentType(result) should equal(Some("application/hal+json"))
  }

}
