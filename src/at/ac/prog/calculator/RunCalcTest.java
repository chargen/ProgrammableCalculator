package at.ac.prog.calculator;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.engine.CalcParser;


public class RunCalcTest {

	public static void main(String[] args) {

		CalcExecutor executor = new CalcExecutor();
		CalcParser parser = new CalcParser();
		executor.prepare(parser, parser.getList());

		try {
			parser.parse("0 [9][9~][4!5#2+#@]@\\n\"");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			parser.parse(" \\n\"E\"i\"n\"g\"a\"b\"e\":\"\\ \"?@'\\n\"");
			parser.debugOutput();
			executor.execute();
			parser.parse("[0]");
			parser.debugOutput();
			executor.printStackTrace();
			executor.execute();
			parser.debugOutput();
			executor.printStackTrace();
			parser.clear();

			parser.parse("3 [3!3!1-2!1=[]5![4!5#2+#@]@3#*]3!4#3!@3#\\n\"");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			//Print 'Enter Operator: 4 ? 5 =', when the users enters '+' it should output 9
			parser.parse("E\"n\"t\"e\"r\"\\ \"O\"p\"e\"r\"a\"t\"o\"r\":\"\\ \"4'\\?\"5'\\ \"\\=\"\\ \"4 5 ?");
			parser.debugOutput();
			executor.execute();
			parser.parse("*");
			executor.execute();
			parser.clear();

			/**
			 * Parsing both expression would be equal to the following
			 * calculation: 45 / (3 * (2+3)) = 3
			 */
			parser.parse("45 3 2 ? * / \\n\"");
			parser.debugOutput();
			executor.execute();
			parser.parse("3 +");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			parser.parse("A\"B\"C\"D\"E\"F\"G\":\"\\n\"");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			parser.parse("100~'\\ \"\\+\"\\ \"100~'\\ \"\\=\"\\ \"200~'\\n\"");
			parser.debugOutput();
			executor.execute();

			//Test single quote used on bracket expression
			parser.parse("[12 14 +]'\\n\"");
			parser.debugOutput();
			executor.execute();

			//Test double quote used on bracket expression
			parser.parse("[12 14 +]\"\\n\"");
			parser.debugOutput();
			executor.execute();

			parser.parse("3[2*]@\\n\"");
			parser.debugOutput();
			executor.execute();

			//Test for the delete operator, 5# should delete the 10 from the stack and the result should be 15
			parser.parse("1 10 2 3 4 5 6# + + + +\\n\"");
			parser.debugOutput();
			executor.execute();

			//Test for the copy operator, copy the digit 2 four times to the top of the stack and print it each time, afterwards
			//calculate the sum of the remaining items.
			parser.parse("1 2 3 4 5 5! 6! 7! 8! ''''++++\\n\"");
			parser.debugOutput();
			executor.execute();
			
			parser.clear();
			parser.parse("15 14 3 4 < 2+ # @");
			parser.debugOutput();
			executor.execute();
			
			parser.clear();
			parser.parse("[1+3!@] 1 3! @");
			parser.debugOutput();
			//executor.execute();
			
			parser.clear();
			parser.parse("[[2!\\n\"' 1+ 2! 5! > 3+ ! @] 1 2! 5! > 3+ ! @] 9 3! 4# @");
			executor.execute();
			//executor.printStackTrace();
			
			parser.clear();
			//parser.parse("[[2!\\n\"' 1+ 5! 2! % 0 = 3+ ! @] 1 2! 5! > 3+ ! @] 21 3! 4# @");
			//executor.execute();
			
			executor.clearStack();
			parser.clear();
			parser.parse("[[2!\\n\"' 1+ 2! 6! % 0 = 3+ ! @][\\n\"[N\"o\"t\"\\ \"P\"r\"i\"m\"e\"][P\"r\"i\"m\"e\"] 4! 8! = 2+ ! @ 2# 2# 2# 2# 2# 2#] 1 2! 6! > 4+ ! @] 5 3! 4# @");
			executor.execute();
			executor.printStackTrace();
			
			System.out.println("------------ CALCULATING PRIME NUMBERS --------------");
			
			parser.parse("[[[Testing: ]\" 2!'\\n\" 1+ 2! 6! % 0 = 3+ ! @][\\n\"[N\"o\"t\"\\ \"P\"r\"i\"m\"e\"\\n\"][P\"r\"i\"m\"e\"\\n\"] 7!'\\ \" 4! 8! = 2+ ! @ 2# 2# 2# 2# 2# 2#] 1 2! 6! > 4+ ! @] 5 3! @");
			executor.execute();
			System.out.println();
			
			parser.parse("15 3! @");
			executor.execute();
			System.out.println();
			
			parser.parse("26 3! @");
			executor.execute();
			System.out.println();
			
			parser.parse("1583 3! @");
			executor.execute();
			System.out.println();
			
			

		} catch(Exception ex) {
			executor.printStackTrace();
			ex.printStackTrace();

		}
	}
}
