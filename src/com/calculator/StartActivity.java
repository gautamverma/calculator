/*
 * This is calculator made for the Android platform 2.2 and beyond. 
 * @Author  Gautam Verma
 * @Since   23 September, 2011
 * 
 * Version 1.0  - Have Addition, Subtraction and Multiplication Facilities
 * */
package com.calculator;

import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// inhearting Activity & using OnClickListener when the program first starts.
// OnClickListener "listens" for any user interaction with the application. 
public class StartActivity extends Activity implements OnClickListener {  // 
 // Intializing variables used in further code.
	Button btn0, btn1, btn2,
	       btn3, btn4, btn5,
	       btn6, btn7, btn8,
	       btn9;
	
	Button btnAdd, btnSub, 
	       btnMul, btnDiv,
	       btnExpo, btnEq;
	
	Button btnClear;
	
	TextView display;
	Toast messagebox;

	/* THE App Constants */
	// Inatilizing Which opperations ( +,-,x,%,^) should happen first when solving a calculation. 
	static final String EMPTY = "";
	static final int ADD_PRECEDENCE_VALUE = 1;
	static final int SUB_PRECEDENCE_VALUE = 1;
	static final int MUL_PRECEDENCE_VALUE = 2;
	static final int DIV_PRECEDENCE_VALUE = 2;
	static final int EXPO_PRECEDENCE_VALUE = 3;
	
	/** Called when the activity is first created. */
	//Andriod built in function that allows for the creation of the whole User interface when the activity begins and associates
	// the screen elements with specific id's to reference in code.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        display = (TextView)findViewById(R.id.editText1);
        
        btnAdd = (Button)findViewById(R.id.buttonAdd);
        btnSub = (Button)findViewById(R.id.buttonSub);
        btnMul = (Button)findViewById(R.id.buttonMul);
        btnDiv = (Button)findViewById(R.id.buttonDiv);
        btnExpo = (Button)findViewById(R.id.buttonExpo);
        
