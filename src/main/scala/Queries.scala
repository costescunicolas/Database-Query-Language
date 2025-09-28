object Queries {
  def query_1(db: Database, ageLimit: Int, cities: List[String]): Option[Table] = {
   Some(Table("ceva", db.selectTables(List("Customers")).get.tables.flatMap(_.tableData.filter(row => (Field("age", x => x.toInt > ageLimit) && Field("city", x => cities.contains(x))).eval(row).get))))
  }

  def query_2(db: Database, date: String, employeeID: Int): Option[Table] = {
    Some(Table("ceva", db.selectTables(List("Orders")).get.tables.flatMap(_.tableData.filter(row => (Field("date", x => x > date) && Field("employee_id", x => x.toInt != employeeID)).eval(row).get))).select(List("order_id", "cost")).sort("cost", false))
  }

  def query_3(db: Database, minCost: Int): Option[Table] = {
    Some(Table("ceva", db.selectTables(List("Orders")).get.tables.flatMap(_.tableData.filter(row => Field("cost", x => x.toInt > minCost).eval(row).get))).select(List("order_id", "employee_id", "cost")).sort("employee_id"))
  }
}
