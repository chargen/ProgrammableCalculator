package at.ac.prog.calculator;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.engine.CalcParser;
import at.ac.prog.calculator.engine.CalcStack;


public class RunCalcTest {

	public static void main(String[] args) {

		CalcExecutor executor = new CalcExecutor();
		CalcParser parser = new CalcParser();
		CalcStack stack = parser.getStack();
		executor.prepare(stack, parser);

		try {
			parser.parse("0 [9][9~][4!5#2+#@]@");
			parser.debugOutput();
			parser.clear();

			parser.parse("0 4!5#2+#34@");
			parser.debugOutput();
			parser.clear();

			parser.parse("0 [9][9~[4!5#2+#@]][]@");
			parser.debugOutput();
			parser.clear();

			parser.parse(" [\\n\"E\"i\"n\"g\"a\"b\"e\":\"\\ \"?@']");
			parser.debugOutput();
			parser.clear();

			parser.parse(" \\n\"E\"i\"n\"g\"a\"b\"e\":\"\\ \"?@'");
			parser.debugOutput();
			executor.execute();
			parser.parse("[0]");
			parser.debugOutput();
			executor.debugOutput();
			executor.execute();
			parser.debugOutput();
			executor.debugOutput();
			parser.clear();

			parser.parse("3 [3!3!1-2!1=[]5![4!5#2+#@]@3#*]3!4#3!@3#");
			parser.debugOutput();
			parser.clear();

			/**
			 * Parsing both expression would be equal to the following
			 * calculation: 45 / (3 * (2+3)) = 3
			 */
			parser.parse("45 3 2 ? * / ");
			parser.debugOutput();
			executor.execute();
			parser.parse("3 +");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			parser.parse("A\"B\"C\"D\"E\"F\"G\":\"");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			parser.parse("100'\\ \"\\+\"\\ \"100'\\ \"\\=\"\\ \"200'");
			parser.debugOutput();
			executor.execute();
			parser.clear();

			//Test single quote used on bracket expression
			parser.parse("[12 14 +]'");
			parser.debugOutput();
			executor.execute();

			//Test double quote used on bracket expression
			parser.parse("[12 14 +]\"");
			parser.debugOutput();
			executor.execute();

			parser.parse("3[2*]@");
			parser.debugOutput();
			executor.execute();

			//Test for the delete operator, 5# should delete the 10 from the stack and the result should be 15
			parser.parse("1 10 2 3 4 5 5# + + + +");
			parser.debugOutput();
			executor.execute();

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
