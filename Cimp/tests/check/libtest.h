#ifndef LIBTEST_H
#define	LIBTEST_H

#include "cimp.h"
#include <check.h>

TCase *modif_test();
TCase *window_test();
TCase *parsing_test();
TCase *util_images_test();
TCase *save_test();
TCase *meta_test();
TCase *utils_test();
TCase *core_test();

t_bool compareSurfaces(SDL_Surface * surf1, SDL_Surface * surf2);
SDL_Surface *copySurface(SDL_Surface * surf);
SDL_Surface *genSurface(const int w, const int h);

#endif
