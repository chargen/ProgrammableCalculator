package at.ac.prog.calculator.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalcStack extends Stack<Object> {

	private static final long serialVersionUID = -2121393778527282706L;

	public void printResult() {
		if(this.size() > 0) {
			System.out.println("Calculation Result: " + this.pop());
		}
	}
	
	public List<String> stackAsList() {
		List<String> stack = new ArrayList<String>();
		for(int i = 0; i < this.size(); i++) {
			stack.add(this.get(i).toString());
		}
		return stack;
	}

}
