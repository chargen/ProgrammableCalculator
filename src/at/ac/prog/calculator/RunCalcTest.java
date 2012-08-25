package at.ac.prog.calculator;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.engine.CalcParser;
import at.ac.prog.calculator.engine.CalcStack;


public class RunCalcTest {

	public static void main(String[] args) {

		CalcExecutor executor = new CalcExecutor();
		CalcParser parser = new CalcParser();

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

			parser.parse(" [\n\"E\"i\"n\"g\"a\"b\"e\":\"\\ \"?@']");
			parser.debugOutput();
			parser.clear();

			parser.parse(" \n\"E\"i\"n\"g\"a\"b\"e\":\"\\ \"?@'");
			parser.debugOutput();
			parser.clear();

			parser.parse(" [[\nEingabe:\\ ]\"?@']");
			parser.debugOutput();
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
			CalcStack stack = parser.getStack();
			executor.prepare(stack);
			executor.execute(stack);
			parser.parse("3 +");
			parser.debugOutput();
			executor.execute(stack);
			parser.clear();

			parser.parse("A\"B\"C\"D\"E\"F\"G\":\"");
			parser.debugOutput();
			stack = parser.getStack();
			executor.prepare(stack);
			executor.execute(stack);
			parser.clear();

			parser.parse("100'\\ \"\\+\"\\ \"100'\\ \"\\=\"\\ \"200'");
			parser.debugOutput();
			stack = parser.getStack();
			executor.prepare(stack);
			executor.execute(stack);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
