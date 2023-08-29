
public class Tokens {
	 enum TokenType {NUMBER, WORD, SEPARATOR}
	 private String value;
	 private int lineNumber;
	 private int position;
	 private TokenType type;
	 public Tokens(String value) {
		 this.value = value;
	 }
	 public Tokens(int LN, int p, TokenType t) {
		 lineNumber = LN;
		 position = p;
		 type = t;
	 }
}
