package reversi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class ReversiSquare extends JButton
{
	
	// constants: black border, green background, size of square
	// variables: piece colour & background colour
	
	Color pieceColor, borderColor;
	IModel model;
	IController controller;
	int x, y, squareValue, player;
	
	public ReversiSquare(IModel model, IController controller, int x, int y, int player)
	{
		this.model = model;
		this.controller = controller;
		this.x = x;
		this.y = y;
		this.player = player; // is this square in the black or white JFrame?
		
		squareValue = model.getBoardContents(x, y); 
		
		if(pieceColor == Color.white) {
			borderColor = Color.black;
		}
		else {
			borderColor = Color.white;
		}
		
		initSquare();
		refreshSquare();
	}
	
	public void initSquare() 
	{
		setBackground(new Color(0,255,0));
		setBorder(new LineBorder(Color.black, 1));
		setPreferredSize(new Dimension(50,50));
		addActionListener(new SquareSelectionHandler());
	}
	
	@Override
	protected void paintComponent (Graphics g) 
	{	
		super.paintComponent(g);	
		
		if (squareValue != 0) {
			
			g.setColor(pieceColor);
			g.fillOval(2, 2, 46, 46);
			g.setColor(borderColor);
			g.drawOval(2, 2, 46, 46);		
			
		}
	}
	
	public void refreshSquare() 
	{
		squareValue = model.getBoardContents(x, y);
		
		//empty = 0; white = 1; black = 2;
		switch(squareValue) {
			case 1:
				pieceColor = Color.white;
				borderColor = Color.black;
				break;
				
			case 2:
				pieceColor = Color.black;
				borderColor = Color.white;
				break;
		}
		repaint();		
	}
	
	public class SquareSelectionHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Square Selected at " + x + "," + y);
			controller.squareSelected(player, x, y);
			
			
			
		}
		
	} 
}
