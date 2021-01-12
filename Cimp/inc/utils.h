#ifndef UTILS_H
#define	UTILS_H

#define	NOT_A_PATH  "The given path is not a regular Unix path"
#define	MALLOC_FAIL "A malloc has failed"
#define	TOO_MUCH_SCREENS \
	"You have already opened the maximal number of screens! \n Close one to open an other one."

// path_utils

char * normalize_path(char * curpath);

// str_utils

char * strjoin(const char * s1, const char * s2);
char * strjoin_c(const char * dir, const char * file, char joiner);

char * dupstr(const char * s);
int nb_digit(int i);
char * itoa(int i);
t_bool safe_strtol10(const char * word, char ** next, int * nump);

// format/*.c

int     save_surface_png(const char * file, SDL_Surface * surface);
int     save_surface_jpeg(const char * file, SDL_Surface * surface);
int     save_surface_bmp(const char * file, SDL_Surface * surface);

t_type_img              get_type_img(char * filepath);
t_export_img_func_ptr   get_func_img(t_type_img type);

// sdl_utils.c

SDL_Rect    sdl_surface_build_good_selection(SDL_Surface * surf, SDL_Rect param);
// void        sdl_surface_map(SDL_Surface * surf, SDL_Rect rect, uint32_t (* fp)(uint32_t));
void        sdl_surface_mapp(SDL_Surface * surf, SDL_Rect rect,
  uint32_t (* fp)(uint32_t, void *), void * v);

#endif // ifndef UTILS_H
