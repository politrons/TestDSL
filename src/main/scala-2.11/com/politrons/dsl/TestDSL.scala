package com.politrons.dsl

import org.scalatest.FeatureSpecLike

import scalaz.~>

/**
  * Created by Pablo Perez Garcia on 06/07/2017.
  *
  * This is the main class of the test framework. Using scalaz we create a DSL where the client can use
  * the common TDD components Given, When, Then.
  *
  * Thanks to this DSL we can separate structure from behaviour of our application.
  *
  * The implementation of this DSL is using the interpreter scenario which apply the logic of the DSL.
  * That interpreter must be implemented by the consumer of the DSL.
  */
trait TestDSL extends FeatureSpecLike with Actions {

  /**
    * Using Free monads we need to provide an interpreter that match the algebras used [Action].
    *
    * Here we define that Action type is transformed in Id which is a generic.
    *
    * @return Any possible value defined in the algebra Action´s
    */
  override def scenario: Action ~> Id = new (Action ~> Id) {
    def apply[A](a: Action[A]): Id[A] = a match {
      case _Action(action, any) => interpreter(action, any)
    }
  }

  /**
    * This interpreter receive the action and value from the Algebra, and is the moment where we give
    * the DSL behave. Using this aproach we can reuse the same DSL with many different interpreter implementations.
    * @param action of the Algebra, normally used to detect which action of the DSL just happen
    * @param any value passed in the pipeline of the DSL
    * @return Any value that the interpreter want to propagate through the pipeline.
    */
  def interpreter(action: Any, any: Any): Any


}




