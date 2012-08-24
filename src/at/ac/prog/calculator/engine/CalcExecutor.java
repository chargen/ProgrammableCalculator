package at.ac.prog.calculator.engine;

public class CalcExecutor {
	private CalcStack stack = null;

	public void execute(CalcStack stack) {
		this.stack = stack;
		Object token;
		while(stack.size() > 0 && (token = stack.pop()) != null) {
			if(token instanceof String) {
				switch(((String) token).charAt(0)) {
					case '+': add(); break;
					case '-': sub(); break;
					case '*': mult(); break;
					case '/': did(); break;
					case '%': mod(); break;
					case '>': greater(); break;
					case '<': less(); break;
					default: {
						stack.printResult();
					}
				}
			} else {
				stack.printResult();
			}
		}
	}

	private void add() throws IllegalArgumentException {
		if(stack.peek() instanceof Integer) {
			int result = (Integer) stack.pop();
			if(stack.peek() instanceof Integer) {
				result += (Integer) stack.pop();
			} else {
				throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
			}
			stack.push(result);
		} else {
			execute(stack);
		}
	}

	private void sub() {
		// TODO Auto-generated method stub
	}

	private void mult() {
		if(stack.peek() instanceof Integer) {
			int result = (Integer) stack.pop();
			if(stack.peek() instanceof Integer) {
				result *= (Integer) stack.pop();
			} else {
				throw new IllegalArgumentException("Expected second argument of '*' operator to be of type integer.");
			}
			stack.push(result);
		} else {
			execute(stack);
		}
	}

	private void did() {
		// TODO Auto-generated method stub
	}

	private void mod() {
		// TODO Auto-generated method stub
	}

	private void greater() {
		// TODO Auto-generated method stub
	}

	private void less() {

	}

}
