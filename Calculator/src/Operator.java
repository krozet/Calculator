
import java.util.HashMap;

public abstract class Operator
{

  public abstract int priority();

  private static final HashMap<String, Operator> operators = new HashMap<String, Operator>();

  static
  {
    operators.put("+", new AdditionOperator());
    operators.put("-", new SubtractionOperator());
    operators.put("*", new MultiplicationOperator());
    operators.put("/", new DivisionOperator());
    operators.put("^", new PowOperator());
    operators.put("(", new BeginParenthesisOperator());
    operators.put(")", new EndParenthesisOperator());
  }

  public abstract Operand execute(Operand op1, Operand op2);

  public static boolean check(String token)
  {
    return operators.containsKey(token);
  }

  public static Operator getOperatorFromHashMap(String token)
  {
    return operators.get(token);
  }
}

class AdditionOperator extends Operator
{

  public int priority()
  {
    return 2;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(op1.getValue() + op2.getValue());
  }
}

class SubtractionOperator extends Operator
{

  public int priority()
  {
    return 2;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(op1.getValue() - op2.getValue());
  }
}

class MultiplicationOperator extends Operator
{

  public int priority()
  {
    return 3;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(op1.getValue() * op2.getValue());
  }
}

class DivisionOperator extends Operator
{

  public int priority()
  {
    return 3;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(op1.getValue() / op2.getValue());
  }
}

class PowOperator extends Operator
{

  public int priority()
  {
    return 4;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand((int) Math.pow((double) op1.getValue(), (double) op2.getValue()));
  }
}

//values are irrelevent, only need this for key in HashMap
class BeginParenthesisOperator extends Operator
{

  public int priority()
  {
    return 0;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(0);
  }
}

//values are irrelevent, only need this for key in HashMap
class EndParenthesisOperator extends Operator
{

  public int priority()
  {
    return 0;
  }

  public Operand execute(Operand op1, Operand op2)
  {
    return new Operand(0);
  }
}
