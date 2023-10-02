## Would you like to work with us? Apply [here](https://looqbox.gupy.io/)!

![Looqbox](logo.png)
# Backend Challenge

## Description

In this challenge you will need to build a **MICROSERVICE** using the stack below and the provided API.

We will **NOT** use anything from your project other than evaluate your skills.

## Stack

You need to use:

- **Java 11+** or **Kotlin 1.8+** (you choose), here in our stack we use Kotlin.
- **Spring Boot** for the framework
- **Gradle** for dependency management and local deployment

## Submitting

- Create a public repository with your code in it.
- Send the link to the HR team for evaluation.

# Guidelines

You will build a REST API that must comply with **ALL** the specified requirements below.

## Functional Requirements

- Consume the [PokéAPI](https://pokeapi.co/docs/v2) data.
- Create and expose 2 endpoints:

**1. GET /pokemons**

- **query**: String, optional, if the query is empty, it should consider all pokemons listed in the PokeAPI.
- **sort**: String/Enum, optional (to the user), defaults to <u>alphabetical</u> sorting in case the parameter is not provided

The idea behind this endpoint is to be able to search by pokémons by their name - the user will send a **part** (any part) of the pokémon **name** as the `query` param to this endpoint, and the service must reply with a list of the pokémons. The search must be case **insensitive**.

Also, the user has to have the possibility to also specify what kind of sorting they want, and the service must comply and sort the pokémon list by the specified order (more on sort types below). The returned JSON must comply with the following format (the pokémon names are just an example):

```JSON
{
   "result": [
      "pidgey",
      "pidgeotto",
      "pidgeot"
   ]
}
```

**2. GET /pokemons/highlight**

- **query**: String, optional, if the query is empty, it should consider all pokemons listed in the PokeAPI.
- **sort**: String/Enum, optional (to the user), defaults to <u>alphabetical</u> sorting in case the parameter is not provided

This endpoint has, for the most part, the same requirements as the first one (must receive the same parameters in the same way), the only difference is the response requirement: alongside the pokémon name, the response must also highlight the substring that matched the pokémon name. The way you must do that is by surrounding the substring with `<pre> </pre>` tags. If the query is empty, it should consider all pokemons listed in the PokeAPI.

Assuming that the user has searched for `pi`, this would be the expected response (the results were truncated for simplicity purposes).

```JSON
{
  "result": [
    {
        "name": "pichu",
        "highlight": "<pre>pi</pre>chu"
    },
    {
        "name": "pikachu",
        "highlight": "<pre>pi</pre>kachu"
    }
  ]
}
```

- Pick and implement a **sorting algorithm** of your choice. Then, use this algorithm to provide sorting support on the two implemented endpoints. Your service must provide support for at least these two sorting options:
  - **Alphabetical**: sort alphabetically by the pokémon name in crescent order;
  - **Length**: sort by the pokémon name's length in crescent order.

- All Spring dependency injection must be done through constructor injection (you can’t use `@Autowired`).

## Out of scope:

- [Java Records](https://www.baeldung.com/java-record-keyword)
- Any **sorting library**, nor anything related to sorting from the Java/Kotlin Standard Library, this includes: `Collections.sort`, `Collections.swap`, `Comparators`, etc.
- Any **caching library** (if you want to implement cache, you must implement it manually).
- Any **automatic task scheduler library** (for recurring tasks). If you want to implement a feature that uses automatic task scheduling, you must only use Java/Kotlin Standard Library or your own classes and methods.
- Anything that auto generate code or auto generate implementations for interfaces/abstract classes, such as [Lombok](https://projectlombok.org/features/all), [Feign Client](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html), [Retrofit2](https://square.github.io/retrofit), [Spring Repositories](https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html) (do not mistake with [Spring Stereotypes](http://blog.triadworks.com.br/entendendo-os-stereotypes-do-spring)), etc.

## Non-functional Requirements

- You need to **explain** your implemented logic of the sorting algorithm used (for instance, you can use inline comments on the source code).
- Explain the **Big-θ** of your sorting algorithms.
- The project must consume and expose **ALL** existing pokémons from the **PokéAPI**.
- Draw a **diagram** explaining your architecture.
- Your API must be built with both performance and maintainability in mind.
- Identify bottleneck points in your code, if any, and provide a possible solution for them.

# Bonus Points

- Design Patterns
- Unit Testing
- Dockerize the application
- Caching

# Useful links

- [Spring Framework](https://spring.io/)
- [Gradle](https://gradle.org/)
- [PokéApi docs](https://pokeapi.co/docs/v2)
