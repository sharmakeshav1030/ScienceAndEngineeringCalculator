package calculator;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import calculator.BusinessLogic;

/**
 * <p>
 * Title: This is a UNumber Calculator with Square Root.
 * </p>
 *
 * <p>
 * Description: The Java/FX-based user interface for the calculator. The class
 * works with String objects and passes work to other classes to deal with all
 * other aspects of the computation.
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
 *          conversions and calculations. of the Units operands along with their
 *          proper implementation.
 *
 */

public class UserInterface {

	/**********************************************************************************************
	 * 
	 * Attributes
	 * 
	 **********************************************************************************************/

	/*
	 * Constants used to parameterize the graphical user interface. We do not use a
	 * layout manager for this application. Rather we manually control the location
	 * of each graphical element for exact control of the look and feel.
	 */
	private final double BUTTON_WIDTH = 60;
	private final double BUTTON_OFFSET = BUTTON_WIDTH / 2;

	// These are the application values required by the user interface
	private Label label_UNumberCalculator = new Label("Science and Engineering Calculator with Units");
	private Label label_Operand1 = new Label("First operand");
	private TextField text_Operand1 = new TextField();
	private TextField text_Operand3 = new TextField();
	private Label label_Operand2 = new Label("Second operand");
	private TextField text_Operand2 = new TextField();
	private TextField text_Operand4 = new TextField();
	private Label label_Result = new Label("Result");
	private TextField text_Result = new TextField();
	private TextField text_Resulterr = new TextField();
	private Label label_variable = new Label("\u00B1");
	private Label label_variable1 = new Label("\u00B1");
	private Label label_variable2 = new Label("\u00B1");
	private Button button_Add = new Button("+");
	private Button button_Sub = new Button("-");
	private Button button_Mpy = new Button("*"); // The multiply symbol: \u00D7
	private Button button_Div = new Button("\u00F7"); // The divide symbol: \u00F7
	private Button button_Sqrt = new Button("\u221A"); // The root symbol: \u221A
	private Label label_errOperand1 = new Label(""); // Label to display specific
	private Label label_errOperand2 = new Label(""); // error messages
	private Label label_errOperand3 = new Label(""); // Label to display specific
	private Label label_errOperand4 = new Label("");
	private Label label_errResult = new Label("");
	private Label label_errResulterr = new Label("");
	private TextFlow err1;
	private Text operand1ErrPart1 = new Text();
	private Text operand1ErrPart2 = new Text();
	private TextFlow err2;
	private Text operand2ErrPart1 = new Text();
	private Text operand2ErrPart2 = new Text();
	private TextFlow err3;
	private Text operand3ErrPart1 = new Text();
	private Text operand3ErrPart2 = new Text();
	private TextFlow err4;
	private Text operand4ErrPart1 = new Text();
	private Text operand4ErrPart2 = new Text();

	private Label units = new Label("Units");
	ComboBox<String> comboBox1 = new ComboBox<String>();
	ComboBox<String> comboBox2 = new ComboBox<String>();
	ComboBox<String> comboBoxRes = new ComboBox<String>();
	private boolean firstTime = true;
	/*
	 * This is the implementation of the lookup table which is used as a mapping
	 * between units and factors.
	 */
	private String[][] lookupTable = {
			{ "", "m", "km", "km/seconds", "miles", "miles/seconds", "seconds", "days", "m2", "km2" },
			{ "m", "1", "0.001", "-", "0.00621371", "-", "-", "-", "-", "-" },
			{ "km", "1000", "1", "-", "0.621371", "-", "-", "-", "-", "-" },
			{ "km/seconds", "-", "-", "1", "-", "0.621371", "-", "-", "-", "-" },
			{ "miles", "1609.34", "1.60934", "-", "1", "-", "-", "-", "-", "-" },
			{ "miles/seconds", "-", "-", "1.60934", "-", "1", "-", "-", "-", "-" },
			{ "seconds", "-", "-", "-", "-", "-", "1", "0.00001157407", "-", "-" },
			{ "days", "-", "-", "-", "-", "-", "86400", "1", "-", "-" },
			{ "m2", "-", "-", "-", "-", "-", "-", "-", "1", "0.000001" },
			{ "km2", "-", "-", "-", "-", "-", "-", "-", "1000000", "1" } };

	/*
	 * This is the matrix which is used to check whether the conversions of the
	 * units mentioned in the lookup table is possible or not.
	 */
	private boolean PossibileConversions[][] = {
			{ false, false, false, false, false, false, false, false, false, false },
			{ false, true, true, false, true, false, false, false, false, false },
			{ false, true, true, false, true, false, false, false, false, false },
			{ false, false, false, true, false, true, false, false, false, false },
			{ false, true, true, false, true, false, false, false, false, false },
			{ false, false, false, true, false, true, false, false, false, false },
			{ false, false, false, false, false, false, true, true, false, false },
			{ false, false, false, false, false, false, true, true, false, false },
			{ false, false, false, false, false, false, false, false, true, true },
			{ false, false, false, false, false, false, false, false, true, true } };

