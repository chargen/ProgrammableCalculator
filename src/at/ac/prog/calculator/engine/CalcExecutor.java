package at.ac.prog.calculator.engine;

public class CalcExecutor {
	private CalcStack stack = null;
	private CalcStack operators = null;

	public void execute(CalcStack stack) {
		this.stack = stack;
		Object token;
		if(stack.size() > 0 && (token = stack.pop()) != null) {
			if(token instanceof String) {
				switch(((String) token).charAt(0)) {
					case '+': add(); break;
					case '-': sub(); break;
					case '*': mult(); break;
					case '/': div(); break;
					case '%': mod(); break;
					case '>': greater(); break;
					case '<': less(); break;
					default: {
						throw new IllegalArgumentException("Encountered an invalid operator: " + token);
					}
				}
			}
		}
	}

	public void execute(CalcStack stack, boolean iterative) {
		this.stack = stack;
		this.operators = new CalcStack();
		Object token;
		while(stack.size() > 0 && (token = stack.peek()) != null) {
			if(token instanceof String) {
				switch(((String) token).charAt(0)) {
					case '+':
					case '-':
					case '*':
					case '/':
					case '%':
					case '>':
					case '<': {
						operators.push(stack.pop());
						break;
					}
					default: {
						throw new IllegalArgumentException("Encountered an invalid operator: " + token);
					}
				}
			} else {
				if(operators.size() == 0) return;
				token = operators.pop();
				switch(((String) token).charAt(0)) {
					case '+': add(); break;
					case '-': sub(); break;
					case '*': mult(); break;
					case '/': div(); break;
					default: {
						throw new IllegalArgumentException("Encountered an invalid operator: " + token);
					}
				}
			}
		}
	}

	private void add() throws IllegalArgumentException {
		if(!(stack.peek() instanceof Integer)) {
			System.out.println("Recursing: +");
			execute(stack);
		}
		int result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result += (Integer) stack.pop();
		} else {
			throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
		}
		stack.push(result);
	}

	private void sub() {
		if(!(stack.peek() instanceof Integer)) {
			System.out.println("Recursing: -");
			execute(stack);
		}
		Integer result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result = (Integer) stack.pop() - result;
		} else {
			throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
		}
		stack.push(Math.floor(result));
	}

	private void mult() {
		if(!(stack.peek() instanceof Integer)) {
			System.out.println("Recursing: *");
			execute(stack);
		}
		int result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result *= (Integer) stack.pop();
		} else {
			throw new IllegalArgumentException("Expected second argument of '*' operator to be of type integer.");
		}
		stack.push(result);
	}

	private void div() {
		if(!(stack.peek() instanceof Integer)) {
			System.out.println("Recursing: /");
			execute(stack);
		}
		double result = new Double((Integer) stack.pop());
		if(stack.peek() instanceof Integer) {
			result = new Double((Integer) stack.pop()) / result;
		} else {
			throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
		}
		stack.push(Math.floor(result));
	}

	private void mod() {
		throw new RuntimeException("Not implemented!");
	}

	private void greater() {
		throw new RuntimeException("Not implemented!");
	}

	private void less() {
		throw new RuntimeException("Not implemented!");
	}

}
