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

import play.api.hal._
import play.api.libs.json.Json
import play.api.mvc._

trait HalWriteController {
  this: AbstractController =>

  def hal = Action {
    Ok(Hal.state(Json.obj("foo" -> "bar")))
  }

  def halOrJson = Action { implicit request =>
    render {
      case Accepts.Json() => Ok(Json.obj("foo" -> "bar"))
      case AcceptHal() => Ok(Hal.state(Json.obj("foo" -> "bar")) ++ HalLink("self", "/orders"))
    }
  }

}