	// Following are the values which will be used for the conversions.
	int ndxOldValue = -2;
	int ndxNewValue = -2;
	private String lastUnit = null;
	// If the multiplication and/or division symbols do not display properly,
	// replace the
	// quoted strings used in the new Button constructor call with the
	// <backslash>u00xx values
	// shown on the same line. This is the Unicode representation of those
	// characters and will
	// work regardless of the underlying hardware being used.
	private double buttonSpace;
	// This is the white space between the operator buttons.

	/* This is the link to the business logic */
	public BusinessLogic perform = new BusinessLogic();

	/**********************************************************************************************
	 * 
	 * Constructors
	 * 
	 **********************************************************************************************/

	/**********
	 * This method initializes all of the elements of the graphical user interface.
	 * These assignments determine the location, size, font, color, and change and
	 * event handlers for each GUI object.
	 */

	public UserInterface(Pane theRoot) {

		// There are five gaps. Compute the button space accordingly.
		buttonSpace = Calculator.WINDOW_WIDTH / 6.5;

		// Label theScene with the name of the calculator, centered at the top of the
		// pane
		setupLabelUI(label_UNumberCalculator, "Arial", 24, Calculator.WINDOW_WIDTH, Pos.CENTER, 0, 10);

		// Label the first operand just above it, left aligned
		setupLabelUI(label_Operand1, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 35, 75);

		// Establish the first text input operand field and when anything changes in
		// operand 1,
		// process both fields to ensure that we are ready to perform as soon as
		// possible.
		setupTextUI(text_Operand1, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 150, 70, true);
		text_Operand1.textProperty().addListener((observable, oldValue, newValue) -> {
			setOperand1();
		});
		// Move focus to the second operand when the user presses the enter (return) key
		text_Operand1.setOnAction((event) -> {
			if (text_Operand1.getText().isEmpty())
				label_errOperand1.setText("Please enter the Values");
			text_Operand3.requestFocus();
		});

		// Bottom proper error message
		label_errOperand1.setTextFill(Color.RED);
		label_errOperand1.setAlignment(Pos.BASELINE_LEFT);
		setupLabelUI(label_errOperand1, "Arial", 14, Calculator.WINDOW_WIDTH, Pos.BASELINE_LEFT, 142, 128);

		setupLabelUI(label_variable, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 515, 75);

		// Label the units and set the text field for the units for the first value.
		setupLabelUI(units, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 990, 45);

		comboBox1.setPromptText("");
		comboBox1.getItems().addAll("km", "miles", "m", "km/seconds", "miles/seconds", "days", "seconds", "No Unit");
		comboBox1.setLayoutX(940);
		comboBox1.setLayoutY(70);
		comboBox1.setOnAction(event -> {
			perform.setOperand1(text_Operand1.getText());
			perform.setOperand2(text_Operand2.getText());
			perform.setOperand3(text_Operand3.getText());
			perform.setOperand4(text_Operand1.getText());
		});
		// Establish the Third text input operand field and when anything changes in
		// operand 3,
		// process both fields to ensure that we are ready to perform as soon as
		// possible.
		setupTextUI(text_Operand3, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 540, 70, true);
		text_Operand3.textProperty().addListener((observable, oldValue, newValue) -> {
			setOperand3();
		});
		// Move focus to the Third operand when the user presses the enter (return) key
		text_Operand3.setOnAction((event) -> {
			text_Operand2.requestFocus();
		});

		// Bottom proper error message
		label_errOperand3.setTextFill(Color.RED);
		label_errOperand3.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelUI(label_errOperand3, "Arial", 14, Calculator.WINDOW_WIDTH - 100 - 10, Pos.BASELINE_LEFT, 540, 128);

		// Label the second operand just above it, left aligned
		setupLabelUI(label_Operand2, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 10, 157.5);

		// Establish the second text input operand field and when anything changes in
		// operand 2,
		// process both fields to ensure that we are ready to perform as soon as
		// possible.
		setupTextUI(text_Operand2, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 150, 155, true);
		text_Operand2.textProperty().addListener((observable, oldValue, newValue) -> {
			setOperand2();
		});

		// Move the focus to the result when the user presses the enter (return) key
		text_Operand2.setOnAction((event) -> {
			if (text_Operand2.getText().isEmpty()) {
				label_errOperand2.setText("Please enter the Values");
			}
			text_Operand4.requestFocus();
		});

		// Bottom proper error message
		label_errOperand2.setTextFill(Color.RED);
		label_errOperand2.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelUI(label_errOperand2, "Arial", 14, Calculator.WINDOW_WIDTH - 150 - 10, Pos.BASELINE_LEFT, 150, 210);
		label_errOperand2.setTextFill(Color.RED);

		setupLabelUI(label_variable1, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 515, 160);

		// Establish the Fourth text input operand field and when anything changes in
		// operand 4,
		// process both fields to ensure that we are ready to perform as soon as
		// possible.
		setupTextUI(text_Operand4, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 540, 155, true);
		text_Operand4.textProperty().addListener((observable, oldValue, newValue) -> {
			setOperand4();
		});

		// Bottom proper error message
		label_errOperand4.setTextFill(Color.RED);
		label_errOperand4.setAlignment(Pos.BASELINE_RIGHT);
		setupLabelUI(label_errOperand4, "Arial", 14, Calculator.WINDOW_WIDTH - 150 - 10, Pos.BASELINE_LEFT, 540, 210);
		label_errOperand4.setTextFill(Color.RED);

		// Set the text field for the units for the second value.
		// Label the units and set the text field for the units for the first value.

		comboBox2.setPromptText("");
		comboBox2.getItems().addAll("km", "miles", "m", "km/seconds", "miles/seconds", "days", "seconds", "No Unit");
		comboBox2.setLayoutX(940);
		comboBox2.setLayoutY(155);
		comboBox2.setOnAction(event -> {
			perform.setOperand1(text_Operand1.getText());
			perform.setOperand2(text_Operand2.getText());
			perform.setOperand3(text_Operand3.getText());
			perform.setOperand4(text_Operand1.getText());
		});

		// Label the result just above the result output field, left aligned
		setupLabelUI(label_Result, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 85, 345);

		// Establish the result output field. It is not editable, so the text can be
		// selected and copied,
		// but it cannot be altered by the user. The text is left aligned.
		setupTextUI(text_Result, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 150, 340, false);

		// Establish an error message for the Result operand just above it with, right
		// aligned

		setupLabelUI(label_errResult, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 150, 210);
		label_errResult.setTextFill(Color.RED);

		setupLabelUI(label_variable2, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 515, 345);

		// Establish the result output field. It is not editable, so the text can be
		// selected and copied,
		// but it cannot be altered by the user. The text is left aligned.
		setupTextUI(text_Resulterr, "Arial", 18, Calculator.WINDOW_WIDTH - 850, Pos.BASELINE_LEFT, 540, 340, false);

		// Establish an error message for the Result operand just above it with, right
		// aligned
		setupLabelUI(label_errResulterr, "Arial", 18, Calculator.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 540, 340);
		label_errResulterr.setTextFill(Color.RED);

		// Set the text field for the units for the second value.
		// Label the units and set the text field for the units for the first value.
		comboBoxRes.setPromptText("");
		comboBoxRes.getItems().addAll("km     ", "miles   ", "m   ", "s      ", "Days   ", "No Unit");
		lastUnit = comboBoxRes.getSelectionModel().getSelectedItem();
		comboBoxRes.setLayoutX(940);
		comboBoxRes.setLayoutY(340);
//		comboBoxRes.setOnAction((event -> {
//			changedResult();
//			firstTime = false;
//		}));

		// Establish the ADD "+" button, position it, and link it to methods to
		// accomplish its work
		setupButtonUI(button_Add, "Symbol", 32, BUTTON_WIDTH, Pos.BASELINE_LEFT, 1 * buttonSpace - BUTTON_OFFSET, 245);
		button_Add.setOnAction((event) -> {
			addOperands();
		});

		// Establish the SUB "-" button, position it, and link it to methods to
		// accomplish its work
		setupButtonUI(button_Sub, "Symbol", 32, BUTTON_WIDTH, Pos.BASELINE_LEFT, 2 * buttonSpace - BUTTON_OFFSET, 245);
		button_Sub.setOnAction((event) -> {
			subOperands();
		});

		// Establish the MPY "x" button, position it, and link it to methods to
		// accomplish its work
		setupButtonUI(button_Mpy, "Symbol", 32, BUTTON_WIDTH, Pos.BASELINE_LEFT, 3 * buttonSpace - BUTTON_OFFSET, 245);
		button_Mpy.setOnAction((event) -> {
			mpyOperands();
		});

		// Establish the DIV "/" button, position it, and link it to methods to
		// accomplish its work
		setupButtonUI(button_Div, "Symbol", 32, BUTTON_WIDTH, Pos.BASELINE_LEFT, 4 * buttonSpace - BUTTON_OFFSET, 245);
		button_Div.setOnAction((event) -> {
			divOperands();
		});

		// Establish the SQRT "root" button, position it, and link it to methods to
		// accomplish its work
		setupButtonUI(button_Sqrt, "Symbol", 32, BUTTON_WIDTH, Pos.BASELINE_LEFT, 5 * buttonSpace - BUTTON_OFFSET, 245);
		button_Sqrt.setOnAction((event) -> {
			sqrtOperands();
		});

		// Error Message for the Measured Value for operand 1
		operand1ErrPart1.setFill(Color.BLACK);
		operand1ErrPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
		operand1ErrPart2.setFill(Color.RED);
		operand1ErrPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		err1 = new TextFlow(operand1ErrPart1, operand1ErrPart2);
		err1.setMinWidth(Calculator.WINDOW_WIDTH - 10);
		err1.setLayoutX(160);
		err1.setLayoutY(100);

		// Error Message for the Measured Value for operand 2
		operand2ErrPart1.setFill(Color.BLACK);
		operand2ErrPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
		operand2ErrPart2.setFill(Color.RED);
		operand2ErrPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		err2 = new TextFlow(operand2ErrPart1, operand2ErrPart2);
		err2.setMinWidth(Calculator.WINDOW_WIDTH - 10);
		err2.setLayoutX(160);
		err2.setLayoutY(190);

		// Error Message for the Measured Value for operand 3
		operand3ErrPart1.setFill(Color.BLACK);
		operand3ErrPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
		operand3ErrPart2.setFill(Color.RED);
		operand3ErrPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		err3 = new TextFlow(operand3ErrPart1, operand3ErrPart2);
		err3.setMinWidth(Calculator.WINDOW_WIDTH - 10);
		err3.setLayoutX(560);
		err3.setLayoutY(100);

		// Error Message for the Measured Value for operand 4
		operand4ErrPart1.setFill(Color.BLACK);
		operand4ErrPart1.setFont(Font.font("Arial", FontPosture.REGULAR, 18));
		operand4ErrPart2.setFill(Color.RED);
		operand4ErrPart2.setFont(Font.font("Arial", FontPosture.REGULAR, 24));
		err4 = new TextFlow(operand4ErrPart1, operand4ErrPart2);
		err4.setMinWidth(Calculator.WINDOW_WIDTH - 10);
		err4.setLayoutX(560);
		err4.setLayoutY(190);

		// Place all of the just-initialized GUI elements into the pane
		theRoot.getChildren().addAll(label_UNumberCalculator, label_variable, label_variable1, label_variable2,
				label_Operand1, text_Operand1, label_errOperand1, label_Operand2, text_Operand2, label_errOperand2,
				text_Operand3, label_errOperand3, text_Operand4, label_errOperand4, label_Result, text_Result,
				label_errResult, text_Resulterr, label_errResulterr, button_Add, button_Sub, button_Mpy, button_Div,
				button_Sqrt, err1, err2, err3, err4, units, comboBox1, comboBox2, comboBoxRes);

	}

//	private void changedResult() {
//		if (firstTime)
//			lastUnit = comboBoxRes.getSelectionModel().getSelectedItem();
//		
//		String newUnit = comboBoxRes.getSelectionModel().getSelectedItem();
//		System.out.println(text_Result.getText());
//		if (conversionIsRequired(lastUnit, newUnit)&&text_Result.getText()!="") {
//			double op1 = Double.parseDouble(text_Result.getText());
//			double op1et = Double.parseDouble(text_Resulterr.getText());
//			text_Result.setText(String.valueOf(op1 * theFactor));
//			text_Resulterr.setText(String.valueOf(op1et * theFactor));
//		}
//		lastUnit = newUnit;
//	}

