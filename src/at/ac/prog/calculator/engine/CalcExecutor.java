package at.ac.prog.calculator.engine;

import at.ac.prog.calculator.engine.exception.CalcParsingException;

public class CalcExecutor {
	private CalcStack stack = null;
	private CalcStack operators = null;
	private CalcParser parser;
	private String printBuffer;

	/**
	 * Reinitialize all variables and set the stack.
	 */
	public void prepare(CalcStack stack, CalcParser parser) {
		this.stack = stack;
		this.parser = parser;
		this.operators = new CalcStack();
		this.printBuffer = "";
	}

	public void execute() throws CalcParsingException {
		Object token;
		boolean question_mark_operator = false;
		while(this.stack.size() > 0 && (token = this.stack.peek()) != null) {
			if(token instanceof String && ((String) token).length() == 1) {
				String expression = (String) token;
				if (parser.isOperator(expression)) {
					if(expression.equals("?")) {
						question_mark_operator = true;
					}
					operators.push(this.stack.pop());
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
					case '@': at(); break;
					case '?': {
						flushPrintBuffer();
						return;
					}
					default: {
						throw new IllegalArgumentException("Encountered an invalid operator: " + token);
					}
				}
			}
		}
		if(question_mark_operator) {
			String operator = (String) operators.pop();
			if(!operator.equals("?")) {
				throw new IllegalArgumentException("Expected a question mark operator");
			}
			flushPrintBuffer();
			return;
		}
		flushPrintBuffer();
		this.stack.printResult();
	}

	private void at() throws CalcParsingException {
		Object value = stack.pop();
		if(value instanceof Integer) {
			stack.push(value);
		} else {
			String expression = (String) value;
			expression = expression.substring(1, expression.length() - 1);
			parser.parse(expression);
		}
	}

	private void flushPrintBuffer() {
		if(!printBuffer.equals("")) {
			System.out.println(printBuffer);
			printBuffer = "";
		}
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
			if(data > 0x1f && data < 0x7f || data == 0x0A || data == 0x09) {
				/* new characters to print must actually be prepended to the buffer
				 * because we are starting from the top of the stack which contains
				 * the end of the string to be printed.
				 */
				printBuffer = ((char) data.intValue()) + printBuffer;
			} else {
				throw new IllegalArgumentException("You tried to print a non printable value.");
			}
		} else {
			String expression = (String) token;
			printBuffer = expression.substring(1, expression.length() - 1) + printBuffer;
		}
	}

	public void debugOutput() {
		System.out.println("---------------------------------- EXECUTOR -------------------------------------");
		int i;
		for(i = 0; i < this.operators.size(); i++) {
			System.out.println("Operator " + i + ": " + this.operators.get(i));
		}
	}
}
