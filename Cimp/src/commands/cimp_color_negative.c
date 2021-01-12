#include "cimp.h"

static uint32_t to_negative(uint32_t col, void * format_pixel) {
	uint8_t r, g, b;
	SDL_PixelFormat * format;

	format = (SDL_PixelFormat *) format_pixel;
	SDL_GetRGB(col, format, &r, &g, &b);
	return SDL_MapRGB(format, 255 - r, 255 - g, 255 - b);
}

t_rc_cmd cimp_color_negative(t_cmd * cmd) {
	SDL_Surface * buff_screen;
	SDL_Rect selection;

	buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	selection   = sdl_surface_build_good_selection(buff_screen, cmd->rect);

	sdl_surface_mapp(buff_screen, selection, to_negative, buff_screen->format);
	return (OK);
} /* cimp_color_negative */
