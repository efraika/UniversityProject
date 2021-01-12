#include "cimp.h"

static void     fill_png(png_structp png_ptr, SDL_Surface * surface, png_bytep row);

int     save_surface_png(const char * file, SDL_Surface * surface) {
	int ret;

	FILE * fp;
	png_structp png_ptr;
	png_infop info_ptr;
	png_bytep row;

	png_ptr  = NULL;
	info_ptr = NULL;
	row      = NULL;
	ret      = 1; // Failure

	if ((fp = fopen(file, "wbe")) == NULL) {
		printf("PNG export : %s\n", strerror(errno));
	}
	else if ((png_ptr = png_create_write_struct(PNG_LIBPNG_VER_STRING, NULL, NULL, NULL)) == NULL) {
		printf("PNG export : Could not allocate write struct\n");
	}
	else if ((info_ptr = png_create_info_struct(png_ptr)) == NULL) {
		printf("PNG export : Could not create info struct\n");
	}
	else if (setjmp(png_jmpbuf(png_ptr))) {
		printf("PNG export : unexpected setjmp failed\n");
	}
	else if ((row = (png_bytep) malloc(4 * surface->w * sizeof(png_byte))) == NULL) {
		printf("PNG export : Malloc fails\n");
	}
	else {
		png_init_io(png_ptr, fp);

		png_set_IHDR(png_ptr, info_ptr, surface->w, surface->h, 8, PNG_COLOR_TYPE_RGBA,
		  PNG_INTERLACE_NONE, PNG_COMPRESSION_TYPE_DEFAULT,
		  PNG_FILTER_TYPE_DEFAULT);

		png_write_info(png_ptr, info_ptr);

		fill_png(png_ptr, surface, row);

		png_write_end(png_ptr, NULL);
		ret = 0; // Success
	}

	if (fp != NULL) fclose(fp);
	if (info_ptr != NULL) png_free_data(png_ptr, info_ptr, PNG_FREE_ALL, -1);
	if (png_ptr != NULL) png_destroy_write_struct(&png_ptr, (png_infopp) NULL);
	if (row != NULL) free(row);

	return (ret);
} /* save_surface_png */

static void     fill_png(png_structp png_ptr, SDL_Surface * surface, png_bytep row) {
	uint32_t * pixels;
	uint8_t r;
	uint8_t g;
	uint8_t b;
	uint8_t a;

	if (SDL_MUSTLOCK(surface))
		SDL_LockSurface(surface);

	pixels = (uint32_t *) surface->pixels;
	for (int j = 0; j < surface->h; j++) {
		for (int i = 0; i < surface->w; i++) {
			SDL_GetRGBA(
			  pixels[j * surface->w + i],
			  surface->format,
			  &r, &g, &b, &a);

			row[4 * i]     = r;
			row[4 * i + 1] = g;
			row[4 * i + 2] = b;
			row[4 * i + 3] = a;
		}

		png_write_row(png_ptr, row);
	}

	if (SDL_MUSTLOCK(surface))
		SDL_UnlockSurface(surface);
}
