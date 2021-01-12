#include "libtest.h"

t_bool compareSurfaces(SDL_Surface * surf1, SDL_Surface * surf2) {
	if (surf1 == NULL || surf2 == NULL)
		return (FALSE);

	if (surf1->w != surf2->w || surf1->h != surf2->h)
		return (FALSE);

	uint32_t * surf1_pixels = (uint32_t *) surf1->pixels;
	uint32_t * surf2_pixels = (uint32_t *) surf2->pixels;
	for (int i = 0; i < surf2->w * surf2->h; i++) {
		if (surf1_pixels[i] != surf2_pixels[i])
			return (FALSE);
	}
	return (TRUE);
}

SDL_Surface * copySurface(SDL_Surface * surf) {
	SDL_Surface * copy;
	SDL_Rect rect;

	rect.x = 0;
	rect.y = 0;
	rect.w = surf->w;
	rect.h = surf->h;
	copy   = SDL_CreateRGBSurface(0, surf->w, surf->h, 32, 0, 0, 0, 0);
	SDL_BlitSurface(surf, &rect, copy, &rect);
	return (copy);
}

SDL_Surface * genSurface(const int w, const int h) {
	SDL_Surface * surf;
	uint32_t * pixels;

	surf   = SDL_CreateRGBSurface(0, w, h, 32, 0, 0, 0, 0);
	pixels = (uint32_t *) surf->pixels;

	for (int i = 0; i < w * h; ++i) {
		pixels[i] = SDL_MapRGBA(surf->format, i, 255 - i, 128 + i, 0);
	}
	return (surf);
}
