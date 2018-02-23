
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EvaluatorUI extends JFrame implements ActionListener
{

  private TextField txField = new TextField();
  private Panel buttonPanel = new Panel();
  private String consoleText = "";
  private String lastEntry = "";
  private boolean lastOperator = false;
  private boolean blockText = false;

  private boolean blockTextPreviousState;
  private boolean lastOperatorPreviousState;
  private String lastEntryPreviousState;
  private String consoleTextPreviousState;

  // total of 20 buttons on the calculator,
  // numbered from left to right, top to bottom
  // bText[] array contains the text for corresponding buttons
  private static final String[] bText =
  {
    "7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3",
    "*", "0", "^", "=", "/", "(", ")", "C", "CE"
  };
  private Button[] buttons = new Button[bText.length];

  public static void main(String argv[])
  {
    EvaluatorUI calc = new EvaluatorUI();
  }

  private boolean isOperator(String button)
  {
    button = button.replaceAll(" ", "");

    if (button.equals("+") || button.equals("-") || button.equals("(") || button.equals("*") || button.equals("/") || button.equals("^"))
    {
      return true;
    }
    return false;
  }

  //store the values of two states backs
  //this is in place just incase the user enters CE when blockText is true
  //this will revert back to two places previous, when they can freely enter again
  private void setPreviousState()
  {
    consoleTextPreviousState = consoleText;
    lastEntryPreviousState = lastEntry;
    lastOperatorPreviousState = lastOperator;
    blockTextPreviousState = blockText;

  }

  private void revertToPreviousState()
  {
    consoleText = consoleTextPreviousState;
    lastEntry = lastEntryPreviousState;
    lastOperator = lastOperatorPreviousState;
    blockText = blockTextPreviousState;
  }

  public EvaluatorUI()
  {
    setLayout(new BorderLayout());

    add(txField, BorderLayout.NORTH);
    txField.setEditable(false);

    add(buttonPanel, BorderLayout.CENTER);
    buttonPanel.setLayout(new GridLayout(5, 4));

    //create 20 buttons with corresponding text in bText[] array
    for (int i = 0; i < 20; i++)
    {
      buttons[i] = new Button(bText[i]);
    }

    //add buttons to button panel
    for (int i = 0; i < 20; i++)
    {
      buttonPanel.add(buttons[i]);
    }

    //set up buttons to listen for mouse input
    for (int i = 0; i < 20; i++)
    {
      buttons[i].addActionListener(this);
    }

    setTitle("Calculator");
    setSize(400, 400);
    setLocationByPlatform(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setVisible(true);
  }

  //handles the txField formatting
  //sends final string to be evaluated
  public void actionPerformed(ActionEvent arg0)
  {
    // You need to fill in this fuction
    Button src = (Button) arg0.getSource();
    String button = src.getLabel();
    if (!button.equals("="))
    {
      ///formatting
      if (blockText || lastEntry.equals(") ") || consoleText.equals(""))
      {
        //only allows for an operand or "(" to be the first element in string
        if (consoleText.equals(""))
        {
          if (isOperator(button) || button.equals(")"))
          {
            if (!button.equals("("))
            {
              blockText = true;
            }
          } else
          {
            blockText = false;
          }
        } else if (lastEntry.equals("(") || lastEntry.equals(" ("))
        {
          if (!isOperator(button))
          {
            blockText = false;
          }
        } else if (lastEntry.equals(") ") && !button.equals(")"))
        //blocks operands
        {
          blockText = !isOperator(button);
        } else if (!isOperator(lastEntry))
        {
          blockText = false;
        } else if (isOperator(lastEntry) && !isOperator(button))
        {
          blockText = false;
        }
      }

      if (isOperator(button) && lastOperator && !button.equals("(") && !button.equals(")"))
      {
        setPreviousState();
        blockText = true;
      } else if (button.equals("+") || button.equals("-") || button.equals("*"))
      {
        if (!blockText)
        {
          setPreviousState();
          lastEntry = " " + button + " ";
          lastOperator = true;
        }
      } else if (button.equals("("))
      {
        if (consoleText.equals(""))
        {
          lastEntry = button;
          lastOperator = true;
        } else if (!isOperator(lastEntry))
        {
          blockText = true;
        } else
        {
          setPreviousState();
          lastEntry = " " + button;
          lastOperator = true;
        }

      } else if (button.equals(")"))
      {
        if (isOperator(lastEntry))
        {
          if (!lastEntry.equals(") "))
            blockText = true;

        } else
        {
          if (lastEntry.equals(") "))
          {
            consoleText = consoleText.substring(0, consoleText.length() - 1);
          }

          setPreviousState();
          lastEntry = button + " ";
          lastOperator = false;
        }

      } else if (button.equals("CE"))
      {
        if (blockText)
        {
          revertToPreviousState();
        }
        if (consoleText.length() >= lastEntry.length() + lastEntryPreviousState.length())
        {
          consoleText = consoleText.substring(0, consoleText.length() - lastEntry.length() - lastEntryPreviousState.length());
        } else
        {
          consoleText = "";
        }

        lastEntry = lastEntryPreviousState;
        lastOperator = lastOperatorPreviousState;
        blockText = blockTextPreviousState;
      } else if (button.equals("C"))
      {
        consoleText = "";
        lastEntry = "";
        lastOperator = false;
        blockText = false;
        setPreviousState();
      } else if (button.equals("/") || button.equals("^"))
      {
        if (!blockText)
        {
          if (lastEntry.equals(") "))
          {
            consoleText = consoleText.substring(0, consoleText.length() - 1);
          }

          setPreviousState();
          lastEntry = button;
          lastOperator = true;
        }
      } else
      {
        if (!blockText)
        {
          setPreviousState();
          lastEntry = button;
          lastOperator = false;
        }
      }

      if (!blockText)
      {
        txField.setText((consoleText += lastEntry));
      }
    } else
    {
      Evaluator calculator = new Evaluator();
      consoleText = Integer.toString(calculator.eval(consoleText));
      
      if (calculator.isAcceptableExpression())
      {
        txField.setText(consoleText);
      }
      else
      {
        txField.setText("Unable to compute. Please try again.");
        consoleText = "";
        lastEntry = "";
        lastOperator = false;
        blockText = false;
        setPreviousState();
      }
    }
  }
}
