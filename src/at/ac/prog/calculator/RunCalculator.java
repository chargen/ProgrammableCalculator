package at.ac.prog.calculator;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.ui.CalculatorUI;

public class RunCalculator {

	public static void main(String[] args) {
		CalculatorUI calcUI = new CalculatorUI();

		CalcExecutor executor = new CalcExecutor();
		executor.registerListener(calcUI);

		calcUI.init(executor);


	}

}
