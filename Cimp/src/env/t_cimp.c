#include "cimp.h"

/**
 * Initialise l'environnement, charge SDL et alloue la structure globale g_cimp
 */
int             cimp_init() {
	if (g_cimp)
		return (0);

	if (g_viewing_enabled) {
		if (SDL_Init(SDL_INIT_VIDEO) < 0) {
			printf("SDL could not initialize! SDL_Error: %s\n", SDL_GetError());
			return (2);
		}
	}
	if (IMG_Init(IMG_INIT_JPG | IMG_INIT_PNG) != (IMG_INIT_JPG | IMG_INIT_PNG)) {
		printf("SDL_IMG couldn't initialize ! IMG_Error: %s\n", IMG_GetError());
		return (2);
	}
	if ((g_cimp = (t_cimp *) malloc(sizeof(t_cimp))) == NULL)
		return (1);

	for (int i = 0; i < NB_SCREENS; i++) {
		g_cimp->screen[i] = NULL;
	}

	g_cimp->select      = NULL;
	g_cimp->event       = init_cimp_event();
	g_cimp->running     = 1;
	g_cimp->copy_buffer = NULL;
	g_cimp->focus       = -1;
	return (0);
}

/**
 * Libère les ressources, et du programme et de la librairie SDL
 */
void            cimp_end() {
	if (g_cimp) {
		for (int i = 0; i < NB_SCREENS; i++) {
			if (g_cimp->screen[i] != NULL)
				cimp_screen_end(g_cimp->screen[i]);
		}

		if (g_cimp->copy_buffer)
			SDL_FreeSurface(g_cimp->copy_buffer);
		cimp_end_select(g_cimp->select);
		free(g_cimp);
		g_cimp = NULL;
		IMG_Quit();
		SDL_Quit();
	}
}

/**Une fonction qui renvoie le plus petit id disponible ou -1 lorsqu'il n'y a plus d'emplacement libre **/
int get_available_id() {
	int i = 0;

	while (i < NB_SCREENS && g_cimp->screen[i] != NULL) {
		i++;
	}
	if (i + 1 > NB_SCREENS)
		return -1;

	return i;
}

int get_next_focus(int a) {
	int i = (a + 1) % NB_SCREENS;

	while (i != a) {
		if (g_cimp->screen[i])
			return i;

		i = (i + 1) % NB_SCREENS;
	}
	return -1;
}
