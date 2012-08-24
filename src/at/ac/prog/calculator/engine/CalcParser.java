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
	}

	public void parse(String command) throws CalcParsingException {
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
						throw new CalcParsingException("Invalid escape character: " + command.charAt(i));
					}
				}
				readSpecialCharacter = false;
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

								pattern = Pattern.compile("\\+|-|\\*|/|%|&|=|<|>|~|!|#|@|\"|'");
								if ((pattern.matcher(String.valueOf(command.charAt(i)))).matches() == true) {
									parsedElems.add(String.valueOf(command.charAt(i)));
								} else if(command.charAt(i) == '?') {
									parsedElems.add(String.valueOf(command.charAt(i)));
								} else if(command.charAt(i) == '\\') {
									readSpecialCharacter = true;
								} else {
									parsedElems.add(String.valueOf(command.charAt(i)));
								}
								break;
						}
					}
				}
			}
		}
		if(numOpenBrackets > 0) {
			throw new CalcParsingException("closingBracket not found");
		}
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
	public boolean createStack() {
		String first;
		while(parsedElems.size() > 0 && (first = parsedElems.remove(0)) != null) {
			if(!first.equals("?")) {
				Integer integer = null;
				try {
					integer = Integer.parseInt(first);
				} catch(NumberFormatException e) { }
				if(integer != null) {
					stack.push(integer);
				} else {
					stack.push(first);
				}
			} else {
				return true;
			}
		}
		return false;
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

	public void parse(String string, boolean b) throws CalcParsingException {
		ArrayList<String> tempList = parsedElems;
		parsedElems = new ArrayList<String>();
		parse(string);
		parsedElems.addAll(tempList);
	}

	public CalcStack getStack() {
		return stack;
	}

}