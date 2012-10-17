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

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		getContentPane().setLayout(gridBagLayout);
		this.setMinimumSize(new Dimension(920, 480));

		this.initalizeMenu();
		this.initializeComponents();
		this.setVisible(true);
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
        c.fill = GridBagConstraints.BOTH; c.insets = new Insets(10, 10, 5, 10);
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.weighty = 0.4; c.gridwidth = 3;
        getContentPane().add(inputTextAreaScrollPane, c);

        d = new GridBagConstraints();
        d.fill = GridBagConstraints.NONE; d.insets = new Insets(0, 10, 5, 5);
        d.gridx = 0; d.gridy = 1; d.weightx = 0.0; d.weighty = 0.1; d.gridwidth = 1;
        getContentPane().add(inputTextLabel, d);

        e = new GridBagConstraints();
        e.fill = GridBagConstraints.HORIZONTAL; e.insets = new Insets(0, 5, 5, 10);
        e.gridx = 1; e.gridy = 1; e.weightx = 0.9; e.weighty = 0.1; e.gridwidth = 1;
        getContentPane().add(inputTextField, e);

        f = new GridBagConstraints();
        f.fill = GridBagConstraints.NONE; f.insets = new Insets(0, 0, 5, 10);
        f.gridx = 2; f.gridy = 1; f.weightx = 0.0; f.weighty = 0.1; f.gridwidth = 1;
        getContentPane().add(this.debugStepButton, f);

        g = new GridBagConstraints();
        g.weightx = 1.0;
        g.fill = GridBagConstraints.BOTH; g.insets = new Insets(5, 10, 10, 10);
        g.gridx = 0; g.gridy = 2;g.weighty = 0.5; g.gridwidth = 3;
        getContentPane().add(outputTextAreaScrollPane, g);

        h = new GridBagConstraints();
        h.fill = GridBagConstraints.BOTH; h.insets = new Insets(10,10,10,10);
        h.gridx = 3; h.gridy = 0; h.weightx = 0.3; h.weighty = 1; h.gridwidth = 1; h.gridheight = 3;
        getContentPane().add(this.createExecutorLoggingPanel(), h);
	}

	private JPanel createExecutorLoggingPanel() {
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(300, 10));
		panel.setLayout(new GridBagLayout());

		this.intputListTextArea = new JTextArea();
		this.intputListTextArea.setEditable(false);
		JScrollPane inputListTextAreaScrollPane = new JScrollPane(this.intputListTextArea);

		this.stackTextArea = new JTextArea();
		this.stackTextArea.setEditable(false);
		JScrollPane stackTextAreaScrollPane = new JScrollPane(this.stackTextArea);


		GridBagConstraints i,j;

		i = new GridBagConstraints();
		i.fill = GridBagConstraints.BOTH; i.insets = new Insets(0,0,0,0);
        i.gridx = 0; i.gridy = 0; i.weightx = 1; i.weighty = 1; i.gridwidth = 1;
        panel.add(inputListTextAreaScrollPane, i);

        j = new GridBagConstraints();
        j.fill = GridBagConstraints.BOTH; j.insets = new Insets(10,0,0,0);
        j.gridx = 0; j.gridy = 1; j.weightx = 1; j.weighty = 1; j.gridwidth = 1;
        panel.add(stackTextAreaScrollPane, j);

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
		this.inputTextField.setEnabled(false);
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
