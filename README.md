# play-hal

A [HAL - Hypertext Application Language](http://stateless.co/hal_specification.html) library based on [Play-JSON](https://www.playframework.com/documentation/2.3.x/ScalaJson).

This repository is a fork of [HALselhof](https://github.com/tobnee/HALselhof)

This library is now cross compiled for Play 2.8, 2.9 and 3.0

## Add the library
```scala
"uk.gov.hmrc" %% "play-hal-play-XX" % "4.X.X"
```
where ``XX`` is the version of Play you require
## Standalone Example
```scala
// Test data which should be reflected in the resource state
case class TestData(total: Int, currency: String, status: String)

// a generated play.api.libs.json.Writes
implicit val testWrites = Json.writes[TestData]

// transfer the resource state into a full HAL resource
val data = TestData(20, "EUR", "shipped")

val resource: HalResource = data.asResource ++
  HalLink("self", "/orders") ++
  HalLink("next", "/orders?page=2") ++
  HalLink("find", "/orders{?id}", templated = true)

// transfer the resource to the Play JSON AST
val json = resource.json
```
[Examples](https://github.com/tobnee/HALselhof/blob/master/src/test/scala/play/api/hal/TestHalConstruction.scala)

## Play Framework Integration
```scala
// within a Play Controller HAL resources can be serialized directly and are supported within content negotiation
import play.api.hal._
import play.api.mvc.hal._

def halOrJson = Action {
  implicit request =>
    render {
      case Accepts.Json() =>
        Ok(Json.obj("foo" -> "bar"))
      case AcceptHal() =>
        Ok(Hal.state(Json.obj("foo" -> "bar")) ++ HalLink("self", "/foo"))
    }
}
```

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").