import scala.language.implicitConversions

trait FilterCond {
  def eval(r: Row): Option[Boolean]

  // TODO 2.2
  def ===(other: FilterCond): FilterCond = Equal(this, other)
  def &&(other: FilterCond): FilterCond = And(this, other)
  def ||(other: FilterCond): FilterCond = Or(this, other)
  def unary_! : FilterCond = Not(this)
}

case class Field(colName: String, predicate: String => Boolean) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
   val exist = r getOrElse(colName, "")
    if (exist == "") None
    else Some(predicate(exist))
  }
}

case class Compound(op: (Boolean, Boolean) => Boolean, conditions: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    val firstCondition = conditions.head
    val rest = conditions.tail
    Some(rest.foldRight(firstCondition.eval(r).get)((element, acc) => op(element.eval(r).get, acc)))
  }
}

case class Not(f: FilterCond) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = {
    if (f.eval(r).get) Some(false)
    else Some(true)
  }
}

def And(f1: FilterCond, f2: FilterCond): FilterCond = {
  val list = f1 :: f2 :: Nil
  Compound(_ && _, list)
}
def Or(f1: FilterCond, f2: FilterCond): FilterCond = {
  val list = f1 :: f2 :: Nil
  Compound(_ || _, list)
}
def Equal(f1: FilterCond, f2: FilterCond): FilterCond = {
  val list = f1 :: f2 :: Nil
  Compound(_ == _, list)
}

case class Any(fs: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = Compound(_ || _, fs).eval(r)

}

case class All(fs: List[FilterCond]) extends FilterCond {
  override def eval(r: Row): Option[Boolean] = Compound(_ && _, fs).eval(r)
}