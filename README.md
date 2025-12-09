# AT – Avaliação Final

# AT – Exercício 1 – Teste Exploratório e Análise de Comportamento Esperado

## Ex1.1 – Teste Exploratório Manual

O teste exploratório realizado sobre o sistema de Cálculo de IMC demonstrou funcionamento correto para entradas válidas, como (70, 1.75) e (90, 1.80). Entretanto, o uso de vírgula, comum no Brasil, como em `1,75`, gera `NumberFormatException`, causando encerramento abrupto do programa. A aplicação aceita pesos e alturas negativos ou iguais a zero, produzindo IMCs matematicamente calculáveis, porém fisicamente impossíveis. Altura zero causa divisão por zero e resulta em `Infinity`.

Trecho de código evidenciando ausência de tratamento de exceções:

```java
peso = Double.parseDouble(scannerPeso.nextLine());
altura = Double.parseDouble(scannerAltura.nextLine());
```

E trecho que permite divisão por zero:

```java
public static double calcularPeso(double peso, double altura) {
    return peso / (altura * altura);
}
```

## Ex1.2 – Problemas Identificados

Foram identificadas falhas graves: ausência de validação das entradas; falta de mensagens de erro amigáveis; encerramento abrupto em caso de falhas; classificação do IMC com intervalos sobrepostos; aceitação de pesos e alturas irreais; e falta de resiliência no fluxo de interação.

A lógica de classificação contém problemas:

```java
if (imc < 16.0) {
    return "Magreza grave";
}
else if (imc == 16.0 || imc < 17.0) {
    return "Magreza moderada";
}
else if (imc == 17.0 || imc < 18.5) {
    return "Magreza leve";
}
```

As expressões `imc == 16.0 || imc < 17.0` e `imc == 17.0 || imc < 18.5` tornam impossível separar limites corretamente.

## Ex1.3 – Especificação Funcional Esperada

O sistema deve:

1. Aceitar entrada de peso e altura usando ponto ou vírgula.
2. Validar peso > 0 e ≤ 500 kg.
3. Validar altura ≥ 0,50 m e ≤ 3,00 m.
4. Calcular o IMC por: IMC = peso / (altura × altura).
5. Classificar IMC com faixas não sobrepostas:

   - < 16,0: Magreza grave
   - 16,0–16,99: Magreza moderada
   - 17,0–18,49: Magreza leve
   - 18,5–24,99: Saudável
   - 25,0–29,99: Sobrepeso
   - 30,0–34,99: Obesidade I
   - 35,0–39,99: Obesidade II
   - ≥ 40: Obesidade III

6. Exibir IMC com duas casas decimais.
7. Tratar erros sem encerrar a aplicação abruptamente.

## Ex1.4 – Casos de Teste (BVA + Partições de Equivalência)

Para peso:

- 0 (inválido)
- 0.1 (limite inferior técnico)
- 500 (válido limite)
- 500.1 (inválido)

Para altura:

- 0 (inválido)
- 0.49 (inválido)
- 0.50 (mínimo válido)
- 3.00 (máximo válido)
- 3.01 (inválido)

Para IMC (limites de classificação):

- 15.99 / 16.0 / 16.01
- 16.99 / 17.0 / 17.01
- 18.49 / 18.5 / 18.51
- 24.99 / 25.0 / 25.01
- 29.99 / 30.0 / 30.01
- 34.99 / 35.0 / 35.01
- 39.99 / 40.0 / 40.01

Trecho de cálculo utilizado nos testes:

```java
double imc = IMCService.calcular(peso, altura);
String classificacao = IMCService.classificar(imc);
```

## Ex1.5 – Justificativa dos Cenários

Os cenários foram definidos com base em:

- Risco clínico direto: classificações erradas podem induzir decisões incorretas sobre saúde.
- Tendência natural a erros humanos (vírgulas, valores fora do domínio plausível).
- Limites matemáticos sensíveis (divisão por zero, negativos, extremos).
- Aplicação de Boundary Value Analysis, pois erros frequentes ocorrem nos limites.
- Partições de equivalência para garantir que cada comportamento possível seja exercitado.
- Garantia de cobertura completa dos intervalos da função `classificarIMC`.

