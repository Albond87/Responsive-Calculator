package application;

public class Calculator
{
    public static double evaluate(String expression)
    {
        for (int e = 0; e < expression.length(); e++)
        {
            if (expression.charAt(e) == '@')
            {
                return Double.MIN_VALUE;
            }
        }

        double result;

        char firstChar = expression.charAt(0);
        String[] args = expression.split(" ");

        result = evaluate(args, 0, 0);
        return result;
    }

    private static double evaluate(String[] tokens, int startIndex, int bracketLevel)
    {
        double[] operands = new double[3];
        int operandCount = 0;
        char[] operators = new char[2];
        int operatorCount = 0;
        int stopIndex = tokens.length;

        for (int n = startIndex; n < stopIndex; n++)
        {
            if (tokens[n].equals("") || tokens[n].equals(" "))
            {
                return Double.MIN_VALUE;
            }
            if (tokens[n].equals("+") || tokens[n].equals("-") || tokens[n].equals("*") || tokens[n].equals("/"))
            {
                if (operandCount == operatorCount + 1)
                {
                    operators[operatorCount] = tokens[n].charAt(0);
                    operatorCount++;
                }
                else
                {
                    return Double.MIN_VALUE;
                }
            }
            else if (operandCount != operatorCount)
            {
                return Double.MIN_VALUE;
            }
            else if (tokens[n].charAt(0) == '(')
            {
                tokens[n] = tokens[n].substring(1);
                operands[operandCount] = evaluate(tokens, n, bracketLevel+1);
                operandCount++;

                while (tokens[n].charAt(0) != '@')
                {
                    n++;
                    if (n == tokens.length)
                    {
                        return Double.MIN_VALUE;
                    }
                }
                if (tokens[n].length() > 1)
                {
                    if (bracketLevel > 0)
                    {
                        tokens[n] = tokens[n].substring(0,tokens[n].length()-1);
                        stopIndex = n;
                    }
                    else
                    {
                        return Double.MIN_VALUE;
                    }
                }
                else
                {
                    tokens[n] = "#";
                }
            }
            else
            {
                if (tokens[n].charAt(tokens[n].length()-1) == ')')
                {
                    if (bracketLevel == 0)
                    {
                        return Double.MIN_VALUE;
                    }
                    String token = tokens[n];
                    int numLength = 0;
                    while (token.charAt(numLength) != ')')
                    {
                        numLength++;
                    }
                    String strip = token.substring(0,numLength);
                    try
                    {
                        operands[operandCount] = Double.parseDouble(strip);
                        operandCount++;
                        stopIndex = n;
                    }
                    catch (Exception e)
                    {
                        return Double.MIN_VALUE;
                    }
                    for (int b = numLength; b < token.length()-1; b++)
                    {
                        if (token.charAt(b) != ')')
                        {
                            return Double.MIN_VALUE;
                        }
                    }
                    strip = "@" + token.substring(numLength,token.length()-1);
                    tokens[n] = strip;
                }
                else
                {
                    try
                    {
                        operands[operandCount] = Double.parseDouble(tokens[n]);
                        operandCount++;
                    }
                    catch (Exception e)
                    {
                        return Double.MIN_VALUE;
                    }
                }
            }
            if (operandCount == 3)
            {
                if ((operators[0] == '+' || operators[0] == '-') && (operators[1] == '*' || operators[1] == '/'))
                {
                    if (operators[1] == '*')
                        operands[1] = operands[1] * operands[2];
                    else
                        if (operands[2] == 0)
                        {
                            return Double.MIN_VALUE;
                        }
                        else
                        {
                            operands[1] = operands[1] / operands[2];
                        }
                }
                else
                {
                    switch (operators[0])
                    {
                        case '+':
                            operands[0] = operands[0] + operands[1];
                            break;

                        case '-':
                            operands[0] = operands[0] - operands[1];
                            break;

                        case '*':
                            operands[0] = operands[0] * operands[1];
                            break;

                        case '/':
                            if (operands[1] == 0)
                            {
                                return Double.MIN_VALUE;
                            }
                            else
                            {
                                operands[0] = operands[0] / operands[1];
                                break;
                            }
                    }
                    operands[1] = operands[2];
                    operators[0] = operators[1];
                }
                operandCount--;
                operatorCount--;
            }
        }

        switch (operators[0])
        {
            case '+':
                return operands[0] + operands[1];

            case '-':
                return operands[0] - operands[1];

            case '*':
                return operands[0] * operands[1];

            case '/':
                if (operands[1] == 0)
                {
                    return Double.MIN_VALUE;
                }
                else
                {
                    return operands[0] / operands[1];
                }

            default:
                if (operators[0] == 0 && operands[1] == 0 & operators[1] == 0 && operands[2] == 0)
                    return operands[0];
        }
        return Double.MIN_VALUE;
    }
}
