public class LexerException extends Exception {
	public LexerException(String charac, int line, int column) {
		super("Unexpected character at line " + line + " column " + column + "." + charac);
	}
}
