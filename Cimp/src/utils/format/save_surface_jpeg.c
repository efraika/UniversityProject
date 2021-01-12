#include "cimp.h"

static void     fill_jpeg(struct jpeg_compress_struct * cinfo, SDL_Surface * surface,
  uint8_t * line);

int     save_surface_jpeg(const char * file, SDL_Surface * surface) {
	int ret;

	FILE * fp;
	struct jpeg_compress_struct cinfo;
	struct jpeg_error_mgr jerr;
	uint8_t * line;

	ret  = 1; // Failure
	line = NULL;

	cinfo.err = jpeg_std_error(&jerr);
	jpeg_create_compress(&cinfo);

	if ((fp = fopen(file, "wbe")) == NULL) {
		printf("JPEG export : %s\n", strerror(errno));
	}
	else if ((line = (uint8_t *) malloc(surface->w * 3)) == NULL) {
		printf("JPEG export : malloc fails\n");
	}
	else {
		jpeg_stdio_dest(&cinfo, fp);

		cinfo.image_width      = surface->w;
		cinfo.image_height     = surface->h;
		cinfo.input_components = 3;
		cinfo.in_color_space   = JCS_RGB;

		jpeg_set_defaults(&cinfo);

		jpeg_start_compress(&cinfo, FALSE);

		fill_jpeg(&cinfo, surface, line);

		jpeg_finish_compress(&cinfo);

		ret = 0; // Success

		fclose(fp);
		fp = NULL;
	}

	jpeg_destroy_compress(&cinfo);
	if (fp != NULL) fclose(fp);
	if (line != NULL) free(line);

	return (ret);
} /* save_surface_jpeg */

static void fill_jpeg(struct jpeg_compress_struct * cinfo, SDL_Surface * surface, uint8_t * line) {
	uint32_t * pixels;
	uint8_t r;
	uint8_t g;
	uint8_t b;

	if (SDL_MUSTLOCK(surface))
		SDL_LockSurface(surface);

	pixels = (uint32_t *) surface->pixels;
	for (int j = 0; j < surface->h; j++) {
		for (int i = 0; i < surface->w; i++) {
			SDL_GetRGB(
			  pixels[j * surface->w + i],
			  surface->format,
			  &r, &g, &b);

			line[3 * i]     = r;
			line[3 * i + 1] = g;
			line[3 * i + 2] = b;
		}

		jpeg_write_scanlines(cinfo, &line, 1);
	}

	if (SDL_MUSTLOCK(surface))
		SDL_UnlockSurface(surface);
}
