package at.ac.prog.calculator.engine.util;

import java.util.List;

public interface CalcExecutorListener {

	public void notifyStackChange(List<String> stack);

	public void notifyInputListChange(List<Object> inputList);

	public void notifyOutput(String output);

	public void notifyNewInput();

	public void notifyNewInput(boolean questionmark);

}
