#ifndef TYPEDEFS_H
#define	TYPEDEFS_H

#include "cimp.h"

#define	TRUE       1
#define	FALSE      0
#define	NB_SCREENS 4

typedef unsigned char t_bool;

#define	max(a, b) (a >= b ? a : b)
#define	min(a, b) (a <= b ? a : b)

/**
 * Ici sont définies les structures standards utilisées dans le programme.
 */
typedef struct s_cimp_select {
	int      id;
	SDL_Rect surface;
} t_cimp_select;

typedef struct          s_cimp_screen {
	SDL_Window *  window;
	SDL_Surface * buff_screen;
	char *        path;
}                       t_cimp_screen;

typedef struct      s_cimp_event {
	SDL_Rect selection;
	int8_t   button_pressed;
} t_cimp_event;

typedef struct          s_cimp {
	t_cimp_screen * screen[NB_SCREENS];
	int             running;
	int             focus;
	t_cimp_select * select;
	t_cimp_event *  event;
	SDL_Surface *   copy_buffer;
}                       t_cimp;

typedef struct      s_cmd {
	char *    cmd;
	char *    name;
	int       num;
	int       focus;
	SDL_Rect  rect;
	SDL_Point point;
	SDL_Color color;
	SDL_Color color2[2];
}                   t_cmd;

typedef enum        s_rc_cmd {
	OK,
	FAIL,
	ABORT,
}                   t_rc_cmd;

#define	NB_ARG_TYPE 8

typedef enum        e_arg_type {
	ARG_NAME   = 0x1,
	ARG_NUM    = 0x2,
	ARG_PATH   = 0x4,
	ARG_RECT   = 0x8,
	ARG_PT     = 0x10,
	ARG_COLOR  = 0x20,
	ARG_COLOR2 = 0x40,
	ARG_FOCUS  = 0x80,
}                   t_arg_type;
// Change NB_ARG_TYPE if you add an argument.
// Increase size of opts et opts_opt if no enough bits

typedef struct      s_cmd_config {
	char *  name;
	t_rc_cmd (* func_cmd_ptr)(t_cmd *);
	uint8_t opts;     // Necessary opts
	uint8_t opts_opt; // Optionnal opts
} t_cmd_config;

typedef enum            e_type_img {
	NO_SUPPORTED = -1,
	BMP,
	JPG,
	PNG,
}                       t_type_img;

typedef struct          s_extension_img {
	t_type_img type;
	char **    li_extensions;
	size_t     len_nb_extensions;
}                       t_extension_img;

typedef int (* t_export_img_func_ptr)(const char *, SDL_Surface *);

typedef struct          s_assoc_type_img_func {
	t_type_img            type;
	t_export_img_func_ptr func_export;
}                       t_assoc_type_img_func;

/**
 * Ici est définie la variable globale g_cimp qui stocke l'instance de notre programme.
 */

extern t_cimp * g_cimp;

// Parser globals
extern const t_cmd_config g_command_list[];
extern const size_t g_command_list_size;

extern const t_extension_img g_extension_img_list[];
extern const size_t g_extension_img_size;

extern const t_assoc_type_img_func g_assoc_type_img_func_list[];
extern const size_t g_assoc_type_img_func_size;

// Childs globals
extern int g_fd_readline;
extern int g_fd_callback;

// libtest globale
extern int g_viewing_enabled;

#endif // ifndef TYPEDEFS_H
