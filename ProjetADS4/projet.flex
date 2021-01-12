%%
%public
%class Lexer
%unicode
%type Token
%line
%column

%yylexthrow LexerException

nombre =  [0-9]+
hex = [0-9A-F]
couleur = #{hex}{hex}{hex}{hex}{hex}{hex}
opérateur = "+"|"-"|"/"|"*"
identificateur =[a-z][a-zA-Z_]*
blank = [\n\r \t]

%%
"If"  {return new Token(Sym.IF);}
"Then"  {return new Token(Sym.THEN);}
"Else"  {return new Token(Sym.ELSE);}
"Begin" {return new Token(Sym.BEGIN);}
"End" {return new Token(Sym.END);}
"Const" {return new Token(Sym.CONST);}
"Var" {return new Token(Sym.VAR);}
"Def" {return new Token(Sym.DEF);}
"While" {return new Token(Sym.WHILE);}
"For" {return new Token(Sym.FOR);}
"Do" {return new Token(Sym.DO);}
{nombre} {return new IntToken(Sym.NOMBRE, Integer.parseInt(yytext()));}
{couleur} {return new StringToken(Sym.COULEUR, yytext());}
"," {return new Token (Sym.VIRG);}
";" {return new Token(Sym.PVIRG);}
{opérateur} {return new StringToken(Sym.OP,yytext());}
"DrawCircle"  {return new Token(Sym.DRAWC);}
"DrawRect"  {return new Token(Sym.DRAWR);}
"FillCircle" {return new Token(Sym.FILLC);}
"FillRect"  {return new Token(Sym.FILLR);}
"(" 	{return new Token(Sym.LPAR);}
")" 	{return new Token(Sym.RPAR);}
"=" 	{return new Token(Sym.EQ);}
{identificateur} {return new StringToken(Sym.ID,yytext());}
{blank}	{}
<<EOF>>		{return new Token(Sym.EOF);}
[^]		{throw new LexerException(yytext(), yyline, yycolumn);}
