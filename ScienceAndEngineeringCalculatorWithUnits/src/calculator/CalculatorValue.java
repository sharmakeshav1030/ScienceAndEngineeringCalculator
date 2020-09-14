package calculator;

import java.util.Scanner;
import uNumberLibrary.UNumber;
import uNumberLibrary.UNumberWithSquareRoot;

/**
 * <p>
 * Title: CalculatorValue Class.
 * </p>
 *
 * <p>
 * Description: A component of a JavaFX demonstration application that performs
 * computations
 * </p>
 *
 * <p>
 * Copyright and Baseline Author: Lynn Robert Carter © 2017
 * </p>
 *
 * @author Lynn Robert Carter and Keshav Sharma
 * 
 * @version 4.50 2017-10-17 The JavaFX-based GUI for the implementation of a
 *          calculator
 * @version 4.60 2018-01-15 The implementation of integer based calculator is
 *          done.
 * @version 4.70 2018-01-25 Square root was added, and the calculator was
 *          changed to floating values calculator with error messages modified.
 * @version 4.80 2018-02-20 Implementation of FSM.
 * @version 4.90 2018-03-13 Implementation of error terms calculation.
 * @version 5.00 2018-04-17 Implementation of the high-precision significant
 *          digits.
 * @version 5.10 2018-10-02 Implementation of UNumber Library.
 * @version 5.20 2018-11-22 Implementation of Unit sysytem along with te proper
 *          conversions and calculations.
 *
 */

public class CalculatorValue {

	/**********************************************************************************************
	 * 
	 * Attributes
	 * 
	 **********************************************************************************************/

	// These are the major values that define a calculator value
	double measuredValue;
	String errorMessage = "";

	String newMessage = "Invalid Input"; // this defines the error message when the divisor is zero
	public static String measuredValueErrorMessage = ""; // The alternate error message text
	public static String measuredValueInput = ""; // The input being processed
	public static int measuredValueIndexofError = -1; // The index where the error was located
	public static int stateMeasuredValue = 0; // The current state value
	public static int nextStateMeasuredValue = 0; // The next state value
	public static boolean finalStateMeasuredValue = false; // Is this state a final state
	private static String inputLineMeasuredValue = ""; // The input line
	private static char currentCharMeasuredValue; // The current character in the line
	private static int currentCharNdxMeasuredValue; // The index of the current character
	private static boolean runningMeasuredValue; // The flag that specifies if it is running
	public static String errorTermErrorMessage = ""; // The alternate error message text
	public static String errorTermInput = ""; // The input being processed
	public static int errorTermIndexofError = -1; // The index where the error was located
	private static int stateErrorTerm = 0; // The current state value
	private static int nextStateErrorTerm = 0; // The next state value
	@SuppressWarnings("unused")
	private static boolean finalStateErrorTerm = false; // Is this state a final state
	private static String inputLineErrorTerm = ""; // The input line
	private static char currentCharErrorTerm; // The current character in the line
	private static int currentCharNdxErrorTerm; // The index of the current character
	private static boolean runningErrorTerm; // The flag that specifies if it is running

	UNumber Uresult;
	UNumber UErrresult;

	/**********************************************************************************************
	 * 
	 * Constructors
	 * 
	 **********************************************************************************************/

	/*****
	 * This is the default constructor
	 */
	public CalculatorValue() {
	}

	/*****
	 * This constructor creates a calculator value based on a double. For future
	 * calculators, it is best to avoid using this constructor.
	 */
	public CalculatorValue(double v) {
		measuredValue = v;
	}

	/*****
	 * This copy constructor creates a duplicate of an already existing calculator
	 * value
	 */
	public CalculatorValue(CalculatorValue v) {
		measuredValue = v.measuredValue;
		errorMessage = v.errorMessage;
	}

