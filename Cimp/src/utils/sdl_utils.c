#include "cimp.h"

static void sdl_bound_result_in_surface(SDL_Surface * surf, SDL_Rect * res) {
	res->x = max(0, res->x);
	res->y = max(0, res->y);
	res->x = min(surf->w, res->x);
	res->y = min(surf->h, res->y);
	res->w = min(surf->w, res->x + res->w) - res->x;
	res->h = min(surf->h, res->y + res->h) - res->y;
}

SDL_Rect    sdl_surface_build_good_selection(SDL_Surface * surf, SDL_Rect param) {
	SDL_Rect res;

	if (param.x != -1) { // isset
		res = param;
	}
	else if (g_cimp->select != NULL) {
		res = g_cimp->select->surface;
	}
	else {
		res.x = 0;
		res.y = 0;
		res.w = surf->w;
		res.h = surf->h;
	}
	sdl_bound_result_in_surface(surf, &res);
	return (res);
}

// void        sdl_surface_map(SDL_Surface * surf, SDL_Rect rect, uint32_t (* fp)(uint32_t)) {
//  if (SDL_MUSTLOCK(surf))
//      SDL_LockSurface(surf);

//  uint32_t * pixels = (uint32_t *) surf->pixels;

//  for (int i = rect.x; i < rect.x + rect.w; ++i) {
//      for (int j = rect.y; j < rect.y + rect.h; ++j) {
//          pixels[j * surf->w + i] = fp(pixels[j * surf->w + i]);
//      }
//  }

//  if (SDL_MUSTLOCK(surf))
//      SDL_UnlockSurface(surf);
// }

void        sdl_surface_mapp(SDL_Surface * surf, SDL_Rect rect,
  uint32_t (* fp)(uint32_t, void *), void * v) {
	if (SDL_MUSTLOCK(surf))
		SDL_LockSurface(surf);

	uint32_t * pixels = (uint32_t *) surf->pixels;

	for (int i = rect.x; i < rect.x + rect.w; ++i) {
		for (int j = rect.y; j < rect.y + rect.h; ++j) {
			pixels[j * surf->w + i] = fp(pixels[j * surf->w + i], v);
		}
	}

	if (SDL_MUSTLOCK(surf))
		SDL_UnlockSurface(surf);
}
