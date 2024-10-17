package reversi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIView implements IView {
	
	IModel model;
	IController controller;
	JFrame whiteframe = new JFrame();
	JFrame blackframe = new JFrame();
	JLabel wplayerlabel = new JLabel();
	JLabel bplayerlabel = new JLabel();
	ReversiSquare[][] whiteboard = new ReversiSquare[8][8];
	ReversiSquare[][] blackboard = new ReversiSquare[8][8];
	
	@Override
	public void initialise(IModel model, IController controller) {
		this.model = model;
		this.controller = controller;
		
		createGUI();
	}
	
	public void createGUI()
	{
		
		//**********WHITE	
		
		//label
		wplayerlabel.setText("White Player"); //you will likely use a StringBuffer here
		whiteframe.getContentPane().add(wplayerlabel,BorderLayout.NORTH);
		
		// board
		JPanel wboardpanel = new JPanel();
		wboardpanel.setLayout(new GridLayout(8,8));
		whiteframe.getContentPane().add(wboardpanel,BorderLayout.CENTER);
		
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				whiteboard[x][y] = new ReversiSquare(model,controller,x,y,1);
				wboardpanel.add(whiteboard[x][y]);				
			}
		}
		
		//buttons
		JPanel wbuttons = new JPanel();
		JButton wgreedybttn = new JButton();
		JButton wrestartbttn = new JButton();
		wbuttons.setLayout(new GridLayout(2,1));
		wgreedybttn.setText("Greedy AI (play white)");
		wgreedybttn.addActionListener(new GreedyAIButtonHandler(1));
		wrestartbttn.setText("Restart");
		wrestartbttn.addActionListener(new RestartButtonHandler());
		wbuttons.add(wgreedybttn, BorderLayout.NORTH);
		wbuttons.add(wrestartbttn, BorderLayout.SOUTH);
		whiteframe.getContentPane().add(wbuttons,BorderLayout.SOUTH);
		
		
		//**********BLACK 
		
		//label
		bplayerlabel.setText("Black Player"); //you will likely use a StringBuffer here
		blackframe.getContentPane().add(bplayerlabel,BorderLayout.NORTH);
		
		// board
		JPanel bboardpanel = new JPanel();
		bboardpanel.setLayout(new GridLayout(8,8));
		blackframe.getContentPane().add(bboardpanel,BorderLayout.CENTER);
		for(int x = 7; x >= 0; x--) {
			for(int y = 7; y >= 0; y--) {
				blackboard[x][y] = new ReversiSquare(model,controller,x,y,2);
				bboardpanel.add(blackboard[x][y]);				
			}
		}
		
		//buttons
		JPanel bbuttons = new JPanel();		
		JButton bgreedybttn = new JButton();
		JButton brestartbttn = new JButton();
		bbuttons.setLayout(new GridLayout(2,1));
		bgreedybttn.setText("Greedy AI (play black)");
		bgreedybttn.addActionListener(new GreedyAIButtonHandler(2));
		brestartbttn.setText("Restart");
		brestartbttn.addActionListener(new RestartButtonHandler());
		bbuttons.add(bgreedybttn,BorderLayout.NORTH);
		bbuttons.add(brestartbttn,BorderLayout.SOUTH);
		blackframe.getContentPane().add(bbuttons,BorderLayout.SOUTH);
		
		//final
		whiteframe.pack();
		whiteframe.setLocationRelativeTo(null);
		whiteframe.setVisible(true);
//		
		blackframe.pack();
		blackframe.setLocationRelativeTo(null);
		blackframe.setVisible(true);
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub
		
//		use get and set for each square
		// refresh white board
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				whiteboard[x][y].refreshSquare();
			}
		}
		
		//refresh black board
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				blackboard[x][y].refreshSquare();
			}
		}
	}

	@Override
	public void feedbackToUser(int player, String message) {
		// TODO Auto-generated method stub
		
		switch(player) {
			case 1:
				wplayerlabel.setText(message);
				wplayerlabel.repaint();
				break;
			
			case 2:
				bplayerlabel.setText(message);
				bplayerlabel.repaint();
				break;
			
			default:
				System.out.println(message);
				break;
				
		}
		
		
	}
	
	public class RestartButtonHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			controller.startup();
		}
	}
	
	public class GreedyAIButtonHandler implements ActionListener{

		int player;
		
		public GreedyAIButtonHandler(int player) {
			this.player = player;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.doAutomatedMove(player);
		}
	}

}