	/*****
	 * This constructor creates a calculator value from a string... Due to the
	 * nature of the input, there is a high probability that the input has errors,
	 * so the routine returns the value with the error message value set to empty or
	 * the string of an error message.
	 */
	public CalculatorValue(String s) {
		measuredValue = 0;
		if (s.length() == 0) { // If there is nothing there,
			errorMessage = "***Error*** Input is empty"; // signal an error
			return;
		}
		// If the first character is a plus sign, ignore it.
		int start = 0; // Start at character position zero
		boolean negative = false; // Assume the value is not negative

		switch (s.charAt(start)) {
		case 1:
			start++;
			break; // Switch case is used to check sign only once as break will stops
		case 2:
			start++;
			negative = true;
			break; // after one check and two consecutive signs will be considered as
					// an invalid input
		}

		// See if the user-entered string can be converted into an double value
		Scanner tempScanner = new Scanner(s.substring(start)); // Create scanner for the digits
		if (!tempScanner.hasNextDouble()) { // See if the next token is a valid
			errorMessage = "***Error*** Invalid value";
			tempScanner.close();
			return;
		}

		// Convert the user-entered string to a integer value and see if something else
		// is following it
		measuredValue = tempScanner.nextDouble(); // Convert the value and check to see
		if (tempScanner.hasNext()) { // that there is nothing else is
			errorMessage = "***Error*** Excess data"; // following the value. If so, it
			tempScanner.close(); // is an error. Therefore we must
			measuredValue = 0; // return a zero value.
			return;
		}
		tempScanner.close();
		errorMessage = "";
		if (negative) // Return the proper value based
			measuredValue = -measuredValue; // on the state of the flag that
	}

	/**********************************************************************************************
	 * 
	 * Getters and Setters
	 * 
	 **********************************************************************************************/

	/*****
	 * This is the start of the getters and setters
	 *
	 * Get the error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	public String getNewMessage() {
		return newMessage; // getter when the value of divisor is 0

	}

	/*****
	 * Set the current value of a calculator value to a specific long integer
	 */
	public void setValue(double v) {
		measuredValue = v;
	}

	/*****
	 * Set the current value of a calculator error message to a specific string
	 */
	public void setErrorMessage(String m) {
		errorMessage = m;
	}

	public void setNewMessage(String n) {
		newMessage = n; // setter when the value of divisor is 0
	}

	/*****
	 * Set the current value of a calculator value to the value of another (copy)
	 */
	public void setValue(CalculatorValue v) {
		measuredValue = v.measuredValue;
		errorMessage = v.errorMessage;
	}

	/**********************************************************************************************
	 * 
	 * The toString() Method
	 * 
	 **********************************************************************************************/

	/*****
	 * This is the default toString method
	 *
	 * When more complex calculator values are creating this routine will need to be
	 * updated
	 */
	public String toString() {
		return Uresult.toDecimalString();
	}

	public String toStringErr() {
		return UErrresult.toDecimalString();
	}

	/*****
	 * This is the debug toString method
	 *
	 * When more complex calculator values are creating this routine will need to be
	 * updated
	 */
	public String debugToString() {
		return "measuredValue = " + measuredValue + "\nerrorMessage = " + errorMessage + "\n";
	}

	/**********************************************************************************************
	 * 
	 * The computation methods
	 * 
	 **********************************************************************************************/

	/*******************************************************************************************************
	 * The following methods implement computation on the calculator values. These
	 * routines assume that the caller has verified that things are okay for the
	 * operation to take place. These methods understand the technical details of
	 * the values and their reputations, hiding those details from the business
	 * logic and user interface modules.
	 *
	 * Since this is addition and we do not yet support units, we don't recognize
	 * any errors.
	 */

	public void add(CalculatorValue v) {
		UNumber a = new UNumber(measuredValue);
		a = new UNumber(a, 25);
		UNumber b = new UNumber(v.measuredValue);
		b = new UNumber(b, 25);
		a.add(b);
		Uresult = new UNumber(a);
		errorMessage = "";
	}

