package sg.edu.ntu.simpleexpenses;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("expenses")
public class ExpensesController {
    private ArrayList<Expense> expenses = new ArrayList<>();

    // helper methods
    private int findExpenseByUuid(String uuid) {
        
        // loops through the expenses array list and compare the uuid, return the index of the element with that uuid
        for (Expense expense : expenses) {
            if (expense.getId().equals(uuid)) {
                return expenses.indexOf(expense);
            }
        }

        // throw exception if no elements in the expenses array has this uuid
        throw new ExpenseNotFoundException(uuid);
    }

    private ArrayList<Expense> findExpensesByCategory(String category, ArrayList<Expense> unfilteredExpenses) {
        ArrayList<Expense> filteredExpenses = new ArrayList<>();
        
        // loop through the expenses array and compare the category
        // add expenses with matching category into filtered expenses array
        for (Expense expense : unfilteredExpenses) {
            if (expense.getCategory().equals(category)) {
                filteredExpenses.add(expense);
            }
        }
        return filteredExpenses;
    }

    private ArrayList<Expense> findExpensesByAmount(Double minAmount, Double maxAmount, ArrayList<Expense> unfilteredExpenses) {
        ArrayList<Expense> filteredExpenses = unfilteredExpenses;

        // removeIf (this condition returns true)
        // remove all expenses amount lesser than minAmount
        if (minAmount != null) {
            filteredExpenses.removeIf(expense -> expense.getAmount() < minAmount);
        }

        // remove all expenses amount greater than maxAmount
        if (maxAmount != null) {
            filteredExpenses.removeIf(expense -> expense.getAmount() > maxAmount);
        }

        return filteredExpenses;
    }

    // READ
    @GetMapping({ "", "/" })
    public ResponseEntity<?> getAllExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {
        ArrayList<Expense> results = new ArrayList<>(expenses);
        // @RequestParam(required) if set to false, client side need not provide parameter, so arguments can be null
        // take note to use Wrapper classes, i.e. Double instead of primitive double type, so that it can be null

        // client side provided 'category' parameter, so we filter by 'category'
        if (category != null) {
            results = findExpensesByCategory(category, results);
        }

        // client side provided 'minAmount' and/or 'maxAmount' parameter, so we filter by amount
        if (minAmount != null || maxAmount != null) {
            results = findExpensesByAmount(minAmount, maxAmount, results);
        }
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Expense> getOneExpense(@PathVariable String uuid) {
        try {
            int index = findExpenseByUuid(uuid);

            return ResponseEntity.status(HttpStatus.OK).body(expenses.get(index));
        } catch (ExpenseNotFoundException enfex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // CREATE
    @PostMapping({ "", "/" }) // localhost:9090/expenses, localhost:9090/expenses/
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        expenses.add(expense);

        return ResponseEntity.status(HttpStatus.CREATED).body(expenses.get(expenses.size() - 1));
    }

    // UPDATE
    @PutMapping("/{uuid}")
    public ResponseEntity<Expense> putExpense(@PathVariable String uuid, @RequestBody Expense expense) {
        try {
            int index = findExpenseByUuid(uuid);
            expenses.set(index, expense);
            return ResponseEntity.status(HttpStatus.OK).body(expenses.get(index));
        } catch (ExpenseNotFoundException enfex) {
            return createExpense(expense);
        }
    }

    // DELETE
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Expense> removeExpense(@PathVariable String uuid) {
        try {
            int index = findExpenseByUuid(uuid);
            Expense deletedExpense = expenses.get(index);
            expenses.remove(index);
            return ResponseEntity.status(HttpStatus.OK).body(deletedExpense);
        } catch (ExpenseNotFoundException enfex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
