/**
 * スクランブラー
 */
public class Scrambler {
	/** ローターとリフレクターの文字セット */
	private String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	/** ローター */
	private Rotor[] rotors;
	/** リフレクター */
	private Reflector reflector;

	/**
	 * 標準的な英字[A-Z]が設定されたスクランブラーを生成します。
	 * 
	 * @param rotorNum ローターの個数。
	 * 0以下の場合はリフレクターのみで構成された単一換字式暗号機になります。
	 */
	public Scrambler(int rotorNum) {
		setScramblerKit(rotorNum);
	}

	/**
	 * 文字セットを基にスクランブラーを生成します。
	 * 
	 * @param rotorNum ローターの個数。
	 * 0以下の場合はリフレクターのみで構成された単一換字式暗号機になります。
	 * @param charSet 文字セット。
	 * 重複した文字が存在しない、長さが2以上かつ偶数の文字列です。
	 * 条件に合致しない文字列が指定された場合は標準的な英字[A-Z]が設定されます。
	 */
	public Scrambler(int rotorNum, String charSet) {
		if (charSet != null && charSet.length() > 0 && charSet.length() % 2 == 0)
			this.charSet = charSet;

		setScramblerKit(rotorNum);
	}

	// Char set

	/**
	 * 文字セットを取得します。
	 * 
	 * @return 文字セット。
	 */
	public String getCharSet() {
		return charSet;
	}

	/**
	 * 文字セットを設定します。
	 * 
	 * @param newCharSet 新しい文字セット。
	 * 重複した文字が存在しない、長さが2以上かつ偶数の文字列です。
	 * NULLや空の文字列が指定された場合はエラーコード1が返されます。
	 * 長さが偶数ではない場合はエラーコード2が返されます。
	 * 重複した文字が存在する場合はエラーコード3が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setCharSet(String newCharSet) { // => error code
		if (newCharSet == null || newCharSet.length() <= 0)
			return 1;

		if (newCharSet.length() % 2 != 0)
			return 2;

		if (!Utility.isNonDuplicateString(newCharSet))
			return 3;

		charSet = newCharSet;

		setScramblerKit(rotors.length);

		return 0;
	}

	// Char set pair

	/**
	 * ローターの出力文字セットを取得します。
	 * 
	 * @param rotorIndex ローターの番地。
	 * 存在しない番地が指定された場合はNULLが返されます。
	 * @return 出力文字セット。
	 */
	public String getRotorCharSetPair(int rotorIndex) {
		if (rotorIndex >= 0 && rotorIndex < rotors.length) {
			return rotors[rotorIndex].getCharSetPair();
		} else {
			return null;
		}
	}

	/**
	 * リフレクターの出力文字セットを取得します。
	 * 
	 * @return 出力文字セット。
	 */
	public String getReflectorCharSetPair() {
		return reflector.getCharSetPair();
	}

	/**
	 * ローターの出力文字セットを設定します。
	 * 
	 * @param rotorIndex ローターの番地。
	 * 存在しない番地が指定された場合はエラーコード3が返されます。
	 * @param charSetPair 新しい出力文字セット。
	 * NULLや入力文字セットと異なる長さの文字列が指定された場合はエラーコード1が返されます。
	 * 入力文字セットに存在しない文字が含まれている場合はエラーコード2が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setRotorCharSetPair(int rotorIndex, String charSetPair) { // => error code
		if (rotorIndex >= 0 && rotorIndex < rotors.length) {
			return rotors[rotorIndex].setCharSetPair(charSetPair);
		} else {
			return 3;
		}
	}

	/**
	 * リフレクターの出力文字セットを設定します。
	 * 
	 * @param charSetPair 新しい出力文字セット。
	 * NULLや入力文字セットと異なる長さの文字列が指定された場合はエラーコード1が返されます。
	 * 入力文字セットに存在しない文字が含まれている場合はエラーコード2が返されます。
	 * 対応する文字が一対一の関係ではない場合はエラーコード3が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setReflectorCharSetPair(String charSetPair) { // => error code
		return reflector.setCharSetPair(charSetPair);
	}

	// Rotor

	/**
	 * ローターの個数を取得します。
	 * 
	 * @return ローターの個数。
	 */
	public int getRotorNum() {
		return rotors.length;
	}

	/**
	 * 全てのローターの位相を文字列（ラベル）で取得します。
	 * 
	 * @return 位相に対応する文字セットの文字列。
	 */
	public String getRotorsOffset() {
		StringBuffer res = new StringBuffer();

		for (Rotor rotor : rotors)
			res.append(rotor.getOffsetChar());

		return res.toString();
	}

