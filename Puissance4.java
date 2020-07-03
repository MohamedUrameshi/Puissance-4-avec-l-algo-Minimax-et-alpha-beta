package up.mi.ken;

import java.util.*;

public class Puissance4 {
	private Board b;
	private Scanner scan;
	private int nextMoveLocation = -1;
	private int maxDepth = 11;

	public int getMaxDepth() {
		return maxDepth;
	}

	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	public Puissance4(Board b) {
		this.b = b;
		scan = new Scanner(System.in);
	}

	/**
	 * Assuming human is the opponent <i>let the opponent play</i>
	 */

	public void letOpponentMove() {
		System.out.println("Joues entre 1-7 : ");
		int move = scan.nextInt();
		while (move < 1 || move > 7 || !b.isPossibleMove(move - 1)) {
			System.out.println("Invalid move.\n\nYour move (1-7): ");
			move = scan.nextInt();
		}

		// on suppose que 1 est le pion de l'IA
		b.placeMove(move - 1, (int) 2);
	}

	/**
	 * <i>checks if a player has won </i>
	 * 
	 * @param b the board
	 * @return 1 if the ai player has won ,2 else
	 */
	public int checkWinner(Board b) {
		int aiScore = 0, humanScore = 0;
		for (int i = 5; i >= 0; --i)// iterating over lines
		{
			for (int j = 0; j <= 6; ++j) // iterate over columns
			{
				if (b.board[i][j] == 0)
					continue;

				// Checking cells to the right
				if (j <= 3) {
					for (int k = 0; k < 4; ++k) {
						if (b.board[i][j + k] == 1)
							aiScore++;
						else if (b.board[i][j + k] == 2)
							humanScore++;
						else
							break;
					}
					if (aiScore == 4)
						return 1;
					else if (humanScore == 4)
						return 2;
					aiScore = 0;
					humanScore = 0;
				}

				// Checking cells up
				if (i >= 3) {
					for (int k = 0; k < 4; ++k) {
						if (b.board[i - k][j] == 1)
							aiScore++;
						else if (b.board[i - k][j] == 2)
							humanScore++;
						else
							break;
					}
					if (aiScore == 4)
						return 1;
					else if (humanScore == 4)
						return 2;
					aiScore = 0;
					humanScore = 0;
				}

				// Checking diagonal up-right
				if (j <= 3 && i >= 3) {
					for (int k = 0; k < 4; ++k) {
						if (b.board[i - k][j + k] == 1)
							aiScore++;
						else if (b.board[i - k][j + k] == 2)
							humanScore++;
						else
							break;
					}
					if (aiScore == 4)
						return 1;
					else if (humanScore == 4)
						return 2;
					aiScore = 0;
					humanScore = 0;
				}

				// Checking diagonal up-left
				if (j >= 3 && i >= 3) {
					for (int k = 0; k < 4; ++k) {
						if (b.board[i - k][j - k] == 1)
							aiScore++;
						else if (b.board[i - k][j - k] == 2)
							humanScore++;
						else
							break;
					}
					if (aiScore == 4)
						return 1;
					else if (humanScore == 4)
						return 2;
					aiScore = 0;
					humanScore = 0;
				}
			}
		}

		for (int j = 0; j < 7; ++j) {
			// if the game hasn't ended yet
			if (b.board[0][j] == 0)
				return -1;
		}
		// if it's a tie
		return 0;
	}

