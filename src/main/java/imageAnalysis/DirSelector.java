package imageAnalysis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Path;

import javax.swing.*;

public class DirSelector extends JFrame {
	//JButton okButton, cancelButton;
	JFileChooser dirSel;
	static Path dir;
	
	public DirSelector() {
		Container mainPane = this.getContentPane();
		
		this.setTitle("Directory selection");
		this.setSize(600,400);
		this.setLocationRelativeTo(null); //centers window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		mainPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		String dxns = "Select a directory then click OK. If OK is pressed without a "
				+ "selection, the directory being currently viewed will be selected";
		
		JTextPane dxnBox = new JTextPane();
		dxnBox.setText(dxns);
		dxnBox.setEditable(false);
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		mainPane.add(dxnBox, gbc);
		
		dirSel = new JFileChooser();
		dirSel.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		mainPane.add(dirSel, gbc);
		dirSel.setVisible(true);
		
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand() == JFileChooser.APPROVE_SELECTION) {
					System.out.print("open");
					if(dirSel.getSelectedFile() == null) {
						System.out.println(" no selection");
						File f = dirSel.getCurrentDirectory();
						setDir(f);
					}
					else {
						System.out.println(" selection");
						File f = dirSel.getSelectedFile();
						setDir(f);
					}
					
					
				}
				else if(e.getActionCommand() == JFileChooser.CANCEL_SELECTION) {
					System.out.println("Selection cancelled");
					System.exit(0);
				}
			}
		};
		
		dirSel.addActionListener(al);
	}
	
	public void setDir(File f) {
		dir = f.toPath();
		System.out.println(dir);
	}
	
	public Path getDir() {
		return dir;
	}

}