	/*******
	 * This public methods invokes the methods of Calculator class and generate a
	 * specific error message when the user enters the value of operand1
	 *
	 */
	public void err1() {
		String errMessage = CalculatorValue.checkMeasureValue(text_Operand1.getText());
		if (errMessage != "") {
			label_errOperand1.setText(CalculatorValue.measuredValueErrorMessage);
			if (CalculatorValue.measuredValueIndexofError <= -1)
				return;
			String input = CalculatorValue.measuredValueInput;
			operand1ErrPart1.setText(input.substring(0, CalculatorValue.measuredValueIndexofError));
			operand1ErrPart2.setText("\u21EB");
		}
	}

	/*******
	 * This public methods invokes the methods of Calculator class and generate a
	 * specific error message when the user enters the value of operand2
	 *
	 */
	public void err2() {
		String errMessage = CalculatorValue.checkMeasureValue(text_Operand2.getText());
		if (errMessage != "") {
			label_errOperand2.setText(CalculatorValue.measuredValueErrorMessage);
			if (CalculatorValue.measuredValueIndexofError <= -1)
				return;
			String input = CalculatorValue.measuredValueInput;
			operand2ErrPart1.setText(input.substring(0, CalculatorValue.measuredValueIndexofError));
			operand2ErrPart2.setText("\u21EB");
		}
	}

