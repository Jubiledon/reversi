package reversi;

public class ReversiController implements IController{

	IModel model;
	IView view;
	
	@Override
	public void initialise(IModel model, IView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void startup() {
		model.setFinished(false);
		
		// Initialise board
		model.clear(0);
		
		// Set Starting Positions
		model.setBoardContents(3, 3, 1);
		model.setBoardContents(3, 4, 2);
		model.setBoardContents(4, 3, 2);
		model.setBoardContents(4, 4, 1);
		
		// Set to White's turn
		model.setPlayer(2);
		switchPlayer();
		
		// Refresh all messages and frames
		view.refreshView();

	}

	@Override
	public void update() {
		
		int player = model.getPlayer();
		
		// If the current player cannot play, change to other player.
		
		if(countPlayerMoves(player) == 0){
			switchPlayer();
		}
		
		// If this player also cannot play then end the game - count the number of pieces, set the labels for players, and set the finished flag.
		if(countPlayerMoves(player) == 0) {
			model.setFinished(true);
		}
		
		// If the game has not finished, send the correct feedback messages to players to tell them whose turn it is.	
		else {
			model.setFinished(false);
			switchPlayer();
			switchPlayer();
		}
		
		// Set labels for players when game finishes.
		if(model.hasFinished()) {
			int whitePieces = countPlayerPieces(1);
			int blackPieces = countPlayerPieces(2);
			
			if(whitePieces > blackPieces) {
				view.feedbackToUser(1, "White won. White " + whitePieces + " to Black " + blackPieces + ". Reset the game to replay.");
				view.feedbackToUser(2, "White won. White " + whitePieces + " to Black " + blackPieces + ". Reset the game to replay.");
			}
			else if(blackPieces > whitePieces) {
				view.feedbackToUser(1, "Black won. Black " + blackPieces + " to White " + whitePieces + ". Reset the game to replay.");
				view.feedbackToUser(2, "Black won. Black " + blackPieces + " to White " + whitePieces + ". Reset the game to replay.");
			}
			else {
				view.feedbackToUser(1, "Draw. Both players ended with " + whitePieces + " pieces. Reset the game to replay.");
				view.feedbackToUser(2, "Draw. Both players ended with " + whitePieces + " pieces. Reset the game to replay.");
			}
		}
		
	}

	@Override
	public void squareSelected(int player, int x, int y) 
	{
		update();
		
		if(model.hasFinished() == false) {
			
			if(player != model.getPlayer()) {
				view.feedbackToUser(player, "It is not your turn!");
			}
			else {
				//if move is valid
				if(countPieceCaptures(x,y) > 0 && model.getBoardContents(x, y) == 0) {
					capturePieces(x,y);
					switchPlayer();
					view.refreshView();							
				}
			}
		}
		
		
	}

	@Override
	public void doAutomatedMove(int player) 
	{
		// Stores the co-ordinates (maxX,maxY) of the move with the highest
		// captures (maxCaptures)
		
		int maxCaptures = 0;
		int maxX = 0;
		int maxY = 0;
		
		for (int x = 0; x < model.getBoardHeight(); x++) {
			for (int y = 0; y < model.getBoardWidth(); y++) {
				
				if(model.getBoardContents(x, y) == 0) {
					
						if(countPieceCaptures(x,y) >= maxCaptures) {
							maxCaptures = countPieceCaptures(x,y);
							maxX = x;
							maxY = y;
						}
					
				}

			}
		}
//		System.out.println("playing at position (" + maxX + "," + maxY + ") will capture " + maxCaptures + " pieces");
		squareSelected(player, maxX, maxY);
		
	}
	
	/** Counts the number of pieces that can be captured by
	 * a piece at a certain position  
	 */
	public int countPieceCaptures(int x, int y) 
	{
		int player = model.getPlayer();
		int countPieces = 0;
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		
		// Horizontal Check

		//left side of board
		if(y == 0) {
			//check pieces to the right
			if(model.getBoardContents(x, y+1) != player
					&& model.getBoardContents(x, y+1) != 0) {
				int increment = 1;
				for(int i = y+2; i<width; i++) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}			
		}
		
		//right side of board
		else if(y == width - 1) {
			//check pieces to the left
			if(model.getBoardContents(x, y-1) != player
					&& model.getBoardContents(x, y-1) != 0) {
				int increment = 1;
				for(int i = y-2; i>-1; i--) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
		}
		
		else {
			//check pieces to the left
			if(model.getBoardContents(x, y-1) != player
					&& model.getBoardContents(x, y-1) != 0) {
				int increment = 1;
				for(int i = y-2; i>-1; i--) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
				
			//check pieces to the right
			if(model.getBoardContents(x, y+1) != player
					&& model.getBoardContents(x, y+1) != 0) {
				int increment = 1;
				for(int i = y+2; i<width; i++) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}			
		
		}

		// Vertical Check
		
		//top of board
		if(x == 0) {
			//check pieces below
			if(model.getBoardContents(x+1, y) != player
					&& model.getBoardContents(x+1, y) != 0) {
				int increment = 1;
				for(int i = x+2; i<height; i++) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
		}
		
		//bottom of board
		else if(x == width-1) {
			//check pieces above
			if(model.getBoardContents(x-1, y) != player
					&& model.getBoardContents(x-1, y) != 0) {
				int increment = 1;
				for(int i = x-2; i>-1; i--) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
		}

		else {	
			
			//check pieces below
			if(model.getBoardContents(x+1, y) != player
					&& model.getBoardContents(x+1, y) != 0) {
				int increment = 1;
				for(int i = x+2; i<height; i++) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			
			//check pieces above
			if(model.getBoardContents(x-1, y) != player
					&& model.getBoardContents(x-1, y) != 0) {
				int increment = 1;
				for(int i = x-2; i>-1; i--) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
		}

		// Diagonal Check
				
		//bottom right
		if(x == 7 && y == 7) {
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
		}
		
		//top left
		else if (x == 0 && y == 0) {
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						countPieces += increment;
						break;
					}
					increment++;
					
				}
			}
		}
		
		//top right
		else if (x == 0 && y == 7) {
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}	
		}
		
		//bottom left
		else if (x == 7 && y == 0) {
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
		}
		
		//piece at the top (first row) of board
		else if(x == 0 && y != 7 && y != 0) {
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						countPieces += increment;
						break;
					}
					increment++;
					
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
		}
		
		//piece at the bottom (last row) of board
		else if(x == 7 && y != 7 && y != 0) {
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
		}
		
		//piece is at left side of board
		else if (y == 0 && x != 0 && x!= 7) {
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						countPieces += increment;
						break;
					}
					increment++;
					
				}
			}
			
		}
		
