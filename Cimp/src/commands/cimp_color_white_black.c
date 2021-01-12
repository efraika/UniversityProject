#include "cimp.h"

struct  s_meta_white_black {
	SDL_PixelFormat * format;
	int               margin;
};

static uint32_t white_black(uint32_t col, void * smwbv) {
	uint8_t r, g, b;
	struct s_meta_white_black * smwbp;
	uint16_t gray;

	smwbp = (struct s_meta_white_black *) smwbv;
	SDL_GetRGB(col, smwbp->format, &r, &g, &b);
	gray  = r + g + b;
	gray /= 3;
	if (gray <= smwbp->margin) {
		return (SDL_MapRGB(smwbp->format, 0, 0, 0));
	}
	return (SDL_MapRGB(smwbp->format, 255, 255, 255));
}

t_rc_cmd cimp_color_white_black(t_cmd * cmd) {
	SDL_Surface * buff_screen;
	SDL_Rect selection;
	struct s_meta_white_black smwb;

	buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	selection   = sdl_surface_build_good_selection(buff_screen, cmd->rect);
	smwb.format = buff_screen->format;
	smwb.margin = cmd->num;

	if (smwb.margin > 255)
		smwb.margin = 255;
	else if (smwb.margin < 0)
		smwb.margin = 0;
	else if (smwb.margin == 0)
		smwb.margin = 128;


	sdl_surface_mapp(buff_screen, selection, white_black, &smwb);
	return (OK);
} /* cimp_color_white_black */
