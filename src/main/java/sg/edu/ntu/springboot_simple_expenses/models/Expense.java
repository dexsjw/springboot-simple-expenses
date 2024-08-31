package sg.edu.ntu.springboot_simple_expenses.models;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expense {

    private final String id;
    private String description;
    private Double amount;
    private String category;
    // Note: Use Wrapper classes, i.e. Double instead of primitive double type, so that it can be null
    // By default, if "double variable" is not declared and initialized, the value will be zero i.e. 0
    // Hence by using Wrapper classes, the default value can be null if the variable is not declared and initialized

    public Expense() {
        this.id = UUID.randomUUID().toString();
    }
}
