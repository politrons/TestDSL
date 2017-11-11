package com.politrons.dsl

import scala.language.postfixOps
import scala.util.matching.Regex

/**
  * Created by Pablo Perez Garcia on 20/02/2017.
  */
class OptionExampleIT extends TestDSL {

  case class Product(name: String)

  case class Basket(products: List[String])

  info("This is a simple test where you can using the DSL mix the result of the previous steps one after the other")
  feature("Online basket to prove the TestDSL") {
    scenario(s"Add and remove some products from the basket and check result") {
      Given("an empty basket", Basket(List()))
        .When("I add product 'coca-cola'")
        .And("I add product 'milk'")
        .And("I add product 'ham'")
        .And("I add product 'cornflakes'")
        .And("I remove product 'ham'")
        .Then("The number of products is '3'")
        .runScenario
    }
  }

  /**
    * Method to make an action over the DSL action message that we use
    *
    * @param action    The message user want to make
    * @param anyBasket Data user send from previous step
    * @return
    */
  override def interpreter(action: Any, anyBasket: Any): Any = {
    action match {
      case "an empty basket" => Some(anyBasket)
      case ADD_PRODUCT(product) => addProduct(anyBasket, product)
      case REMOVE_PRODUCT(product) => removeProduct(anyBasket, product)
      case NUMBER_OF_PRODUCTS(numberOfProducts) => checkNumberOfProducts(anyBasket, numberOfProducts)
      case _ => throw new RuntimeException(s"Error action not controlled")
    }
  }

  private def addProduct(anyBasket: Any, product: String): Option[Basket] = {
    getBasket(anyBasket).map(basket =>
      basket.copy(products = basket.products ++ List(product)))
  }

  private def removeProduct(anyBasket: Any, newProduct: String): Option[Basket] = {
    getBasket(anyBasket).map(basket =>
      basket.copy(products = basket.products.filter(product => product != newProduct)))
  }

  private def checkNumberOfProducts(anyBasket: Any, numberOfProducts: String): Any = {
    assert(numberOfProducts.toInt == getBasket(anyBasket).get.products.length)
    anyBasket
  }

  private def getBasket(anyBasket: Any): Option[Basket] = {
    anyBasket.asInstanceOf[Option[Basket]]
  }

  val ADD_PRODUCT: Regex = s"I add product '(.*)'".r
  val REMOVE_PRODUCT: Regex = s"I remove product '(.*)'".r
  val NUMBER_OF_PRODUCTS: Regex = s"The number of products is '(.*)'".r


}