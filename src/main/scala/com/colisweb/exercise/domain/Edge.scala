package com.colisweb.exercise.domain

final case class Edge(from: Point, to: Point) {
  def distance2: Double = sqr(from.x - to.x) + sqr(from.y - to.y)

  def distance: Double = math.sqrt(distance2)

  private def sqr(d: Double): Double = d * d

  override def toString: String = s"${from.x}x${from.y}-${to.x}x${to.y}"
}