	public void add1(CalculatorValue v, CalculatorValue m, CalculatorValue e, CalculatorValue s) {
		UNumber two2 = new UNumber(2.0); // This is the constant 2.0
		two2 = new UNumber(two2, 25);
		UNumber a = new UNumber(m.measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber a2 = new UNumber(a1);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.sub(b1);
		UNumber lb1 = a1;
		a2.add(b1);
		UNumber ub1 = a2;
		UNumber c = new UNumber(s.measuredValue);
		UNumber c1 = new UNumber(c, 25);
		UNumber c2 = new UNumber(c1);
		UNumber d = new UNumber(e.measuredValue);
		UNumber d1 = new UNumber(d, 25);
		c1.sub(d1);
		UNumber lb2 = c1;
		c2.add(d1);
		UNumber ub2 = c2;
		lb1.add(lb2);
		UNumber first = lb1;
		ub1.add(ub2);
		UNumber second = ub1;
		second.sub(first);
		UNumber range = second;
		range.div(two2);
		UErrresult = new UNumber(range);
		errorMessage = "";
	}

	/*****
	 * For subtraction
	 * 
	 * @param v
	 */
	public void sub(CalculatorValue v) {
		UNumber a = new UNumber(measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.sub(b1);
		Uresult = new UNumber(a1);
		errorMessage = "";
	}

	public void sub1(CalculatorValue v, CalculatorValue m, CalculatorValue e, CalculatorValue s) {
		UNumber two2 = new UNumber(2.0); // This is the constant 2.0
		two2 = new UNumber(two2, 25);
		UNumber a = new UNumber(m.measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber a2 = new UNumber(a1);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.sub(b1);
		UNumber lb1 = a1;
		a2.add(b1);
		UNumber ub1 = a2;
		UNumber c = new UNumber(s.measuredValue);
		UNumber c1 = new UNumber(c, 25);
		UNumber c2 = new UNumber(c1);
		UNumber d = new UNumber(e.measuredValue);
		UNumber d1 = new UNumber(d, 25);
		c1.sub(d1);
		UNumber lb2 = c1;
		c2.add(d1);
		UNumber ub2 = c2;
		lb1.add(lb2);
		UNumber first = lb1;
		ub1.add(ub2);
		UNumber second = ub1;
		second.sub(first);
		UNumber range = second;
		range.div(two2);
		UErrresult = new UNumber(range);
		errorMessage = "";
	}

	/*****
	 * For multiplication
	 * 
	 * @param v
	 */

	public void mpy(CalculatorValue v) {
		UNumber a = new UNumber(measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.mpy(b1);
		Uresult = new UNumber(a1);
		errorMessage = "";
	}

	public void mpy1(CalculatorValue v, CalculatorValue m, CalculatorValue e, CalculatorValue s) {
		UNumber a = new UNumber(m.measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber a2 = new UNumber(a1);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		b1.div(a1);
		UNumber errfrac1 = b1;
		UNumber c = new UNumber(s.measuredValue);
		UNumber c1 = new UNumber(c, 25);
		UNumber c2 = new UNumber(c1);
		UNumber d = new UNumber(e.measuredValue);
		UNumber d1 = new UNumber(d, 25);
		d1.div(c1);
		UNumber errfrac2 = d1;
		errfrac1.add(errfrac2);
		UNumber Productsum = errfrac1;
		a2.mpy(c2);
		Productsum.mpy(a2);
		UNumber finalErr = Productsum;
		UErrresult = new UNumber(finalErr);
		errorMessage = "";
	}

	/*****
	 * For division
	 * 
	 * @param v
	 */

	public void div(CalculatorValue v) {
		UNumber a = new UNumber(measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.div(b1);
		Uresult = new UNumber(a1);
		errorMessage = "";

	}

	public void div1(CalculatorValue v, CalculatorValue m, CalculatorValue e, CalculatorValue s) {
		UNumber a = new UNumber(m.measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber a2 = new UNumber(a1);
		UNumber b = new UNumber(v.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		b1.div(a1);
		UNumber errfrac1 = b1;
		UNumber c = new UNumber(s.measuredValue);
		UNumber c1 = new UNumber(c, 25);
		UNumber c2 = new UNumber(c1);
		UNumber d = new UNumber(e.measuredValue);
		UNumber d1 = new UNumber(d, 25);
		d1.div(c1);
		UNumber errfrac2 = d1;
		errfrac1.add(errfrac2);
		UNumber Divsum = errfrac1;
		a2.div(c2);
		Divsum.mpy(a2);
		UNumber finalErr = Divsum;
		UErrresult = new UNumber(finalErr);
		errorMessage = "";
	}

	/*****
	 * For square root only one operand is required, thus operation is only
	 * performed over measuredValue only
	 *
	 * @param v
	 */
	public void sqrt(CalculatorValue v) {
		UNumberWithSquareRoot a = new UNumberWithSquareRoot(v.measuredValue);
		UNumberWithSquareRoot a1 = new UNumberWithSquareRoot(a, 25);
		UNumber res = a1.sqrt(a1);
		Uresult = res;
		errorMessage = "";
	}

	public void sqrt1(CalculatorValue v, CalculatorValue m) {
		UNumber half = new UNumber(0.5); // This is the constant 2.0
		half = new UNumber(half, 25);
		UNumber a = new UNumber(v.measuredValue);
		UNumber a1 = new UNumber(a, 25);
		UNumber b = new UNumber(m.measuredValue);
		UNumber b1 = new UNumber(b, 25);
		a1.div(b1);
		UNumber errfrac = a1;
		errfrac.mpy(half);
		UNumberWithSquareRoot b2 = new UNumberWithSquareRoot(b, 25);
		UNumber power = b2.sqrt(b2);
		errfrac.mpy(power);
		UNumber powerErr = new UNumber(errfrac);
		UErrresult = new UNumber(powerErr);
		errorMessage = "";
	}

	private static String displayInput(String input, int currentCharNdx) {
		// Display the entire input line
		String result = input + "\n";

		// Display a line with enough spaces so the up arrow point to the point of an
		// error
		for (int ndx = 0; ndx < currentCharNdx; ndx++)
			result += " ";

		// Add the up arrow to the end of the second line
		return result + "\u21EB"; // A Unicode up arrow with a base
	}

	private static void displayDebuggingInfo() {
		// if (currentCharNdxErrorTerm >= inputLineErrorTerm.length())
		// System.out.println(((stateErrorTerm < 10) ? " " : " ") + stateErrorTerm +
		// ((finalStateErrorTerm) ? " F " : " ") + "None");
		// else
		// System.out.println(((stateErrorTerm < 10) ? " " : " ") + stateErrorTerm +
		// ((finalStateErrorTerm) ? " F " : " ") + " " + currentCharErrorTerm + " " +
		// ((nextStateErrorTerm < 10) && (nextStateErrorTerm != -1) ? " " : " ") +
		// nextStateErrorTerm );
	}

	private static void moverrorTermoNextCharacterMeasuredValue() {
		currentCharNdxMeasuredValue++;
		if (currentCharNdxMeasuredValue < inputLineMeasuredValue.length())
			currentCharMeasuredValue = inputLineMeasuredValue.charAt(currentCharNdxMeasuredValue);
		else {
			currentCharMeasuredValue = ' ';
			runningMeasuredValue = false;
		}
	}

	private static void moveToNextCharacterErrorTerm() {
		currentCharNdxErrorTerm++;
		if (currentCharNdxErrorTerm < inputLineErrorTerm.length())
			currentCharErrorTerm = inputLineErrorTerm.charAt(currentCharNdxErrorTerm);
		else {
			currentCharErrorTerm = ' ';
			runningErrorTerm = false;
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram
	 * into a Java method.
	 *
	 * @param input The input string for the Finite State Machine
	 * @return An output string that is empty if every things is okay or it will be
	 *         a string with a help description of the error follow by two lines
	 *         that shows the input line follow by a line with an up arrow at the
	 *         point where the error was found.
	 */

	public static String checkMeasureValue(String input) {
		if (input.length() <= 0)
			return "";
		// The following are the local variable used to perform the Finite State Machine
		// simulation
		stateMeasuredValue = 0; // This is the FSM state number
		inputLineMeasuredValue = input; // Save the reference to the input line as a global
		currentCharNdxMeasuredValue = 0; // The index of the current character
		currentCharMeasuredValue = input.charAt(0); // The current character from the above indexed position

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state

		measuredValueInput = input; // Set up the alternate result copy of the input
		runningMeasuredValue = true; // Start the loop

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state
		while (runningMeasuredValue) {
			// The switch statement takes the execution to the code for the current state,
			// where
			// that code sees whether or not the current character is valid to transition to
			// a
			// next state
			switch (stateMeasuredValue) {
			case 0:
				// State 0 has three valid transitions. Each is addressed by an if statement.

				// This is not a final state
				finalStateMeasuredValue = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 1;
					break;
				}
				// If the current character is a decimal point, it transitions to state 3
				else if (currentCharMeasuredValue == '.') {
					nextStateMeasuredValue = 3;
					break;
				}

				else if (currentCharMeasuredValue == '-') {
					nextStateMeasuredValue = 0;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 1:
				// State 1 has three valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalStateMeasuredValue = true;

				// In state 1, if the character is 0 through 9, it is accepted and we stay in
				// this
				// state
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 1;
					break;
				}

				// If the current character is a decimal point, it transitions to state 2
				else if (currentCharMeasuredValue == '.') {
					nextStateMeasuredValue = 2;
					break;
				}
				// If the current character is an E or an e, it transitions to state 5
				else if (currentCharMeasuredValue == 'E' || currentCharMeasuredValue == 'e') {
					nextStateMeasuredValue = 5;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 2:
				// State 2 has two valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalStateMeasuredValue = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 2;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentCharMeasuredValue == 'E' || currentCharMeasuredValue == 'e') {
					nextStateMeasuredValue = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 3:
				// State 3 has only one valid transition. It is addressed by an if statement.

				// This is not a final state
				finalStateMeasuredValue = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 4;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 4:
				// State 4 has two valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalStateMeasuredValue = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 4;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentCharMeasuredValue == 'E' || currentCharMeasuredValue == 'e') {
					nextStateMeasuredValue = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 5:
				// State 5 has two valid transitions. Each is addressed by an if statement.

				finalStateMeasuredValue = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 7;
					break;
				}
				// If the current character is an 'E' or 'e", it transitions to state 5
				else if (currentCharMeasuredValue == '+' || currentCharMeasuredValue == '-') {
					nextStateMeasuredValue = 6;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 6:
				// State 6 has one valid transitions. It is addressed by an if statement.

				finalStateMeasuredValue = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 7;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			case 7:
				// State 7 has one valid transitions. It is addressed by an if statement.

				// This is a final state
				finalStateMeasuredValue = true;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharMeasuredValue >= '0' && currentCharMeasuredValue <= '9') {
					nextStateMeasuredValue = 7;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningMeasuredValue = false;

				// The execution of this state is finished
				break;

			}

			if (runningMeasuredValue) {

				// When the processing of a state has finished, the FSM proceeds to the next
				// character
				// in the input and if there is one, it fetches that character and updates the
				// currentChar. If there is no next character the currentChar is set to a blank.
				moverrorTermoNextCharacterMeasuredValue();

				// Move to the next state
				stateMeasuredValue = nextStateMeasuredValue;

			}

		}

		measuredValueIndexofError = currentCharNdxMeasuredValue; // Copy the index of the current character;

		// When the FSM halts, we must determine if the situation is an error or not.
		// That depends
		// of the current state of the FSM and whether or not the whole string has been
		// consumed.
		// This switch directs the execution to separate code for each of the FSM
		// states.
		switch (stateMeasuredValue) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			measuredValueIndexofError = currentCharNdxMeasuredValue; // Copy the index of the current character;
			measuredValueErrorMessage = "The first character must be a digit or a decimal point.";
			return "The first character must be a \"+\" sign, digit a decimal point.";

		case 1:
			// State 1 is a final state, so we must see if the whole string has been
			// consumed.
			if (currentCharNdxMeasuredValue < input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", a digit, "
						+ "a \".\", or it must be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdxMeasuredValue);
			} else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 2:
		case 4:
			// States 2 and 4 are the same. They are both final states with only one
			// possible
			// transition forward, if the next character is an E or an e.
			if (currentCharNdxMeasuredValue < input.length()) {
				measuredValueErrorMessage = "This character may only be an \"E\", an \"e\", a digit or it must"
						+ " be the end of the input.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdxMeasuredValue);
			}
			// If there is no more input, the input was recognized.
			else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}
		case 3:
		case 6:
			// States 3, and 6 are the same. None of them are final states and in order to
			// move forward, the next character must be a digit.
			measuredValueErrorMessage = "This character may only be a digit.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdxMeasuredValue);

		case 7:
			// States 7 is similar to states 3 and 6, but it is a final state, so it must be
			// processed differently. If the next character is not a digit, the FSM stops
			// with an
			// error. We must see here if there are no more characters. If there are no more
			// characters, we accept the input, otherwise we return an error
			if (currentCharNdxMeasuredValue < input.length()) {
				measuredValueErrorMessage = "This character may only be a digit.\n";
				return measuredValueErrorMessage + displayInput(input, currentCharNdxMeasuredValue);
			} else {
				measuredValueIndexofError = -1;
				measuredValueErrorMessage = "";
				return measuredValueErrorMessage;
			}

		case 5:
			// State 5 is not a final state. In order to move forward, the next character
			// must be
			// a digit or a plus or a minus character.
			measuredValueErrorMessage = "This character may only be a digit, a plus, or minus " + "character.\n";
			return measuredValueErrorMessage + displayInput(input, currentCharNdxMeasuredValue);

		default:
			return "";
		}
	}

	/**********
	 * This method is a mechanical transformation of a Finite State Machine diagram
	 * into a Java method.
	 *
	 * @param input The input error string for the Finite State Machine
	 * @return An output string that is empty if every things is okay or it will be
	 *         a string with a help description of the error follow by two lines
	 *         that shows the input line follow by a line with an up arrow at the
	 *         point where the error was found.
	 */

	public static String checkErrorTerm(String input) {
		if (input.length() <= 0)
			return "";
		// The following are the local variable used to perform the Finite State Machine
		// simulation
		stateErrorTerm = 0; // This is the FSM state number
		inputLineErrorTerm = input; // Save the reference to the input line as a global
		currentCharNdxErrorTerm = 0; // The index of the current character
		currentCharErrorTerm = input.charAt(0); // The current character from the above indexed position

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state

		errorTermInput = input; // Set up the alternate result copy of the input
		runningErrorTerm = true; // Start the loop

		// The Finite State Machines continues until the end of the input is reached or
		// at some
		// state the current character does not match any valid transition to a next
		// state
		while (runningErrorTerm) {
			// The switch statement takes the execution to the code for the current state,
			// where
			// that code sees whether or not the current character is valid to transition to
			// a
			// next state
			switch (stateErrorTerm) {
			case 0:
				// State 0 has three valid transitions. Each is addressed by an if statement.

				// This is not a final state
				finalStateErrorTerm = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 1
				if (currentCharErrorTerm >= '1' && currentCharErrorTerm <= '9') {
					nextStateErrorTerm = 1;
					break;
				}
				// If the current character is a decimal point, it transitions to state 3
				else if (currentCharErrorTerm == '.') {
					nextStateErrorTerm = 3;
					break;
				}
				// If the current character is 0, it transitions to state 8
				else if (currentCharErrorTerm == '0') {
					nextStateErrorTerm = 8;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 1:
				// State 1 has three valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalStateErrorTerm = true;

				// If the character is 0, it is accepted and we stay in same state.
				if (currentCharErrorTerm == '0') {
					break;
				}

				// If the current character is a decimal point, it transitions to state 2
				else if (currentCharErrorTerm == '.') {
					nextStateErrorTerm = 2;
					break;
				}
				// If the current character is an E or an e, it transitions to state 5
				else if (currentCharErrorTerm == 'E' || currentCharErrorTerm == 'e') {
					nextStateErrorTerm = 5;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 2:
				// State 2 has one valid transition.

				// This is a final state
				finalStateErrorTerm = true;

				// If the current character is in the range from 0 to 9, it transitions to state
				// 5
				if (currentCharErrorTerm == 'E' || currentCharErrorTerm == 'e') {
					nextStateErrorTerm = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 3:
				// State 3 has two valid transitions. Each of them is addressed by an if
				// statement.

				// This is not a final state
				finalStateErrorTerm = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharErrorTerm >= '1' && currentCharErrorTerm <= '9') {
					nextStateErrorTerm = 4;
					break;
				}
				// If the character is 0, it is accepted and we stay in same state.
				else if (currentCharErrorTerm == '0') {
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 4:
				// State 4 has one valid transition.

				// This is a final state
				finalStateErrorTerm = true;

				// If the current character is an 'E' or 'e", it transitions to state 5
				if (currentCharErrorTerm == 'E' || currentCharErrorTerm == 'e') {
					nextStateErrorTerm = 5;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 5:
				// State 5 has two valid transitions. Each is addressed by an if statement.

				// This is a final state
				finalStateErrorTerm = false;

				// If the current character is in the range from 0 to 9, it transitions to state
				// 7
				if (currentCharErrorTerm >= '0' && currentCharErrorTerm <= '9') {
					nextStateErrorTerm = 7;
					break;
				}
				// If the current character is an '+' or '-", it transitions to state 6
				else if (currentCharErrorTerm == '+' || currentCharErrorTerm == '-') {
					nextStateErrorTerm = 6;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 6:
				// State 6 has one valid transition.

				// This is a final state
				finalStateErrorTerm = false;

				// If the current character is in the range from 0 to 9, it transitions to state
				// 7
				if (currentCharErrorTerm >= '0' && currentCharErrorTerm <= '9') {
					nextStateErrorTerm = 7;
					break;
				}

				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;

			case 7:
				// State 7 has one valid transition.

				// This is a final state
				finalStateErrorTerm = true;

				// If the current character is in the range from 0 to 9, it remains in the same
				// state.
				if (currentCharErrorTerm >= '0' && currentCharErrorTerm <= '9') {
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;
			case 8:
				// State 8 has one valid transition.

				// This is not a final state
				finalStateErrorTerm = false;

				// If the current character is a decimal point, it transitions to state 9
				if (currentCharErrorTerm == '.') {
					nextStateErrorTerm = 9;
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;
			case 9:
				// State 9 has two valid transitions. Each of them is addressed by an if
				// statement.

				// This is not a final state
				finalStateErrorTerm = false;

				// If the current character is in the range from 1 to 9, it transitions to state
				// 4
				if (currentCharErrorTerm >= '1' && currentCharErrorTerm <= '9') {
					nextStateErrorTerm = 4;
					break;
				}
				// If the character is 0, it is accepted and we stay in same state.
				else if (currentCharErrorTerm == '0') {
					break;
				}
				// If it is none of those characters, the FSM halts
				else
					runningErrorTerm = false;

				// The execution of this state is finished
				break;
			}

			if (runningErrorTerm) {
				displayDebuggingInfo();
				// When the processing of a state has finished, the FSM proceeds to the next
				// character
				// in the input and if there is one, it fetches that character and updates the
				// currentChar. If there is no next character the currentChar is set to a blank.
				moveToNextCharacterErrorTerm();

				// Move to the next state
				stateErrorTerm = nextStateErrorTerm;

			}
			// Should the FSM get here, the loop starts again

		}

		errorTermIndexofError = currentCharNdxErrorTerm; // Copy the index of the current character;

		// When the FSM halts, we must determine if the situation is an error or not.
		// That depends
		// of the current state of the FSM and whether or not the whole string has been
		// consumed.
		// This switch directs the execution to separate code for each of the FSM
		// states.
		switch (stateErrorTerm) {
		case 0:
			// State 0 is not a final state, so we can return a very specific error message
			errorTermIndexofError = currentCharNdxErrorTerm; // Copy the index of the current character;
			errorTermErrorMessage = "The first character must be a digit or a decimal point.";
			return "The first character must be a digit or a decimal point.";

		case 1:
			// State 1 is a final state, so we must see if the whole string has been
			// consumed.
			if (currentCharNdxErrorTerm < input.length()) {
				// If not all of the string has been consumed, we point to the current character
				// in the input line and specify what that character must be in order to move
				// forward.
				errorTermErrorMessage = "This character may only be an \"E\", an \"e\", 0, "
						+ "a decimal, or it must be the end of the input.\n";
				return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
			} else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}

		case 2:
			// States 2 and 4 are the same. They are both final states with only one
			// possible
			// transition forward, if the next character is an E or an e.
			if (currentCharNdxErrorTerm < input.length()) {
				errorTermErrorMessage = "This character may only be an \"E\", an \"e\", or it must"
						+ " be the end of the input.\n";
				return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
			}
			// If there is no more input, the input was recognized.
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}
		case 4:
			// States 2 and 4 are the same. They are both final states with only one
			// possible
			// transition forward, if the next character is an E or an e.
			if (currentCharNdxErrorTerm < input.length()) {
				errorTermErrorMessage = "This character may only be an \"E\", an \"e\", or it must"
						+ " be the end of the input.\n";
				return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
			}
			// If there is no more input, the input was recognized.
			else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}
		case 3:
			// States 3, and 6 are the same. None of them are final states and in order to
			// move forward, the next character must be a digit.
			errorTermErrorMessage = "This character may only be a digit.\n";
			return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
		case 6:
			// States 3, and 6 are the same. None of them are final states and in order to
			// move forward, the next character must be a digit.
			errorTermErrorMessage = "This character may only be a digit.\n";
			return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);

		case 7:
			// States 7 is similar to states 3 and 6, but it is a final state, so it must be
			// processed differently. If the next character is not a digit, the FSM stops
			// with an
			// error. We must see here if there are no more characters. If there are no more
			// characters, we accept the input, otherwise we return an error
			if (currentCharNdxErrorTerm < input.length()) {
				errorTermErrorMessage = "This character may only be a digit.\n";
				return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
			} else {
				errorTermIndexofError = -1;
				errorTermErrorMessage = "";
				return errorTermErrorMessage;
			}

		case 8:
			errorTermErrorMessage = "This character may only be a decimal.\n";
			return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);

		case 9:
			errorTermErrorMessage = "This character may only be a digit.\n";
			return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);

		case 5:
			// State 5 is not a final state. In order to move forward, the next character
			// must be
			// a digit or a plus or a minus character.
			errorTermErrorMessage = "This character may only be a digit, a plus, or minus " + "character.\n";
			return errorTermErrorMessage + displayInput(input, currentCharNdxErrorTerm);
		default:
			return "";
		}
	}
}
