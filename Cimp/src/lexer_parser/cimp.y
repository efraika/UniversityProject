%{
    #include "cimp.h"

    extern int yylex();

    static t_li_token * li_token = NULL;
    t_bool lex_par_ok = TRUE;

    void yyerror(const char *s);
%}

%define parse.error verbose

%union {
    int numval;
    char * strval;
}

%token <numval> NUM_B
%token <strval> WORD_B
%token <strval> PATH_B
%token <strval> COLOR_B
%token ARROW_B

%%

toks:
    | tok toks
    ;

tok:
        WORD_B  { li_token_add(li_token, token_word($1)); }
    |   PATH_B  { li_token_add(li_token, token_path($1)); }
    |   NUM_B   { li_token_add(li_token, token_num($1)); }
    |   '[' NUM_B ']' { li_token_add(li_token, token_focus($2)); }
    |   '(' NUM_B NUM_B NUM_B NUM_B ')'
                {
                    li_token_add(li_token, token_rect($2, $3, $4, $5));
                }
    |   '(' NUM_B NUM_B ')'
                {
                    li_token_add(li_token, token_point($2, $3));
                }
    |   COLOR_B { li_token_add(li_token, token_color($1)); }
    |   '(' COLOR_B ARROW_B COLOR_B ')'
                {
                    li_token_add(li_token, token_color2($2, $4));
                }
    ;

%%

extern void     _yy_set_buffer(const char * str);

t_li_token    *scan_string(const char *str) {
    li_token = li_token_alloc();
    lex_par_ok = TRUE;
    _yy_set_buffer(str);
    yyparse();
    if (lex_par_ok == FALSE) {
        li_token_free(li_token);
        printf("Unexpected behaviour...\n");
        return (NULL);
    }
    return (li_token);
}

void            yyerror(const char * str) {
    printf("Error when parsing : %s\n", str);
}