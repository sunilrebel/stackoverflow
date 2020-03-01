import play.api.libs.json.{JsArray, JsObject, JsString, Json}

object Main {
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
        |         "attr":{
        |            "name":"test_name",
        |            "age":"17",
        |            "color":"blue"
        |         }
        |      },
        |      {
        |         "collected":{
        |            "field":"field_2"
        |         },
        |         "attr":{
        |            "name":"test_name2",
        |            "age":"18",
        |            "color":"red"
        |         }
        |      }
        |   ]
        |}
        |""".stripMargin

    val students = (Json.parse(input) \ "students").as[JsArray]
    val requiredStudents = students.value.map { student =>
      val attr = student \ "attr"

      val updatedAttributes = attr.get.as[JsObject].fields.map { x =>
        val (key, value) = x
        (key, JsString(key + value.as[String]))
      }

      val requiredStudent = student.as[JsObject] ++ Json.obj("attr" -> JsObject(updatedAttributes))
      requiredStudent
    }

    requiredStudents.foreach(println)
  }
}
