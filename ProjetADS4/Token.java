class Token {
  private Sym symbol;

  public Token(Sym s){  symbol=s; }

  public Sym getSym(){ return symbol; }

  public String toString(){ return "Symbol :"+symbol; }
}

class IntToken extends Token{
  private int value;

  public IntToken(Sym s, int n){
    super(s);
    value=n;
  }

  public String toString(){ return super.toString()+" Value :"+value; }

  public int getVal(){ return value; }
}

class StringToken extends Token{
  private String value;

  public StringToken(Sym s, String v){
    super(s);
    value=v;
  }

  public String toString(){ return super.toString()+" Value :"+value; }

  public String getVal(){ return value; }
}
