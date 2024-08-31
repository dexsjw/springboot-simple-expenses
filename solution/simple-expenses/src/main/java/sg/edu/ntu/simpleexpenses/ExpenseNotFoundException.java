package sg.edu.ntu.simpleexpenses;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(String uuid) {
        super("Expense with id: " + uuid + " is not found.");
    }
}
