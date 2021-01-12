#ifndef ENV_H
#define	ENV_H

// t_cimp.c
int             cimp_init();
void            cimp_end();
int get_available_id();
int get_next_focus(int a);
// t_cimp_screen.c
t_cimp_screen * cimp_screen_init(char * path_img, t_bool * must_abort);
void cimp_screen_update(t_cimp_screen * screen, int num);

void cimp_screen_end(t_cimp_screen * sc);
t_bool cimp_screen_set_surface(t_cimp_screen * screen, char * path);

// t_cimp_select.c
t_cimp_select * cimp_init_select(SDL_Rect rectangle, int id);
void cimp_end_select(t_cimp_select * selection);

// t_cimp_event.c
t_cimp_event * init_cimp_event();
void free_cimp_event(t_cimp_event * evnmt);
void update_event(t_cimp_event * evnmt);
t_bool cimp_screen_set_path(t_cimp_screen * screen, char * path);

// t_cmd.c
t_cmd * cmd_alloc();
void cmd_free(t_cmd * cmd);

// t_arg_type.c
const char * arg_type_to_string(t_arg_type atype);

#endif // ifndef ENV_H
