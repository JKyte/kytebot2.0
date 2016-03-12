package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 
 * @author JKyte
 * 
 * This class is based on code found on stackoverflow
 */
@SuppressWarnings("serial")
public class UserInputBox extends JFrame implements ActionListener {

	private JButton sendMsgBtn = new JButton("Send msg");

	private JTextArea textArea = new JTextArea(8, 40);

	private JScrollPane scrollPane = new JScrollPane(textArea);

	private ConcurrentLinkedQueue<String> outboundMsgQ;
	

	public UserInputBox(  ConcurrentLinkedQueue<String> msgQ ) {

		this.outboundMsgQ = msgQ;

		JPanel p = new JPanel();

		p.add(sendMsgBtn);
		sendMsgBtn.addActionListener(this);

		getContentPane().add(p, "South");

		getContentPane().add(scrollPane, "Center");

		setTitle("TextAreaTest");
		setSize(300, 300);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == sendMsgBtn){

			String cmd = textArea.getText();
			outboundMsgQ.add(cmd);
			textArea.setText("");
			
		}else{
			System.err.println("Unhandled action event!");
		}
	}
	
	public void stub(){
		//	I do nothing, save get rid of 'unused warnings'.
	}

}
