import play.api.libs.json._

object Try2 {
  def main(args: Array[String]): Unit = {
    val input =
      """
        |{
        |   "id":123,
        |   "students":[
        |      {
        |         "collected":{
        |            "field":"field_1"
        |         },
        |         "attr":[{
        |            "name":"test_name",
        |            "age":"17",
        |            "color":"blue"
        |         }]
        |      },
        |      {
        |         "collected":{
        |            "field":"field_2"
        |         },
        |         "attr":[{
        |            "name":"test_name2",
        |            "age":"18",
        |            "color":"red"
        |         }]
        |      }
        |   ]
        |}
        |""".stripMargin

    val json = Json.parse(input)

    val attrTransformer = (__ \ "attr").json.update {
      __.read[JsArray].map {
        case JsArray(values) =>

          val updatedValues = values.map { x =>
            JsObject(x.as[JsObject].fields.map { z =>
              val (key, value) = z
              (key, JsString(key + value.as[String]))
            })
          }

          JsArray(updatedValues)
      }
    }

    val transformer = (__ \ "students").json.update(Reads.list(attrTransformer).map(x => JsArray(x)))

    val output = json.transform(transformer).get
    println("output: " + Json.prettyPrint(output))
  }
}
