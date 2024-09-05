package sg.edu.ntu.springboot_simple_expenses.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import sg.edu.ntu.springboot_simple_expenses.exceptions.ExpenseNotFoundException;
import sg.edu.ntu.springboot_simple_expenses.models.Expense;

@Slf4j
@RestController
@RequestMapping("expenses")
public class ExpenseController {
    
    List<Expense> expenseList = new ArrayList<>();

    private int findExpenseIndexByUuid(String uuid) {
        // loops through the expenses array list and compare the uuid, return the index of the element with that uuid
        for (Expense expense : expenseList) {
            if (expense.getId().equals(uuid)) {
                return expenseList.indexOf(expense);
            }
        }
        // throw exception if no elements in the expenses array has this uuid
        throw new ExpenseNotFoundException(uuid);
    }

    private List<Expense> filterExpenseListByCategory(String category, List<Expense> unfilteredExpenseList) {
        return unfilteredExpenseList.stream()
            .filter(expense -> expense.getCategory().equals(category))
            .toList();
    }

    private List<Expense> filterExpenseListByAmount(Double minAmount, Double maxAmount, List<Expense> unfilteredExpenseList) {
        List<Expense> filteredExpenseList = new ArrayList<>(unfilteredExpenseList);
        
        if (minAmount != null) {
            filteredExpenseList = filteredExpenseList.stream()
                .filter(expense -> expense.getAmount() > minAmount)
                .toList();
        }

        if (maxAmount != null) {
            filteredExpenseList = filteredExpenseList.stream()
                .filter(expense -> expense.getAmount() < maxAmount)
                .toList();
        }

        return filteredExpenseList;
    }

    // CREATE
    @PostMapping({"", "/"}) // localhost:9090/expenses, localhost:9090/expenses/
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        expenseList.add(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseList.get((expenseList.size() - 1)));
    }

    // READ - One
    @GetMapping("/{uuid}")
    public ResponseEntity<Expense> findExpenseById(@PathVariable String uuid) {
        try {
            return ResponseEntity.ok(expenseList.get(findExpenseIndexByUuid(uuid)));
        } catch (ExpenseNotFoundException enfe) {
            log.error(enfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // READ - All
    @GetMapping({"", "/"})
    public ResponseEntity<List<Expense>> findAllExpense(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) Double minAmount,
        @RequestParam(required = false) Double maxAmount) {
        // @RequestParam(required) if set to false, client side need not provide parameter, so arguments can be null
        // take note to use Wrapper classes, i.e. Double instead of primitive double type, so that it can be null
            List<Expense> filteredExpenseList = new ArrayList<>(expenseList);

            // client side provided 'category' parameter, so we filter by 'category'
            if (category != null && !category.isEmpty() && !category.isBlank()) {
                filteredExpenseList = filterExpenseListByCategory(category, filteredExpenseList);
            }

            if (minAmount != null || maxAmount != null) {
                filteredExpenseList = filterExpenseListByAmount(minAmount, maxAmount, filteredExpenseList);
            }

            return ResponseEntity.ok(filteredExpenseList);
    }

    // UPDATE
    @PutMapping("/{uuid}")
    public ResponseEntity<Expense> updateExpense(@PathVariable String uuid, @RequestBody Expense expense) {
        try {
            int index = findExpenseIndexByUuid(uuid);
            expenseList.set(index, expense);
            return ResponseEntity.ok(expenseList.get(index));
        } catch (ExpenseNotFoundException enfe) {
            return createExpense(expense);
        }
    }

    // DELETE
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Expense> deleteExpense(@PathVariable String uuid) {
        try {
            return ResponseEntity.ok(expenseList.remove(findExpenseIndexByUuid(uuid)));
        } catch (ExpenseNotFoundException enfe) {
            log.error(enfe.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
