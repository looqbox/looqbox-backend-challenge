## Would you like to work with us? Apply [here](https://looqbox.gupy.io/)!

![Looqbox](https://github.com/looqbox/looqbox-backend-challenge/blob/main/logo.png)
# Backend Challenge

## Description
In this challenge you will need to build a **MICROSERVICE** using the stack below and the provided **API**.

We will **NOT** use anything from your project other than evaluate your skills and you are free to use this project in your portfolio.

## Stack
We use:
- Java/Kotlin (We accept **ANY** of them in the test)
- `Spring Boot` for the framework
- `Gradle` for dependency management and local deployment

## Submitting
- Create a public repository with your code in it.
- Send the link to the **HR** team for evaluation.

# Guidelines
### You need to make a HTTP REST API that:
- Consumes the [PokeAPI](https://pokeapi.co/) data.
- Provides an endpoint to query pokemons based on the substring of its name. For example:
  - Request: `GET /pokemons?name=pidge`
  - Expected response: ```{"result" : ["pidgey", "pidgeotto", "pidgeot"]}```
- Implement a highlight regarding the piece of its queried name. For example:
  - The queried name was `pi`
  - The highlight object must be ```{"name": "pikachu", "highlight": "<pre>pi</pre>kachu"}```
- You need to apply sorting using a algorithm of your choice for: 
  - Pokemon name's length in crescent order; 
  - Pokemon name's alphabetical order.
- Draw a **diagram** explaining your architecture.

### You can't use:
- Any library that generates code (Example: **Lombok**)
- Any sorting library, nor any sort methods provided by Java Standard Library. `Comparators` and `Collections.swap` are **OK** to use

### Observations:
- The project must support **ALL** existing pokemons in the provided **API**.
- It’s very important to **explain** your implemented logic of the algorithm used *(For instance, you can use inline comments on the source code)*

## Bonus Points
- Design Patterns
- Unit Testing
- Dockerize the application
- Explain the **Big-Ω** of your sorting algorithms

## Useful links
- [Spring Framework](https://spring.io/)
- [Gradle](https://gradle.org/)
- [PokeApi docs](https://pokeapi.co/docs/v2.html)
