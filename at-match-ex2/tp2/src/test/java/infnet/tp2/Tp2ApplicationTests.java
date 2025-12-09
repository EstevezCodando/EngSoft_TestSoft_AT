package infnet.tp2;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class MathFunctionsPropriedadesTest {

    @Property
    boolean dobroDeNumeroNaoNegativoDeveSerMaiorOuIgualAoNumero(
            @ForAll("numerosNaoNegativos") int numero) {

        int resultado = MathFunctions.multiplyByTwo(numero);
        return resultado >= numero;
    }

    @Property
    boolean dobroDeNumeroParDeveSerPar(@ForAll("numerosPares") int numeroPar) {
        int resultado = MathFunctions.multiplyByTwo(numeroPar);
        return resultado % 2 == 0;
    }

    @Provide
    Arbitrary<Integer> numerosNaoNegativos() {
        return Arbitraries.integers()
                .between(0, 1_073_741_823);
    }

    @Provide
    Arbitrary<Integer> numerosPares() {
        return Arbitraries.integers()
                .between(-1_000_000, 1_000_000)
                .filter(numero -> numero % 2 == 0);
    }
}