	/*******
	 * This public methods invokes the methods of Calculator class and generate a
	 * specific error message when the user enters the value of operand2
	 *
	 */

	private void err3() {
		String errMessage = CalculatorValue.checkMeasureValue(text_Operand3.getText());
		if (errMessage != "") {
			// System.out.println(errMessage);
			label_errOperand3.setText(CalculatorValue.measuredValueErrorMessage);
			if (CalculatorValue.measuredValueIndexofError <= -1)
				return;
			String input = CalculatorValue.measuredValueInput;
			operand3ErrPart1.setText(input.substring(0, CalculatorValue.measuredValueIndexofError));
			operand3ErrPart2.setText("\u21EB");
		} else {
			errMessage = CalculatorValue.checkErrorTerm(text_Operand3.getText());
			if (errMessage != "") {
				// System.out.println(errMessage);
				label_errOperand3.setText(CalculatorValue.errorTermErrorMessage);
				String input = CalculatorValue.errorTermInput;
				if (CalculatorValue.errorTermIndexofError <= -1)
					return;
				operand3ErrPart1.setText(input.substring(0, CalculatorValue.errorTermIndexofError));
				operand3ErrPart2.setText("\u21EB");
			}
		}
	}

	/*******
	 * This public methods invokes the methods of Calculator class and generate a
	 * specific error message when the user enters the value of operand 4.
	 *
	 */