	/**
	 * 
	 * @param depth the depth of the explorartion tree
	 * @param turn  who's turn?
	 * @return the best score for the player
	 */
	public int minimax(int depth, int turn) {

		// we'll evaluate the board when we reach the maxDepth
		if (depth == getMaxDepth())
			return fonctionEvaluation(1, 2);
		int winner = checkWinner(b);

		if (winner == 1)
			return 1000;
		else if (winner == 2)
			return -1000;
		else if (winner == 0)
			return 0;

		int maxScore = -100000, minScore = 100000;

		for (int j = 0; j <= 6; ++j) {

			int currentScore = 0;

			if (!b.isPossibleMove(j))
				continue;

			if (turn == 1) {
				b.placeMove(j, 1);
				currentScore = minimax(depth + 1, 2);

				if (depth == 0) {

					if (currentScore > maxScore)
						nextMoveLocation = j;
					
				}

				maxScore = Math.max(currentScore, maxScore);

				// alpha = Math.max(currentScore, alpha);
			} else if (turn == 2) {
				b.placeMove(j, 2);
				currentScore = minimax(depth + 1, 1);
				minScore = Math.min(currentScore, minScore);

				// beta = Math.min(currentScore, beta);
			}
			b.undoMove(j);
			
		}
		return turn == 1 ? maxScore : minScore;
	}

	/**
	 * the minimaxAlphaBeta algorithm
	 * 
	 * @param depth the depth of the tree
	 * @param turn  who's turn
	 * @param alpha
	 * @param beta
	 * @return
	 */
	public int minimaxAlphaBeta(int depth, int turn, int alpha, int beta) {

		if (beta <= alpha) {
			if (turn == 1)
				return Integer.MAX_VALUE;
			else
				return Integer.MIN_VALUE;
		}
		int winner = checkWinner(b);

		if (winner == 1)
			return 1000;
		else if (winner == 2)
			return -1000;
		else if (winner == 0)
			return 0;
		// we'll evaluate the board when we reach the maxDepth
		if (depth == getMaxDepth())
			return fonctionEvaluation(1, 2);

		int maxScore = Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;

		for (int j = 0; j <= 6; ++j) {

			int currentScore = 0;

			if (!b.isPossibleMove(j))
				continue;

			if (turn == 1) {
				b.placeMove(j, 1);
				currentScore = minimaxAlphaBeta(depth + 1, 2, alpha, beta);

				if (depth == 0) {

					if (currentScore > maxScore)
						nextMoveLocation = j;
					if (currentScore == Integer.MAX_VALUE / 2) {
						b.undoMove(j);
						break;
					}
				}

				maxScore = Math.max(currentScore, maxScore);

				alpha = Math.max(currentScore, alpha);
			} else if (turn == 2) {
				b.placeMove(j, 2);
				currentScore = minimaxAlphaBeta(depth + 1, 1, alpha, beta);
				minScore = Math.min(currentScore, minScore);

				beta = Math.min(currentScore, beta);
			}
			b.undoMove(j);
			if (currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE)
				break;
		}
		return turn == 1 ? maxScore : minScore;
	}

	/**
	 * <i>calculates the favorbleness for an AI player using the MINIMAX
	 * algorithm</i>
	 * 
	 * @return the favorable spot to play at on the board
	 */
	public int getAIMove() {
		nextMoveLocation = -1;
		minimaxAlphaBeta(0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
		return nextMoveLocation;
	}

	/**
	 * human plays against ai
	 */
	public void playAgainstAIConsole() {
		int humanMove = -1;
		Scanner scan = new Scanner(System.in);
		b.displayBoard();
		System.out.println("Would you like to play first ? Type (yes/no) ");
		String answer = scan.next().trim();

		if (answer.equalsIgnoreCase("yes"))
			letOpponentMove();
		b.displayBoard();
		b.placeMove(3, 1);
		b.displayBoard();

		while (true) {
			letOpponentMove();
			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}

			b.placeMove(getAIMove(), 1);
			b.displayBoard();
			winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
		}

	}

