# Practice-Exericse
- Create an Expenses API using Java Spring Boot - Simple Expenses.
- The application should run on port 9090.
- Use an ArrayList to store the data.
- It should have the endpoints for Create, Get One, Get All, Update, Delete
- Test that your endpoints can perform all the CRUD operations and it returns the appropriate HTTP status code.

### Expenses POJO fields:
```
private String id;
private String description;
private Double amount;
private String category;
```

## Optional Challenge 1
Allow the user to specify an optional category query parameter to the GET ``/expenses`` endpoint
> e.g. localhost:9090/expenses?category=grocery

### Hint:  
Making a copy of an ArrayList
https://www.baeldung.com/java-copy-list-to-another\
ArrayList has a ``removeIf()`` method 
https://howtodoinjava.com/java/collections/arraylist/arraylist-removeif/


## Optional Challenge 2
Allow the user to specify an optional minAmount and/or maxAmount query parameter
> e.g.\
> localhost:9090/expenses?maxAmount=50\
localhost:9090/expenses?minAmount=90\
localhost:9090/expenses?minAmount=90&maxAmount=1000

It should be able to work together with the previous parameter as well.
> e.g. localhost:9090/expenses?category=tech&minAmount=90&maxAmount=1000

### Sample data:
```
{
    "description": "Oranges",
    "amount": "10.99",
    "category": "grocery"
}
{
    "description": "Apples",
    "amount": "15.99",
    "category": "grocery"
}
{
    "description": "iPad",
    "amount": "1099",
    "category": "tech"
}
```