	private void err4() {
		String errMessage = CalculatorValue.checkMeasureValue(text_Operand4.getText());
		if (errMessage != "") {
			// System.out.println(errMessage);
			label_errOperand4.setText(CalculatorValue.measuredValueErrorMessage);
			if (CalculatorValue.measuredValueIndexofError <= -1)
				return;
			String input = CalculatorValue.measuredValueInput;
			operand4ErrPart1.setText(input.substring(0, CalculatorValue.measuredValueIndexofError));
			operand4ErrPart2.setText("\u21EB");
		} else {
			errMessage = CalculatorValue.checkErrorTerm(text_Operand4.getText());
			if (errMessage != "") {
				// System.out.println(errMessage);
				label_errOperand4.setText(CalculatorValue.errorTermErrorMessage);
				String input = CalculatorValue.errorTermInput;
				if (CalculatorValue.errorTermIndexofError <= -1)
					return;
				operand4ErrPart1.setText(input.substring(0, CalculatorValue.errorTermIndexofError));
				operand4ErrPart2.setText("\u21EB");
			}
		}
	}

	/**********
	 * Private local method to initialize the standard fields for a label
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	/**********
	 * Private local method to initialize the standard fields for a text field
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e) {
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
	}

	/**********
	 * Private local method to initialize the standard fields for a button
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

	/**********************************************************************************************
	 * 
	 * User Interface Actions
	 * 
	 **********************************************************************************************/

	/**********
	 * Private local method to set the value of the first operand given a text
	 * value. The method uses the business logic class to perform the work of
	 * checking the string to see it is a valid value and if so, saving that value
	 * internally for future computations. If there is an error when trying to
	 * convert the string into a value, the called business logic method returns
	 * false and actions are taken to display the error message appropriately.
	 */
	private void setOperand1() {
		text_Result.setText(""); // Any change of an operand probably invalidates
		label_Result.setText("Result"); // the result, so we clear the old result.
		label_errResult.setText("");
		if (perform.setOperand1(text_Operand1.getText())) { // Set the operand and see if there was an error
			label_errOperand1.setText(""); // If no error, clear this operands error
			operand1ErrPart1.setText(""); // Clear the first term of error part
			operand1ErrPart2.setText(""); // Clear the second term of error part
			if (text_Operand2.getText().length() == 0) // If the other operand is empty, clear its error
				label_errOperand2.setText(""); // as well.
		} else // If there's a problem with the operand, display
			err1();
	}

	/**********
	 * Private local method to set the value of the second operand given a text
	 * value. The logic is exactly the same as used for the first operand, above.
	 */
	private void setOperand2() {
		text_Result.setText("");
		label_Result.setText("Result");
		label_errResult.setText("");
		if (perform.setOperand2(text_Operand2.getText())) {
			label_errOperand2.setText("");
			operand2ErrPart1.setText(""); // Clear the first term of error part
			operand2ErrPart2.setText(""); // Clear the second term of error part
			if (text_Operand1.getText().length() == 0)
				label_errOperand1.setText("");
		} else
			err2();
	}

	private void setOperand3() {
		label_errOperand3.setText("");
		operand3ErrPart1.setText("");
		operand3ErrPart2.setText("");
		if (perform.setOperand3(text_Operand3.getText())) {
			text_Resulterr.setText(""); // Any change of an operand probably invalidates
			label_errResulterr.setText(""); // Set the operand and see if there was an error
			label_errOperand3.setText("");
		} // If no error, clear this operands error

		if (text_Operand4.getText().length() == 0) // If the other operand is empty, clear its error
			label_errOperand4.setText(""); // as well.

		err3();
	}

	private void setOperand4() {
		label_errOperand4.setText("");
		operand4ErrPart1.setText("");
		operand4ErrPart2.setText("");
		if (perform.setOperand4(text_Operand4.getText())) {
			text_Resulterr.setText(""); // Any change of an operand probably invalidates
			label_errResulterr.setText(""); // Set the operand and see if there was an error
			label_errOperand3.setText("");
		} // If no error, clear this operands error

		if (text_Operand3.getText().length() == 0) // If the other operand is empty, clear its error
			label_errOperand3.setText(""); // as well.

		err4();
	}

	/**********
	 * Private local method to set the value of the first operand given a text
	 * value. The method uses the business logic class to perform the work of
	 * checking the string to see it is a valid value and if so, saving that value
	 * internally for future computations. If there is an error when trying to
	 * convert the string into a value, the called business logic method returns
	 * false and actions are taken to display the error message appropriately.
	 */

	/**********
	 * Private local method to set the value of the first operand given a text
	 * value. The method uses the business logic class to perform the work of
	 * checking the string to see it is a valid value and if so, saving that value
	 * internally for future computations. If there is an error when trying to
	 * convert the string into a value, the called business logic method returns
	 * false and actions are taken to display the error message appropriately.
	 */

	/**********
	 * This method is called when an binary operation (expect square root) button
	 * has been pressed. It assesses if there are issues with either of the binary
	 * operands or they are not defined. If not return false (there are no issues)
	 *
	 * @return True if there are any issues that should keep the calculator from
	 *         doing its work.
	 */

