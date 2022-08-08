package com.colisweb.exercise.parsing

import com.colisweb.exercise.domain.{Edge, PathProblem, Point}

import scala.io.Source

object ReadFromResources {

  def cycle(fileName: String): List[Point] =
    Source
      .fromResource(s"cycle/$fileName")
      .getLines()
      .collect {
        case line if line.nonEmpty =>
          val Array(_, x, y) = line.split(" ")
          Point(x.toDouble, y.toDouble)
      }
      .toList

  def path(fileName: String): PathProblem = {
    val maze  = Source
      .fromResource(s"path/$fileName")
      .getLines()
      .toList
    val edges = for {
      rowIndex     <- maze.indices
      row           = maze(rowIndex)
      colIndex     <- row.indices
      cell          = row(colIndex)
      if cell != '#'
      point         = Point(rowIndex, colIndex)
      neighbor     <- neighbors(rowIndex, colIndex, maze.indices.last, row.indices.last)
      neighborRow  <- maze.lift(neighbor.x.toInt)
      neighborCell <- neighborRow.lift(neighbor.y.toInt)
      if neighborCell != '#'
    } yield Edge(point, neighbor)

    def findChar(c: Char): Point = {
      val row = maze.indexWhere(_.contains(c))
      val col = maze(row).indexWhere(c.==)
      Point(row, col)
    }

    PathProblem(edges.toList, findChar('S'), findChar('E'))
  }

  private def neighbors(row: Int, col: Int, lastRow: Int, lastCol: Int): Seq[Point] =
    Seq((row, col + 1), (row, col - 1), (row + 1, col), (row - 1, col)).map {
      case (r, c) => Point(r, c)
    }

}
