package at.ac.prog.calculator.engine;

import java.util.ArrayList;
import java.util.regex.Pattern;

import at.ac.prog.calculator.engine.exception.CalcParsingException;

public class CalcParser {

	private ArrayList<String> parsedElems;
	private final CalcStack stack = new CalcStack();

	public CalcParser() {
		parsedElems = new ArrayList<String>();
	}

	/**
	 * For debug purposes.
	 */
	public void clear() {
		this.parsedElems.clear();
		this.stack.clear();
	}

	public void parse(String command) throws CalcParsingException {
		ArrayList<String> tempList = null;
		if(parsedElems.size() != 0) {
			tempList = parsedElems;
			parsedElems = new ArrayList<String>();
		}
		command = command.replaceAll("\\s+", " ");

		String newElem = null;
		int numOpenBrackets = 0;
		boolean readSpecialCharacter = false;
		for (int i = 0; i < command.length(); i++) {
			// DEBUG: System.out.println("TESTING: " + command.charAt(i));
			if(readSpecialCharacter == true) {
				switch(command.charAt(i)) {
					case 'n': {//line feed
						parsedElems.add("\n");
						break;
					}
					case 't': { //tabulator
						parsedElems.add("\t");
						break;
					}
					case 'r': { //carriage return
						parsedElems.add("\r");
						break;
					}
					case ' ': {//space
						parsedElems.add(" ");
						break;
					}
					case '\\': { //backslash
						parsedElems.add("\\");
						break;
					} default: {
						if(isOperator("" + command.charAt(i))) {
							parsedElems.add("\\" + (command.charAt(i)));
						} else {
							throw new CalcParsingException("Invalid escape character: " + command.charAt(i));
						}
					}
				}
				readSpecialCharacter = false;
				continue;
			}
			switch(command.charAt(i)) {
				case '[': {
					if(numOpenBrackets != 0) {
						newElem += command.charAt(i);
						numOpenBrackets++;
					} else {
						newElem = String.valueOf(command.charAt(i));
						numOpenBrackets = 1;
					}
					break;
				} case ']': {
					if(numOpenBrackets == 1) {
						newElem += command.charAt(i);
						numOpenBrackets = 0;
						parsedElems.add(newElem);
						newElem = null;
					} else {
						newElem += command.charAt(i);
						numOpenBrackets--;
					}
					break;
				} default: {
					if(numOpenBrackets != 0) {
						newElem += command.charAt(i);
					}
					else {
						switch(command.charAt(i)) {
							case ' ':
								if(newElem != null) {
									parsedElems.add(newElem);
									newElem = null;
								}
								break;
							default:
								Pattern pattern = Pattern.compile("\\d");
								if ((pattern.matcher(String.valueOf(command.charAt(i)))).matches() == true) {
									if((newElem != null) && (isNumeric(newElem) == true)) {
										newElem += command.charAt(i);
									} else {
										newElem = String.valueOf(command.charAt(i));
									}
									break;
								} else {
									if(isNumeric(newElem) == true) {
										parsedElems.add(newElem);
										newElem = null;
									}
								}

								pattern = Pattern.compile("\\+|-|\\*|/|%|&|=|<|>|~|!|#|@|\"|'|\\|");
								if ((pattern.matcher(String.valueOf(command.charAt(i)))).matches() == true) {
									parsedElems.add(String.valueOf(command.charAt(i)));
								} else if(command.charAt(i) == '?') {
									parsedElems.add(String.valueOf(command.charAt(i)));
								} else if(command.charAt(i) == '\\') {
									readSpecialCharacter = true;
								} else {
									parsedElems.add(String.valueOf(command.charAt(i))); //Printable ASCII Characters
								}
								break;
						}
					}
				}
			}
		}
		if(newElem != null) {
			parsedElems.add(newElem);
		}
		if(numOpenBrackets > 0) {
			throw new CalcParsingException("closingBracket not found");
		}
		if(tempList != null && tempList.size() > 0) {
			parsedElems.addAll(tempList);
		}
		createStack();
	}

	public static boolean isNumeric(String str)  {
		try {
			Integer.parseInt(str);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	/**
	 * @return True if a question mark was reached, false otherwise.
	 */
	public void createStack() {
		String first;
		while(parsedElems.size() > 0 && (first = parsedElems.remove(0)) != null) {
			Integer integer = null;
			try {
				integer = Integer.parseInt(first);
			} catch(NumberFormatException e) { }
			if(integer != null) {
				stack.push(integer);
			} else {
				if(first.length() == 2 && first.charAt(0) == '\\') {
					char c = first.charAt(1);
					stack.push(new Integer(c));
				} else if(first.length() == 1 && !isOperator(first)) {
					char c = first.charAt(0);
					stack.push(new Integer(c));
				} else {
					stack.push(first); //we got an operator or an expression
				}
			}
		}
		assert(parsedElems.size() == 0);
	}

	private boolean isOperator(String token) {
		Pattern pattern = Pattern.compile("\\+|-|\\*|/|%|&|=|<|>|~|!|#|@|\"|'|\\||\\?");
		return pattern.matcher(token).matches();
	}

	public void debugOutput() {
		System.out.println("---------------------------------- DEBUG -------------------------------------");
		int i;
		for(i = 0; i < this.stack.size(); i++) {
			System.out.println("Element Stack" + i + ": " + this.stack.get(i));
		}
		for(int j = 0; j < this.parsedElems.size(); j++) {
			System.out.println("Element List" + (j+i) + ": " + this.parsedElems.get(j));
		}
	}

	public CalcStack getStack() {
		return stack;
	}

}
