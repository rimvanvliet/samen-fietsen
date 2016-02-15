import scala.collection.mutable.ListBuffer

object Members {
  var members = new ListBuffer[MemberInfo]

  def init = {
    members = ListBuffer(
      new MemberInfo("1", Some("van Vliet"), Some("Ruud"), Some("Kneuterstraat 7"), Some("7384 CM  Wilp")),
      new MemberInfo("2", Some("Puk"), Some("Piet"), Some("Kuilstraat 2"), Some("City")),
      new MemberInfo("3", Some("Pukkel"), Some("Johan"), Some("Breuninkhof 3"), Some("")),
      new MemberInfo("4", Some("Puist"), Some("Joris"), Some("Goedzak 4"), Some("")),
      new MemberInfo("5", Some("Vlekje"), Some("Jan"), Some("Olie 5"), Some("Plaats")))
  }
}

