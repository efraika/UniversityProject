#ifndef PARSER_H
#define	PARSER_H

#define	NO_ANGLE (-1)

typedef enum    e_type_token {
	WORD,
	PATH,
	NUM,
	RECT,
	FOCUS,
	POINT,
	COLOR,
	COLOR2,
}               t_type_token;

typedef struct  s_token {
	t_type_token type;
	union {
		int64_t   num;
		char *    str;
		SDL_Rect  rect;
		SDL_Point point;
		SDL_Color color;
		SDL_Color color2[2];
	}                u;
	struct s_token * next;
}               t_token;

typedef struct  s_li_token {
	t_token * first;
	t_token * last;
	t_token * actu;
	t_bool    ok;
}               t_li_token;

t_cmd * parser(char * line);
void cmd_free(t_cmd * cmd);

void free_li_token();
void print_token(t_token * tok);
t_token * token_word(const char * word);
t_token * token_path(const char * word);
t_token * token_num(int num);
t_token * token_focus(int num);
t_token * token_rect(int x, int y, int w, int h);
t_token * token_point(int x, int y);
t_token * token_color(const char * word);
t_token * token_color2(const char * word1, const char * word2);

t_li_token * li_token_alloc();
void li_token_free(t_li_token * li);
t_bool li_token_empty(t_li_token * li);
void li_token_print(t_li_token * li);
void li_token_add(t_li_token * li, t_token * tok);
t_token * li_token_get_next(t_li_token * li);

#endif // ifndef PARSER_H
