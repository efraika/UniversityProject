import java.io.*;

class LookAhead{

	private Token current;
	private Lexer lexer;

	public LookAhead(Lexer l) throws Exception {
		lexer=l;
		current=lexer.yylex();
	}

	public boolean check(Sym s) {
		/* check whether the first character is of type s*/
		return (current.getSym() == s);
	}

	public void eat(Sym s) throws Exception {
		/* consumes a token of type s from the stream,
	   exception when the contents does not start on s.   */
		if (!check(s)) {
			throw new Exception("\nCan't eat "+s+" current being "+current);
		}

		//System.out.println(current); //for debug only!!!

		current=lexer.yylex();
	}

	public int getIntValue() throws Exception {
		// gives the value of the IntToken, or rises an exception if not IntToken
		if (current instanceof IntToken) {
			IntToken t = (IntToken) current;
			return t.getVal();
		}
		throw new Exception("LookAhead error: get value from a non-valued token");
	}

	public String getStringValue() throws Exception {
		// gives the value of the VarToken, or rises an exception if not VarToken
		if (current instanceof StringToken) {
			StringToken t = (StringToken) current;
			return t.getVal();
		}
		throw new Exception("LookAhead error: get value from a non-valued token");
	}

	public String getString() { // outputs a string form of the current Token
		return current.toString();
	}
}
