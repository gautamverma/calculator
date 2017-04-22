/*
 * Date: 22 April 2017
 * Name: Akram Zaky
 * Student ID : 1072758, Zakya
 * Descripition: The purpose of this Assignment to comment code for a public repository to make it easier to understand and maintain the code.
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

/*
this is a single-page android calculator app. All logic is handled in this activity.
*/
public class StartActivity extends Activity implements OnClickListener {  // 
 // buttons corresponding to the 10 digits on a calculator
	Button btn0, btn1, btn2,
	       btn3, btn4, btn5,
	       btn6, btn7, btn8,
	       btn9;
// buttons corresponding to the 6 available operations	
	Button btnAdd, btnSub, 
	       btnMul, btnDiv,
	       btnExpo, btnEq;
// the button responsible for clearing the screen	
	Button btnClear;
	
	TextView display;
	Toast messagebox;

	/* These constants are responsible for determining the order of operations. */ 
	static final String EMPTY = "";
	static final int ADD_PRECEDENCE_VALUE = 1;
	static final int SUB_PRECEDENCE_VALUE = 1;
	static final int MUL_PRECEDENCE_VALUE = 2;
	static final int DIV_PRECEDENCE_VALUE = 2;
	static final int EXPO_PRECEDENCE_VALUE = 3;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
	    
	    /* this method is automatically called when this activity is created. 
    	    It handles all of the "setup" necessary for the app to run properly.
    	    In particular, it links the button variables to the button layouts defined in
    	    res/layout/main.xml. It also sets up the click listeners for the different buttons
    	*/

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

	@Override
	public void onClick(View v) {
		
		/*This method will be called on each click of a button (operators or digits) and appends the buttons clicked to the display.
		When the equals button is clicked, it calls the compute() function, which will calculate a result.
		If no button is pressed the init() function is run to clear/reset the display.
		*/
		
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

	public void init() {
		
// this function shows a message saying the display has been cleared, and then clears the display.
		
		messagebox = new Toast(this);
		messagebox.setDuration(Toast.LENGTH_SHORT);
		display.setText("");
	}
	
	public int getPrecedence(char operator) {
/*this function returns the precedence of an operator, based on the constants defined above */
		
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

	public void compute() {
		
				/* This function implements the first half of of Dijkstra's two-stack algorithm for evaluating expressions (aka the Shunting-Yard algorithm).
	    The function parses through the display text and pulls out the operators and numbers.
	    These are then added to seperate stacks for either operators or numbers. These stacks are then passed to 
	    evaluateExpression(), where the second half of Dijkstra's two-stack algorithm is implemented, and the result is calculated.
		*/

		Log.d("Compute Method", "");
	
		CharSequence exp = display.getText();// the expression to be parsed

		// the two stacks (operators and numbers) used in Dijkstra's two-stack algorithm
		Stack<Character> operator = new Stack<Character>();
		Stack<String> number = new Stack<String>();
		
		
		for( int i = 0; i<exp.length(); i++ ) {
			
			// iterate through the characters in the expression
			String sNumber = "";
			char sOperator = '$';
			
			
			int j = i;
			while(j < exp.length() && (exp.charAt(j) >= '0' && exp.charAt(j) <= '9' || exp.charAt(j) == '.')) {
				// starting from index i,  if the character at index i is part of a number, increment j until you reach the end of the number
				sNumber += exp.charAt(j); //keep track of the numbers as they are iterated through
				j++;
			}
			
			if( sNumber.equals("") ) // if no number was found, that means an operator was found
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
	
	public void evaluateExpression( Stack<String> number, Stack<Character> operator) {
		/* This function implements the second half of Dijkstra's two-stack algorithm. It takes a stack of numbers and operations,
		representing an expression, and pushes the result back to the top of the number stack (where it might be used in future 
		calls of this function). This process can be repeated (by repeatedly using this function) to calculate long and complex expressions
		*/


		// get two numbers from the numbers stack
		double num2 = Double.parseDouble(number.pop());
		double num1 = Double.parseDouble(number.pop());
		
		double res = 0;// used to store the result of the operation
		
		// get the operator that will be applied to the above 2 numbers
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
		
		// pushes the resulting value to the top of the number stack. 
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
	
	public void invalidOperation() {
		// shows a message showing that an invalid operation has occurred. shows this message, and then reinitializes display back to default
		messagebox.setText(R.string.invalidMessage);
		messagebox.show(); 
		init(); /* Setting back things to the default */
	}
}