	private boolean binaryOperandIssues() {
		String errorMessage1 = perform.getOperand1ErrorMessage(); // Fetch the error messages, if there are any
		String errorMessage2 = perform.getOperand2ErrorMessage();
		if (errorMessage1.length() > 0) { // Check the first. If the string is not empty
			label_errOperand1.setText(errorMessage1); // there's an error message, so display it.
			if (errorMessage2.length() > 0) { // Check the second and display it if there is
				label_errOperand2.setText(errorMessage2); // and error with the second as well.

				return true; // Return true when both operands have errors
			} else {
				return true; // Return true when only the first has an error
			}
		} else if (errorMessage2.length() > 0) { // No error with the first, so check the second
			label_errOperand2.setText(errorMessage2); // operand. If non-empty string, display the error
			return true; // message and return true... the second has an error
		} // Signal there are issues

		// If the code reaches here, neither the first nor the second has an error
		// condition. The following code
		// check to see if the operands are defined.
		if (!perform.getOperand1Defined()) { // Check to see if the first operand is defined
			label_errOperand1.setText("No value found"); // If not, this is an issue for a binary operator
			if (!perform.getOperand2Defined()) { // Now check the second operand. It is is also
				label_errOperand2.setText("No value found"); // not defined, then two messages should be displayed

				return true; // Signal there are issues
			}
			return true;
		}

		else if (!perform.getOperand2Defined()) { // If the first is defined, check the second. Both
			label_errOperand2.setText("No value found"); // operands must be defined for a binary operator.
			return true; // Signal there are issues
		}
		return false; // Signal there are no issues with the operands
	}

	/**********
	 * This method is called when square root button has been pressed. It assesses
	 * if there are issues with either of the binary operand1 or it is not defined.
	 * If not return false (there are no issues) As to perform square root, we only
	 * need operand1 thus any value added in the second field is automatically
	 * cleared when square root button is pressed
	 *
	 * @return True if there are any issues that should keep the calculator from
	 *         doing its work.
	 */

	private boolean unaryOperandIssues() {
		String errorMessage1 = perform.getOperand1ErrorMessage();
		if (errorMessage1.length() == 0) {
			label_errOperand1.setText(errorMessage1);
		}

		if (!perform.getOperand1Defined()) {
			label_errOperand1.setText("No Value found");
			return true;
		}
		return false;
	}

	/*******************************************************************************************************
	 * This portion of the class defines the actions that take place when the
	 * various calculator buttons (add, subtract, multiply, divide and square root)
	 * are pressed.
	 */

	/**********
	 * This is the add routine
	 *
	 */

	private void addOperands() {

		if (text_Operand3.getText().isEmpty()) {
			text_Operand3.setText("5");
		}
		if (text_Operand4.getText().isEmpty()) {
			text_Operand4.setText("5");
		}

		/*
		 * It will pass the value to the bussiness logic so trhat the further logics can
		 * be performed on them.
		 */

		if (conversionIsRequired(comboBox1.getSelectionModel().getSelectedItem(),
				comboBox2.getSelectionModel().getSelectedItem())) {
			double op1 = Double.parseDouble(text_Operand1.getText());
			double op1et = Double.parseDouble(text_Operand3.getText());
			perform.setOperand1(String.valueOf(op1 * theFactor));
			perform.setOperand3(String.valueOf(op1et * theFactor));
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setContentText("Addition Not Possible");
			a.show();
			return;
		}

		comboBoxRes.getSelectionModel().select(comboBox2.getSelectionModel().getSelectedItem());
		// Check to see if both operands are defined and valid
		if (binaryOperandIssues()) // If there are issues with the operands, return
			return; // without doing the computation

		// If the operands are defined and valid, request the business logic method to
		// do the addition and return the
		// result as a String. If there is a problem with the actual computation, an
		// empty string is returned
		String theAnswer = perform.addition(); // Call the business logic add method
		label_errResult.setText(""); // Reset any result error messages from before
		String theAnswer1 = perform.addition1(); // Call the business logic add method
		label_errResulterr.setText(""); // Reset any result error messages from before
		if (theAnswer.length() > 0 || theAnswer1.length() > 0) { // Check the returned String to see if it is okay
			text_Result.setText(theAnswer); // If okay, display it in the result field and
			label_Result.setText("Sum"); // change the title of the field to "Sum".
			text_Resulterr.setText(theAnswer1); // If okay, display it in the result field.
			label_Result.setLayoutX(100);
			label_Result.setLayoutY(345);
		} else { // Some error occurred while doing the addition.
			text_Result.setText(""); // Do not display a result if there is an error.
			label_Result.setText("Result"); // Reset the result label if there is an error.
			label_errResult.setText(perform.getResultErrorMessage()); // Display the error message.
			text_Resulterr.setText(""); // Do not display a result if there is an error.
			label_errResulterr.setText(perform.getResulterrErrorMessage()); // Display the error message.
		}
	}

