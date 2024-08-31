package sg.edu.ntu.springboot_simple_expenses.exceptions;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException(String id) {
        super("Expense with id '" + id + "' cannot be found");
    }
}
