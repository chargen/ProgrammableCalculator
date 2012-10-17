package at.ac.prog.calculator.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import at.ac.prog.calculator.engine.CalcExecutor;
import at.ac.prog.calculator.engine.exception.CalcParsingException;
import at.ac.prog.calculator.engine.util.CalcExecutorListener;

public class CalculatorUI extends JFrame implements WindowListener, CalcExecutorListener {

	private static final long serialVersionUID = 986427844864093227L;
	private JTextField inputTextField;
	private JTextArea inputTextArea;
	private JTextArea outputTextArea;
	private JTextArea stackTextArea;
	private JTextArea intputListTextArea;

	private JButton debugStepButton;
	private CalcExecutor executor;

	public CalculatorUI() {
		this.setTitle("Programmable Calculator");
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(640, 480));
		this.setVisible(true);
		
		this.initalizeMenu();
		this.initializeComponents();
		
		// just in case swing doesn't do it itself --> validate GUI so that it is shown correctly
		this.validate();
		
	}

	public void init(CalcExecutor executor) {
		this.executor = executor;
	}

	private void initalizeMenu() {

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem closeAppMItem = new JMenuItem("Close App");
		closeAppMItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
			    CalculatorUI.this.initializeShutdown();
			}

		});

		JMenuItem enableDebugMItem = new JMenuItem("Toggle Debug");
		enableDebugMItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
			    CalculatorUI.this.executor.toggleDebug();
			    CalculatorUI.this.debugStepButton.setEnabled(!CalculatorUI.this.debugStepButton.isEnabled());
			}

		});

		JMenuItem clearStackMItem = new JMenuItem("Clear Stack");
		clearStackMItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
			    CalculatorUI.this.executor.clearStack();
			    CalculatorUI.this.stackTextArea.setText("");
			}
		});

		JMenuItem clearInputAreaMItem = new JMenuItem("Clear Input Area");
		clearInputAreaMItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
			    CalculatorUI.this.inputTextArea.setText("");
			}
		});

		JMenuItem clearOutputAreaMItem = new JMenuItem("Clear Output Area");
		clearOutputAreaMItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
			    CalculatorUI.this.outputTextArea.setText("");
			}
		});

		fileMenu.add(enableDebugMItem);
		fileMenu.add(clearStackMItem);
		fileMenu.add(clearInputAreaMItem);
		fileMenu.add(clearOutputAreaMItem);
		fileMenu.addSeparator();
		fileMenu.add(closeAppMItem);
		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);
	}

	private void initializeComponents() {

		this.inputTextArea = new JTextArea();
		this.inputTextArea.setEditable(false);
		JScrollPane inputTextAreaScrollPane = new JScrollPane(this.inputTextArea);

		JLabel inputTextLabel = new JLabel("Input: ");

		this.inputTextField = new JTextField();
		this.inputTextField.setEditable(true);
		this.inputTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = CalculatorUI.this.inputTextField.getText();
				CalculatorUI.this.processNewParseInput(input);
			}
		});

		this.initializeDebugStepButton();

		this.outputTextArea = new JTextArea();
		this.outputTextArea.setEditable(false);
		JScrollPane outputTextAreaScrollPane = new JScrollPane(this.outputTextArea);

		GridBagConstraints c,d,e,f,g,h;

		c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,10,0,10);
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 0.4; c.gridwidth = 3;
        this.add(inputTextAreaScrollPane, c);
        
        c.fill = GridBagConstraints.NONE; c.insets = new Insets(0,10,0,0);
        c.gridx = 0; c.gridy = 1; c.weightx = 0.0; c.weighty = 0.1; c.gridwidth = 1;
        this.add(inputTextLabel, c);
        c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(0,5,0,10);
        c.gridx = 1; c.gridy = 1; c.weightx = 0.9; c.weighty = 0.1; c.gridwidth = 1;
        this.add(inputTextField, c);
        c.fill = GridBagConstraints.NONE; c.insets = new Insets(0,0,0,10);
        c.gridx = 2; c.gridy = 1; c.weightx = 0.0; c.weighty = 0.1; c.gridwidth = 1;
        this.add(this.debugStepButton, c);
        
        c.fill = GridBagConstraints.BOTH; c.insets = new Insets(15,10,10,10);
        c.gridx = 0; c.gridy = 2; c.weightx = 1; c.weighty = 0.5; c.gridwidth = 3;
        this.add(outputTextAreaScrollPane, c);
        
        c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,10,10,10);
        c.gridx = 4; c.gridy = 0; c.weightx = 0.3; c.weighty = 1; c.gridwidth = 1; c.gridheight = 4;
        this.add(this.createExecutorLoggingPanel(), c);
	}

	private JPanel createExecutorLoggingPanel() {
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(300, 10));
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		this.intputListTextArea = new JTextArea();
		this.intputListTextArea.setEditable(false);
		JScrollPane inputListTextAreaScrollPane = new JScrollPane(this.intputListTextArea);

		this.stackTextArea = new JTextArea();
		this.stackTextArea.setEditable(false);
		JScrollPane stackTextAreaScrollPane = new JScrollPane(this.stackTextArea);
		
		c.fill = GridBagConstraints.BOTH; c.insets = new Insets(0,0,0,0);
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 1; c.gridwidth = 1;
        panel.add(inputListTextAreaScrollPane, c);
        c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10,0,0,0);
        c.gridx = 0; c.gridy = 1; c.weightx = 1; c.weighty = 1; c.gridwidth = 1;
        panel.add(stackTextAreaScrollPane, c);

		return panel;
	}

	private void initializeDebugStepButton() {
		this.debugStepButton = new JButton("Next Step");
		this.debugStepButton.setEnabled(false);
		debugStepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CalculatorUI.this.nextProcessStep();
			}
		});
	}


	private void addOutputMessage(String message) {
		this.outputTextArea.append(message);
	}


	private int getValidationDialogResult() {
		return (JOptionPane.showConfirmDialog(
					this,
					"Really Quit?",
					"GUI",
					JOptionPane.OK_CANCEL_OPTION));
	}

	private void initializeShutdown() {
		if (JOptionPane.OK_OPTION == getValidationDialogResult()){
			//TODO: close APP
			System.exit(0);
		}
	}

	private void processNewParseInput(String input) {
		this.inputTextField.setText("");
		this.inputTextArea.append(input + "\n");
		this.inputTextArea.setEditable(false);
		try {
			this.executor.parse(input);
			this.executor.execute();
		} catch (CalcParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void nextProcessStep() {
		try {
			this.executor.execute();
		} catch (CalcParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.initializeShutdown();
	}

	@Override
	public void windowDeactivated(WindowEvent e) { }

	@Override
	public void windowDeiconified(WindowEvent e) { }

	@Override
	public void windowIconified(WindowEvent e) { }

	@Override
	public void windowOpened(WindowEvent e) { }

	@Override
	public void windowActivated(WindowEvent e) { }

	@Override
	public void windowClosed(WindowEvent e) { }

	@Override
	public void notifyStackChange(List<String> stack) {
		this.stackTextArea.setText("");
		for(Object item : stack) {
			this.stackTextArea.append(item + "\n");
		}
	}

	@Override
	public void notifyInputListChange(List<Object> inputList) {
		this.intputListTextArea.setText("");
		for(Object item : inputList) {
			this.stackTextArea.append(item + "\n");
		}
	}

	@Override
	public void notifyOutput(String output) {
		this.addOutputMessage(output);
	}

	@Override
	public void notifyNewInput() {
		this.inputTextField.setEnabled(true);
	}
}
