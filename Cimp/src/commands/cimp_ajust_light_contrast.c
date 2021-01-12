#include "cimp.h"

// Link to article
// https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/

struct  s_meta_ajust {
	SDL_PixelFormat * format;
	float             factor;
};

static uint8_t bound_8bit(float f) {
	if (f < 0)
		return (0);

	if (f > 255)
		return (255);

	return (uint8_t) roundf(f);
}

static uint32_t ajust(uint32_t col, void * metav) {
	uint8_t r, g, b;
	float rf, gf, bf;
	struct s_meta_ajust * meta;

	meta = (struct s_meta_ajust *) metav;
	SDL_GetRGB(col, meta->format, &r, &g, &b);
	rf = meta->factor * ((float) r - 128) + 128;
	gf = meta->factor * ((float) g - 128) + 128;
	bf = meta->factor * ((float) b - 128) + 128;
	r  = bound_8bit(rf);
	g  = bound_8bit(gf);
	b  = bound_8bit(bf);
	return SDL_MapRGB(meta->format, r, g, b);
}

t_rc_cmd cimp_ajust_light_contrast(t_cmd * cmd) {
	SDL_Surface * buff_screen;
	SDL_Rect selection;
	struct s_meta_ajust meta;
	int num;

	buff_screen = g_cimp->screen[cmd->focus]->buff_screen;
	selection   = sdl_surface_build_good_selection(buff_screen, cmd->rect);

	num = cmd->num;
	if (num > 255)
		num = 255;
	else if (num < -255)
		num = -255;
	meta.format = buff_screen->format;
	meta.factor = (float) (259 * (num + 255)) / (float) (255 * (259 - num));

	sdl_surface_mapp(buff_screen, selection, ajust, &meta);
	return (OK);
} /* cimp_fill */
