package at.ac.prog.calculator;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.engine.CalcParser;
import at.ac.prog.calculator.engine.CalcStack;


public class RunCalcTest {

	public static void main(String[] args) {

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

			parser.parse("36 3 2 ? * / ");
			boolean result = parser.createStack();
			if(result) {
				parser.parse("3 +", true);
			}
			parser.createStack();
			parser.debugOutput();
			CalcExecutor executor = new CalcExecutor();
			CalcStack stack = parser.getStack();
			executor.execute(stack);
			stack.printResult();
			parser.clear();

		} catch(Exception ex) {
			ex.printStackTrace();
		}


	}

}
