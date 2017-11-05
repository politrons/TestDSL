package com.politrons.dsl

import org.scalatest.FeatureSpecLike

import scalaz.~>

/**
  * Created by Pablo Perez Garcia on 06/07/2017.
  *
  * This is the main class of the test framework. Using scalaz we create a DSL where the client can use
  * the common TDD components Given, When, Then.
  * The implementation of this DSL is using the interpreter scenario which apply the logic of the DSL
  */
trait TestDSL extends FeatureSpecLike with Actions {

  /**
    * Using Free monads we need to provide an interpreter that match the algebras used.
    * Here we define that Action type is transformed in Id which is a generic.
    *
    * @return Any possible value defined in the algebra Action´s
    */
  override def scenario: Action ~> Id = new (Action ~> Id) {
    def apply[A](a: Action[A]): Id[A] = a match {
      case _Action(action, any) => interpreter(action, any)
    }
  }

  def interpreter(action: String, any: Any): Any


}




