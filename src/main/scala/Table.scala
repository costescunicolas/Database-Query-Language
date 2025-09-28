import scala.annotation.tailrec

type Row = Map[String, String]
type Tabular = List[Row]

case class Table (tableName: String, tableData: Tabular) {

  // TODO 1.0
  def header: List[String] = {
    tableData.head.keys.toList
  }
  def data: Tabular = tableData
  def name: String = tableName


  // TODO 1.1

  private def makeRow(list: List[String]): String = {
    list.take(list.length - 1).foldRight("")(_ + "," + _) + list.drop(list.length - 1).head
  }

  private def makeRowNewLine(list: List[String]): String = {
    makeRow(list) + "\n"
  }

  override def toString: String = {
    val firstRow = makeRowNewLine(header)
    val middleRows = tableData.take(tableData.length - 1).map(_.values.toList).map(makeRowNewLine).foldRight("")(_ + _)
    val lastRow = makeRow(tableData.drop(tableData.length - 1).head.values.toList)
    firstRow + middleRows + lastRow
  }

  // TODO 1.3
  def insert(row: Row): Table = {
    if (tableData.contains(row)) this
    else Table(tableName, tableData ++ List(row))
  }

  // TODO 1.4
  def delete(row: Row): Table = {
    Table(tableName, tableData.filterNot(_ == row))
  }

  // TODO 1.5
  def sort(column: String, ascending: Boolean = true): Table = {
    if (ascending) Table("ceva", tableData.sortBy(row => row getOrElse(column, "")))
    else Table("ceva", tableData.sortBy(row => row getOrElse(column, ""))(Ordering[String].reverse))
  }

  // TODO 1.6
  def select(columns: List[String]): Table = {
    Table(tableName, tableData.map(_.filter((key, _) => columns.contains(key))))
  }

  // TODO 1.7
  // Construiti headerele noi concatenand numele tabelei la headere
  private def product(row: Row, table: Table): Tabular = {
    val newRow = row.map((key, value) => (tableName + "." + key, value))
    table.tableData.map(_.map((key, value) => (table.tableName + "." + key, value)) ++ newRow)
  }
  def cartesianProduct(otherTable: Table): Table = {
    Table(tableName, tableData.flatMap(product(_, otherTable)))
  }

  // TODO 1.8

  private def emptyMap(table: Tabular): Map[String, String] = {
    table.head.map((key, _) => (key, ""))
  }
  
  private def combineTwoMaps(map1: Map[String, String], map2: Map[String, String], col: String): Map[String, String] = {
    val combined = map1.map((key, value) => {
      val exist = map2 getOrElse(key, "")
      if (exist != "" && value != exist && key != col) (key, exist + ";" + value)
      else (key, value)
    })
    
    val different = map2.filter((key, _) => !map1.contains(key))
    combined ++ different
  }

  private def changeKey(dict: Map[String, String], oldName: String, newName: String): Map[String, String] = {
    dict.map {
      case (`oldName`, value) => newName -> value
      case (key, value) => key -> value   
    }
  }

  private def equalNameList(list1: Tabular, list2: Tabular, col: String): Tabular =
    list1.flatMap(row1 => list2.filter(row2 => row1.getOrElse(col, "") == row2.getOrElse(col, "")))
  
  private def differentNameList(list1: Tabular, list2: Tabular, col: String): Tabular = {
    list1.filter(row1 => {
      val exist = list2.filter(row2 => row1.getOrElse(col, "") == row2.getOrElse(col, ""))
      if (exist.isEmpty) true
      else false
    })
  }
  
  def join(other: Table)(col1: String, col2: String): Table = {
    val changedOther = other.tableData.map(changeKey(_, col2, col1))
    val firstSimilar = equalNameList(tableData, changedOther, col1)
    val secondSimilar = equalNameList(changedOther, tableData, col1)
    
    val combined = firstSimilar.map(row1 => {
      val similar = secondSimilar.filter(row2 => row1.getOrElse(col1, "") == row2.getOrElse(col1, ""))
      combineTwoMaps(row1, similar.head, col1)
    })
    
    val firstDifferent = differentNameList(tableData, changedOther, col1)
    val secondDifferent = differentNameList(changedOther, tableData, col1)
    val firstDifferentCombined = firstDifferent.map(row => combineTwoMaps(row, emptyMap(changedOther), col1))
    val secondDifferentCombined = secondDifferent.map(row => combineTwoMaps(row, emptyMap(tableData), col1))
    Table(tableName, combined ++ firstDifferentCombined ++ secondDifferentCombined)
  }

  // TODO 2.3
  def filter(f: FilterCond): Table = Table(tableName, tableData.filter(f.eval(_).get))

  // TODO 2.4
  def update(f: FilterCond, updates: Map[String, String]): Table = {
    val newTable = tableData.map(row => {
      if (f.eval(row).get) {
        val different = row.filter((key, _) => !updates.contains(key))
        updates ++ different
      } else {
        row
      }
    })
    Table(tableName, newTable)
  }

  // TODO 3.5
  // Implement indexing
  def apply(index: Int): Row = tableData(index)
}

object Table {
  // TODO 1.2
  def fromCSV(csv: String): Table = {
    val allRows = csv.split("\n").toList.map(_.split(",").toList)
    val theKeys = allRows.head
    val theValues = allRows.tail
    Table("ceva", theValues.map(theKeys.zip(_).toMap))
  }

  // TODO 1.9
  def apply(name: String, s: String): Table = {
    val table = fromCSV(s)
    Table(name, table.tableData)
  }
}