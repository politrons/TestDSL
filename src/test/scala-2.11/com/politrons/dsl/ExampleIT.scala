package com.politrons.dsl

import scala.language.postfixOps
import scala.util.matching.Regex

/**
  * Created by Pablo Perez Garcia on 20/02/2017.
  */
class ExampleIT extends TestDSL {

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
    * @param action The message user want to make
    * @param any    Data user send from previous step
    * @return
    */
  override def interpreter(action: String, any: Any): Any = {
    action match {
      case "an empty basket" => any
      case ADD_PRODUCT(product) => addProduct(any, product)
      case REMOVE_PRODUCT(product) => removeProduct(any, product)
      case NUMBER_OF_PRODUCTS(numberOfProducts) => checkNumberOfProducts(any, numberOfProducts)
      case _ => throw new RuntimeException(s"Error action not controlled")
    }
  }

  private def checkNumberOfProducts(any: Any, numberOfProducts: String): Any = {
    assert(numberOfProducts.toInt == getBasket(any).products.length)
    any
  }

  private def addProduct(any: Any, product: String): Basket = {
    val basket = getBasket(any)
    basket.copy(products = basket.products ++ List(product))
  }

  private def removeProduct(any: Any, newProduct: String): Basket = {
    val basket = getBasket(any)
    basket.copy(products = basket.products.filter(product => product != newProduct))
  }

  private def getBasket(any: Any) = {
    any.asInstanceOf[Basket]
  }

  val ADD_PRODUCT: Regex = s"I add product '(.*)'".r
  val REMOVE_PRODUCT: Regex = s"I remove product '(.*)'".r
  val NUMBER_OF_PRODUCTS: Regex = s"The number of products is '(.*)'".r


}