        btn0 = (Button)findViewById(R.id.button0); btn1 = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2); btn3 = (Button)findViewById(R.id.button3);
        btn4 = (Button)findViewById(R.id.button4); btn5 = (Button)findViewById(R.id.button5);
        btn6 = (Button)findViewById(R.id.button6); btn7 = (Button)findViewById(R.id.button7);
        btn8 = (Button)findViewById(R.id.button8); btn9 = (Button)findViewById(R.id.button9);
        
        btnAdd.setOnClickListener(this); btnSub.setOnClickListener(this);
        btnMul.setOnClickListener(this); btnDiv.setOnClickListener(this);
        btnExpo.setOnClickListener(this);
        
        btn0.setOnClickListener(this); btn1.setOnClickListener(this); btn2.setOnClickListener(this);
        btn3.setOnClickListener(this); btn4.setOnClickListener(this); btn5.setOnClickListener(this);
        btn6.setOnClickListener(this); btn7.setOnClickListener(this); btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        
        btnEq = (Button)findViewById(R.id.buttonEq); btnEq.setOnClickListener(this);
        btnClear = (Button)findViewById(R.id.buttonClear); btnClear.setOnClickListener(this);
        
        init();
    }

    /*This method will be called on each click of button ( operators or values) and appends buttons clicked to display,
	Aftter all buttons are clicked runs the compute method to calculate the answer.
	If no button pressed or calculations is done init function is run to clear/reset display.
     * calculations here.*/
	@Override
	public void onClick(View v) {
		
		switch( v.getId() ) {
		case R.id.button0:
			display.append("0");
			break;
		case R.id.button1:
			display.append("1");;
			break;
		case R.id.button2:
			display.append("2");
			break;
		case R.id.button3:
			display.append("3");
			break;
		case R.id.button4:
			display.append("4");
			break;
		case R.id.button5:
			display.append("5");
			break;
		case R.id.button6:
			display.append("6");
			break;
		case R.id.button7:
			display.append("7");
			break;
		case R.id.button8:
			display.append("8");
			break;
		case R.id.button9:
			display.append("9");
			break;
		case R.id.buttonAdd:
			display.append("+");
			break;
		case R.id.buttonSub:
			display.append("-");
			break;
		case R.id.buttonMul:
			display.append("*");
			break;
		case R.id.buttonDiv:
			display.append("/");
			break;
		case R.id.buttonExpo:
			display.append("^");
			break;
		case R.id.buttonEq:
				compute(); // Compute function called and answer returned  
				break;
		default:
			init(); // setting the display back to default
		}
	}
	 // function to show message that display has been cleared and setting display to "".
	public void init() {
		messagebox = new Toast(this);
		messagebox.setDuration(Toast.LENGTH_SHORT);
		display.setText("");
	}
	 // this function getPrecendence takes in opperations found in the display and returns the precedence values perviously defined
	// for every operator.
	// This is used in the compute function to determine order of opperations.
	public int getPrecedence(char operator) {
		
		switch(operator) {
		case '+':
			return ADD_PRECEDENCE_VALUE;
		case '-':
			return SUB_PRECEDENCE_VALUE;
		case '*':
			return MUL_PRECEDENCE_VALUE;
		case '/':
			return DIV_PRECEDENCE_VALUE;
		case '^':
			return EXPO_PRECEDENCE_VALUE;
		}
		
		// this case should never be reached as in this current version there's no way of dealing with it.
		return 0;
	}
	 /* function is used to parse through the display text and pull out the opperators and numbers.
	    These are then added to seperate stacks for either operator or numbers. These values are then run 
	    through evaluateexpression function where the values in the stack are computed.
	    ** Note: only number stack is popped in this function.
	*/
	public void compute() {
		Log.d("Compute Method", "");
		
		// setting values in display to charactersequence. There is also 2 stacks for numbers and operators initialized.
		CharSequence exp = display.getText();
		Stack<Character> operator = new Stack<Character>();
		Stack<String> number = new Stack<String>();
		
		
		for( int i = 0; i<exp.length(); i++ ) {
			
			// sets the default values for Numbers and Operators
			String sNumber = "";
			char sOperator = '$';
			
			// checks that the current value in the index i is a number and adds that to sNumber string
			int j = i;
			while(j < exp.length() && (exp.charAt(j) >= '0' && exp.charAt(j) <= '9' || exp.charAt(j) == '.')) {
				sNumber += exp.charAt(j); 
				j++;
			}
			// if value is not a number then it must be a character. Add that to sOperator character
			if( sNumber.equals("") )
				sOperator = exp.charAt(i);
			
			
			i = j>i?j-1:j;
			//checks if sOperator does not have $ and the operator stack is not empty. If any of the following conditions that 
			//this if statement contains fails, then add sOperator to top of operator stack.
			if(sOperator != '$') {
				if(!operator.empty()) {
					// peeks (like a pop without removing from stack) at the value at the top of the stack and adds that operator to op
					char op = operator.peek();
					// checks the precedence of the character is op (top of stack) and the current sOperator.
					if(getPrecedence(op)>getPrecedence(sOperator)) {
						// if the operator stack is still not empty and the precendence of operator value is greater than that of 
						// sOperator then evaluate the expression,passing it to evaluateExpression function
						while(!operator.empty() && getPrecedence(op)>getPrecedence(sOperator)) { 
							evaluateExpression(number,operator);
							//if operator stack is not empty then set op value to top of current operator stack
							if(!operator.empty())
								op = operator.peek();
						}
						//add sOperator to top of stack
						operator.push(sOperator);
					}
					else operator.push(sOperator);
				}
				else operator.push(sOperator);
			}
			else
				number.push(sNumber);
		}
		// if operator stack is not empty then evaluate the expression by passing the stacks for number and operator to evaluateExpression function
		if(!operator.empty()) {
			while(!operator.empty())
				evaluateExpression(number,operator);
		}
		// set the current display value to the value popped off of the top of the number stack. This value at the top is the result 
		//of the evaluateExpression calculation that occurs
		display.setText(number.pop());
	}
	
	// Function that takes in the two stacks for numbers and operators, from what was entered originally into display, and evaluates 
	// according to specific cases and in order of precedence (determined in compute function.
	public void evaluateExpression( Stack<String> number, Stack<Character> operator) {
		// taking two numbers from the top of the number stack and setting the numbers to doubles,num1 and num2.
		double num2 = Double.parseDouble(number.pop());
		double num1 = Double.parseDouble(number.pop());
		
		double res = 0;
		
		//popping the top operator in the operator stack to be used with the two numbers, num1 and num2, set above
		char op = operator.pop();
		
		// matches op character with case character and based on the match performs the corresponding calculation.
		switch( op ) {
		case '+':
			res = num1 + num2;
			break;
		case '-':
			res = num1 - num2;
			break;
		case '*':
			res = num1 * num2;
			break;
		case '/':
			res = num1 / num2;
			break;
		case '^':
			res = calculatePower( num1, num2 ); // calls on calculatePower function to calculate the power of the two numbers
		default:
			Log.d("evaluateExpression", "Wrong Expression");
		}
		
		// pushes the resulting value to the top of the number stack. This will be popped in compute function above to display the 
		// answer
		number.push(Double.toString(res));
	}
	
	// sets num1 as number to be exponentiated and num2 as exponent number. Uses a for loop to multiply num1 value by itself num2 times.
	// Returns resulting power answer
	public double calculatePower(double num1, double num2) {
		double pow = num1; 
		for(int i = 1; i<num2; i++ )
			num1 *= pow;
		return num1;
	}
	// returns a message showing that an invalid operation has occurred, shows this message, and then reinitializes display back to default
	public void invalidOperation() {
		messagebox.setText(R.string.invalidMessage);
		messagebox.show(); 
		init(); /* Setting back things to the default */
	}
}
