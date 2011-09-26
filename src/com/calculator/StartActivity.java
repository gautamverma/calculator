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

public class StartActivity extends Activity implements OnClickListener {

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
	static final String EMPTY = "";
	static final int ADD_PRECEDENCE_VALUE = 1;
	static final int SUB_PRECEDENCE_VALUE = 1;
	static final int MUL_PRECEDENCE_VALUE = 2;
	static final int DIV_PRECEDENCE_VALUE = 2;
	static final int EXPO_PRECEDENCE_VALUE = 3;
	
	/** Called when the activity is first created. */
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

    /* This method will be called on each click and will be handling all the 
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
				compute();
				break;
		default:
			init();
		}
	}
	
	public void init() {
		messagebox = new Toast(this);
		messagebox.setDuration(Toast.LENGTH_SHORT);
		display.setText("");
	}
	
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
		
		// It must not be reached for Version 1.0
		return 0;
	}
	
	public void compute() {
		Log.d("Compute Method", "");
		
		CharSequence exp = display.getText();
		Stack<Character> operator = new Stack<Character>();
		Stack<String> number = new Stack<String>();
		
		for( int i = 0; i<exp.length(); i++ ) {
			String sNumber = "";
			char sOperator = '$';
			
			int j = i;
			while(j < exp.length() && (exp.charAt(j) >= '0' && exp.charAt(j) <= '9' || exp.charAt(j) == '.')) {
				sNumber += exp.charAt(j); 
				j++;
			}
			if( sNumber.equals("") )
				sOperator = exp.charAt(i);
			
			i = j>i?j-1:j;
			if(sOperator != '$') {
				if(!operator.empty()) {
					char op = operator.peek();
					if(getPrecedence(op)>getPrecedence(sOperator)) {
						while(!operator.empty() && getPrecedence(op)>getPrecedence(sOperator)) {
							evaluateExpression(number,operator);
							if(!operator.empty())
								op = operator.peek();
						}
						operator.push(sOperator);
					}
					else operator.push(sOperator);
				}
				else operator.push(sOperator);
			}
			else
				number.push(sNumber);
		}
		
		if(!operator.empty()) {
			while(!operator.empty())
				evaluateExpression(number,operator);
		}
		display.setText(number.pop());
	}
	
	public void evaluateExpression( Stack<String> number, Stack<Character> operator) {
		double num2 = Double.parseDouble(number.pop());
		double num1 = Double.parseDouble(number.pop());
		
		double res = 0;
		
		char op = operator.pop();
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
			res = calculatePower( num1, num2 );
		default:
			Log.d("evaluateExpression", "Wrong Expression");
		}
		
		number.push(Double.toString(res));
	}
	
	public double calculatePower(double num1, double num2) {
		double pow = num1; 
		for(int i = 1; i<num2; i++ )
			num1 *= pow;
		return num1;
	}
	public void invalidOperation() {
		messagebox.setText(R.string.invalidMessage);
		messagebox.show(); 
		init(); /* Setting back things to the default */
	}
}