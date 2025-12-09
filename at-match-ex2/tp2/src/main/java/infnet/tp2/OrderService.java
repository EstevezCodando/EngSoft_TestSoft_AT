package infnet.tp2;

public class OrderService {

    private final PaymentProcessor paymentProcessor;

    public OrderService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public boolean processOrder(double amount) {
        boolean paymentApproved = paymentProcessor.processPayment(amount);

        if (paymentApproved) {
            System.out.println("Pedido confirmado.");
            return true;
        }

        System.out.println("Pagamento recusado.");
        return false;
    }
}
