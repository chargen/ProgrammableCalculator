package at.ac.prog.calculator.engine;

import java.util.ArrayList;
import java.util.List;

import at.ac.prog.calculator.engine.exception.CalcParsingException;
import at.ac.prog.calculator.engine.util.CalcExecutorListener;

public class CalcExecutor {

	/*
	 * This is the current list of input tokens that is going to be executed.
	 * The execution starts at the lower (left) end of the list. If a question
	 * mark is reached, execution is interrupted and new input is parsed.
	 *
	 * New input from the question mark operator will be prepended to the
	 * input list, and thus will be executed first.
	 */
	private List<Object> inputList;

	/**
	 * This stack contains the current DATA that has been parsed from the input list
	 * and now needs to be used by the operators. Operators always push their result
	 * on the tokenStack and never put it into the input list.
	 */
	private CalcStack stack;

	private CalcParser parser;
	private Object token;
	private boolean bDebug;
	private List<CalcExecutorListener> listeners;

	/*
	 * This variable is set to true if the ?-operator (question mark) is reached.
	 * The current execution loop should then be interruped and further input should be
	 * read!
	 */
	boolean questionMarkOperator;

	public CalcExecutor() {
		this.bDebug = false;
		this.stack = new CalcStack();
		this.prepare();
	}

	/**
	 * initialize all variables and set the stack.
	 */
	private void prepare() {
		this.parser = new CalcParser();
		this.inputList = this.parser.getList();
		this.bDebug = false;
	}

	/**
	 * Reinitialise all variables and set the stack.
	 */
	public void prepare(CalcParser parser, List<Object> list) {
		this.parser = parser;
		this.inputList = list;
		this.bDebug = false;
	}

	public void execute() throws CalcParsingException {
		if (bDebug == true) {
			if (this.inputList.size() > 0 && (token = this.inputList.remove(0)) != null) {
				this.processStep();
			}
			if (this.inputList.size() == 0) {
				this.notifyNewInputPossible();
			}
		}
		else {
			while(this.inputList.size() > 0 && !questionMarkOperator && (token = this.inputList.remove(0)) != null) {
				this.processStep();
				this.notifyInputListChange();
			}
			this.notifyNewInputPossible();
		}
		this.notifyInputListChange();
		this.notifyStackChange();
		questionMarkOperator = false; //reset the question mark operator
	}

	private void processStep() throws CalcParsingException {
		if(token instanceof String && ((String) token).length() == 1) {
			String expression = (String) token;
			if (parser.isOperator(expression)) {
				if(expression.equals("?")) {
					notifyNewInputPossible(true);
					questionMarkOperator = true;
					return;
				} else {
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
						case '#': delete(); break;
						case '!': copy(); break;
						case '~': unaryminus(); break;
						case '=': equal(); break;
						default: {
							throw new IllegalArgumentException("Encountered an unimplemented operator: " + token);
						}
					}
				}
			} else {
				throw new IllegalArgumentException("Encountered an invalid operator or expression: " + expression);
			}
		} else {
			stack.push(token);
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
		stack.push((int) Math.floor(result));
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
		Object value1 = stack.pop();
		Object value2 = stack.pop();
		if(value1 instanceof Integer) {
			double result = new Double((Integer) value1);
			if(result == 0) throw new IllegalArgumentException("Error in '/' operator: Division by Zero");
			if(value2 instanceof Integer) {
				result = new Double((Integer) value2) / result;
			} else {
				throw new IllegalArgumentException("Expected first argument of '/' operator to be of type integer.");
			}
			stack.push((int) Math.floor(result));
		} else {
			throw new IllegalArgumentException("Expected second argument of '/' operator to be of type integer.");
		}
	}

