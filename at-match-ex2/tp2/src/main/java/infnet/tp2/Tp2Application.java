package infnet.tp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Tp2Application {

    public static void main(String[] args) {
        int entrada = 10;
        int resultado = MathFunctions.multiplyByTwo(entrada);

        System.out.println("Entrada: " + entrada);
        System.out.println("Saída esperada da função: " + resultado);
    }
}