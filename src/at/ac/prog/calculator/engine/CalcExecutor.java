package at.ac.prog.calculator.engine;

import java.util.regex.Pattern;

public class CalcExecutor {
	private CalcStack stack = null;
	private CalcStack operators = null;
	private String printBuffer = "";

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
					case '\'': singleQuote(); break;
					case '"': doubleQuote(); break;
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
				Pattern pattern = Pattern.compile("\\+|-|\\*|/|%|&|=|<|>|~|!|#|@|\"|'");
				String expression = (String) token;
				if ((pattern.matcher(String.valueOf(expression.charAt(0)))).matches() == true) {
					operators.push(stack.pop());
				} else if(expression.charAt(0) == '[' && expression.charAt(expression.length()) == ']') {
					expression.substring(1, expression.length()-1);
						//handle expression
				} else {
					throw new IllegalArgumentException("Encountered an invalid operator or expression: " + expression);
				}
			} else {
				if(operators.size() == 0) return;
				token = operators.pop();
				switch(((String) token).charAt(0)) {
					case '+': add(); break;
					case '-': sub(); break;
					case '*': mult(); break;
					case '/': div(); break;
					case '%': mod(); break;
					case '>': greater(); break;
					case '<': less(); break;
					case '\'': singleQuote(); break;
					case '"': doubleQuote(); break;
					default: {
						throw new IllegalArgumentException("Encountered an invalid operator: " + token);
					}
				}
			}
		}
		if(!printBuffer.equals("")) {
			System.out.println(printBuffer);
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

	private void singleQuote() {
		throw new RuntimeException("Not implemented!");
	}

	private void doubleQuote() {
		Object token = stack.pop();
		if(token instanceof Integer) {
			Integer data = (Integer) token;
			if(data > 0x1f && data < 0x7f) {
				/* new characters to print must actually be prepended to the buffer
				 * because we are starting from the top of the stack which contains
				 * the end of the string to be printed.
				 */
				printBuffer = ((char) data.intValue()) + printBuffer;
			} else {
				throw new IllegalArgumentException("You tried to print a non printable value.");
			}
		} else {
			System.out.println((String) token);
		}
	}

}