	private void mod() {
		Object value1 = stack.pop();
		Object value2 = stack.pop();
		if(value1 instanceof Integer) {
			Integer result = (Integer) value2;
			if(value2 instanceof Integer) {
				if(result == 0) throw new IllegalArgumentException("Error in '%' operator: Modulo Zero not allowed");
				result = ((Integer) value1) % result;
			} else {
				throw new IllegalArgumentException("Expected first argument of '%' operator to be of type integer.");
			}
			stack.push(result);
		} else {
			throw new IllegalArgumentException("Expected second argument of '%' operator to be of type integer.");
		}
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
			//System.out.print(Integer.toString(data.intValue(), 10));
			this.notifyOutputChange(Integer.toString(data.intValue(), 10));
		} else {
			String expression = (String) token;
			//System.out.print(expression);
			this.notifyOutputChange(expression);
		}
	}

	private void doubleQuote() {
		Object token = stack.pop();
		if(token instanceof Integer) {
			Integer data = (Integer) token;
			if(data > 0x1f && data < 0x7f || data == 0x0A || data == 0x09) {
				//System.out.print((char) data.intValue());
				this.notifyOutputChange(String.valueOf((char) data.intValue()));
			} else {
				throw new IllegalArgumentException("You tried to print a non printable value.");
			}
		} else {
			String expression = (String) token;
			//System.out.print( expression.substring(1, expression.length() - 1));
			this.notifyOutputChange(expression.substring(1, expression.length() - 1));
		}
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

	private void delete() {
		Object value = stack.pop();
		if(value instanceof Integer) {
			Integer n = (Integer) value;
			if(n >= 0) {
				int pos = stack.size() - n.intValue() + 1;
				if(pos < 0) {
					throw new IllegalArgumentException("Operator # has not found enough items on the stack " +
														"to delete the " + n + "-th item");
				}
				stack.remove(pos);
			} else {
				throw new IllegalArgumentException("Operator # requires a positive number");
			}
		} else {
			throw new IllegalArgumentException("Expected an integer for operator # (delete-Operator).");
		}
	}

	private void copy() {
		Object value = stack.pop();
		if(value instanceof Integer) {
			Integer n = (Integer) value;
			if(n >= 0) {
				int pos = stack.size() - n.intValue() + 1;
				if(pos < 0) {
					throw new IllegalArgumentException("Operator ! has not found enough items on the stack " +
														"to copy the " + n + "-th item");
				}
				stack.push(stack.get(pos));
			} else {
				throw new IllegalArgumentException("Operator ! requires a positive number");
			}
		} else {
			throw new IllegalArgumentException("Expected an integer for operator ! (copy).");
		}
	}
	private void unaryminus() {
		Object value = stack.pop();
		if(value instanceof Integer) {
			Integer number = (Integer) value;
			stack.push(number * -1);
		} else {
			throw new IllegalArgumentException("Operator ~ (unary minus) excepted a number.");
		}
	}

	private void equal() {
		Object value1 = stack.pop();
		Object value2 = stack.pop();
		if(value1 instanceof Integer && value2 instanceof Integer) {
			Integer a = (Integer) value1;
			Integer b = (Integer) value2;
			stack.push(a - b == 0 ? 0 : 1); //0=true, 1=false
		} else if(value1 instanceof String && value2 instanceof String) {
			String a = (String) value1;
			String b = (String) value2;
			if(a.equals(b)) {
				stack.push(0);
			} else {
				stack.push(1);
			}
		}

	}

	/**
	 * Print the current stack, the current operation and the current input list.
	 * The operation is executed on the top most stack items. The next lowest item
	 * in the input list will be put on the stack (or be executed) next.
	 */
	public void printStackTrace() {
		System.out.println("---------------------------------- EXECUTOR STACK TRACE -------------------------------------");
		int i;
		for(i = this.stack.size() - 1; i >= 0; i--) {
			System.out.println("Stack [" + i + "]: " + this.stack.get(i));
		}
		System.out.println("Current Operation: " + token);
		System.out.println("Tokens remaining in input list:");
		for(i= this.inputList.size() - 1; i >= 0; i--) {
			System.out.println("Input List [" + i + "]: " + this.inputList.get(i));
		}
	}

	public void clearStack() {
		this.stack.clear();
	}

	public boolean isbDebug() {
		return bDebug;
	}

	public void setbDebug(boolean bDebug) {
		this.bDebug = bDebug;
	}

	public void toggleDebug() {
		this.bDebug = !this.bDebug ;
	}

	public void registerListener(CalcExecutorListener listener) {
		if(this.listeners == null) {
			this.listeners = new ArrayList<CalcExecutorListener>();
		}
		if(this.listeners.contains(listener) == false) {
			this.listeners.add(listener);
		}
	}

	public void parse(String input) throws CalcParsingException {
		this.parser.parse(input);
	}

	private void notifyStackChange() {
		if (this.listeners != null) {
			for(CalcExecutorListener listener : listeners) {
				listener.notifyStackChange(this.stack.stackAsList());
			}
		}
	}

	private void notifyInputListChange() {
		if (this.listeners != null) {
			for(CalcExecutorListener listener : listeners) {
				listener.notifyInputListChange(this.inputList);
			}
		}
	}

	private void notifyOutputChange(String output) {
		if (this.listeners != null) {
			for(CalcExecutorListener listener : listeners) {
				listener.notifyOutput(output);
			}
		}
	}

	private void notifyNewInputPossible() {
		if (this.listeners != null) {
			for(CalcExecutorListener listener : listeners) {
				listener.notifyNewInput();
			}
		}
	}

	private void notifyNewInputPossible(boolean questionmark) {
		if (this.listeners != null) {
			for(CalcExecutorListener listener : listeners) {
				listener.notifyNewInput(questionmark);
			}
		}
	}
}