	/**
	 * human vs human
	 */
	public void playAgainstHuman() {
		int humanMove = -1;
		Scanner scan = new Scanner(System.in);
		b.displayBoard();
		System.out.println("Game started!!!");

		while (true) {

			b.displayBoard();

			System.out.println("Play between 1-7");
			int move = scan.nextInt();

			b.placeMove(move - 1, 1);
			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("Player 1 has won!");
				break;
			} else if (winner == 2) {
				System.out.println("Player 2 has won!");
				break;
			} else if (winner == 0) {
				System.out.println("It's a Draw!");
				break;
			}
			System.out.println("Player 2, play between 1-7");
			int move2 = scan.nextInt();
			b.placeMove(move2 - 1, 2);

		}
	}

	/**
	 * Makes AI play against AI
	 */
	public void playAIAgainstAI() {

		while (true) {
			b.placeMove(getAIMove(), 1);
			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI 1 Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("AI 2 Wins!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}

			b.placeMove(getAIMove(), 2);
			b.displayBoard();
			winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
		}

	}

	/**
	 * <i>calculates the favorbleness for an AI player using the MINIMAX
	 * algorithm</i> it fills the nextMoveLocation using minimax algorithm
	 * 
	 * @return the favorable spot to play at on the board
	 */
	public int getAIMoveMinimax() {
		nextMoveLocation = -1;
		minimax(0, 1);
		return nextMoveLocation;
	}

	/**
	 * human plays against ai
	 */
	public void playAgainstAIConsoleUsingMinimax() {
		int humanMove = -1;
		Scanner scan = new Scanner(System.in);
		b.displayBoard();
		System.out.println("Would you like to play first ? Type (yes/no) ");
		String answer = scan.next().trim();

		if (answer.equalsIgnoreCase("yes"))
			letOpponentMove();
		b.displayBoard();
		b.placeMove(3, 1);
		b.displayBoard();

		while (true) {
			letOpponentMove();
			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}

			b.placeMove(getAIMoveMinimax(), 1);
			b.displayBoard();
			winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
		}

	}

	/**
	 * Makes AI play against AI using simple minimax algorithm
	 */
	public void playAIAgainstAIUsingMinimax() {
		setMaxDepth(11);

		while (true) {
			// the AI 1 Plays
			b.placeMove(getAIMoveMinimax(), 1);

			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI 1 Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("AI 2 Wins!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
			// the AI 2 Plays
			b.placeMove(getAIMoveMinimax(), 2);
			b.displayBoard();
			winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * definition de l'heuristique du jeu
	 * 
	 * @param nbPions le nombre pions alignés
	 * @return le poids associé à la feuille
	 */
	public  int heuristique(int nbPions) {
		int score = 0;
		if (nbPions > 4) {
			nbPions = 4;
		}

		switch (nbPions) {
		case 0:
			score = 0;
			break;
		case 1:
			score = 1;
			break;
		case 2:
			score = 50;
			break;
		case 3:
			score = 150;
			break;
		case 4:
			score = 5000;
			break;
		default:
			break;
		}

		return score;
	}

	/**
	 * <i>La fonction d'évaluation du jeu</i>
	 * 
	 * 
	 * @param joueur  <i>Idendifiant joueur 1</i>
	 * 
	 * @param joueur2 <i>Identifiant joueur 2</i>
	 * 
	 * @return <i>La fonction d'évéluation</i>
	 */
	public int fonctionEvaluation(int joueur, int joueur2) {

		int j1 = score(joueur);
		int j2 = score(joueur2);
		if (j1 > 5000)
			j1 = 5000;
		if (j2 > 5000)
			j2 = 5000;

		return (j1 - j2);
	}

	/**
	 * 
	 * @param cas    la case de la grille
	 * @param joueur le joueur
	 * @return le nbre de pions alignés verticalement
	 */
	public int verticalWinCheck(Case cas, int joueur) {
		boolean trouve = false;
		int nbPions = 0;

		if (b.board[cas.getLigne()][cas.getColonne()] == joueur)
			nbPions = 1;
		if (cas.getLigne() > 0) {
			for (int i = cas.getLigne(); i > 0 & !trouve; i--) {
				if (b.board[i - 1][cas.getColonne()] == joueur)
					nbPions++;
				else
					trouve = true;
			}
		}

		return nbPions;
	}

	/**
	 * 
	 * @param cas    la case de la grille
	 * @param joueur le joueur
	 * @return le nombre de pions aligné horizontalement
	 */

	public int horizontalWinCheck(Case cas, int joueur) {
		boolean trouve = false;
		int nbPions = 0;
		if (b.board[cas.getLigne()][cas.getColonne()] == joueur)
			++nbPions;
		if (cas.getColonne() < 6) {
			for (int i = cas.getColonne(); i < 6 & !trouve; i++) {

				if (b.board[cas.getLigne()][i + 1] == joueur)
					nbPions++;
				else
					trouve = true;
			}
		}

		return nbPions;
	}

	/**
	 * 
	 * @param cas    la case de la grille
	 * @param joueur le joueur courant
	 * @return le nbre de pions aligné en diagonale droite
	 */
	public int righDiagonalWinCheck(Case cas, int joueur) {

		boolean trouve = false;
		int nbPions = 0;

		if (b.board[cas.getLigne()][cas.getColonne()] == joueur)
			++nbPions;
		if (cas.getLigne() > 0 & cas.getColonne() < 6) {
			int y = 1;

			for (int i = cas.getLigne(); i > 0 & !trouve; i--) {

				if (cas.getColonne() + y < 7) {
					if (b.board[i - 1][cas.getColonne() + y] == joueur)
						nbPions++;
					else
						trouve = true;
					y++;
				}
			}
		}
		return nbPions;
	}

	/**
	 * 
	 * @param cas    la case de la grille
	 * @param joueur le joueur courant
	 * @return le nbre de pions aligné en diagonale gauche
	 */
	public int leftDiagonalWinCheck(Case cas, int joueur) {

		boolean trouve = false;
		int nbPions = 0;

		if (b.board[cas.getLigne()][cas.getColonne()] == joueur)
			nbPions = 1;
		if (cas.getColonne() > 0 & cas.getLigne() > 0) {
			int j = 1;

			for (int i = cas.getColonne(); i > 0 & !trouve; i--) {
				if (cas.getLigne() - j >= 0) {

					if (b.board[cas.getLigne() - j][i - 1] == joueur)
						nbPions++;
					else
						trouve = true;
					j++;
				}
			}
		}
		return nbPions;
	}

	/**
	 * Calcule le score d'un joueur (aide pour minimax)
	 * 
	 * @param joueur le joueur
	 * @return
	 */
	public int score(int joueur) {

		int score = 0;

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				if (b.board[i][j] == joueur) {

					Case cas = new Case(i, j);
					score += heuristique(verticalWinCheck(cas, joueur)) + heuristique(horizontalWinCheck(cas, joueur))
							+ heuristique(righDiagonalWinCheck(cas, joueur))
							+ heuristique(leftDiagonalWinCheck(cas, joueur));

				}

			}
		}

		return score;
	}

	/**
	 * human plays against ai
	 */
	public void playAgainstAIConsole2() {
		int humanMove = -1;
		Scanner scan = new Scanner(System.in);
		b.displayBoard();
		System.out.println("Would you like to play first ? Type (yes/no) ");
		String answer = scan.next().trim();

		if (answer.equalsIgnoreCase("yes"))
			letOpponentMove();
		b.displayBoard();
		b.placeMove(3, 1);
		b.displayBoard();

		while (true) {
			letOpponentMove();
			b.displayBoard();

			int winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}

			b.placeMove(getAIMoveSimpleMinimax(), 1);
			b.displayBoard();
			winner = checkWinner(b);
			if (winner == 1) {
				System.out.println("AI Wins!");
				break;
			} else if (winner == 2) {
				System.out.println("You Win!");
				break;
			} else if (winner == 0) {
				System.out.println("Draw!");
				break;
			}
		}

	}

	/**
	 * 
	 * @return the next column to play at for AI
	 */
	private int getAIMoveSimpleMinimax() {
		nextMoveLocation = -1;
		minimax(0, 1);
		return nextMoveLocation;

	}
}
