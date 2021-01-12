#include "cimp.h"

struct  s_meta_replace {
	SDL_PixelFormat * format;
	SDL_Color         col1;
	SDL_Color         col2;
	int               margin;
};

static uint32_t replace(uint32_t col, void * smrv) {
	uint8_t r, g, b;
	struct s_meta_replace * smrp;

	smrp = (struct s_meta_replace *) smrv;
	SDL_GetRGB(col, smrp->format, &r, &g, &b);
	if (abs(r - smrp->col1.r) <= smrp->margin &&
	  abs(g - smrp->col1.g) <= smrp->margin &&
	  abs(b - smrp->col1.b) <= smrp->margin)
	{
		return (SDL_MapRGB(smrp->format, smrp->col2.r, smrp->col2.g, smrp->col2.b));
	}
	return col;
}

t_rc_cmd cimp_color_replace(t_cmd * cmd) {
	SDL_Surface * buff_screen;
	SDL_Rect selection;
	struct s_meta_replace smr;

	buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	selection   = sdl_surface_build_good_selection(buff_screen, cmd->rect);
	smr.format  = buff_screen->format;
	smr.col1    = cmd->color2[0];
	smr.col2    = cmd->color2[1];
	smr.margin  = cmd->num;

	sdl_surface_mapp(buff_screen, selection, replace, &smr);
	return (OK);
} /* cimp_color_replace */
