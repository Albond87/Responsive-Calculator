package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Controller
{
    String expression = "";
    int bracketLevel = 0;
    @FXML private Label display;
    @FXML private Button delete;
    @FXML private Pane numbers;
    @FXML private Pane operators;
    @FXML private Button equals;
    @FXML private Button negative;
    @FXML private Button decimalPoint;
    @FXML private Button openBracket;
    @FXML private Button closeBracket;

    //private Calculator calculator = new Calculator();

    public void clear(ActionEvent event)
    {
        expression = "";
        display.setText("0");
        bracketLevel = 0;

        setButtons("clear");
        operators.setDisable(false);
    }

    public void delete(ActionEvent event)
    {
        if (expression.length() == 1)
        {
            clear(new ActionEvent());
        }
        else if (expression.length() > 1)
        {
            int cutLength = 1;
            char lastChar = expression.charAt(expression.length()-1);
            char newLast = '_';
            if (lastChar == ')')
            {
                bracketLevel++;
                newLast = expression.charAt(expression.length()-2);
            }
            else if (lastChar == '(')
            {
                bracketLevel--;
                setButtons("clear");
            }
            else if (lastChar == '-')
            {
                setButtons("clear");
            }
            else if (lastChar == ' ')
            {
                cutLength = 3;
                newLast = expression.charAt(expression.length()-4);
            }
            else if (lastChar == '.')
            {
                newLast = expression.charAt(expression.length()-2);
                decimalPoint.setDisable(false);
            }
            else
            {
                newLast = expression.charAt(expression.length()-2);
                if (newLast != '(' && newLast != ' ' && newLast != '-')
                {
                    if (newLast == '.')
                    {
                        equals.setDisable(true);
                        operators.setDisable(true);
                    }
                    newLast = '_';
                }
            }

            if (newLast == ')')
            {
                closeBracket.setDisable(bracketLevel == 0);
                numbers.setDisable(true);
                setButtons("number");
            }
            else if (newLast == ' ' || newLast == '(')
            {
                setButtons("clear");
                equals.setDisable(true);
            }
            else if (newLast == '-')
            {
                setButtons("negative");
                decimalPoint.setDisable(false);
            }
            else if (newLast != '_')
            {
                int checkPos = expression.length() - cutLength - 2;
                while (checkPos >= 0)
                {
                    switch (expression.charAt(checkPos))
                    {
                        case ('.'):
                            decimalPoint.setDisable(true);
                            checkPos = -1;
                            break;
                        case ('-'):
                        case (' '):
                        case ('('):
                            decimalPoint.setDisable(false);
                            checkPos = -1;
                            break;
                        default:
                            checkPos--;
                    }
                }
                setButtons("number");
                numbers.setDisable(false);
            }
            expression = expression.substring(0,expression.length()-cutLength);
            display.setText(expression);
        }
    }

    public void number(ActionEvent event)
    {
        expression += ((Button)event.getSource()).getText();
        display.setText(expression);

        setButtons("number");
    }

    public void operator(ActionEvent event)
    {
        if (expression.equals(""))
            expression = "0";
        expression += " " + ((Button)event.getSource()).getText() + " ";
        display.setText(expression);

        setButtons("clear");
        equals.setDisable(true);
    }

    public void negative(ActionEvent event)
    {
        expression += "-";
        display.setText(expression);

        setButtons("negative");
    }

    public void decimalPoint(ActionEvent event)
    {
        if (expression.equals(""))
            expression = "0";
        expression += ".";
        display.setText(expression);

        setButtons("decimal point");
    }

    public void openBracket(ActionEvent event)
    {
        expression += "(";
        display.setText(expression);
        bracketLevel++;

        setButtons("clear");
    }

    public void closeBracket(ActionEvent event)
    {
        expression += ")";
        display.setText(expression);
        bracketLevel--;

        if (bracketLevel == 0)
            closeBracket.setDisable(true);
        numbers.setDisable(true);
        decimalPoint.setDisable(true);
        setButtons("number");
    }

    public void calculate(ActionEvent event)
    {
        double result = Calculator.evaluate(display.getText());

        if (result == Double.MIN_VALUE)
        {
            display.setText("ERROR");
            setButtons("error");
        }
        else
        {
            DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
            df.setMaximumFractionDigits(340);
            expression = df.format(result);

            if (expression.endsWith(".0"))
            {
                expression = expression.substring(0, expression.length() - 2);
                decimalPoint.setDisable(false);
            }
            else
            {
                decimalPoint.setDisable(true);
            }

            display.setText(expression);
            bracketLevel = 0;

            numbers.setDisable(false);
            setButtons("number");
        }
    }

    public void setButtons(String state)
    {
        switch (state)
        {
            case ("clear"):
                delete.setDisable(false);
                numbers.setDisable(false);
                operators.setDisable(true);
                equals.setDisable(bracketLevel != 0);
                negative.setDisable(false);
                decimalPoint.setDisable(false);
                openBracket.setDisable(false);
                closeBracket.setDisable(true);
                break;

            case ("number"):
                if (bracketLevel == 0)
                    equals.setDisable(false);
                else
                    closeBracket.setDisable(false);
                operators.setDisable(false);
                negative.setDisable(true);
                openBracket.setDisable(true);
                break;

            case ("negative"):
                operators.setDisable(true);
                equals.setDisable(true);
                negative.setDisable(true);
                openBracket.setDisable(true);
                closeBracket.setDisable(true);
                break;

            case ("decimal point"):
                operators.setDisable(true);
                equals.setDisable(true);
                negative.setDisable(true);
                decimalPoint.setDisable(true);
                openBracket.setDisable(true);
                closeBracket.setDisable(true);
                break;

            case ("error"):
                delete.setDisable(true);
                numbers.setDisable(true);
                operators.setDisable(true);
                equals.setDisable(true);
                negative.setDisable(true);
                decimalPoint.setDisable(true);
                openBracket.setDisable(true);
                closeBracket.setDisable(true);
                break;
        }
    }
}
