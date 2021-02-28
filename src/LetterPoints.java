public enum LetterPoints {
	A(1, 'A'),
	Ą(5, 'Ą'),
	B(3, 'B'),
	C(2, 'C'),
	Ć(6, 'Ć'),
	D(2, 'D'),
	E(1, 'E'),
	Ę(5, 'Ę'),
	F(5, 'F'),
	G(3, 'G'),
	H(3, 'H'),
	I(1, 'I'),
	J(3, 'J'),
	K(2, 'K'),
	L(2, 'L'),
	Ł(3, 'Ł'),
	M(2, 'M'),
	N(1, 'N'),
	Ń(7, 'Ń'),
	O(1, 'O'),
	Ó(5, 'Ó'),
	P(2, 'P'),
	R(1, 'R'),
	S(1, 'S'),
	Ś(5, 'Ś'),
	T(2, 'T'),
	U(3, 'U'),
	W(1, 'W'),
	Y(2, 'Y'),
	Z(1, 'Z'),
	Ź(9, 'Ź'),
	Ż(5, 'Ż');

	public final Integer value;
	public final Character letter;

	LetterPoints(final int value, final char letter) {
		this.value = value;
		this.letter = letter;
	}

	public static int fromLetter(final Character c) {
		for (final LetterPoints letters : LetterPoints.values()) {

			if (Character.toLowerCase(letters.letter) == Character.toLowerCase(c)) {
				return letters.value;
			}
		}
		return 0;
	}
}
