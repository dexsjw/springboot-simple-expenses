package sg.edu.ntu.simpleexpenses;

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

    public Expense() {
        this.id = UUID.randomUUID().toString();
    }    
}
