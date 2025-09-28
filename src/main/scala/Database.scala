case class Database(tables: List[Table]) {
  // TODO 3.0
  override def toString: String = tables.toString()

  // TODO 3.1
  def insert(tableName: String): Database = {
    val newTable = Table(tableName, List[Map[String, String]]())
    val exist = tables.filter(_.tableName == tableName)
    if (exist.isEmpty) Database(tables ++ List(newTable))
    else this

  }

  // TODO 3.2
  def update(tableName: String, newTable: Table): Database = {
    val exist = tables.filterNot(_.tableName == tableName)
    if (exist.length == tables.length) this
    else Database(exist ++ List(newTable))
  }
  // TODO 3.3
  def delete(tableName: String): Database = {
    Database(tables.filterNot(_.tableName == tableName))
  }

  // TODO 3.4
  def selectTables(tableNames: List[String]): Option[Database] = {
    val newTable = tables.filter(table => tableNames.contains(table.tableName))
    if (newTable.length == tableNames.length) Some(Database(newTable))
    else None
  }

  // TODO 3.5
  // Implement indexing here
  def apply(index: Int): Table = tables(index)
}
