
import java.util.*;

public class Evaluator
{

  private Stack<Operand> operandStack;
  private Stack<Operator> operatorStack;

  private StringTokenizer tokenizer;
  private static final String DELIMITERS = "+-*^/#! ()";

  private boolean acceptableExpression = true;

  public Evaluator()
  {
    operandStack = new Stack<>();
    operatorStack = new Stack<>();
  }

  public final boolean isAcceptableExpression()
  {
    return acceptableExpression;
  }

  public int eval(String expression)
  {
    String token;

    this.tokenizer = new StringTokenizer(expression, DELIMITERS, true);

    while (this.tokenizer.hasMoreTokens())
    {
      if (!(token = this.tokenizer.nextToken()).equals(" "))
      {
        // check if token is an operand
        if (Operand.check(token))
        {
          operandStack.push(new Operand(token));
        } else
        {
          if (!Operator.check(token))
          {
            System.out.println("*****invalid token****** \nToken is:" + token);
          } else
          {
            Operator newOperator = Operator.getOperatorFromHashMap(token);
            if (newOperator == Operator.getOperatorFromHashMap("("))
            {
              evalParenthesis();
              //if end is reached in parenthesis
              if (this.tokenizer.countTokens() <= 1)
              {
                break;
              }
            }
            //if there is a ")" without a preceeding "(", the equation will not be evaluated
            if (newOperator == Operator.getOperatorFromHashMap(")"))
            {
              acceptableExpression = false;
              break;
            }

            if (newOperator != Operator.getOperatorFromHashMap("("))
            {
              //handles priority cases going forward through the tokens
              while (!operatorStack.isEmpty() && (operatorStack.peek().priority() >= newOperator.priority()))
              {
                Operator oldOpr = operatorStack.pop();
                Operand op2 = operandStack.pop();
                Operand op1 = operandStack.pop();
                operandStack.push(oldOpr.execute(op1, op2));
              }
              operatorStack.push(newOperator);
            }
          }
        }
      }
    }

    ///handles contents of the stacks left over
    while (!operatorStack.isEmpty())
    {
      if (operatorStack.size() == 1)
      {
        Operator oldOpr = operatorStack.pop();
        Operand op2 = operandStack.pop();
        Operand op1 = operandStack.pop();
        operandStack.push(oldOpr.execute(op1, op2));
      }

      if (operatorStack.size() >= 2)
      {
        Operator topOpr = operatorStack.pop();
        if (topOpr.priority() >= operatorStack.peek().priority())
        {
          Operand op2 = operandStack.pop();
          Operand op1 = operandStack.pop();
          operandStack.push(topOpr.execute(op1, op2));
        } else
        //topOpr has less priority than operator below it
        {
          Operator belowOpr = operatorStack.pop();
          //need to store top operand and push it later to correspond with the corrend operator
          Operand temp = operandStack.pop();

          Operand op2 = operandStack.pop();
          Operand op1 = operandStack.pop();
          operandStack.push(belowOpr.execute(op1, op2));
          operandStack.push(temp);
          operatorStack.push(topOpr);
        }
      }
    }
    return operandStack.pop().getValue();
  }

  //Creates a temp stack that handles valid expressions
  private void evalParenthesis()
  {
    String token;
    Stack<Operator> tempOperatorStack = new Stack<Operator>();
    Stack<Operand> tempOperandStack = new Stack<Operand>();

    while (this.tokenizer.hasMoreTokens())
    {
      // filter out spaces
      if (!(token = this.tokenizer.nextToken()).equals(" "))
      {
        // check if token is an operand
        if (Operand.check(token))
        {
          tempOperandStack.push(new Operand(token));
        } else
        {
          if (!Operator.check(token))
          {
            System.out.println("*****invalid token****** \nToken is:" + token);
          } else
          {
            Operator newOperator = Operator.getOperatorFromHashMap(token);
            if (newOperator == Operator.getOperatorFromHashMap("("))
            {
              //recurrsive call for imbedded parenthesis
              evalParenthesis();
              //takes the value from last parenthesis call and places it in
              //this parenthesis's tempStack
              tempOperandStack.push(operandStack.pop());
              //if end is reached in parenthesis
              if (this.tokenizer.countTokens() <= 1)
              {
                break;
              }
            }

            //stops reading tokens and will move to processing the tempStacks
            if (newOperator == Operator.getOperatorFromHashMap(")"))
            {
              break;
            }

            if (newOperator != Operator.getOperatorFromHashMap("("))
            {
              if (tempOperandStack.size() > 1)
              {
                while (!tempOperatorStack.isEmpty() && tempOperatorStack.peek().priority() >= newOperator.priority())
                {
                  Operator oldOpr = tempOperatorStack.pop();
                  Operand op2 = tempOperandStack.pop();
                  Operand op1 = tempOperandStack.pop();
                  tempOperandStack.push(oldOpr.execute(op1, op2));
                }
              }
              tempOperatorStack.push(newOperator);

              //necessary for multiple parenthesis
              //retrieves last computed parenthesis
              if (tempOperandStack.isEmpty() && tempOperatorStack.size() == 1)
              {
                tempOperandStack.push(operandStack.pop());
              }
            }
          }
        }
      }
    }

    //handles contents of the stacks left over
    while (!tempOperatorStack.isEmpty())
    {
      //handles cases like (2) - not fully implemented since it's not needed
      if (tempOperatorStack.isEmpty())
      {
        if (tempOperandStack.isEmpty())
        {
          acceptableExpression = false;
          break;
        } else
        {
          operandStack.push(tempOperandStack.pop());
        }
      }

      //final step in while loop
      if (tempOperatorStack.size() == 1)
      {
        Operator oldOpr = tempOperatorStack.pop();
        Operand op2 = tempOperandStack.pop();
        Operand op1 = tempOperandStack.pop();

        //adds final operand to the operandStack
        operandStack.push(oldOpr.execute(op1, op2));
      }

      //this is where most of the stack will be taken care of
      if (tempOperatorStack.size() >= 2)
      {
        Operator topOpr = tempOperatorStack.pop();
        if (tempOperatorStack.peek().priority() >= topOpr.priority())
        {
          Operator tempOpr = tempOperatorStack.pop();
          Operand op2 = tempOperandStack.pop();
          Operand op1 = tempOperandStack.pop();
          tempOperandStack.push(tempOpr.execute(op1, op2));
        } else
        //topOpr has less priority than operator below it
        {
          tempOperatorStack.push(topOpr);
        }
      }
    }
  }

}
