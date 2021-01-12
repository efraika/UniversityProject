#include "cimp.h"

/**Une fonction qui initialise tous les champs d'un t_cimp_event **/
t_cimp_event * init_cimp_event() {
	t_cimp_event * res = malloc(sizeof(t_cimp_event));

	if (res == NULL) {
		printf("AU SECOURS\n");
		return NULL;
	}

	SDL_Rect rectangle;
	rectangle.x = -1;
	rectangle.y = -1;
	rectangle.w = -1;
	rectangle.h = -1;

	res->selection      = rectangle;
	res->button_pressed = 1;

	return res;
}

void free_cimp_event(t_cimp_event * evnmt) {
	free(evnmt);
}

/**Une fonction qui mets a jour les champs de event en fonction des SDL_Event produits.
 * Appelle les fonctions necessaires apres avoir mis a jour les differents champs **/
void update_event(t_cimp_event * evnmt) {
	SDL_Event event;

	while (SDL_PollEvent(&event) == 1) {
		if (event.type == SDL_QUIT ||
		  (event.type == SDL_WINDOWEVENT && event.window.event == SDL_WINDOWEVENT_CLOSE))
		{
			for (int i = 0; i < NB_SCREENS; i++) {
				if (g_cimp->screen[i] &&
				  SDL_GetWindowID(g_cimp->screen[i]->window) == event.window.windowID)
				{
					g_cimp->focus = i;
					cimp_close(NULL);
				}
			}
		}
		else if (event.type == SDL_MOUSEBUTTONDOWN) {
			for (int i = 0; i < NB_SCREENS; i++) {
				if (g_cimp->screen[i] &&
				  SDL_GetWindowID(g_cimp->screen[i]->window) == event.window.windowID)
				{
					g_cimp->focus         = i;
					evnmt->button_pressed = 0;
					evnmt->selection.x    = event.button.x;
					evnmt->selection.y    = event.button.y;
				}
			}
		}
		else if (event.type == SDL_MOUSEBUTTONUP) {
			evnmt->button_pressed = 1;
			evnmt->selection.w    = abs(event.button.x - evnmt->selection.x);
			evnmt->selection.h    = abs(event.button.y - evnmt->selection.y);
			evnmt->selection.x    = min(evnmt->selection.x, event.button.x);
			evnmt->selection.y    = min(evnmt->selection.y, event.button.y);
			if (evnmt->selection.x >= -0 && evnmt->selection.y >= 0 && evnmt->selection.w > 0 &&
			  evnmt->selection.h > 0)
			{
				t_cmd * cmd = cmd_alloc();
				cmd->rect  = evnmt->selection;
				cmd->focus = g_cimp->focus;
				if (cimp_select(cmd) < 0)
					printf("Terrible erreur\n");
				cmd_free(cmd);
			}
		}
	}
} /* update_event */
