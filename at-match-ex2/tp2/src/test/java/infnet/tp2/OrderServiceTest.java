package infnet.tp2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PaymentProcessor paymentProcessor;

    @InjectMocks
    private OrderService orderService;

    private final ByteArrayOutputStream saidaConsole = new ByteArrayOutputStream();
    private PrintStream consoleOriginal;

    @BeforeEach
    void redirecionarSaidaConsole() {
        consoleOriginal = System.out;
        System.setOut(new PrintStream(saidaConsole));
    }

    @AfterEach
    void restaurarSaidaConsole() {
        System.setOut(consoleOriginal);
    }

    @Test
    @DisplayName("Deve confirmar o pedido quando o pagamento for aprovado")
    void deveConfirmarPedidoQuandoPagamentoForAprovado() {
        double amount = 100.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(true);

        boolean resultado = orderService.processOrder(amount);

        assertTrue(resultado);
        verify(paymentProcessor).processPayment(amount);
        String saida = saidaConsole.toString();
        assertTrue(saida.contains("Pedido confirmado."));
    }

    @Test
    @DisplayName("Deve recusar o pedido quando o pagamento for rejeitado")
    void deveRecusarPedidoQuandoPagamentoForRejeitado() {
        double amount = 200.0;
        when(paymentProcessor.processPayment(amount)).thenReturn(false);

        boolean resultado = orderService.processOrder(amount);

        assertFalse(resultado);
        verify(paymentProcessor).processPayment(amount);
        String saida = saidaConsole.toString();
        assertTrue(saida.contains("Pagamento recusado."));
    }
}
