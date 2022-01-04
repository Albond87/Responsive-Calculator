# Responsive Calculator
A basic calculator application written in Java and using JavaFX that enables and disables buttons in such a way that ensures that only syntactically valid expressions can be passed to the underlying Calculator class to be evaluated.  

The Calculator class is based on something I had already written for university work which I adapted for this. The syntax accepted by the evaluator is as follows (in BNF) (the spaces are important):  
- expr ::= (expr) | expr + expr | expr - expr | expr * expr | expr / expr | num
- num ::= any number, including negatives and numbers with a decimal point in

The evaluator accounts for order of precedence, while reading expressions left to right.
