#include "cimp.h"

/** Une fonction qui stocke dans le champs copy_buffer de g_cimp la partie de l'image designee
 * par l'argument rect passe a la commande.
 *  Dans le cas ou le rectangle sors de l'image on selectionne le plus grand rectangle possible dans l'image
 * Renvoie 0 en cas de succes et en cas d'echec ou si aucune image n'es ouverte renvoie -1**/
t_rc_cmd cimp_copy(t_cmd * cmd) {
	SDL_Rect rect;
	SDL_Surface * surface_src, * surface_dest;

	surface_src = g_cimp->screen[cmd->focus]->buff_screen;
	if (cmd->rect.x != -1 && cmd->rect.y != -1 && cmd->rect.w != -1 && cmd->rect.h != -1) {
		rect = cmd->rect;
	}
	else {
		if (g_cimp->select == NULL) {
			printf("Pas de zone selectionnee : copie impossible \n");
			return FAIL;
		}
		if (g_cimp->screen[g_cimp->select->id] == NULL) {
			printf("La zone selectionnee n'est plus accessible \n");
			return FAIL;
		}
		rect        = g_cimp->select->surface;
		surface_src = g_cimp->screen[g_cimp->select->id]->buff_screen;
	}

	// Si les coordonnes initiales du rectangles sont hors de l'image on echoue
	if (surface_src->w < rect.x ||
	  surface_src->h < rect.y ||
	  rect.x < 0 || rect.y < 0 ||
	  rect.w < 0 || rect.h < 0 ||
	  rect.w + rect.x > surface_src->w ||
	  rect.h + rect.y > surface_src->h)
	{
		printf("La zone a copiee est hors de l'image\n");
		return FAIL;
	}

	// Si un copy_buffer est deja present on le libere pour un en creer un nouveau
	if (g_cimp->copy_buffer) {
		SDL_FreeSurface(g_cimp->copy_buffer);
		g_cimp->copy_buffer = NULL;
	}

	// On cree notre nouvelle surface puis on la remplit
	if ( (surface_dest = SDL_CreateRGBSurface(0, rect.w, rect.h, 32, 0, 0, 0, 0)) == NULL)
		return ABORT;

	Uint32 * pixels_dest, * pixels_src;

	if (SDL_MUSTLOCK(surface_dest))
		SDL_LockSurface(surface_dest);

	pixels_dest = (Uint32 *) surface_dest->pixels;
	pixels_src  = (Uint32 *) surface_src->pixels;
	for (int i = rect.x; i < rect.x + rect.w; i++) {
		for (int j = rect.y; j < rect.y + rect.h; j++)
			pixels_dest[(j - rect.y) * surface_dest->w
			+ (i - rect.x)] = pixels_src[j * surface_src->w + i];
	}
	if (SDL_MUSTLOCK(surface_dest))
		SDL_UnlockSurface(surface_dest);

	g_cimp->copy_buffer = surface_dest;

	return OK;
} /* cimp_copy */
