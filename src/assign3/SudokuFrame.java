package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {
	 
	private JTextArea source;
	private JTextArea results;
	
	private JButton check;
	private JCheckBox box;
	private Box menu;
	
	
	public SudokuFrame() {
		super("Sudoku Solver");
		
        setLayout(new BorderLayout(4, 4));
        
        source = new JTextArea(15, 20);
        results = new JTextArea(15, 20);
        
		menu = Box.createHorizontalBox();
		check = new JButton("check");
		box = new JCheckBox("Auto Check");
		box.setSelected(true);
		
        source.setBorder(new TitledBorder("Puzzle"));
        results.setBorder(new TitledBorder("Solution"));
        
        menu.add(check);
        menu.add(box);
        
        add(source, BorderLayout.CENTER);
        add(results, BorderLayout.EAST);
        add(menu, BorderLayout.SOUTH);
        
        addListeners();
        
		setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	private void addListeners() {
		Document doc = source.getDocument();
		doc.addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				if(box.isSelected()){
					updateSolutions();
				}
				
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if(box.isSelected()){
					updateSolutions();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if(box.isSelected()){
					updateSolutions();
				}
			}
		});
		
		check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateSolutions();
				
			}
		});
		
		box.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateSolutions();
			}
		});
	}
		
	private void updateSolutions() {
		try{
			int numOfSolutions = 0;
			Sudoku s = new Sudoku(source.getText());
			numOfSolutions = s.solve();
			if(numOfSolutions > 0){
				String solutionText = s.getSolutionText();
				solutionText += "solutions: " + numOfSolutions + "\n" 
						+ "elapsed: " + s.getElapsed() + "\n";
				results.setText(solutionText);
			}
		}catch (Exception e){
			results.setText("Parsing Problem");
		}
		
	}
		


	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