	/**
	 * ローターの個数を（再）設定します。
	 * このメソッドが実行されると全てのローターとリフレクターが初期化されます。
	 * 
	 * @param newRotorNum ローターの個数。
	 * 0の場合はリフレクターのみで構成された単一換字式暗号機になります。
	 * 負の値が指定された場合はエラーコード1が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setRotorNum(int newRotorNum) { // => error code
		if (newRotorNum < 0)
			return 1;

		setScramblerKit(newRotorNum);

		return 0;
	}

	/**
	 * 全てのローターの位相を文字列（ラベル）で設定します。
	 * 
	 * @param offsetText 位相に対応する文字セットの文字列。
	 * NULLやローターの個数と異なる長さの文字列が指定された場合はエラーコード1が返されます。
	 * 文字セットに存在しない文字が指定された場合はエラーコード2が返されます。
	 * {@link Rotor#setOffset(char)}でエラーが発生した場合はエラーコード3が返されます。
	 * @return エラーコード。
	 * 正常に設定された場合は0が返されます。
	 */
	public int setRotorsOffset(String offsetText) { // => error code
		if (offsetText == null || rotors.length != offsetText.length())
			return 1;

		for (int i = 0; i < offsetText.length(); i++)
			if (charSet.indexOf(offsetText.charAt(i)) == -1)
				return 2;

		boolean errorFlag = false;

		for (int i = 0; i < rotors.length; i++)
			if (rotors[i].setOffset(offsetText.charAt(i)) != 0)
				errorFlag = true;

		return errorFlag ? 3 : 0;
	}

	// Function

	/**
	 * スクランブラーの状況を視覚的な文字列として取得します。
	 * ローターは入出力の対応関係と位相が表示されます。
	 * リフレクターは入出力の対応関係が表示されます。
	 * 
	 * @return スクランブラーの状況。
	 */
	public String getStatus() {
		StringBuffer res = new StringBuffer();

		for (int i = 0; i < rotors.length; i++)
			res.append("Rotor " + (i + 1) + ":\n" + getRotorStatus(i) + "\n\n");

		res.append("Reflector:\n" + getReflectorStatus());

		return res.toString();
	}

	/**
	 * ローターの状況を視覚的な文字列として取得します。
	 * 1行目に入力文字セットが表示されます。
	 * 3行目に対応する出力文字セットが表示されます。
	 * 4行目に対応する位相の地点が表示されます。
	 * 
	 * @param rotorIndex ローターの番地。
	 * @return ローターの状況。
	 */
	private String getRotorStatus(int rotorIndex) {
		String charSet = rotors[rotorIndex].getCharSet();
		String charSetConnecter = "|".repeat(charSet.length());
		String charSetPair = rotors[rotorIndex].getCharSetPair();
		String currentOffset = "-".repeat(rotors[rotorIndex].getOffset()) + "@";

		return charSet + "\n" + charSetConnecter + "\n" + charSetPair + "\n" + currentOffset;
	}

	/**
	 * リフレクターの状況を視覚的な文字列として取得します。
	 * 1行目に入力文字セットが表示されます。
	 * 3行目に対応する出力文字セットが表示されます。
	 * 
	 * @return リフレクターの状況。
	 */
	private String getReflectorStatus() {
		String charSet = reflector.getCharSet();
		String charSetConnecter = "|".repeat(charSet.length());
		String charSetPair = reflector.getCharSetPair();

		return charSet + "\n" + charSetConnecter + "\n" + charSetPair;
	}

	/**
	 * スクランブラーを（再）生成します。
	 * このメソッドが実行されると全てのローターとリフレクターが初期化されます。
	 * 
	 * @param rotorNum ローターの個数。
	 * 0の場合はリフレクターのみで構成された単一換字式暗号機になります。
	 */
	private void setScramblerKit(int rotorNum) {
		rotors = new Rotor[rotorNum];

		for (int i = 0; i < rotorNum; i++)
			rotors[i] = new Rotor(i + 1, charSet);

		reflector = new Reflector(0, charSet);
	}

	/**
	 * 文字列を暗号化もしくは復号化します。
	 * 
	 * @param text 変換する文字列。
	 * 文字セットに存在しない文字は変換されずに返されます。
	 * 文字が変換されると自動的にローターが回転します。
	 * @return 変換された文字列。
	 */
	public String convertText(String text) {
		StringBuffer res = new StringBuffer();

		for (int i = 0; i < text.length(); i++)
			res.append(convertChar(text.charAt(i)));

		return res.toString();
	}

	/**
	 * 文字を暗号化もしくは復号化します。
	 * 
	 * @param charCode 変換する文字。
	 * 文字セットに存在しない文字が指定された場合は変換されずに返されます。
	 * 変換されると自動的にローターが回転します。
	 * @return 変換された文字。
	 */
	public char convertChar(char charCode) {
		if (charSet.indexOf(charCode) != -1) {
			for (int i = 0; i < rotors.length; i++)
				if (!rotors[i].rotate())
					break;

			for (int i = 0; i < rotors.length; i++)
				charCode = rotors[i].convertChar(charCode);

			charCode = reflector.convertChar(charCode);

			for (int i = rotors.length - 1; i >= 0; i--)
				charCode = rotors[i].convertReverseChar(charCode);
		}

		return charCode;
	}
}
