public class Calculator {
    
    /**
     * Performs basic arithmetic operations with validation
     * 
     * @param num1 First operand
     * @param num2 Second operand
     * @param operation Operation: +, -, *, /, %
     * @return Result of the operation
     * @throws IllegalArgumentException if operation is invalid or division by zero
     */
    public static double calculate(double num1, double num2, String operation) 
            throws IllegalArgumentException {
        
        if (operation == null || operation.isEmpty()) {
            throw new IllegalArgumentException("Operation cannot be empty");
        }

        switch (operation) {
            case "+":
                return add(num1, num2);
            case "-":
                return subtract(num1, num2);
            case "*":
                return multiply(num1, num2);
            case "/":
                return divide(num1, num2);
            case "%":
                return modulus(num1, num2);
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation + 
                    ". Supported operations: +, -, *, /, %");
        }
    }

    private static double add(double num1, double num2) {
        return num1 + num2;
    }

    private static double subtract(double num1, double num2) {
        return num1 - num2;
    }

    private static double multiply(double num1, double num2) {
        return num1 * num2;
    }

    private static double divide(double num1, double num2) 
            throws IllegalArgumentException {
        if (num2 == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        return num1 / num2;
    }

    private static double modulus(double num1, double num2) 
            throws IllegalArgumentException {
        if (num2 == 0) {
            throw new IllegalArgumentException("Modulus by zero is not allowed");
        }
        return num1 % num2;
    }
}