# AT – Exercício 2 – Testes Baseados em Propriedades e Refatoração para Injeção de Dependência

## Ex2.1.1 – Propriedade para `MultiplyByTwo`: resultado sempre par

**Ideia da propriedade**

Para qualquer número inteiro de entrada, o resultado de `MultiplyByTwo` deve ser sempre par, pois é o produto de um inteiro por 2. Mesmo em cenários de overflow, a aritmética de inteiros em duas complementares preserva a paridade.

**Trecho de código de teste com jqwik**

Caminho sugerido:  
`src/test/java/br/com/at/math/MathFunctionsPropertyTests.java`

```java
package br.com.at.math;

import net.jqwik.api.*;
import org.junit.jupiter.api.Assertions;

class MathFunctionsPropertyTests {

    private final MathFunctions mathFunctions =
        new MathFunctions(operation -> {
            // Logger fake para os testes; em cenários reais, usar mock (Mockito, etc.).
        });

    @Property
    void multiplyByTwo_deveRetornarSempreNumeroPar(@ForAll int numero) {
        int resultado = mathFunctions.multiplyByTwo(numero);
        Assertions.assertEquals(0, resultado % 2,
            "O resultado de multiplyByTwo deve ser sempre par.");
    }
}
```

---

## Ex2.1.2 – Propriedade para `GenerateMultiplicationTable`: todos múltiplos do número original

**Ideia da propriedade**

Para qualquer número inteiro e um limite positivo, todos os elementos da tabela gerada devem obedecer à relação:  
`resultado[i] == numero * (i + 1)` para `i` de `0` até `limit - 1`.  
Isso cobre casos normais, negativos, zero e limites maiores, garantindo consistência da tabela.

**Trecho de código de teste com jqwik**

```java
package br.com.at.math;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Assertions;

class MathFunctionsPropertyTests {

    private final MathFunctions mathFunctions =
        new MathFunctions(operation -> {});

    @Property
    void generateMultiplicationTable_todosElementosDevemSerMultiplosDoNumero(
            @ForAll int numero,
            @ForAll @IntRange(min = 1, max = 100) int limite) {

        int[] tabela = mathFunctions.generateMultiplicationTable(numero, limite);

        Assertions.assertEquals(limite, tabela.length,
            "O tamanho da tabela deve ser igual ao limite.");

        for (int i = 0; i < limite; i++) {
            int esperado = numero * (i + 1);
            Assertions.assertEquals(esperado, tabela[i],
                "Elemento da posição " + i + " não corresponde ao múltiplo esperado.");
        }
    }
}
```

Esse teste cobre cenários realistas, negativos, positivos e zero (tabela de zeros), e limita o tamanho do array para evitar explosões de memória.

---

## Ex2.1.3 – Propriedade para `IsPrime`: nenhum divisor além de 1 e dele mesmo

**Ideia da propriedade**

Para qualquer número inteiro inteiro `n ≥ 2`, se `IsPrime(n)` retornar `true`, então não pode existir nenhum divisor inteiro `d` com `2 ≤ d ≤ sqrt(n)` tal que `n % d == 0`. Em outras palavras, se a função afirmar que um número é primo, essa afirmação deve ser consistente com a definição matemática de primalidade.

**Trecho de código de teste com jqwik**

```java
package br.com.at.math;

import net.jqwik.api.*;
import net.jqwik.api.constraints.*;
import org.junit.jupiter.api.Assertions;

class MathFunctionsPropertyTests {

    private final MathFunctions mathFunctions =
        new MathFunctions(operation -> {});

    @Property
    void isPrime_quandoRetornaTrue_naoHaDivisoresEntre2ESqrt(
            @ForAll @IntRange(min = 2, max = Integer.MAX_VALUE) int numero) {

        // Só nos interessa investigar os casos em que o método afirma que é primo.
        Assume.that(mathFunctions.isPrime(numero));

        int limite = (int) Math.sqrt(numero);
        for (int divisor = 2; divisor <= limite; divisor++) {
            Assertions.assertNotEquals(0, numero % divisor,
                "Numero " + numero + " não deveria ter divisor " + divisor + ".");
        }
    }
}
```

