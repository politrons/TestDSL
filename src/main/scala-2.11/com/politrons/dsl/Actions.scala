package com.politrons.dsl

import scalaz.Free._
import scalaz._

/**
  * Created by Pablo Perez Garcia on 07/07/2017.
  *
  * Here we define the action functions which create the Free monads using the algebras that we defined, and passing the values received.
  * Those functions are the real DSL that the client will use in their implementations.
  */
trait Actions extends Algebras {

  def Given(action: String, any: Any): ActionMonad[Any] ={
    liftF[Action, Any](_Action(action, any))
  }

  implicit class customFree(free: ActionMonad[Any]) {

    def When(action: String): ActionMonad[Any] = {
      free.flatMap(any =>liftF[Action, Any](_Action(action, any)))
    }

    def Then(action: String): ActionMonad[Any] = {
      free.flatMap(any => liftF[Action, Any](_Action(action, any)))
    }

    def And(action: String): ActionMonad[Any] = {
      free.flatMap(any => liftF[Action, Any](_Action(action, any)))
    }

    def And(action: String, newAny: Any): ActionMonad[Any] ={
      free.flatMap(any => liftF[Action, Any](_Action(action, newAny)))
    }

    def runScenario = free.foldMap(scenario)
  }

  def scenario: Action ~> Id

}
