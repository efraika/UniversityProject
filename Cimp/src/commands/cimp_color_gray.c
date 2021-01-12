#include "cimp.h"

static uint32_t to_gray(uint32_t col, void * format_pixel) {
	uint8_t r, g, b;
	uint16_t gray;
	SDL_PixelFormat * format;

	format = (SDL_PixelFormat *) format_pixel;
	SDL_GetRGB(col, format, &r, &g, &b);
	gray  = r + g + b;
	gray /= 3;
	return SDL_MapRGB(format, gray, gray, gray);
}

t_rc_cmd cimp_color_gray(t_cmd * cmd) {
	SDL_Surface * buff_screen;
	SDL_Rect selection;

	buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	selection   = sdl_surface_build_good_selection(buff_screen, cmd->rect);

	sdl_surface_mapp(buff_screen, selection, to_gray, buff_screen->format);
	return (OK);
} /* cimp_color_gray */
