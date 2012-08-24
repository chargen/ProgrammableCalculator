package at.ac.prog.calculator.engine;

import java.util.Stack;

public class CalcStack extends Stack<Object> {

	private static final long serialVersionUID = -2121393778527282706L;

	public void printResult() {
		if(this.size() > 0) {
			System.out.println("Calculation Result: " + this.peek());
		}
	}

}
