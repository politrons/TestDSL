Author  Pablo Perez Garcia

![My image](src/main/resources/img/dsl-icon.png)


A Test DSL based on ScalaTest and ScalaZ.

This project provide an DSL based on [Free monads](http://eed3si9n.com/learning-scalaz/Free+Monad.html) of scalaZ.
It´s using also scalaTest framework to provide the sites and scenarios.

## Use

* clone the project
```
git clone https://github.com/politrons/TestDSL.git
```
* Run the test example
```
sbt test

```

## Create your own Test and Interpreter**

Free monads are a bunch of algebras that help us to glue monads and then create those pipelines that can be used
as DSLs.
Free monads are meant to be used to separate strucuture and behave on your program, that´ why your DSL always need
an interpreter that understand and give behave of the strucutre of the DSL.

In our example we create a test that is using the DSL to test a shopping basket.

```
      Given("an empty basket", Basket(List()))
        .When("I add product 'coca-cola'")
        .And("I add product 'milk'")
        .And("I add product 'ham'")
        .And("I add product 'cornflakes'")
        .And("I remove product 'ham'")
        .Then("The number of products is '3'")
```

As you can expect giving this DSL we are not doing nothing. Just a glue to contact one monad to another in this pipeline.

In order to give a behave to this strucutre we need to implement our own interpreter.

```
  override def interpreter(action: String, any: Any): Any = {
    action match {
      case "an empty basket" => any
      case ADD_PRODUCT(product) => addProduct(any, product)
      case REMOVE_PRODUCT(product) => removeProduct(any, product)
      case NUMBER_OF_PRODUCTS(numberOfProducts) => checkNumberOfProducts(any, numberOfProducts)
      case _ => throw new RuntimeException(s"Error action not controlled")
    }
  }

```
Now we can give our DSL a behave.



