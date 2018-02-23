
import java.util.*;
import java.util.Scanner;


public class EvaluatorTester
{

  public static void main(String[] args)
  {
    Evaluator evaluator = new Evaluator();

    ///Choose one or the other
    //manualInput(evaluator);
    userInterface(evaluator);
    
  }
  
  private static void userInterface(Evaluator evaluator)
  {
    boolean exitLoop = false;
    Scanner scan = new Scanner(System.in);
    ArrayList<String> equations = new ArrayList<String>();
    
    while(!exitLoop)
    {
      displayOptions();
      switch(getUserSelection())
      {
        case 1:
          System.out.println("Please enter an equation... ex. ((3-4)*5)\n");
          equations.add(scan.next());
          break;
        case 2:
          for (String equ : equations)
          {
            System.out.println(equ);
          }
          break;
        case 3:
          for(int i = 0; i < equations.size(); i++)
          {
            System.out.format("%s = %d\n", equations.get(i), evaluator.eval(equations.get(i)));
          }
          exitLoop = true;
          break;
        default:
          break;
      }
    }
  }
  
  private static void manualInput(Evaluator evaluator)
  {
    /*
    Can use this for testing as well
    Manually adjust the values for the args string
    Can also plug in values for expected answers for easier testing
    */
    
    int i = 0;
    String[] args = new String[6];
    String[] answers = new String[6];
  
    //alter these values to use for testing
    args[0] = "(3-7)/2";
    answers[0] = "-2";
    
    args[1] = "(6*6/(30/10)+4)";
    answers[1] = "16";
    
    args[2] = "3^2 + (5^(3-1))";
    answers[2] = "34";
    
    args[3] = "(5*6)/(20/2)+((5+1)/2)^2";
    answers[3] = "12";
    
    args[4] = "(((5^2)/5)-(16/8))";
    answers[4] = "3";
    
    args[5] = "(((5+2)+3)*3) + 3";
    answers[5] = "33";
    
    for (String arg : args)
    {
      System.out.format("%s = %d\n", arg, evaluator.eval(arg));
      
      System.out.println("Expected answer: " + answers[i] + "\n");
      i++;
      
      //System.out.println( arg + " = " + evaluator.eval( arg ) );
    }
  }
  
  private static int getUserSelection()
  {
    Scanner reader = new Scanner(System.in);
    int selection = -1;
    
    while(selection == -1)
    {
      int choice = reader.nextInt();
      if(choice >= 1 && choice <= 3)
      {
        selection = choice;
      }
      else
      {
        System.out.println("Please enter a number between 1 and 3");
      }
    }
    return selection;
  }
  
  private static void displayOptions()
  {
    System.out.println("\n------------------------------------------------------");
    System.out.println("- Please select an option from the list below.       -");
    System.out.println("-                                                    -");
    System.out.println("- 1. Add an equation to the array.                   -");
    System.out.println("- 2. Display current array of unevaluated equations. -");
    System.out.println("- 3. Evaluate equations.                             -");
    System.out.println("-                                                    -");
    System.out.println("------------------------------------------------------\n");
  }
}
