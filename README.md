Aplicação dos Princípios SOLID

A arquitetura da aplicação foi estruturada seguindo os princípios SOLID, com o objetivo de garantir baixo acoplamento, alta coesão, facilidade de manutenção e extensibilidade.

Single Responsibility Principle (SRP)

Cada camada da aplicação possui uma responsabilidade única e bem definida:

Controller → responsável apenas pela exposição dos endpoints HTTP e validação das requisições.
Service → concentra as regras de negócio da aplicação.
Client → responsável exclusivamente pela comunicação com a PokéAPI.
Cache → gerencia estratégias de armazenamento temporário e otimização de acesso.
Sorting → implementa exclusivamente os algoritmos de ordenação.

Essa separação reduz complexidade e facilita manutenção e testes.

Open/Closed Principle (OCP)

A aplicação foi projetada para permitir extensão sem necessidade de modificar o fluxo principal existente.

Novos tipos de ordenação podem ser adicionados através de novas estratégias de sorting, mantendo o restante da aplicação inalterado.

Liskov Substitution Principle (LSP)

Os componentes podem depender de abstrações ao invés de implementações concretas.

Por exemplo, uma interface PokemonClient permite substituir diferentes implementações de consumo externo sem impactar a camada de serviço.

Interface Segregation Principle (ISP)

As interfaces da aplicação são pequenas e específicas, evitando contratos excessivamente genéricos.

Cada componente possui apenas os métodos necessários para sua responsabilidade, como:

PokemonClient
PokemonSorter
PokemonCache

Isso reduz acoplamento e melhora legibilidade.

Dependency Inversion Principle (DIP)

As camadas superiores não instanciam diretamente suas dependências.

Toda a injeção de dependência é realizada através de constructor injection utilizando o ecossistema Spring, permitindo:

maior desacoplamento;
facilidade de testes unitários;
substituição simplificada de implementações;
melhor manutenção da arquitetura.