		//piece is at right side of board
		else if (y == 7 && x != 0 && x != 7) {
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			
		}
		
		else {
	
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						countPieces += increment;
						break;
					}
					increment++;
					
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						countPieces += increment;
						break;
					}
						increment++;
				}
			}
			
		}

		return countPieces;				
	}

	/** Counts the number of moves a player can make */
	public int countPlayerMoves(int player) 
	{
		int countMoves = 0;
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		
		// check all players pieces on the board and see if they can play a legal move
		for(int x = 0; x < height; x++) {
			for(int y = 0; y < width; y++) {
				// if square is empty and pieces can be captured, then it is legal
				if(model.getBoardContents(x, y) == 0 && countPieceCaptures(x,y) > 0) {
					countMoves++;
				}
				
			}
		}
		
		return countMoves;
	}

	/** Counts the number of pieces a player has on the board currently*/
	public int countPlayerPieces(int player)
	{
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		int pieceCount = 0;
		
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++){
				
				if(model.getBoardContents(x, y) == player) {
					pieceCount++;
				}
			}
		}
		
		return pieceCount;
	}

	/** Plays valid moves and captures pieces */
	public void capturePieces(int x, int y) 
	{
		int player = model.getPlayer();
		int countPieces = 0;
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		
		model.setBoardContents(x, y, player);
		
		// Horizontal Check

		//left side of board
		if(y == 0) {
			//check pieces to the right
			if(model.getBoardContents(x, y+1) != player
					&& model.getBoardContents(x, y+1) != 0) {
				int increment = 1;
				for(int i = y+2; i<width; i++) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x, y+k, player);
						}
						break;
					}
						increment++;
				}
			}				
		}
		
		//right side of board
		else if(y == width - 1) {
			//check pieces to the left
			if(model.getBoardContents(x, y-1) != player
					&& model.getBoardContents(x, y-1) != 0) {
				int increment = 1;
				for(int i = y-2; i>-1; i--) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
		}
		
		else {
			//check pieces to the left
			if(model.getBoardContents(x, y-1) != player
					&& model.getBoardContents(x, y-1) != 0) {
				int increment = 1;
				for(int i = y-2; i>-1; i--) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
				
			//check pieces to the right
			if(model.getBoardContents(x, y+1) != player
					&& model.getBoardContents(x, y+1) != 0) {
				int increment = 1;
				for(int i = y+2; i<width; i++) {
					if(model.getBoardContents(x, i) == 0) {
						break;
					}
					else if(model.getBoardContents(x, i) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x, y+k, player);
						}
						break;
					}
						increment++;
				}
			}			
		
		}

		// Vertical Check
		
		//top of board
		if(x == 0) {
			//check pieces below
			if(model.getBoardContents(x+1, y) != player
					&& model.getBoardContents(x+1, y) != 0) {
				int increment = 1;
				for(int i = x+2; i<height; i++) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y, player);
						}
						break;
					}
						increment++;
				}
			}
		}
		
		//bottom of board
		else if(x == width-1) {
			//check pieces above
			if(model.getBoardContents(x-1, y) != player
					&& model.getBoardContents(x-1, y) != 0) {
				int increment = 1;
				for(int i = x-2; i>-1; i--) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y, player);
						}
						break;
					}
						increment++;
				}
			}
		}

		else {	
			
			//check pieces below
			if(model.getBoardContents(x+1, y) != player
					&& model.getBoardContents(x+1, y) != 0) {
				int increment = 1;
				for(int i = x+2; i<height; i++) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces above
			if(model.getBoardContents(x-1, y) != player
					&& model.getBoardContents(x-1, y) != 0) {
				int increment = 1;
				for(int i = x-2; i>-1; i--) {
					if(model.getBoardContents(i, y) == 0) {
						break;
					}
					else if(model.getBoardContents(i, y) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y, player);
						}
						break;
					}
						increment++;
				}
			}
			
		}

		// Diagonal Check
				
		//bottom right
		if(x == 7 && y == 7) {
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
		}
		
		//top left
		else if (x == 0 && y == 0) {
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y+k, player);
						}
						break;
					}
					increment++;
					
				}
			}
		}
		
		//top right
		else if (x == 0 && y == 7) {
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}	
		}
		
		//bottom left
		else if (x == 7 && y == 0) {
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y+k, player);
						}
						break;
					}
						increment++;
				}
			}
		}
		
		//piece at the top (first row) of board
		else if(x == 0 && y != 7 && y != 0) {
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) != player) {
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y+k, player);
						}
						break;
					}
					increment++;
					
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
		}
		
		//piece at the bottom (last row) of board
		else if(x == 7 && y != 7 && y != 0) {
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y+k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
		}
		
		//piece is at left side of board
		else if (y == 0 && x != 0 && x!= 7) {
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y+k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) != player) {
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y+k, player);
						}
						break;
					}
					increment++;
					
				}
			}
			
		}
		
		//piece is at right side of board
		else if (y == 7 && x != 0 && x != 7) {
			
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			
		}
		
		else {
	
			//check pieces upper-left
			if(model.getBoardContents(x-1, y-1) != player
					&& model.getBoardContents(x-1, y-1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y-2; i > -1 && j > -1; i--, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces lower-right
			if(model.getBoardContents(x+1, y+1) != player
					&& model.getBoardContents(x+1, y+1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y+2; i < height && j < width; i++, j++) {
					
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) {
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y+k, player);
						}
						break;
					}
					increment++;
					
				}
			}
			
			//check pieces lower-left
			if(model.getBoardContents(x+1, y-1) != player
					&& model.getBoardContents(x+1, y-1) != 0) {
				int increment = 1;
				for(int i = x+2, j = y-2; i < height && j > -1; i++, j--) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x+k, y-k, player);
						}
						break;
					}
						increment++;
				}
			}
			
			//check pieces upper-right
			if(model.getBoardContents(x-1, y+1) != player
					&& model.getBoardContents(x-1, y+1) != 0) {
				int increment = 1;
				for(int i = x-2, j = y+2; i > -1 && j < width; i--, j++) {
					if(model.getBoardContents(i, j) == 0) {
						break;
					}
					else if(model.getBoardContents(i, j) == player) 
					{
						for (int k = 1; k <= increment; k++) {
							model.setBoardContents(x-k, y+k, player);
						}
						break;
					}
						increment++;
				}
			}
			
		}
		
	}
	
	public void switchPlayer() 
	{	
		switch(model.getPlayer()) {
			case 1:
				model.setPlayer(2);
				view.feedbackToUser(1,"White player – not your turn");
				view.feedbackToUser(2,"Black player – choose where to put your piece");
				break;
			case 2:
				model.setPlayer(1);
				view.feedbackToUser(1,"White player – choose where to put your piece");
				view.feedbackToUser(2,"Black player – not your turn");
				break;
		}	
	}
	
}