	/**********
	 * This is the subtract routine
	 *
	 */
	private void subOperands() {

		if (text_Operand3.getText().isEmpty()) {
			text_Operand3.setText("5");
		}
		if (text_Operand4.getText().isEmpty()) {
			text_Operand4.setText("5");
		}

		/*
		 * It will pass the value to the bussiness logic so trhat the further logics can
		 * be performed on them.
		 */

		if (conversionIsRequired(comboBox1.getSelectionModel().getSelectedItem(),
				comboBox2.getSelectionModel().getSelectedItem())) {
			double op1 = Double.parseDouble(text_Operand1.getText());
			double op1et = Double.parseDouble(text_Operand3.getText());
			perform.setOperand1(String.valueOf(op1 * theFactor));
			perform.setOperand3(String.valueOf(op1et * theFactor));
		} else {
			Alert a = new Alert(AlertType.WARNING);
			a.setContentText("Subtraction Not Possible");
			a.show();
			return;
		}
		comboBoxRes.getSelectionModel().select(comboBox2.getSelectionModel().getSelectedItem());
		// Check to see if both operands are defined and valid
		if (binaryOperandIssues()) // If there are issues with the operands, return
			return; // without doing the computation

		// If the operands are defined and valid, request the business logic method to
		// do the Subtraction and return the
		// result as a String. If there is a problem with the actual computation, an
		// empty string is returned
		String theAnswer = perform.subtraction(); // Call the business logic Subtract method
		label_errResult.setText(""); // Reset any result error messages from before
		String theAnswer1 = perform.subtraction1(); // Call the business logic Subtract method
		label_errResulterr.setText(""); // Reset any result error messages from before
		if (theAnswer.length() > 0 || theAnswer1.length() > 0) { // Check the returned String to see if it is okay
			text_Result.setText(theAnswer); // If okay, display it in the result field and
			label_Result.setText("Difference"); // change the title of the field to "Subtraction".
			text_Resulterr.setText(theAnswer1); // If okay, display it in the result field
			label_Result.setLayoutX(60);
			label_Result.setLayoutY(345);
		} else { // Some error occurred while doing the Subtraction.
			text_Result.setText(""); // Do not display a result if there is an error.
			label_Result.setText("Result"); // Reset the result label if there is an error.
			label_errResult.setText(perform.getResultErrorMessage()); // Display the error message.
			text_Resulterr.setText(""); // Do not display a result if there is an error.
			label_errResulterr.setText(perform.getResulterrErrorMessage()); // Display the error message.
		}

	}

	/**********
	 * This is the multiply routine
	 *
	 */
	private void mpyOperands() {
		if (text_Operand3.getText().isEmpty()) {
			text_Operand3.setText("5");
		}
		if (text_Operand4.getText().isEmpty()) {
			text_Operand4.setText("5");
		}

		/*
		 * It will pass the value to the bussiness logic so trhat the further logics can
		 * be performed on them.
		 */

		if (conversionIsRequired(comboBox1.getSelectionModel().getSelectedItem(),
				comboBox2.getSelectionModel().getSelectedItem())) {
			double op1 = Double.parseDouble(text_Operand1.getText());
			double op1et = Double.parseDouble(text_Operand3.getText());
			perform.setOperand1(String.valueOf(op1 * theFactor));
			perform.setOperand3(String.valueOf(op1et * theFactor));
		}
		// Check to see if both operands are
		if (binaryOperandIssues()) // If there are issues with the operands, return
			return; // without doing the computation

		// If the operands are defined and valid, request the business logic method to
		// do the Multiplication and return the
		// result as a String. If there is a problem with the actual computation, an
		// empty string is returned
		String theAnswer = perform.multiplication(); // Call the business logic product method
		String theAnswer1 = perform.multiplication1(); // Call the business logic Product method
		label_errResult.setText(""); // Reset any result error messages from before
		label_errResulterr.setText(""); // Reset any result error messages from before
		if (theAnswer.length() > 0 || theAnswer1.length() > 0) { // Check the returned String to see if it is okay
			text_Result.setText(theAnswer); // If okay, display it in the result field and
			text_Resulterr.setText(theAnswer1);
			label_Result.setText("Product"); // change the title of the field to "Product".
			label_Result.setLayoutX(80);
			label_Result.setLayoutY(345);
			comboBoxRes.getSelectionModel().select(comboBox2.getSelectionModel().getSelectedItem() + "x"
					+ comboBox1.getSelectionModel().getSelectedItem());
		} else { // Some error occurred while doing the Multiplication.
			text_Result.setText(""); // Do not display a result if there is an error.
			text_Resulterr.setText(""); // Do not display a result if there is an error.
			label_Result.setText("Result"); // Reset the result label if there is an error.
			label_errResult.setText(perform.getResultErrorMessage()); // Display the error message.
			label_errResulterr.setText(perform.getResulterrErrorMessage()); // Display the error message.
		}

	}

