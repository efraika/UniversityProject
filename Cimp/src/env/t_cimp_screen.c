#include "cimp.h"

/**
 * cimp_init_screen est la fonction qui crée une instance de t_cimp_screen
 * à partir d'un chemin et renvoie un pointeur vers la structure si tout
 * s'est bien passé, sinon renvoie null et stocke un message d'erreur dans errno_str.
 * Cette fonction fait tous les checks d'instanciation à savoir :
 *  - construire le chemin absolu
 *  - ouvrir le fichier est construire un SDL_Surface.
 *  - créer la SDL_Window associé
 *  - construire enfin la structure et tout stocker
 * @param  path_img   le chemin de l'image (relative, absolu avec ~/ en préfixe)
 * @param  must_abort un pointeur vers un booléen qui permet de nous dire si une commande
 * @return            un pointeur vers la structure allouée
 */
t_cimp_screen * cimp_screen_init(char * path_img, t_bool * must_abort) {
	t_cimp_screen * sc;
	SDL_Window * win;
	SDL_Surface * surf;
	SDL_Surface * tmp;
	SDL_Rect origin;
	char * path;

	sc          = NULL;
	win         = NULL;
	surf        = NULL;
	tmp         = NULL;
	path        = NULL;
	*must_abort = FALSE;

	if ((path = normalize_path(path_img)) == NULL) {
		printf("%s\n", NOT_A_PATH);
	}
	else if ((tmp = IMG_Load(path)) == NULL) {
		printf("%s\n", IMG_GetError());
	}
	else if ((surf = SDL_CreateRGBSurface(0, tmp->w, tmp->h, 32, 0, 0, 0, 0)) == NULL) {
		*must_abort = TRUE;
		printf("%s\n", SDL_GetError());
	}
	else if (g_viewing_enabled && (win = SDL_CreateWindow(basename(path),
			SDL_WINDOWPOS_UNDEFINED,
			SDL_WINDOWPOS_UNDEFINED,
			tmp->w,
			tmp->h,
			SDL_WINDOW_SHOWN)) == NULL)
	{
		*must_abort = TRUE;
		printf("%s\n", SDL_GetError());
	}
	else if ((sc = (t_cimp_screen *) malloc(sizeof(t_cimp_screen))) == NULL) {
		printf("%s\n", MALLOC_FAIL);
	}
	else {
		origin.x = 0;
		origin.y = 0;
		origin.w = tmp->w;
		origin.h = tmp->h;
		SDL_BlitSurface(tmp, &origin, surf, NULL);
		if (g_viewing_enabled)
			sc->window = win;
		else
			sc->window = NULL;
		sc->buff_screen = surf;
		sc->path        = path;
	}

	if (tmp != NULL) SDL_FreeSurface(tmp);
	if (sc == NULL) {
		if (path != NULL) free(path);
		if (surf != NULL) SDL_FreeSurface(surf);
		if (win != NULL) SDL_DestroyWindow(win);
	}

	return (sc);
} /* cimp_init_screen */

/**
 * cimp_end_screen libère les ressources associé à cette instance de t_cimp_screen
 * @param  sc l'instance de t_cimp_screen
 */
void                cimp_screen_end(t_cimp_screen * sc) {
	if (sc) {
		if (sc->path)
			free(sc->path);
		if (sc->buff_screen)
			SDL_FreeSurface(sc->buff_screen);
		if (g_viewing_enabled && sc->window)
			SDL_DestroyWindow(sc->window);
		free(sc);
	}
}

/**
 * update the SDL window using the buffered SDL_Surface.
 * @param  screen the cimp screen pointer
 * @return        nothing
 */
void                cimp_screen_update(t_cimp_screen * screen, int num) {
	SDL_Rect full_rect;
	int w_buff;
	int h_buff;
	char * name_actu, * path, * focus;

	if (screen == NULL)
		return;

	// Update window size
	SDL_GetWindowSize(screen->window, &w_buff, &h_buff);
	if (w_buff != screen->buff_screen->w || h_buff != screen->buff_screen->h) {
		SDL_SetWindowSize(screen->window, screen->buff_screen->w, screen->buff_screen->h);
	}

	// Update window title
	path      = basename(screen->path);
	focus     = itoa(num + 1);
	focus     = strjoin(" n°", focus);
	name_actu = strjoin(path, focus);
	if (strcmp(name_actu, SDL_GetWindowTitle(screen->window)) != 0) {
		SDL_SetWindowTitle(screen->window, name_actu);
	}
	free(name_actu);
	free(focus);

	// Update content of the window
	full_rect.x = 0;
	full_rect.y = 0;
	full_rect.w = screen->buff_screen->w;
	full_rect.h = screen->buff_screen->h;
	SDL_BlitSurface(screen->buff_screen, &full_rect, SDL_GetWindowSurface(screen->window), NULL);
	SDL_UpdateWindowSurface(screen->window);
} /* cimp_screen_update */

t_bool              cimp_screen_set_path(t_cimp_screen * screen, char * path) {
	char * p;

	p = normalize_path(path);
	if (p == NULL)
		return (FALSE);

	free(screen->path);
	screen->path = p;
	return (TRUE);
}

t_bool              cimp_screen_set_surface(t_cimp_screen * screen, char * path) {
	SDL_Rect rect;
	SDL_Surface * tmp;
	SDL_Surface * surf;
	t_bool res;

	res  = FALSE;
	tmp  = NULL;
	surf = NULL;
	if ((tmp = IMG_Load(path)) == NULL) {
		printf("%s\n", IMG_GetError());
	}
	else if ((surf = SDL_CreateRGBSurface(0, tmp->w, tmp->h, 32, 0, 0, 0, 0)) == NULL) {
		printf("%s\n", SDL_GetError());
	}
	else {
		res = TRUE;

		rect.x = 0;
		rect.y = 0;
		rect.w = tmp->w;
		rect.h = tmp->h;
		SDL_BlitSurface(tmp, &rect, surf, NULL);
		SDL_FreeSurface(screen->buff_screen);
		screen->buff_screen = surf;
		SDL_FreeSurface(tmp);
		tmp = NULL;
	}

	if (tmp)
		SDL_FreeSurface(tmp);
	if (res == FALSE) {
		if (surf)
			SDL_FreeSurface(surf);
	}
	return (res);
} /* cimp_screen_set_surface */
