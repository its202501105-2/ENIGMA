import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ENIGMA
 */
public class Enigma {
	/** コマンドの説明文：標準入力 */
	static final String COMMAND_DEFAULT = "<STRING> ... Encodes and decodes text.";
	/** コマンドの説明文：文字セットの表示 */
	static final String COMMAND_GCS = "/GCS ... Get the character set.";
	/** コマンドの説明文：文字セットの設定 */
	static final String COMMAND_SCS = "/SCS <STRING> ... Set the character set.";
	/** コマンドの説明文：スクランブラーの表示 */
	static final String COMMAND_GSI = "/GSI ... Get all rotor / reflector information.";
	/** コマンドの説明文：ローターの個数の設定 */
	static final String COMMAND_SRN = "/SRN <INT> ... Set the number of rotors.";
	/** コマンドの説明文：ローター／リフレクターの出力文字セットの設定 */
	static final String COMMAND_SRP = "/SRP <INT> <STRING> ... Set the rotor / reflector character set pair (0th: reflector, 1st~: rotor).";
	/** コマンドの説明文：ローターの位相の表示 */
	static final String COMMAND_GRO = "/GRO ... Get the rotor offset.";
	/** コマンドの説明文：ローターの位相の設定 */
	static final String COMMAND_SRO = "/SRO <STRING> ... Set the rotor offset.";
	/** コマンドの説明文：自動大文字変換機能の設定 */
	static final String COMMAND_TUC = "/TUC <BOOLEAN> ... Set the function to convert lowercase to uppercase.";
	/** コマンドの説明文：プログラムの終了 */
	static final String COMMAND_END = "/END ... End the program.";

	/** スクランブラー */
	static Scrambler scrambler = new Scrambler(3);

	/** 自動大文字変換機能の有効化 */
	static boolean activeUppercaseConversionFunction = true;

