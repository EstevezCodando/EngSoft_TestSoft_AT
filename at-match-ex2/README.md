#TP2

## E1 Testes Baseados em Propriedades — Resumo Objetivo

Testes baseados em propriedades (property-based testing) verificam **regras gerais** que o software deve cumprir para **qualquer entrada válida**, em vez de depender de poucos casos de teste específicos.  
Enquanto testes tradicionais verificam “entrada → saída”, testes baseados em propriedades definem **invariantes** (ex.: tamanho não muda, valores permanecem, resultado é ordenado).  
O framework então gera automaticamente centenas de entradas aleatórias para tentar quebrar essas propriedades.

### E1.2 Principais vantagens

- Maior cobertura de entradas sem esforço manual.
- Descoberta automática de casos de canto (listas vazias, repetidos, negativos).
- Menos viés do testador, pois os dados são gerados automaticamente.
- Melhora a qualidade da especificação, focando em regras do domínio e não em exemplos pontuais.
- Quando há falha, a ferramenta reduz o caso para o exemplo mínimo, facilitando o debug.

---

### E1.3 Exemplo Prático (Java + Jqwik)

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/test/java/infnet/tp2/Tp2ApplicationTests.java

## E2

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/test/java/infnet/tp2/Tp2ApplicationTests.java

## E3

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/main/java/infnet/tp2/OrderService.java

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/test/java/infnet/tp2/OrderServiceTest.java

## E4

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/main/java/infnet/tp2/CustomerService.java

https://github.com/EstevezCodando/EngSoft_TesteSoftware_tp2/blob/main/tp2/src/test/java/infnet/tp2/CustomerServiceTest.javaS
