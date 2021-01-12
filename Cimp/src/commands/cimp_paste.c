#include "cimp.h"

/**Une fonction qui colle la surface stockee dans copy_buffer a partir des coordonnes x0 y0
 * passees en arguments.
 * Renvoie 0 en cas de succes et -1 sinon en affichant un message d'erreur. **/
t_rc_cmd cimp_paste(t_cmd * cmd) {
	if (!g_cimp->copy_buffer) {
		printf("Rien n'a ete copie : le collage est impossible\n");
		return FAIL;
	}

	// Si les coordonnes sont hors de l'ecran on echoue
	if (cmd->point.x < 0 || cmd->point.y < 0 ||
	  cmd->point.x > g_cimp->screen[cmd->focus]->buff_screen->w ||
	  cmd->point.y > g_cimp->screen[cmd->focus]->buff_screen->h)
	{
		printf("Coordonees en dehors de l'image : collage impossible\n");
		return FAIL;
	}

	SDL_Surface * surface_src, * surface_dest;
	surface_src  = g_cimp->copy_buffer;
	surface_dest = g_cimp->screen[cmd->focus]->buff_screen;

	Uint32 * pixels_dest, * pixels_src;
	if (SDL_MUSTLOCK(surface_dest))
		SDL_LockSurface(surface_dest);
	pixels_dest = (Uint32 *) surface_dest->pixels;
	pixels_src  = (Uint32 *) surface_src->pixels;
	for (int i = cmd->point.x; i < min(cmd->point.x + surface_src->w, surface_dest->w); i++) {
		for (int j = cmd->point.y; j < min(cmd->point.y + surface_src->h, surface_dest->h); j++)
			pixels_dest[j * surface_dest->w
			+ i] = pixels_src[(j - cmd->point.y) * surface_src->w + (i - cmd->point.x)];
	}
	if (SDL_MUSTLOCK(surface_dest))
		SDL_UnlockSurface(surface_dest);


	return OK;
} /* cimp_paste */
