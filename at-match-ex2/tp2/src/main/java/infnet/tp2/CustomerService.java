package infnet.tp2;

public class CustomerService {

    public boolean registerCustomer(Customer customer) {
        if (customer.getAge() < 18 || customer.getAge() > 99) {
            return false;
        }

        if (!customer.getEmail().matches("^[\\w.-]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$")) {
            return false;
        }

        return true;
    }

    public boolean updateCustomer(Customer customer, String newName, String newEmail, int newAge) {
        if (!customer.isActive()) {
            return false;
        }

        customer.setName(newName);
        customer.setEmail(newEmail);
        customer.setAge(newAge);
        return true;
    }

    public boolean deleteCustomer(Customer customer) {
        if (!customer.isActive()) {
            return false;
        }
        return true;
    }
}
