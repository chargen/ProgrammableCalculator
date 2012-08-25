package at.ac.prog.calculator.engine;

import java.util.regex.Pattern;

public class CalcExecutor {
	private CalcStack stack = null;
	private CalcStack operators = null;
	private String printBuffer;

	public void execute(CalcStack stack) {
		this.stack = stack;
		this.operators = new CalcStack();
		this.printBuffer = "";
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
				if(operators.size() == 0) break;
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
		stack.printResult();
	}

	private void add() throws IllegalArgumentException {
		int result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result += (Integer) stack.pop();
		} else {
			throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
		}
		stack.push(result);
	}

	private void sub() {
		Integer result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result = (Integer) stack.pop() - result;
		} else {
			throw new IllegalArgumentException("Expected second argument of '+' operator to be of type integer.");
		}
		stack.push(Math.floor(result));
	}

	private void mult() {
		int result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result *= (Integer) stack.pop();
		} else {
			throw new IllegalArgumentException("Expected second argument of '*' operator to be of type integer.");
		}
		stack.push(result);
	}

	private void div() {
		double result = new Double((Integer) stack.pop());
		if(stack.peek() instanceof Integer) {
			result = new Double((Integer) stack.pop()) / result;
		} else {
			throw new IllegalArgumentException("Expected second argument of '/' operator to be of type integer.");
		}
		stack.push(Math.floor(result));
	}

	private void mod() {
		Integer result = (Integer) stack.pop();
		if(stack.peek() instanceof Integer) {
			result = ((Integer) stack.pop()) % result;
		} else {
			throw new IllegalArgumentException("Expected second argument of '%' operator to be of type integer.");
		}
		stack.push(result);
	}

	private void greater() {
		Integer a= (Integer) stack.pop();
		boolean result;
		if(stack.peek() instanceof Integer) {
			result = a > ((Integer) stack.pop());
		} else {
			throw new IllegalArgumentException("Expected second argument of '>' operator to be of type integer.");
		}
		stack.push(result ? 0 : 1);
	}

	private void less() {
		Integer a= (Integer) stack.pop();
		boolean result;
		if(stack.peek() instanceof Integer) {
			result = a < ((Integer) stack.pop());
		} else {
			throw new IllegalArgumentException("Expected second argument of '<' operator to be of type integer.");
		}
		stack.push(result ? 0 : 1);
	}

	private void singleQuote() {
		Object token = stack.pop();
		if(token instanceof Integer) {
			Integer data = (Integer) token;
			printBuffer = Integer.toString(data.intValue(), 10) + printBuffer;
		} else {
			String expression = (String) token;
			printBuffer = expression + printBuffer;
		}
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