Esse teste garante que, sempre que a função declara um número como primo, essa declaração é consistente com a ausência de divisores não triviais.

---

## Ex2.1.4 – Propriedade para `CalculateAverage`: média entre o menor e o maior elemento

**Ideia da propriedade**

Para qualquer array de inteiros não vazio:

- A média aritmética deve estar sempre entre o menor e o maior valor do array.
- O método deve lançar exceção quando o array é nulo ou vazio (já coberto por testes tradicionais; aqui focamos na propriedade da média).

**Gerador customizado para arrays não vazios**

```java
package br.com.at.math;

import net.jqwik.api.*;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

class MathFunctionsPropertyTests {

    private final MathFunctions mathFunctions =
        new MathFunctions(operation -> {});

    @Provide
    Arbitrary<int[]> arraysInteirosNaoVazios() {
        return Arbitraries.integers()
                .between(-1_000_000, 1_000_000)
                .array(int[].class)
                .ofMinSize(1)
                .ofMaxSize(1000);
    }

    @Property
    void calculateAverage_mediaDeveEstarEntreMinimoEMaximo(
            @ForAll("arraysInteirosNaoVazios") int[] numeros) {

        double media = mathFunctions.calculateAverage(numeros);
        int minimo = Arrays.stream(numeros).min().orElseThrow();
        int maximo = Arrays.stream(numeros).max().orElseThrow();

        Assertions.assertTrue(media >= minimo && media <= maximo,
                "A média deve estar entre o mínimo e o máximo do array.");
    }
}
```

O gerador cobre arrays com valores grandes (positivos e negativos), tamanhos variados e casos potencialmente problemáticos para overflow de soma, enquanto a propriedade se baseia apenas na relação entre média, mínimo e máximo.

---

## Ex2.2 – Refatoração da `MathFunctions` para Injeção de Dependência com `MathLogger`

### Objetivo da refatoração

A versão original da classe `MathFunctions` utiliza apenas métodos estáticos, o que dificulta o uso de mocks e stubs em testes de unidades que dependem dessa biblioteca. Para melhorar a testabilidade e alinhar-se a princípios de design (como SOLID), a classe foi refatorada para:

1. Aceitar uma dependência via construtor de uma interface `MathLogger`.
2. Armazenar essa dependência internamente.
3. Permitir que, em testes, seja injetado um mock (por exemplo, com Mockito) ou um logger fake.

### Interface `MathLogger`

Caminho sugerido:  
`src/main/java/br/com/at/math/MathLogger.java`

```java
package br.com.at.math;

public interface MathLogger {

    void log(String operation, int[] inputs);
}
```

### Classe `MathFunctions` refatorada

Caminho sugerido:  
`src/main/java/br/com/at/math/MathFunctions.java`

```java
package br.com.at.math;

import java.util.Arrays;

public class MathFunctions {

    private final MathLogger mathLogger;

    public MathFunctions(MathLogger mathLogger) {
        this.mathLogger = mathLogger;
    }

    public int multiplyByTwo(int number) {
        log("multiplyByTwo", new int[]{number});
        return number * 2;
    }

    public int[] generateMultiplicationTable(int number, int limit) {
        int[] result = new int[limit];
        for (int i = 0; i < limit; i++) {
            result[i] = number * (i + 1);
        }
        log("generateMultiplicationTable", new int[]{number, limit});
        return result;
    }

    public boolean isPrime(int number) {
        if (number <= 1) {
            log("isPrime", new int[]{number});
            return false;
        }

        int limite = (int) Math.sqrt(number);
        for (int divisor = 2; divisor <= limite; divisor++) {
            if (number % divisor == 0) {
                log("isPrime", new int[]{number, divisor});
                return false;
            }
        }

        log("isPrime", new int[]{number});
        return true;
    }

    public double calculateAverage(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            log("calculateAverage", new int[0]);
            throw new IllegalArgumentException("Array não pode ser nulo ou vazio.");
        }

        double media = Arrays.stream(numbers)
                .average()
                .orElseThrow(() -> new IllegalArgumentException("Não foi possível calcular a média."));

        log("calculateAverage", numbers);
        return media;
    }

    private void log(String operation, int[] inputs) {
        if (mathLogger != null) {
            mathLogger.log(operation, inputs);
        }
    }
}
```

