package infnet.tp2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private final CustomerService customerService = new CustomerService();

    @Test
    @DisplayName("Cadastro deve falhar para idade menor que 18 (limite inferior inválido)")
    void registerCustomer_deveFalharParaIdadeMenorQue18() {
        Customer customer = new Customer(1, "João", "joao@example.com", 17, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cadastro deve ser aceito para idade igual a 18 (limite inferior válido)")
    void registerCustomer_deveAceitarIdadeIgualA18() {
        Customer customer = new Customer(2, "Maria", "maria@example.com", 18, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Cadastro deve ser aceito para idade igual a 99 (limite superior válido)")
    void registerCustomer_deveAceitarIdadeIgualA99() {
        Customer customer = new Customer(3, "Ana", "ana@example.com", 99, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Cadastro deve falhar para idade maior que 99 (limite superior inválido)")
    void registerCustomer_deveFalharParaIdadeMaiorQue99() {
        Customer customer = new Customer(4, "Carlos", "carlos@example.com", 100, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cliente ativo deve ser atualizado corretamente")
    void updateCustomer_deveAtualizarClienteAtivo() {
        Customer customer = new Customer(5, "Lucas", "lucas@antigo.com", 30, true);

        boolean resultado = customerService.updateCustomer(
                customer,
                "Lucas Novo",
                "lucas.novo@example.com",
                31
        );

        assertTrue(resultado);
        assertEquals("Lucas Novo", customer.getName());
        assertEquals("lucas.novo@example.com", customer.getEmail());
        assertEquals(31, customer.getAge());
    }

    @Test
    @DisplayName("Cliente inativo não deve ser atualizado")
    void updateCustomer_naoDeveAtualizarClienteInativo() {
        Customer customer = new Customer(6, "Paulo", "paulo@example.com", 40, false);

        boolean resultado = customerService.updateCustomer(
                customer,
                "Paulo Novo",
                "paulo.novo@example.com",
                41
        );

        assertFalse(resultado);
        assertEquals("Paulo", customer.getName());
        assertEquals("paulo@example.com", customer.getEmail());
        assertEquals(40, customer.getAge());
    }

    @Test
    @DisplayName("Cliente ativo deve ser excluído corretamente")
    void deleteCustomer_deveExcluirClienteAtivo() {
        Customer customer = new Customer(7, "Fernanda", "fernanda@example.com", 25, true);

        boolean resultado = customerService.deleteCustomer(customer);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Cliente inativo não deve ser excluído")
    void deleteCustomer_naoDeveExcluirClienteInativo() {
        Customer customer = new Customer(8, "Rafael", "rafael@example.com", 28, false);

        boolean resultado = customerService.deleteCustomer(customer);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cadastro deve aceitar e-mail válido (partição válida)")
    void registerCustomer_deveAceitarEmailValido() {
        Customer customer = new Customer(9, "Valid Email", "usuario.valido@example.com", 30, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Cadastro deve rejeitar e-mail sem arroba (partição inválida)")
    void registerCustomer_deveRejeitarEmailSemArroba() {
        Customer customer = new Customer(10, "Sem Arroba", "usuario.exemple.com", 30, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cadastro deve rejeitar e-mail sem domínio adequado (partição inválida)")
    void registerCustomer_deveRejeitarEmailSemDominio() {
        Customer customer = new Customer(11, "Sem Dominio", "usuario@example", 30, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Cadastro completo deve ser bem-sucedido com todos os campos válidos")
    void registerCustomer_deveCadastrarClienteComDadosValidos() {
        Customer customer = new Customer(12, "Cliente Completo", "cliente.completo@example.com", 35, true);

        boolean resultado = customerService.registerCustomer(customer);

        assertTrue(resultado);
        assertEquals(12, customer.getId());
        assertEquals("Cliente Completo", customer.getName());
        assertEquals("cliente.completo@example.com", customer.getEmail());
        assertEquals(35, customer.getAge());
        assertTrue(customer.isActive());
    }
}
