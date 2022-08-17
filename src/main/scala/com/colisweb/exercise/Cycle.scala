package com.colisweb.exercise

import com.colisweb.exercise.domain.{Edge, Path, Point}

import scala.annotation.tailrec


object Cycle {

  def shortCycle(points: Iterable[Point]): Path = {

    def sqr(d: Double): Double = d * d

    def distance(p1: Point, p2: Point): Double = math.sqrt(sqr(p2.x - p1.x) + sqr(p2.y - p1.y))

    // NN construction heuristic

    val pointList:List[Point] = points.toList

    def NN(pointsRestants: List[Point]): List[Point] = pointsRestants match {

      case Nil => Nil
      case p::Nil => p::Nil
      case p :: tail =>
        val nearest:Point = tail.minBy(t => distance(t,p))
        p::nearest::NN(tail diff List(nearest,p))

    }

    val NNPath = NN(pointList)


    // 2-opt

    @tailrec
    def getNextElem[T](liste:List[T], elem:T):Option[T] = liste match {
      case Nil => None
      case `elem` :: t :: _ => Some(t)
      case _ :: tail => getNextElem(tail,elem)
    }

    @tailrec
    def getPreviousElem[T](liste:List[T], elem:T):Option[T] = liste match {
      case Nil => None
      case t :: `elem` :: _ => Some(t)
      case _ :: tail => getPreviousElem(tail, elem)
    }

    def TwoOpt(cycle:List[Point]):List[Point] =  {

      def boucleWhile(cycle:List[Point],cycleCopy:List[Point],cycleCopy2:List[Point],amelioration:List[Boolean]):List[Point] = {
        if (amelioration.contains(true)) {
        def boucleFor1(cycle: List[Point], cycleCopy: List[Point], cycleCopy2: List[Point],amelioration1:List[Boolean] ): List[Point] = cycleCopy match {
          case Nil => boucleWhile(cycle, cycleCopy, cycleCopy2,amelioration1)
          case xi :: tail1 =>
            @tailrec
            def boucleFor2(xi:Point, cycle: List[Point], cycleCopy: List[Point], cycleCopy2: List[Point], amelioration2:List[Boolean]): List[Point] = cycleCopy2 match {
              case Nil => boucleFor1(cycle, cycleCopy, cycle,amelioration2)
              case xj :: tail2 =>
                if (xj != xi && xj != getNextElem(cycle, xi).getOrElse(cycle.head) && xj != getPreviousElem(cycle, xi).getOrElse(cycle.last)) {

                    if ((distance(xi,getNextElem(cycle,xi).getOrElse(cycle.head)) + distance(xj,getNextElem(cycle,xj).getOrElse(cycle.head)))
                    > (distance(xi,xj) + distance(getNextElem(cycle,xi).getOrElse(cycle.head),getNextElem(cycle,xj).getOrElse(cycle.head)))) {

                      def permEdges(cycle:List[Point],xi:Point,xi_suiv:Point,xj:Point,xj_suiv:Point):List[Point] = cycle match {
                        case Nil => Nil

                        case `xi` :: _ :: tail => xi :: xj :: permEdges(tail,xi, xi_suiv, xj, xj_suiv)
                        case `xj` :: _ :: tail => xi_suiv :: xj_suiv :: permEdges(tail,xi, xi_suiv, xj, xj_suiv)
                        case p::tail => p::permEdges(tail,xi, xi_suiv, xj, xj_suiv)
                      }
                      val newCycle:List[Point] = permEdges(cycle,xi,xj,getNextElem(cycle,xi).getOrElse(cycle.head),getNextElem(cycle,xj).getOrElse(cycle.head))

                      val b:Int = newCycle.indexOf(getNextElem(cycle,xi).getOrElse(cycle.head))
                      val x:Int = newCycle.indexOf(xj)

                      val newCycleOrdered:List[Point] = if (x < b) newCycle.take(x) ::: newCycle.slice(x + 1,b+1).reverse ::: newCycle.takeRight(newCycle.size - b)
                      else newCycle.take(b) ::: newCycle.slice(b + 1,x+1).reverse ::: newCycle.takeRight(newCycle.size - x)
                      boucleFor2(xi,newCycleOrdered,cycleCopy,tail2,true::amelioration2)
                    }
                    else boucleFor2(xi,cycle,cycleCopy,tail2,false::amelioration2)
                }
                else boucleFor2(xi,cycle, cycleCopy, tail2,false::amelioration2)

            }
          boucleFor2(xi,cycle,tail1,cycleCopy2,amelioration1)
        }
        boucleFor1(cycle, cycleCopy, cycleCopy2,List[Boolean]())
      } else {
        cycle
      }
      }

      boucleWhile(cycle,cycle,cycle,List[Boolean](true))
    }
    val twoOpt = TwoOpt(NNPath)
    Path(twoOpt :+ twoOpt.head)
}
}
