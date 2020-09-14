package calculator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*******
 * <p>
 * Title: Calculator Class.
 * </p>
 * 
 * <p>
 * Description: A JavaFX demonstration application and baseline for a sequence
 * of projects
 * </p>
 * 
 * <p>
 * Copyright and Baseline Author: Lynn Robert Carter © 2016
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

public class Calculator extends Application {

	public final static double WINDOW_WIDTH = 1200;
	public final static double WINDOW_HEIGHT = 450;

	public UserInterface theGUI;

	/**********
	 * This is the start method that is called once the application has been loaded
	 * into memory and is ready to get to work.
	 * 
	 * 
	 */
	@Override
	public void start(Stage theStage) throws Exception {

		theStage.setTitle("Keshav Sharma"); // Label the stage (a window)

		Pane theRoot = new Pane(); // Create a pane within the window

		theGUI = new UserInterface(theRoot); // Create the Graphical User Interface

		Scene theScene = new Scene(theRoot, WINDOW_WIDTH, WINDOW_HEIGHT); // Create the scene

		theStage.setScene(theScene); // Set the scene on the stage

		theStage.show(); // Show the stage to the user

		// When the stage is shown to the user, the pane within the window is visible.
		// This means that the
		// labels, fields, and buttons of the Graphical User Interface (GUI) are
		// visible//
	}

	/*******************************************************************************************************
	 * This is the method that launches the JavaFX application
	 * 
	 */
	public static void main(String[] args) { // This method may not be required
		launch(args); // for all JavaFX applications using
	} // other IDEs.
}