	/**********
	 * This is the divide routine. If the divisor is zero, the divisor is declared
	 * to be invalid.
	 *
	 */
	private void divOperands() {
		if (text_Operand3.getText().isEmpty()) {
			text_Operand3.setText("5");
		}
		if (text_Operand4.getText().isEmpty()) {
			text_Operand4.setText("5");
		}

		/*
		 * It will pass the value to the bussiness logic so trhat the further logics can
		 * be performed on them.
		 */

		if (conversionIsRequired(comboBox1.getSelectionModel().getSelectedItem(),
				comboBox2.getSelectionModel().getSelectedItem())) {
			double op1 = Double.parseDouble(text_Operand1.getText());
			double op1et = Double.parseDouble(text_Operand3.getText());
			perform.setOperand1(String.valueOf(op1 * theFactor));
			perform.setOperand3(String.valueOf(op1et * theFactor));
		}
		if (binaryOperandIssues()) // If there are issues with the operands, return
			return; // without doing the computation
		double x = Double.parseDouble(text_Operand2.getText());
		if (x == 0f) {
			label_errResult.setText("Divide by zero is not allowed");
			text_Result.setText("");

		} else {
			String theAnswer = perform.division(); // Call the business logic Division method
			label_errResult.setText(""); // Reset any result error messages from before
			String theAnswer1 = perform.division1(); // Call the business logic Division method
			label_errResulterr.setText(""); // Reset any result error messages from before

			if (theAnswer.length() > 0 || theAnswer1.length() > 0) { // Check the returned String to see if it is okay
				text_Result.setText(theAnswer); // If okay, display it in the result field and
				label_Result.setText("Quotient"); // change the title of the field to "Divide".
				label_Result.setLayoutX(70);
				label_Result.setLayoutY(345);
				text_Resulterr.setText(theAnswer1); // If okay, display it in the result field and
				// change the title of the field to "Divide"
				comboBoxRes.getSelectionModel().select(comboBox1.getSelectionModel().getSelectedItem() + "/"
						+ comboBox2.getSelectionModel().getSelectedItem());
			} else { // Some error occurred while doing the division.
				text_Result.setText(""); // Do not display a result if there is an error.
				label_Result.setText("Result"); // Reset the result label if there is an error.
				label_errResult.setText(perform.getResultErrorMessage()); // Display the error message.
				text_Resulterr.setText(""); // Do not display a result if there is an error.
				// Reset the result label if there is an error.
				label_errResulterr.setText(perform.getResulterrErrorMessage()); // Display the error message.
			}

		}
	}

	/**********
	 * This is the square root routine.
	 *
	 */
	private void sqrtOperands() {
		if (text_Operand1.getText().isEmpty()) {
			text_Operand3.setText("5");
		}
		if (unaryOperandIssues())
			return;

		if (text_Operand1.getText() != "")
			if (text_Operand1.getText().charAt(0) == '-') {
				label_errOperand1.setText("Negative Input not Allowed");
				text_Result.setText("");
				text_Resulterr.setText("");
				label_Result.setText("");
				label_errResult.setText("");
				return;
			}
		if (text_Operand1.getText().isEmpty()) {
			text_Operand1.setText("0.0");
		}
		// Check to see if both operands are defined and valid

		// Check to see if both operands are defined and valid
		// without doing the computation

		if (text_Operand2.getLength() != 0) {
			text_Operand2.setText("");
			label_errOperand2.setText("Second Input not Required.");
		}
		if (text_Operand4.getLength() != 0) {
			text_Operand4.setText("");
			label_errOperand4.setText("Second Input not Required.");

		}

		// If the operands are defined and valid, request the business logic method to
		// do the Square Root and return the
		// result as a String. If there is a problem with the actual computation, an
		// empty string is returned
		String theAnswer = perform.squareroot(); // Call the business logic Root method
		label_errResult.setText(""); // Reset any result error messages from before
		String theAnswer1 = perform.squareroot1(); // Call the business logic Square Root method
		label_errResulterr.setText(""); // Reset any result error messages from before
		if (theAnswer.length() > 0 || theAnswer1.length() > 0) { // Check the returned String to see if it is okay
			text_Result.setText(theAnswer); // If okay, display it in the result field and
			label_Result.setText("Square Root"); // change the title of the field to "Square Root".
			label_Result.setLayoutX(40);
			label_Result.setLayoutY(345);
			text_Resulterr.setText(theAnswer1); // If okay, display it in the result field and
			comboBoxRes.getSelectionModel().select("\u221A" + comboBox1.getSelectionModel().getSelectedItem());
			// change the title of the field to "Square Root"
		} else { // Some error occurred while doing the Square Root.
			text_Result.setText(""); // Do not display a result if there is an error.
			label_Result.setText("Result"); // Reset the result label if there is an error.
			label_errResult.setText(perform.getResultErrorMessage()); // Display the error message.
			text_Resulterr.setText(""); // Do not display a result if there is an error.
			// Reset the result label if there is an error.
			label_errResulterr.setText(perform.getResulterrErrorMessage()); // Display the error message.
		}
	}

	/*
	 * Following is the implementation of the logic which checks if the two units
	 * selected are compatible to do the operations or not.
	 */

	// Some constants which are used in the loop are used.
	private double theFactor = 1;
	private int rowi = -1;
	private int coli = -1;

	private boolean conversionIsRequired(String selectedItem, String selectedItem2) {

		if (selectedItem.equals(selectedItem2))
			return true;
		for (int i = 0; i < lookupTable.length; i++) {
			if (lookupTable[0][i].equals(selectedItem))
				rowi = i;
		}
		for (int i = 0; i < lookupTable.length; i++) {
			if (lookupTable[i][0].equals(selectedItem2))
				coli = i;
		}
		if (rowi >= 0 && coli >= 0 && PossibileConversions[rowi][coli]) {
			theFactor = Double.parseDouble(lookupTable[rowi][coli]);
			return true;
		}
		return false;
	}
}