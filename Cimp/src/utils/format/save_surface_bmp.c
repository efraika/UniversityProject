#include "cimp.h"

int save_surface_bmp(const char * file, SDL_Surface * surface) {
	int ret;

	if ((ret = SDL_SaveBMP(surface, file)) < 0)
		printf("BMP export : %s\n", SDL_GetError());
	return (ret);
}