### Como essa refatoração facilita o uso de mocks

Com essa estrutura, qualquer classe que dependa de `MathFunctions` pode receber uma instância configurada com um `MathLogger` específico. Em testes, podemos injetar facilmente:

```java
MathLogger loggerMock = Mockito.mock(MathLogger.class);
MathFunctions mathFunctions = new MathFunctions(loggerMock);
```

Isso permite:

- Verificar se determinadas operações foram logadas.
- Simular comportamentos específicos do logger.
- Isolar testes de alto nível das implementações concretas de logging.

Dessa forma, o código fica mais flexível, testável e alinhado ao objetivo de ser uma biblioteca matemática de alta confiabilidade, preparada para ser integrada em aplicações críticas.

# AT – Exercício 3 – Teste de API: Funcionalidade, Robustez e Estratégia de Teste

## Ex3.1.1 – Testes para CEP inválido ou inexistente

A API ViaCEP utiliza a rota `/ws/{cep}/json/` para consultas. Os testes devem validar adequadamente:

- **CEP vazio ou nulo:** deve ser bloqueado antes da requisição.
- **CEP com letras ou caracteres especiais:** deve ser rejeitado por validação local.
- **CEP com menos ou mais de 8 dígitos:** inválido por formato.
- **CEP válido mas inexistente:** ViaCEP retorna `{"erro": true}`, e a aplicação deve tratar como “CEP não encontrado”.
- **CEP válido e existente:** deve retornar campos completos corretamente.

Trecho ilustrativo da resposta de CEP inexistente:

```json
{
  "erro": true
}
```

---

## Ex3.1.2 – Testes para consulta por endereço

Rota utilizada:
`/ws/{UF}/{cidade}/{logradouro}/json/`

Cenários testáveis:

- **UF válida + cidade válida + logradouro válido:** retorno esperado com lista de resultados.
- **UF válida + cidade válida + logradouro inexistente:** lista vazia ou retorno indicando ausência.
- **Cidade com e sem acento:** garantir tratamento correto de UTF-8 e URL encoding.
- **UF inválida (tamanho ≠ 2 ou inexistente):** deve ser bloqueada antes da chamada.
- **Cidade inválida (números, símbolos) ou vazia:** deve ser validada localmente.
- **Logradouro vazio ou inválido:** evitar requisições desnecessárias.

Exemplo de requisição válida:

```
https://viacep.com.br/ws/SP/Sao%20Paulo/Avenida%20Paulista/json/
```

---

## Ex3.1.3 – Tabela de decisão

| Caso | UF  | Cidade    | Logradouro             | Situação                | Resultado esperado | Comportamento esperado    |
| ---- | --- | --------- | ---------------------- | ----------------------- | ------------------ | ------------------------- |
| 1    | SP  | Sao Paulo | Avenida Paulista       | Tudo válido             | Lista com itens    | Exibir dados              |
| 2    | SP  | São Paulo | Avenida Paulista       | Acento na cidade        | Lista com itens    | Tratar UTF-8 corretamente |
| 3    | SP  | Sao Paulo | Logradouro inexistente | Não encontrado          | Lista vazia        | Indicar ausência          |
| 4    | SP  | _vazio_   | Avenida Paulista       | Cidade ausente          | Erro               | Bloquear requisição       |
| 5    | SP  | 12345     | Avenida Paulista       | Cidade inválida         | Erro ou vazio      | Tratar adequadamente      |
| 6    | S   | Sao Paulo | Avenida Paulista       | UF com tamanho inválido | Erro               | Bloquear requisição       |
| 7    | ZZ  | Sao Paulo | Avenida Paulista       | UF inexistente          | Vazio              | Informar “UF inválida”    |
| 8    | SP  | Sao Paulo | _vazio_                | Logradouro ausente      | Erro               | Bloquear requisição       |

