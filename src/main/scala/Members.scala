
object Members {
  var members = List.empty[MemberInfo]

  def init = {
    members ::= new MemberInfo("1", Some("van Vliet"), Some("Ruud"), Some("Kneuterstraat 7"), Some("7384 CM  Wilp"))
    members ::= new MemberInfo("2", Some("Puk"), Some("Piet"), Some("Kuilstraat 2"), Some("City"))
    members ::= new MemberInfo("3", Some("Pukkel"), Some("Johan"), Some("Breuninkhof 3"), Some(""))
    members ::= new MemberInfo("4", Some("Puist"), Some("Joris"), Some("Goedzak 4"), Some(""))
    members ::= new MemberInfo("5", Some("Vlekje"), Some("Jan"), Some("Olie 5"), Some("Plaats"))
  }
}