	/**
	 * 主となるプログラムを実行します。
	 * 
	 * @param args 引数。
	 * 機能はありません。
	 */
	public static void main(String[] args) {
		System.out.println("╔═╗ ╔╗╔ ╦ ╔═╗ ╔╦╗ ╔═╗");
		System.out.println("╠╣  ║║║ ║ ║ ╦ ║║║ ╠═╣");
		System.out.println("╚═╝ ╝╚╝ ╩ ╚═╝ ╩ ╩ ╩ ╩");
		System.out.println("---------------------");
		System.out.println(COMMAND_DEFAULT);
		System.out.println(COMMAND_GCS);
		System.out.println(COMMAND_SCS);
		System.out.println(COMMAND_GSI);
		System.out.println(COMMAND_SRN);
		System.out.println(COMMAND_SRP);
		System.out.println(COMMAND_GRO);
		System.out.println(COMMAND_SRO);
		System.out.println(COMMAND_TUC);
		System.out.println(COMMAND_END);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String text;

		// ENIGMA Iのスクランブラーを再現

		scrambler.setRotorCharSetPair(0, "EKMFLGDQVZNTOWYHXUSPAIBRCJ");
		scrambler.setRotorCharSetPair(1, "AJDKSIRUXBLHWTMCQGZNPYFVOE");
		scrambler.setRotorCharSetPair(2, "BDFHJLCPRTXVZNYEIWGAKMUSQO");
		scrambler.setReflectorCharSetPair("EJMZALYXVBWFCRQUONTSPIKHGD");

		try {
			while (true) {
				System.out.print("> ");

				text = bufferedReader.readLine();

				if (text.length() > 0) {
					if (activeUppercaseConversionFunction)
						text = text.toUpperCase();

					if (text.charAt(0) != '/') {
						System.out.println(scrambler.convertText(text));
					} else {
						if (runCommand(text.substring(1)) == 1)
							break;
					}
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * 文字列を基にコマンドを実行します。
	 * 
	 * @param text コマンド。
	 * 空白文字で区切られた引数です。
	 * クォーテーションやエスケープシーケンスに対応しています。
	 * コマンドが要求する引数の個数と一致しない場合は説明文が表示されます。
	 * @return 終了コード。
	 * 標準で0が返されます。
	 * プログラムを終了する場合は1が返されます。
	 */
	static int runCommand(String text) {
		String[] args = Utility.evalArguments(text);

		switch (args[0].toUpperCase()) {
		case "GCS":
			if (args.length == 1) {
				getCharacterSet();
			} else {
				System.out.println(COMMAND_GCS);
			}

			return 0;

		case "SCS":
			if (args.length == 2) {
				setCharacterSet(args[1]);
			} else {
				System.out.println(COMMAND_SCS);
			}

			return 0;

		case "GSI":
			if (args.length == 1) {
				getScramblerInformation();
			} else {
				System.out.println(COMMAND_GSI);
			}

			return 0;

		case "SRN":
			if (args.length == 2) {
				setRotorNumber(args[1]);
			} else {
				System.out.println(COMMAND_SRN);
			}

			return 0;

		case "SRP":
			if (args.length == 3) {
				setScramblerCharSetPair(args[1], args[2]);
			} else {
				System.out.println(COMMAND_SRP);
			}

			return 0;

		case "GRO":
			if (args.length == 1) {
				getRotorOffset();
			} else {
				System.out.println(COMMAND_GRO);
			}

			return 0;

		case "SRO":
			if (args.length == 2) {
				setRotorOffset(args[1]);
			} else {
				System.out.println(COMMAND_SRO);
			}

			return 0;

		case "TUC":
			if (args.length == 2) {
				setUppercaseConversionFunction(args[1]);
			} else {
				System.out.println(COMMAND_TUC);
			}

			return 0;

		case "END":
			if (args.length == 1) {
				System.out.println("Good bye.");
			} else {
				System.out.println(COMMAND_END);
			}

			return 1;

		default:
			System.out.println("Command does not exist.");
			return 0;
		}
	}

	/**
	 * 文字セットを表示します。
	 */
	static void getCharacterSet() {
		System.out.println(scrambler.getCharSet());
	}

	/**
	 * 文字セットを設定します。
	 * 
	 * @param charSet 新しい文字セット。
	 * 重複した文字が存在しない、長さが2以上かつ偶数の文字列です。
	 * 条件に合致しない文字列が指定された場合はエラー文が出力されます。
	 */
	static void setCharacterSet(String charSet) {
		if (scrambler.setCharSet(charSet) != 0)
			System.out.println(
					"The character set must be a multiple of 2 in length and contain no duplicate characters.");
	}

	/**
	 * スクランブラーの状況を視覚的な文字列として表示します。
	 */
	static void getScramblerInformation() {
		System.out.println(scrambler.getStatus());
	}

	/**
	 * ローターの個数を（再）設定します。
	 * このメソッドが実行されると全てのローターとリフレクターが初期化されます。
	 * 
	 * @param rotorNum ローターの個数。
	 * 文字列から整数値に変換されます。
	 * 0の場合はリフレクターのみで構成された単一換字式暗号機になります。
	 * 条件に合致しない値が指定された場合はエラー文が出力されます。
	 */
	static void setRotorNumber(String rotorNum) {
		if (!rotorNum.matches("^\\d+$") || scrambler.setRotorNum(Integer.parseInt(rotorNum)) != 0)
			System.out.println("The number of rotors must be a positive integer.");
	}

	/**
	 * ローター／リフレクターの出力文字セットを設定します。
	 * 
	 * @param rotorIndexBuf ローター／リフレクターの番地。
	 * 文字列から整数値に変換されます。
	 * 0の場合はリフレクターが指定されます。
	 * 1以上の場合は番地に対応したローターが指定されます。
	 * 条件に合致しない値が指定された場合はエラー文が出力されます。
	 * @param charSetPair 出力文字セット。
	 */
	static void setScramblerCharSetPair(String rotorIndexBuf, String charSetPair) {
		int rotorIndex;

		if (!rotorIndexBuf.matches("^\\d+$") || (rotorIndex = Integer.parseInt(rotorIndexBuf)) < 0) {
			System.out.println("The rotor index must be an integer greater than or equal to 0.");

			return;
		}

		if (rotorIndex == 0) {
			setReflectorCharSetPair(charSetPair);
		} else {
			setRotorCharSetPair(rotorIndex - 1, charSetPair);
		}
	}

	/**
	 * ローターの出力文字セットを設定します。
	 * 
	 * @param rotorIndex ローターの番地。
	 * 存在しない番地が指定された場合はエラー文が出力されます。
	 * @param charSetPair 新しい出力文字セット。
	 * 文字セットと同じ要素と長さで構成された文字列です。
	 * 条件に合致しない値が指定された場合はエラー文が出力されます。
	 */
	static void setRotorCharSetPair(int rotorIndex, String charSetPair) {
		if (scrambler.setRotorCharSetPair(rotorIndex, charSetPair) != 0)
			System.out.println(
					"The character set pair consists of the character set, which must be an unordered string.");
	}

	/**
	 * リフレクターの出力文字セットを設定します。
	 * 
	 * @param charSetPair 新しい出力文字セット。
	 * 文字セットと同じ要素と長さで構成された、入力文字セットと一対一の関係を持つ文字列です。
	 * 条件に合致しない値が指定された場合はエラー文が出力されます。
	 */
	static void setReflectorCharSetPair(String charSetPair) {
		if (scrambler.setReflectorCharSetPair(charSetPair) != 0)
			System.out.println(
					"The character set pair consists of the character set, which must have corresponding relationships to each other.");
	}

	/**
	 * ローターの位相を文字列（ラベル）で表示します。
	 */
	static void getRotorOffset() {
		System.out.println(scrambler.getRotorsOffset());
	}

	/**
	 * ローターの位相を文字列（ラベル）で設定します。
	 * 
	 * @param data 位相に対応する文字セットの文字列。
	 * ローターの個数と同じ長さを持つ、文字セットで構成された文字列です。
	 * 条件に合致しない値が指定された場合はエラー文が出力されます。
	 */
	static void setRotorOffset(String data) {
		if (scrambler.setRotorsOffset(data) != 0)
			System.out.println(
					"The rotor offset must be a string equal to the number of rotors, composed of characters from the character set.");
	}

	/**
	 * 自動大文字変換機能を設定します。
	 * これが有効化されると小文字の英字は大文字に変換されます。
	 * 
	 * @param isActive 有効化は真を示す文字列、無効化は偽を示す文字列。
	 * 文字列を基に真偽を判定します。
	 * 条件に合致しない値（"TRUE"や"FALSE"以外の文字列）が指定された場合はエラー文が出力されます。
	 */
	static void setUppercaseConversionFunction(String isActive) {
		if (isActive.toUpperCase().matches("^TRUE|T$")) {
			activeUppercaseConversionFunction = true;
		} else if (isActive.toUpperCase().matches("^FALSE|F$")) {
			activeUppercaseConversionFunction = false;
		} else {
			System.out.println("The boolean value must be \"TRUE\" or \"FALSE\".");
		}
	}
}
