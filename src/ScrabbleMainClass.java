import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class ScrabbleMainClass extends JFrame implements ActionListener {

	//wszystkie litery
	@Getter
	public static Map<Character, Integer> letters = Utils.fillLettersCount();

	//todo dorobic jokery
	@Getter
	private static int letterCount = 98;

	JPanel titlePanel;
	JPanel menuPanel;
	JPanel gamePanel;

	JLabel titleLabel;
	JLabel menuTitleLabel;
	JLabel lettersLabel;
	JLabel displayLetters;
	JLabel pointsLabel;

	//plansza
	JButton[][] buttons = new JButton[15][15];

	//przyciski po prawej stronie (menu)
	JButton changeLetters;
	JButton done;
	JButton wait;
	JButton undo;
	JButton cancel;

	@Getter
	private final Player player1;
	@Getter
	private final Player player2;

	private boolean player1Move = true;

	//aktualne litery gracza
	private java.util.List<Character> curr_letters;
	//uzyte litery przez gracza w danym ruchu
	private final java.util.List<Character> used_letters = new LinkedList<>();
	//kordynaty i,j glownego slowa ulozonego przez gracza
	private final Integer[][] curr_buttons = new Integer[7][2];

	private boolean first_move = true;

	public ScrabbleMainClass() {

		final ImageIcon icon = new ImageIcon("logo.png");
		this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1100, 980);
		this.setLayout(new BorderLayout());
		this.setTitle("Scrabble");


		player1 = new Player("Gracz 1");
		player2 = new Player("Gracz 2");
		System.out.println(letters); //litery przed rozdaniem

		//na poczatku 7 liter dla kazdego
		player1.setLetters(Utils.drawLetters(7, player1));
		player2.setLetters(Utils.drawLetters(7, player2));

		System.out.println(player1);
		System.out.println(player2);
		System.out.println(letters); //litery po rozdaniu


		titleLabel = new JLabel();
		titleLabel.setText("Scrabble        ");
		titleLabel.setForeground(new Color(10, 200, 100)); //kolor tekstu
		titleLabel.setFont(new Font("MV Boli", Font.PLAIN, 50)); //czcionka, rozmiar
		titleLabel.setVerticalAlignment(JLabel.CENTER); //ustawia  pionowe wyrownanie do gory
		titleLabel.setHorizontalAlignment(JLabel.CENTER); //ustawia poziome wyrownanie
		//label.setBounds(100, 100,250,250);

		titlePanel = new JPanel();
		titlePanel.setBackground(new Color(50, 50, 50));
		titlePanel.setPreferredSize(new Dimension(900, 80));
		titlePanel.add(titleLabel);

		menuTitleLabel = new JLabel();
		menuTitleLabel.setText(player1.getName());
		menuTitleLabel.setForeground(new Color(20, 20, 20));
		menuTitleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 50));
		menuTitleLabel.setHorizontalAlignment(JLabel.CENTER); //ustawia poziome wyrownanie
		menuTitleLabel.setVerticalAlignment(JLabel.TOP); //ustawia  pionowe wyrownanie do gory

		lettersLabel = new JLabel();
		lettersLabel.setText("Twoje litery:");
		lettersLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 25));
		lettersLabel.setForeground(new Color(20, 20, 20));
		lettersLabel.setVerticalAlignment(JLabel.TOP);
		lettersLabel.setHorizontalAlignment(JLabel.CENTER);

		displayLetters = new JLabel();


		displayLetters.setText(displayLetters(player1));
		displayLetters.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		displayLetters.setForeground(new Color(20, 20, 20));
		displayLetters.setVerticalAlignment(JLabel.TOP);
		displayLetters.setHorizontalAlignment(JLabel.CENTER);
		//todo problem z tym, ze jak jest malo liter, to przycisk sie przesuwa tam...
		displayLetters.setHorizontalTextPosition(JLabel.CENTER);

		done = new JButton();
		done.setText("Zrobione");
		done.setFocusable(false);
		done.addActionListener(this);
		done.setBounds(new Rectangle(80, 40));
		done.setPreferredSize(new Dimension(170, 30));

		wait = new JButton();
		wait.setBounds(new Rectangle(80, 40));
		wait.setText("Czekaj");
		wait.setFocusable(false);
		wait.addActionListener(this);
		wait.setPreferredSize(new Dimension(170, 30));

		changeLetters = new JButton();
		changeLetters.setText("Wymien");
		changeLetters.setFocusable(false);
		changeLetters.addActionListener(this);
		changeLetters.setBounds(new Rectangle(80, 40));
		changeLetters.setPreferredSize(new Dimension(170, 30));

		undo = new JButton();
		undo.setText("Cofnij");
		undo.setFocusable(false);
		undo.addActionListener(this);
		undo.setBounds(new Rectangle(150, 40));
		undo.setPreferredSize(new Dimension(170, 30));


		cancel = new JButton();
		cancel.setText("Anuluj");
		cancel.setFocusable(false);
		cancel.addActionListener(this);
		cancel.setBounds(new Rectangle(150, 40));
		cancel.setPreferredSize(new Dimension(170, 30));

		pointsLabel = new JLabel();
		pointsLabel.setText("Twoje punkty: 0");
		pointsLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		pointsLabel.setForeground(new Color(20, 20, 20));
		pointsLabel.setVerticalAlignment(JLabel.TOP);
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);

		menuPanel = new JPanel();
		menuPanel.setBackground(new Color(100, 100, 100));
		menuPanel.setPreferredSize(new Dimension(200, 1000));
		//menuPanel.setLayout(null);
		menuPanel.add(menuTitleLabel);
		menuPanel.add(lettersLabel);
		menuPanel.add(displayLetters);

		menuPanel.add(done);
		menuPanel.add(wait);
		menuPanel.add(changeLetters);
		menuPanel.add(undo);
		menuPanel.add(cancel);

		menuPanel.add(pointsLabel);


		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(000, 50, 000));
		gamePanel.setPreferredSize(new Dimension(900, 900));
		gamePanel.setLayout(new GridLayout(15, 15));

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setFocusable(false);
				buttons[i][j].addActionListener(this);
				buttons[i][j].setFont(new Font("Comic Sans MS", Font.BOLD, 30));

				drawButtons(i, j);

				gamePanel.add(buttons[i][j]);
			}
		}

		this.add(titlePanel, BorderLayout.NORTH);
		this.add(menuPanel, BorderLayout.EAST);
		this.add(gamePanel);

		this.setVisible(true);

		start();
	}


	@Override
	public void actionPerformed(final ActionEvent e) {

		if (e.getSource() == wait) {
			if (used_letters.size() > 0)
				JOptionPane.showMessageDialog(this, "Nie mozesz czekac, kiedy zaczales juz ukladac!");
			else
				nextPlayerHelper();

		} else if (e.getSource() == changeLetters) {
			if (used_letters.size() > 0) {
				JOptionPane.showMessageDialog(this, "Nie mozesz wymieniac, kiedy zaczales juz ukladac!");
				return;
			}
			//todo najpierw curr_letters = Utis.draw..., a pote4m musimy oddac aktualne litery gracza do puli liter!
			System.out.println("LETTERS BEFORE: " + letters);
			for (final Character c : getActPlayer().getLetters()) {
				letters.put(c, letters.get(c) + 1);
				letterCount++;
			}
			getActPlayer().clearLetters();
			//TODO zaimplementowac pozniej mozliwosc wymiany mniej niz 7 liter
			curr_letters = Utils.drawLetters(7, getActPlayer());
			nextPlayerHelper();
			System.out.println("Ilosc liter: " + letterCount);
			System.out.println("Mapa liter: (AFTER)" + letters);

		} else if (e.getSource() == done) {
			//todo all safety checks

			if (used_letters.size() == 0) {
				JOptionPane.showMessageDialog(this, "Musisz uzyc co najmniej jednej litery, albo zaczekac");

			} else {
				nextPlayerHelper();
				System.out.println("Ilosc liter: " + letterCount);
				System.out.println("Mapa liter: " + letters);
			}

		} else if (e.getSource() == undo) {
			final int i = used_letters.size() - 1;
			if (i >= 0)
				undo(i);

			//anulowanie ruchu - postawionych liter
		} else if (e.getSource() == cancel) {
			System.out.println("dlugosc: " + curr_buttons.length);
			System.out.println("Dlugosc uzytych: " + used_letters.size());
			//for (int i = 0; i < used_letters.size(); i++)
			//undo(i);

			for (int i = 0; i < curr_buttons.length; i++) {
				if (curr_buttons[i][0] == null) {
					break;
				}
				undo(i);
			}
		}

		//nacisnelismy na plansze
		check:
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (e.getSource() == buttons[i][j]) {
					if (player1Move) {
						//wpisuje litere w pole
						putLetter(i, j, player1);
					} else {
						//new HelpWindow(player2);
						putLetter(i, j, player2);
					}
					break check;
				}
			}
		}
	}

	private void putLetter(final int i, final int j, final Player player) {
		if (!buttons[i][j].getText().equals(""))
			return;

		final String letter = JOptionPane.showInputDialog("Podaj litere, dostępne: " + displayLetters(curr_letters));

		System.out.println(player.getLetters() + " <-- Litery ");
		System.out.println("i " + i + ", j" + j);


		if (letter == null || letter.length() != 1) {
			JOptionPane.showMessageDialog(this, "Wprowadz pojedyncza litere!");
			return;
		}
		//todo nie dziala poprawne wyswietlanie jak juz kilka liter sie usunie, ale mozna to w razie czego obejsc dodajac spacje do wyswietlanych liter x d

		for (final Character c : player.getLetters()) {
			if (c.equals(letter.charAt(0))) {

				buttons[i][j].setText(letter.toLowerCase(Locale.ROOT));

				curr_buttons[used_letters.size()][0] = i;
				curr_buttons[used_letters.size()][1] = j;

				curr_letters.remove(c);
				used_letters.add(c);

				displayLetters.setText(displayLetters(curr_letters));

				System.out.println("Uzyte litery: " + used_letters);
				System.out.println("Aktualne litery: " + curr_letters);
				return;
			}
		}
		JOptionPane.showMessageDialog(this, "Nie masz tej litery!");
	}

	//metoda potrzebna do cofania ruchow
	private void undo(final int i) {
		final Character c = buttons[curr_buttons[i][0]][curr_buttons[i][1]].getText().charAt(0);

		curr_letters.add(c);
		used_letters.remove(c);
		buttons[curr_buttons[i][0]][curr_buttons[i][1]].setText("");
		displayLetters.setText(displayLetters(curr_letters));
		curr_buttons[i][0] = null;
		curr_buttons[i][1] = null;

	}

	public Player getActPlayer() {
		return player1Move ? player1 : player2;
	}

	private String displayLetters(final Player player) {
		final StringBuilder letters = new StringBuilder();
		for (final Character c : player.getLetters()) {
			letters.append(c).append(", ");
		}
		return player.getLetters().size() == 0 ? "" : letters.substring(0, letters.length() - 2);
	}

	private String displayLetters(final List<Character> letters) {
		final StringBuilder str_letters = new StringBuilder();
		for (final Character c : letters) {
			str_letters.append(c).append(", ");
		}
		return letters.size() == 0 ? "" : str_letters.substring(0, str_letters.length() - 2);
	}

	private void start() {
		curr_letters = player1.getLetters();
	}

	private void nextPlayerHelper() {
		//List<Character> tempLetters = Utils.drawLetters(used_letters.size());
		System.out.println(curr_letters);

		//to znaczy, ze gracz czekal / wymienil
		if (used_letters.size() == 0) {
			System.out.println("CZEKANIE / WYMIAnA");
			if (player1Move) {
				player1Move = false;

				displayLetters.setText(displayLetters(player2));
				menuTitleLabel.setText(player2.getName());
				player1.setLetters(curr_letters);
				pointsLabel.setText("Twoje punkty: " + player2.getPoints());

				curr_letters = player2.getLetters();

			} else {
				player1Move = true;

				displayLetters.setText(displayLetters(player1));
				menuTitleLabel.setText(player1.getName());
				player2.setLetters(curr_letters);
				pointsLabel.setText("Twoje punkty: " + player1.getPoints());

				curr_letters = player1.getLetters();
			}

			for (int i = 0; i < 7; i++) {
				if (curr_buttons[i][0] != null) {
					curr_buttons[i][0] = null;
					curr_buttons[i][1] = null;
				} else
					break;
			}

			used_letters.clear();

			return;
		}

		//safety check + test joptionpane
		if (!(validateInput(curr_buttons) && (wordSticks(curr_buttons) || first_move) && validateInput2(curr_buttons)) || checkHorizontally(curr_buttons) == -1) {
			//final UIManager UI = new UIManager();
			//UI.put("OptionPane.background", Color.lightGray);
			//UI.put("Panel.background", Color.lightGray);
			//UI.put("OptionPane.messageForeground", Color.black);

			JOptionPane.showMessageDialog(this, "Ułóż słowoo w poprawny sposób i spróbuj ponownie!",
					"Info", JOptionPane.WARNING_MESSAGE);

			return;
		}

		//safety check
		if (first_move && used_letters.size() == 1) {
			JOptionPane.showMessageDialog(this, "Nie możesz ułożyc jednoliterowego słowa na początku gry!");
			return;
		}

		//sprawdzamy czy pierwsze slowo przechodzi przez srodek
		if (first_move) {
			boolean valid = false;

			for (int i = 0; i < used_letters.size(); i++) {
				if (buttons[curr_buttons[i][0]][curr_buttons[i][1]].getBackground().equals(Utils.Constants.MID_COLOR)) {
					System.out.println("jest na srodku");
					valid = true;
				}
			}
			if (!valid) {
				JOptionPane.showMessageDialog(this, "Twoj wyraz musi przechodzic przez srodek!");
				return;
			}
		}

		final List<Word> allWords = findAllWords(curr_buttons);
		//to oznacza, ze tez jest blad w w wyrazei!
		if (allWords.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ułóż słowo w poprawny sposób i spróbuj ponownie!",
					"Info", JOptionPane.WARNING_MESSAGE);
			return;
		}
		System.out.println(allWords);

		int tempPoints = 0;


		for (final Word word : allWords) {
			tempPoints += word.calculatePoints();
		}

		if (player1Move) {
			nextPlayerHelper(player1, tempPoints);

			player1.addWord(allWords.get(0));

			player1Move = false;
			displayLetters.setText(displayLetters(player2));
			menuTitleLabel.setText(player2.getName());
			player1.setLetters(curr_letters);

			pointsLabel.setText("Twoje punkty: " + player2.getPoints());


			System.out.println(player1.getPoints() + "<--- punkty gracza 1");
			System.out.println(player1.getPlayersWords() + "<--- wyrazy gracza 1");

			curr_letters = player2.getLetters();

		} else {
			nextPlayerHelper(player2, tempPoints);

			player2.addWord(allWords.get(0));

			player1Move = true;
			displayLetters.setText(displayLetters(player1));
			menuTitleLabel.setText(player1.getName());
			player2.setLetters(curr_letters);

			pointsLabel.setText("Twoje punkty: " + player1.getPoints());

			System.out.println(player2.getPoints() + "<--- punkty gracza 2");
			System.out.println(player2.getPlayersWords() + "<--- wyrazy gracza 2");

			curr_letters = player1.getLetters();
		}

		for (int i = 0; i < 7; i++) {
			if (curr_buttons[i][0] != null) {
				curr_buttons[i][0] = null;
				curr_buttons[i][1] = null;
			} else
				break;
		}

		first_move = false;
		used_letters.clear();
	}

	//byl ruch tego gracza ktory jest podawany przez parametr
	private void nextPlayerHelper(final Player player, final int points) {
		//losowanie nowych liter
		curr_letters.addAll(Utils.drawLetters(used_letters.size(), player));

		//dodawanie punktow zoabczycy czy dziala ;pp
		//player.increasePoints(addPoints(used_letters, curr_buttons) + addOtherPoints(curr_buttons));
		player.increasePoints(points);

		if (curr_letters.size() == 0) {
			final Player winner = (player1.getPoints() > player2.getPoints() ? player1 : player2);
			JOptionPane.showMessageDialog(this, "Wygral " + winner.getName() + ". Jego" +
					" punkty: " + winner.getPoints() + ". Przewaga: " + Math.abs(player1.getPoints() - player2.getPoints()));
			return;
		}
	}

	public static void decreaseLetters() {
		letterCount--;
	}

	//sprawdzamy czy input jest ok
	private boolean validateInput(final Integer[][] curr_buttons) {
		final boolean try1;
		final boolean try2;

		try1 = inputTry(0, curr_buttons);

		try2 = inputTry(1, curr_buttons);
		return try1 | try2;
	}

	//sprawdzamy czyu input jest ok
	private boolean validateInput2(final Integer[][] curr_buttons) {
		boolean try1 = true;
		boolean try2 = true;

		final int i = curr_buttons[0][0];
		for (int x = 0; x < 7; x++) {
			if (curr_buttons[x][0] == null) {
				break;
			}
			if (i != curr_buttons[x][0]) {
				try1 = false;
				break;
			}
		}
		final int j = curr_buttons[0][1];
		for (int x = 0; x < 7; x++) {
			if (curr_buttons[x][1] == null) {
				break;
			}
			if (j != curr_buttons[x][1]) {
				try2 = false;
				break;
			}
		}
		return try1 | try2;
	}

	//metoda sprawdzajaca, czy wyraz nie ma zadnych przerw.
	//second - albo i albo j, 0 - i, 1 - j;
	private boolean inputTry(final int second, final Integer[][] buttons) {
		final List<Integer> area = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			//to oznacza, ze slowo jest krotsze niz 7 luter i na koncu talbicy jest null
			if (buttons[i][second] == null) {
				break;
			}
			area.add(buttons[i][second]);
		}

		area.sort(Comparator.comparingInt(o -> o));

		for (int i = 0; i < area.size() - 1; i++) {

			if (area.get(i + 1) - area.get(i) != 1) {
				//to znaczy, ze jest pusta przestrzen w ukladanym slowie
				//if (this.buttons[buttons[area.get(i)][0]][buttons[area.get(i)][1]].getText().equals("")) {
				if (second == 0) {
					final int area_j = buttons[0][1];

					if (isWithinBounds(area.get(i) + 1) && this.buttons[area.get(i) + 1][area_j].getText().equals("")) {
						//ISTA PO POSORTOWANIU: [4, 6] --musimy sprawdzac piatke i czy jest tam jakas litera
						return false;
					}

				} else {
					//second == 1, czyli sprawdzamy joty
					final int area_i = buttons[0][0];
					if (isWithinBounds(area.get(i) + 1) && this.buttons[area_i][area.get(i) + 1].getText().equals("")) {
						//ISTA PO POSORTOWANIU: [4, 6] --musimy sprawdzac piatke i czy jest tam jakas litera
						return false;
					}
				}
			}
		}
		return true;
	}

	//sprawdzamy czy slowo sie trzyma czegos
	private boolean wordSticks(final Integer[][] buttons) {

		//tylko jedna litera
		if (buttons[1][0] == null) {

			final int i = buttons[0][0];
			final int j = buttons[0][1];

			if (isWithinBounds(i + 1) && !this.buttons[i + 1][j].getText().equals(""))
				return true;

			if (isWithinBounds(i - 1) && !this.buttons[i - 1][j].getText().equals(""))
				return true;

			if (isWithinBounds(j + 1) && !this.buttons[i][j + 1].getText().equals(""))
				return true;

			if (isWithinBounds(j - 1) && !this.buttons[i][j - 1].getText().equals(""))
				return true;

			return false;
		}

		final boolean horizontally = Math.abs(buttons[0][0] - buttons[1][0]) == 0;

		final List<Integer> button_cordinates = addCordinates(buttons, horizontally);

		button_cordinates.sort(Comparator.comparingInt(o -> o));
		//sortujemy liste przyciskow

		boolean try1 = false;

		//sprawdzamy, czy nasze slowo nie dopasowalo sie do jakiejs litery juz
		for (int i = 0; i < button_cordinates.size() - 1; i++) {
			if (button_cordinates.get(i + 1) - button_cordinates.get(i) != 1) {
				//to znaczy, ze jest pusta przestrzen w ukladanym slowie
				//if (this.buttons[buttons[area.get(i)][0]][buttons[area.get(i)][1]].getText().equals("")) {
				if (!horizontally) {
					final int area_j = buttons[0][1];

					if (isWithinBounds(button_cordinates.get(i) + 1) && !this.buttons[button_cordinates.get(i) + 1][area_j].getText().equals("")) {
						//ISTA PO POSORTOWANIU: [4, 6] --musimy sprawdzac piatke i czy jest tam jakas litera
						try1 = true;
					}

				} else {
					final int area_i = buttons[0][0];
					if (isWithinBounds(button_cordinates.get(i) + 1) && !this.buttons[area_i][button_cordinates.get(i) + 1].getText().equals("")) {
						//ISTA PO POSORTOWANIU: [4, 6] --musimy sprawdzac piatke i czy jest tam jakas litera
						try1 = true;
					}
				}
			}
		}

		if (try1)
			return true;

		//poziomo jest wyraz
		if (horizontally) {
			final int i = buttons[0][0];
			for (final int j : button_cordinates) {
				if (isWithinBounds(i + 1) && !this.buttons[i + 1][j].getText().equals(""))
					return true;
				if (isWithinBounds(i - 1) && !this.buttons[i - 1][j].getText().equals(""))
					return true;
			}
			if (isWithinBounds(button_cordinates.get(0) - 1) && !this.buttons[i][button_cordinates.get(0) - 1].getText().equals("")) {
				return true;
			}
			if (isWithinBounds(button_cordinates.get(button_cordinates.size() - 1) + 1) && !this.buttons[i][button_cordinates.get(button_cordinates.size() - 1) + 1].getText().equals("")) {
				return true;
			}

			//pionowo jest wyraz
		} else {
			final int j = buttons[0][1];
			for (final int i : button_cordinates) {
				if (isWithinBounds(j + 1) && !this.buttons[i][j + 1].getText().equals(""))
					return true;
				if (isWithinBounds(j - 1) && !this.buttons[i][j - 1].getText().equals(""))
					return true;
			}
			if (isWithinBounds(button_cordinates.get(0) - 1) && !this.buttons[button_cordinates.get(0) - 1][j].getText().equals("")) {
				return true;
			}
			if (isWithinBounds(button_cordinates.get(button_cordinates.size() - 1) + 1) && !this.buttons[button_cordinates.get(button_cordinates.size() - 1) + 1][j].getText().equals("")) {
				return true;
			}
		}

		return false;
	}

	private boolean isWithinBounds(final int i) {
		return i >= 0 && i <= 14;
	}

	//metoda pomocnicza, sprawdzajaca poboczne slowa gdy glowne jest poziomo
	//i, j <- kordy literki w wyrazie glownym
	private Word checkHorizontally(final int i, final int j) {
		final List<Character> word = new LinkedList<>();
		char letter = this.buttons[i][j].getText().charAt(0);

		if (isWithinBounds(i + 1) && !this.buttons[i + 1][j].getText().equals("")) {
			final int tempMultiplier = multipliers(i, j);
			int tempPoints = letterPoints(i, j, letter);
			word.add(letter);
			int temp = i + 1;

			while (isWithinBounds(temp) && !this.buttons[temp][j].getText().equals("")) {
				letter = this.buttons[temp][j].getText().charAt(0);
				word.add(letter);
				tempPoints += LetterPoints.fromLetter(letter);
				System.out.println("dodanie punktow w petli: " + LetterPoints.fromLetter(letter));
				temp++;
				//oznacza, ze dalej mamy literke...
			}

			if (isWithinBounds(i - 1) && !this.buttons[i - 1][j].getText().equals("")) {
				temp = i - 1;
				while (isWithinBounds(temp) && !this.buttons[temp][j].getText().equals("")) {
					letter = this.buttons[temp][j].getText().charAt(0);
					word.add(letter);
					tempPoints += LetterPoints.fromLetter(letter);
					System.out.println("dodanie punktow w petli: " + LetterPoints.fromLetter(letter));
					temp--;
				}
			}
			System.out.println("Slowo: " + createWord(word));
			System.out.println("Punkty: " + tempPoints);
			System.out.println("Mnozniki: " + tempMultiplier);
			return new Word(createWord(word), tempPoints, tempMultiplier);

		} else if (isWithinBounds(i - 1) && !this.buttons[i - 1][j].getText().equals("")) {
			final int tempMultiplier = multipliers(i, j);
			int tempPoints = letterPoints(i, j, letter);
			word.add(letter);

			//w tej petli zmniejszamy i, bo idziemy w gore
			int temp = i - 1;
			while (isWithinBounds(temp) && !this.buttons[temp][j].getText().equals("")) {
				letter = this.buttons[temp][j].getText().charAt(0);
				word.add(letter);
				tempPoints += LetterPoints.fromLetter(letter);
				temp--;
				//oznacza, ze dalej mamy literke...
			}
			System.out.println("Slowo: " + createWord(word));
			System.out.println("Punkty: " + tempPoints);
			System.out.println("Mnozniki: " + tempMultiplier);
			return new Word(createWord(word), tempPoints, tempMultiplier);
		}

		return null;
	}

	//metoda pomocnicza, sprawdzajaca poboczne slowa gdy glowne jest pionowo
	//i, j <- kordy literki w wyrazie glownym

	private Word checkVertically(final int i, final int j) {
		final List<Character> word = new LinkedList<>();
		char letter = this.buttons[i][j].getText().charAt(0);

		if (isWithinBounds(j + 1) && !this.buttons[i][j + 1].getText().equals("")) {
			final int tempMultiplier = multipliers(i, j);
			int tempPoints = letterPoints(i, j, letter);
			word.add(letter);
			int temp = j + 1;

			while (isWithinBounds(temp) && !this.buttons[i][temp].getText().equals("")) {
				letter = this.buttons[i][temp].getText().charAt(0);
				word.add(letter);
				tempPoints += LetterPoints.fromLetter(letter);
				System.out.println("dodanie punktow w petli: " + LetterPoints.fromLetter(letter));
				temp++;
				//oznacza, ze dalej mamy literke...
			}

			if (isWithinBounds(j - 1) && !this.buttons[i][j - 1].getText().equals("")) {
				temp = j - 1;
				while (isWithinBounds(temp) && !this.buttons[i][temp].getText().equals("")) {
					letter = this.buttons[i][temp].getText().charAt(0);
					word.add(letter);
					tempPoints += LetterPoints.fromLetter(letter);
					System.out.println("dodanie punktow w petli: " + LetterPoints.fromLetter(letter));
					temp--;
				}
			}
			System.out.println("Slowo: " + createWord(word));
			System.out.println("Punkty: " + tempPoints);
			System.out.println("Mnozniki: " + tempMultiplier);
			return new Word(createWord(word), tempPoints, tempMultiplier);

		} else if (isWithinBounds(j - 1) && !this.buttons[i][j - 1].getText().equals("")) {
			final int tempMultiplier = multipliers(i, j);
			int tempPoints = letterPoints(i, j, letter);
			word.add(letter);

			//w tej petli zmniejszamy i, bo idziemy w gore
			int temp = j - 1;
			while (isWithinBounds(temp) && !this.buttons[i][temp].getText().equals("")) {
				letter = this.buttons[i][temp].getText().charAt(0);
				word.add(letter);
				tempPoints += LetterPoints.fromLetter(letter);
				temp--;
				//oznacza, ze dalej mamy literke...
			}
			System.out.println("Slowo: " + createWord(word));
			System.out.println("Punkty: " + tempPoints);
			System.out.println("Mnozniki: " + tempMultiplier);
			return new Word(createWord(word), tempPoints, tempMultiplier);
		}
		return null;
	}

	//metoda znajdujaca wszystkei slowa ulozone w danym ruchu.
	private List<Word> findAllWords(final Integer[][] curr_buttons) {
		final List<Word> lista = new ArrayList<>();

		//to oznacza, ze dokladamy tylko jedna litere!
		if (curr_buttons[1][0] == null) {
			final int constI = curr_buttons[0][0];
			final int constJ = curr_buttons[0][1];

			final Word horizontally = checkHorizontally(constI, constJ);
			final Word vertically = checkVertically(constI, constJ);

			if (horizontally != null)
				lista.add(horizontally);

			if (vertically != null)
				lista.add(vertically);

			return lista;
		}

		final int validation = checkHorizontally(curr_buttons);
		//(raczej niemozliwe, przed wywolaniem tej metody powinnismy sprawdzic czy wyraz jest poprawnie ulozony)
		if (validation == -1) {
			JOptionPane.showMessageDialog(this, "Blad w tescie... Wyraz nie jest ani poziomo ani pionowo");
		}
		//horizontally to 0, a vertically(pionowo) to 1
		final boolean horizontally = validation == 0;
		final List<Integer> cords = new ArrayList<>();

		//dodajemy za kazdym razem literke do slowa.
		final List<Character> word = new LinkedList<>();

		int points = 0;
		int multiplier = 1;

		if (horizontally) {
			final int constI = curr_buttons[0][0];

			//tylko j sie zmienia, i jest takie samo!
			for (int i = 0; i < 7; i++) {

				//wyraz sie skonczyl
				if (curr_buttons[i][0] == null)
					break;

				//dodajemy do listy kordynaty (tylko j)
				cords.add(curr_buttons[i][1]);
			}
			//sortujemy je na wszelki wypadek
			cords.sort(Comparator.comparingInt(o -> o));

			//czy przed slowem jest jakas litera?
			if (isWithinBounds(cords.get(0) - 1) && !this.buttons[constI][cords.get(0) - 1].getText().equals("")) {
				final char letter = this.buttons[constI][cords.get(0) - 1].getText().charAt(0);
				word.add(letter);
				points += LetterPoints.fromLetter(letter);
			}


			for (int i = 0; i < cords.size(); i++) {

				char letter = this.buttons[constI][cords.get(i)].getText().charAt(0);

				word.add(letter);

				multiplier *= multipliers(constI, cords.get(i));
				points += letterPoints(constI, cords.get(i), letter);

				//to oznacza, ze pomiedzy nastepnymi elementami jest przerwa, czyli raczej musi byc tam inny wyraz
				//sprawdzam warunek i <c ords.size() zeby nie wyjsc za indexy listy
				if (i < cords.size() - 1 && cords.get(i + 1) - cords.get(i) != 1) {

					for (int j = 1; j < cords.get(i + 1) - cords.get(i); j++) {
						final String s = this.buttons[constI][cords.get(i) + j].getText();

						if (s.isEmpty()) {
							JOptionPane.showMessageDialog(this, "Wyraz jest zle ulozony... Pomiedzyu literalmi brakuje lacznika ? ");
							return new ArrayList<>();
						}

						//dodajemy literke
						letter = s.charAt(0);
						word.add(letter);
						points += LetterPoints.fromLetter(letter);
					}

					//wszystko jest ok (a przynajmniej powinno)
				}

			}

			//czy za slowem jest jakas litera?
			if (isWithinBounds(cords.get(cords.size() - 1) + 1) && !this.buttons[constI][cords.get(cords.size() - 1) + 1].getText().equals("")) {
				final char letter = this.buttons[constI][cords.get(cords.size() - 1) + 1].getText().charAt(0);
				word.add(letter);
				points += LetterPoints.fromLetter(letter);
			}


			//to oznacza, ze wyraz jest pionowo.
		} else {
			final int constJ = curr_buttons[0][1];

			//tylko i sie zmienia, j jest takie samo!
			for (int i = 0; i < 7; i++) {

				//wyraz sie skonczyl
				if (curr_buttons[i][0] == null)
					break;

				//dodajemy do listy kordynaty (tylko i)
				cords.add(curr_buttons[i][0]);
			}
			//sortujemy je na wszelki wypadek
			cords.sort(Comparator.comparingInt(o -> o));

			//czy przed slowem jest jakas litera
			if (isWithinBounds(cords.get(0) - 1) && !this.buttons[cords.get(0) - 1][constJ].getText().equals("")) {
				final char letter = this.buttons[cords.get(0) - 1][constJ].getText().charAt(0);
				word.add(letter);
				points += LetterPoints.fromLetter(letter);
			}


			for (int i = 0; i < cords.size(); i++) {

				char letter = this.buttons[cords.get(i)][constJ].getText().charAt(0);

				word.add(letter);

				multiplier *= multipliers(cords.get(i), constJ);
				points += letterPoints(cords.get(i), constJ, letter);

				//to oznacza, ze pomiedzy nastepnymi elementami jest przerwa, czyli raczej musi byc tam inny wyraz
				//sprawdzam warunek i <c ords.size() zeby nie wyjsc za indexy listy
				if (i < cords.size() - 1 && cords.get(i + 1) - cords.get(i) != 1) {

					for (int j = 1; j < cords.get(i + 1) - cords.get(i); j++) {
						final String s = this.buttons[cords.get(i) + j][constJ].getText();

						if (s.isEmpty()) {
							JOptionPane.showMessageDialog(this, "Wyraz jest zle ulozony... Pomiedzyu literalmi brakuje lacznika ? ");
							break;
						}

						//dodajemy literke
						letter = s.charAt(0);
						word.add(letter);
						points += LetterPoints.fromLetter(letter);
					}

					//wszystko jest ok (a przynajmniej powinno)
				}

			}

			//czy za slowem jest jakas litera
			if (isWithinBounds(cords.get(cords.size() - 1) + 1) && !this.buttons[cords.get(cords.size() - 1) + 1][constJ].getText().equals("")) {
				final char letter = this.buttons[cords.get(cords.size() - 1) + 1][constJ].getText().charAt(0);
				word.add(letter);
				points += LetterPoints.fromLetter(letter);
			}

		}

		lista.add(new Word(createWord(word), points, multiplier, used_letters.size() == 7));

		System.out.println("Slowo: " + createWord(word));
		System.out.println("Punkty: " + points);
		System.out.println("Mnozniki: " + multiplier);

		word.clear();

		//dziala poki co tylko glowne slowo. teraz musimy zbadac poboczne.
		//slowo jest POZIOMO
		if (horizontally) {
			final int constI = curr_buttons[0][0];
			for (final int j : cords)
				if (checkHorizontally(constI, j) != null)
					lista.add(checkHorizontally(constI, j));

		}
		//slowo jest PIONOWO
		else {
			final int constJ = curr_buttons[0][1];
			for (final int i : cords)
				if (checkVertically(i, constJ) != null)
					lista.add(checkVertically(i, constJ));

		}
		return lista;
	}

	//tworzy slowa z listy charow, metoda pomocnicza
	private String createWord(final List<Character> arrayWord) {
		final StringBuilder tempWord = new StringBuilder();
		for (final Character c : arrayWord)
			tempWord.append(c);

		return tempWord.toString();

	}

	//zwraca mnozniki na danym polu
	private int multipliers(final int i, final int j) {
		final Color background = this.buttons[i][j].getBackground();

		if (background.equals(Utils.Constants.MID_COLOR) || background.equals(Utils.Constants.X2_WORD))
			return 2;
		else if (background.equals(Utils.Constants.X3_WORD))
			return 3;
		else
			return 1;
	}

	//zwraca punkty litery na danym polu
	private int letterPoints(final int i, final int j, final char letter) {
		final Color background = this.buttons[i][j].getBackground();

		if (background.equals(Utils.Constants.X3_LETTER))
			return 3 * LetterPoints.fromLetter(letter);
		else if (background.equals(Utils.Constants.X2_LETTER))
			return 2 * LetterPoints.fromLetter(letter);
		else
			return LetterPoints.fromLetter(letter);
	}

	//wyraz musi miec co najmniej dwie litery!!
	private int checkHorizontally(final Integer[][] buttons) {
		//zwracamy dwa, kiedy dokladamy tylko jedna litere!
		if (buttons[1][0] == null)
			return 2;

		boolean horizontally = false;
		boolean vertically = false;
		for (int i = 0; i < used_letters.size() - 1; i++) {
			if (buttons[i + 1][0] - buttons[i][0] == 0) {
				horizontally = true;
			}
			if (buttons[i + 1][1] - buttons[i][1] == 0) {
				vertically = true;
			}
		}
		if (vertically && horizontally)
			return -1;
		if (!vertically && !horizontally)
			return -1;

		//pionowo - 1
		if (vertically)
			return 1;

		//poziomo - 0
		if (horizontally)
			return 0;

		//blad - -1
		return -1;
	}

	//metoda pomocnicza zwracajaca liste zmiennych kordynatow danego slowa
	private List<Integer> addCordinates(final Integer[][] buttons, final boolean horizontally) {
		//jezeli jest poziomo, to sprawdzamy tylko wedlug j, oraz najwyzszego i najnizszego przycisku
		final List<Integer> button_cordinates = new ArrayList<>();

		for (int i = 0; i < 7; i++) {
			if (buttons[i][0] == null)
				break;

			if (horizontally) {
				button_cordinates.add(buttons[i][1]);

			} else {
				button_cordinates.add(buttons[i][0]);
			}
		}
		return button_cordinates;
	}

	//metoda rysujaca plansze
	private void drawButtons(final int i, final int j) {
		//mid - 2x word
		if (i == 7 && j == 7)
			buttons[i][j].setBackground(Utils.Constants.MID_COLOR);

			//3x word
		else if (((j == 0 || j == 7 || j == 14) && (i == 0 || i == 14)) || ((i == 7) && (j == 0 || j == 14)))
			buttons[i][j].setBackground(Utils.Constants.X3_WORD);

			//2x word
		else if ((j == i || i == 14 - j) && (j < 5 || j > 9))
			buttons[i][j].setBackground(Utils.Constants.X2_WORD);

			//2x letter
		else if (((i == j || i == 14 - j) && (i == 6 || i == 8)))
			buttons[i][j].setBackground(Utils.Constants.X2_LETTER);

			//3x letter
		else if (((i == 5 || i == 9) && (j % 4 == 1)) || ((i % 4 == 1) && (j == 5 || j == 9)))
			buttons[i][j].setBackground(Utils.Constants.X3_LETTER);

			//2x letter
		else if (((j == 0 || j == 7 || j == 14) && (i == 3 || i == 11)) || ((i == 0 || i == 7 || i == 14) && (j == 3 || j == 11)))
			buttons[i][j].setBackground(Utils.Constants.X2_LETTER);

			//2x letter
		else if (((j == 6 || j == 8) && (i == 2 || i == 12)) || ((j == 2 || j == 12) && (i == 6 || i == 8)))
			buttons[i][j].setBackground(Utils.Constants.X2_LETTER);

			//default
		else
			buttons[i][j].setBackground(Utils.Constants.DEFAULT);

	}
}