---

## Ex3.2 – Partição de equivalência, análise de valor limite e justificativa

### CEP

**Partições:**

- Válido (8 dígitos)
- Válido mas inexistente
- Inválido por formato
- Vazio/nulo

**Valores limite:**

- 7 dígitos (inválido)
- 8 dígitos (válido)
- 9 dígitos (inválido)

### UF

**Partições:**

- Válida (2 letras e existente)
- Inexistente (2 letras não pertencentes a estados reais)
- Inválida por tamanho

**Valores limite:**

- Tamanho 1 → inválido
- Tamanho 2 → válido
- Tamanho 3 → inválido

### Cidade

**Partições:**

- Válida (com ou sem acento)
- Inválida (números, caracteres especiais)
- Vazia

**Valores limite:**

- Tamanho 0 (inválido)
- Mínimo (1–2 caracteres)
- Muito longa (stress test)

### Logradouro

**Partições:**

- Válido existente
- Válido porém inexistente
- Vazio
- Inválido (caracteres aleatórios)

**Valores limite:**

- 0 caracteres → inválido
- 1 caractere → mínimo
- Muito longo → teste de robustez

### Justificativa geral

Utilizar **partição de equivalência** reduz a quantidade de testes mantendo ampla cobertura, enquanto a **análise de valor limite** explora pontos propensos a falhas, como tamanho de CEP e UF. A API ViaCEP é tolerante, mas a responsabilidade de interpretar corretamente erros, inexistência de dados, encoding e inputs inválidos recai sobre a aplicação cliente. A estratégia garante robustez, segurança e previsibilidade, essenciais em sistemas que consomem APIs públicas em produção

# AT – Exercício 4

## Automação de Testes End-to-End com Selenium WebDriver

# Ex4.1 – Contexto Geral

O objetivo deste exercício foi automatizar fluxos essenciais de um e-commerce utilizando o site **https://automationexercise.com** como ambiente de testes. A automação inclui:

- Cadastro de novo usuário
- Login com credenciais válidas
- Login com credenciais inválidas
- Estruturação seguindo **Page Object Model (POM)**
- Execução com **JUnit 5**
- Setup via **WebDriverManager**
- Captura de screenshots em falhas

Devido a restrições de rede e ao fato de o host **msedgedriver.azureedge.net** estar fora do ar, foi necessário implementar **fallback para driver local**, garantindo que os testes pudessem rodar independentemente do ambiente.

---

# Ex4.2 – Arquitetura do Projeto

```
src/
 └── test/
      └── java/
           └── br/com/infnet/at/atseleniumex4/
                ├── base/
                │     └── BaseTest.java
                ├── extensions/
                │     └── ScreenshotOnFailureExtension.java
                ├── pages/
                │     ├── HomePage.java
                │     ├── SignupLoginPage.java
                │     ├── AccountCreationPage.java
                │     └── LoggedInHomePage.java
                └── tests/
                      └── FluxoCadastroLoginTests.java
```

A estrutura adota o padrão **POM**, mantendo páginas, testes, base de driver e extensões separados.

---

# Ex4.3 – Configuração do WebDriver + Fallback Offline

O Selenium normalmente utiliza WebDriverManager para baixar automaticamente os drivers.  
Entretanto, no ambiente de execução:

- O domínio **https://msedgedriver.azureedge.net** estava inacessível.
- O WebDriverManager não conseguiu baixar o driver do Edge.
- Uma exceção `UnknownHostException` era lançada.

Para contornar isso, foi implementado:

1. **Tentativa de uso do WebDriverManager**
2. **Fallback automático para driver local (`msedgedriver.exe`)**

Trecho principal:

```java
try {
    WebDriverManager.edgedriver().setup();
    driver = new EdgeDriver();
} catch (Exception e) {
    System.setProperty("webdriver.edge.driver", "C:\webdrivers\msedgedriver.exe");
    driver = new EdgeDriver();
}
```

Esse mecanismo mantém compatibilidade com o enunciado e garante execução robusta.

---

# Ex4.4 – Page Object Model

Cada classe representa uma página real da aplicação:

### **HomePage**

- Acessa a área de login/cadastro.

### **SignupLoginPage**

- Inicia login ou criação de usuário.

### **AccountCreationPage**

- Preenche o formulário obrigatório do cadastro.

### **LoggedInHomePage**

- Valida que o usuário aparece como logado após autenticação.

O uso de POM:

- Aumenta reusabilidade
- Facilita manutenção
- Separa claramente responsabilidades

---

# Ex4.5 – Captura Automática de Screenshots

Foi criada uma extensão JUnit:

```
ScreenshotOnFailureExtension
```

Ela captura screenshots automaticamente no diretório:

```
target/screenshots/
```

A captura ocorre **somente quando o teste falha**, seguindo boas práticas de QA.

---

# Ex4.6 – Testes Criados

### **1. Fluxo de Cadastro + Login com Sucesso**

Valida:

- Abertura da Home
- Acesso ao formulário de cadastro
- Preenchimento obrigatório
- Login automático após criação da conta
- Verificação de que o usuário está logado

### **2. Login Inválido**

Verifica:

- Mensagem de erro exibida
- Comportamento esperado em credenciais incorretas

Ambos os cenários são essenciais em regressão de plataforma e‑commerce.

---

# Ex4.7 – Execução, Problemas e Soluções

Durante a automação, foram encontrados:

### **1. WebDriverManager sem acesso ao host do Edge**

- Motivo: domínio da Microsoft indisponível.
- Solução: fallback para driver local.

### **2. Warnings de Selenium (CDP)**

Avisos como:

```
Unable to find CDP implementation matching 143
```

Eles não impedem a automação e são comuns quando versões do navegador são mais novas que as do Selenium.

### **3. Aviso SLF4J sem provider**

```
SLF4J(W): No SLF4J providers were found.
```

Apenas indica falta de implementação de logging; não afeta os testes.

No final, os testes executaram com sucesso:

✔ O Edge abriu  
✔ A automação navegou corretamente  
✔ Login válido passou  
✔ Login inválido foi validado  
✔ Screenshots configurados  
✔ Todos os testes passaram

# AT – Exercício 5 – Análise Estrutural de Código

## 1. Funcionalidade escolhida

A funcionalidade analisada do repositório **TheAlgorithms/Java** foi o algoritmo **BinarySearch**, localizado em `com.thealgorithms.searches.BinarySearch`.

Essa escolha foi motivada porque possui lógica clara e ramificações explícitas, inclui versões iterativa e recursiva, faz validações importantes (null, array não ordenado) e possibilita ampla cobertura estrutural.

---

## 2. Estratégia de teste estrutural

Foram desenvolvidos testes cobrindo casos de sucesso, falha e validações de erro. A meta foi exercitar todas as decisões (if/else), ramificações, e o fluxo completo da recursão.

---

## 3. Testes unitários (JUnit 5)

```java
// Conteúdo reduzido para demonstrar a estrutura; versão completa utilizada nos testes
assertEquals(2, BinarySearch.search(new int[]{1,3,5,7}, 5));
assertThrows(IllegalArgumentException.class, () -> BinarySearch.search(null, 3));
```

---

## 4. Relatório de cobertura

Gerado com:

```
mvn clean test jacoco:report
```

Relatório disponível em:
`target/site/jacoco/index.html`

A cobertura esperada é próxima de 100% (linhas e ramos), exceto trechos auxiliares internos.

---

## 5. Conclusão

A auditoria evidencia que a funcionalidade BinarySearch é adequadamente testada e robusta frente a entradas inválidas, limites e casos recursivos.
