#include "libtest.h"

static void exec(SDL_Surface ** surf, char ** lines, size_t len) {
	g_cimp->screen[1] = malloc(sizeof(t_cimp_screen) * NB_SCREENS);
	g_cimp->screen[1]->buff_screen = *surf;
	g_cimp->focus = 1;
	treat_line("unselect");
	for (size_t i = 0; i < len; ++i)
		treat_line(lines[i]);
	*surf = g_cimp->screen[1]->buff_screen;
	free(g_cimp->screen[1]);
	g_cimp->screen[1] = NULL;
}

static void test_img(
  char *    line,
  const int w,
  const int h,
  const int point,
  const int w_new,
  const int h_new,
  const int point_new) {
	SDL_Surface * surf;
	uint32_t * pixels;
	uint32_t value;

	surf   = genSurface(w, h);
	pixels = (uint32_t *) surf->pixels;

	value = pixels[point];
	exec(&surf, &line, 1);
	ck_assert(surf->w == w_new);
	ck_assert(surf->h == h_new);
	pixels = (uint32_t *) surf->pixels;
	ck_assert(pixels[point_new] == value);

	SDL_FreeSurface(surf);
}

static void test_idempotent(
  char ** lines,
  size_t  len_lines) {
	SDL_Surface * surf;
	SDL_Surface * copy;
	const int w = 50;
	const int h = 50;

	surf = genSurface(w, h);
	copy = copySurface(surf);

	exec(&surf, lines, len_lines);

	ck_assert(compareSurfaces(surf, copy));

	SDL_FreeSurface(copy);
	SDL_FreeSurface(surf);
}

static void test_same_treatment(
  char ** lines1,
  size_t  len_lines1,
  char ** lines2,
  size_t  len_lines2) {
	SDL_Surface * surf1;
	SDL_Surface * surf2;
	const int w = 50;
	const int h = 50;

	surf1 = genSurface(w, h);
	surf2 = genSurface(w, h);

	exec(&surf1, lines1, len_lines1);
	exec(&surf2, lines2, len_lines2);

	ck_assert(compareSurfaces(surf1, surf2));

	SDL_FreeSurface(surf1);
	SDL_FreeSurface(surf2);
}

static void test_expected_pixels(
  char **     lines,
  size_t      len_lines,
  SDL_Color * inputs,
  SDL_Color * outputs,
  size_t      nb_pixels) {
	SDL_Surface * surf;
	SDL_Surface * expected;
	uint32_t * pixels;

	surf   = genSurface(nb_pixels, 1);
	pixels = (uint32_t *) surf->pixels;

	for (size_t i = 0; i < nb_pixels; ++i)
		pixels[i] = SDL_MapRGBA(surf->format, inputs[i].r, inputs[i].g, inputs[i].b, inputs[i].a);

	expected = genSurface(nb_pixels, 1);
	pixels   = (uint32_t *) expected->pixels;

	for (size_t i = 0; i < nb_pixels; ++i)
		pixels[i] =
		  SDL_MapRGBA(surf->format, outputs[i].r, outputs[i].g, outputs[i].b, outputs[i].a);

	exec(&surf, lines, len_lines);

	ck_assert(compareSurfaces(surf, expected));

	SDL_FreeSurface(surf);
	SDL_FreeSurface(expected);
}

START_TEST(test_rotate) {
	test_img("rotate 45", 3, 2, 0, 3, 2, 0);
	test_img("rotate 0", 3, 2, 0, 3, 2, 0);
	test_img("rotate 720", 3, 2, 0, 3, 2, 0);
	test_img("rotate 90", 3, 2, 0, 2, 3, 1);
	test_img("rotate 180", 3, 2, 0, 3, 2, 5);
	test_img("rotate 270", 3, 2, 0, 2, 3, 4);
	test_img("rotate -90", 3, 2, 0, 2, 3, 4);

	test_idempotent((char *[]) {"rotate 0"}, 1);
	test_idempotent((char *[]) {"rotate 0", "rotate 0"}, 2);
	test_idempotent((char *[]) {"rotate 360"}, 1);
	test_idempotent((char *[]) {"rotate 720"}, 1);
	test_idempotent((char *[]) {"rotate -360"}, 1);

	test_same_treatment((char *[]) {"rotate 0"}, 1,
	  (char *[]) {"rotate 360"}, 1);
	test_same_treatment((char *[]) {"rotate 90"}, 1,
	  (char *[]) {"rotate -270"}, 1);
	test_same_treatment((char *[]) {"rotate 180"}, 1,
	  (char *[]) {"rotate -180"}, 1);
	test_same_treatment((char *[]) {"rotate 270"}, 1,
	  (char *[]) {"rotate -90"}, 1);
} END_TEST;

START_TEST(test_sym) {
	test_img("sym_verti", 3, 2, 0, 3, 2, 2);
	test_img("sym_hori", 3, 2, 0, 3, 2, 3);

	test_idempotent((char *[]) {"sym_verti", "sym_verti"}, 2);
	test_idempotent((char *[]) {"sym_hori", "sym_hori"}, 2);
	test_idempotent((char *[]) {"sym_hori", "sym_hori", "sym_verti", "sym_verti"}, 4);
	test_idempotent((char *[]) {"sym_hori", "sym_verti", "sym_hori", "sym_verti"}, 4);
} END_TEST;

START_TEST(test_fill) {
	test_same_treatment((char *[]) {"fill 0xFF0000", "fill 0x00FF00"}, 2,
	  (char *[]) {"fill 0x00FF00"}, 1);

	test_same_treatment((char *[]) {"select (0 0 20 20)", "fill 0xFF0000", "unselect",
									"fill 0x00FF00"}, 4,
	  (char *[]) {"fill 0x00FF00"}, 1);

	test_same_treatment((char *[]) {"fill 0xFF0000 (0 0 20 20)", "fill 0x00FF00"}, 2,
	  (char *[]) {"fill 0x00FF00"}, 1);

	test_same_treatment((char *[]) {"fill 0xFF0000 (0 0 20 20)", "fill (0 0 25 25) 0x00FF00"}, 2,
	  (char *[]) {"fill 0x00FF00 (0 0 25 25)"}, 1);
} END_TEST;

START_TEST(test_gray) {
	test_expected_pixels((char *[]) {"color_gray"}, 1,
	  (SDL_Color []) {{80, 100, 120, 0}, {10, 15, 35, 0}, {243, 250, 255, 0}},
	  (SDL_Color []) {{100, 100, 100, 0}, {20, 20, 20, 0}, {249, 249, 249, 0}},
	  3);

	test_idempotent((char *[]) {"color_negative", "color_negative"}, 2);
} END_TEST;

START_TEST(test_negative) {
	test_expected_pixels((char *[]) {"color_negative"}, 1,
	  (SDL_Color []) {{100, 110, 120, 0}, {0, 255, 128, 0}},
	  (SDL_Color []) {{155, 145, 135, 0}, {255, 0, 127, 0}},
	  2);

	test_same_treatment((char *[]) {"color_gray", "color_gray"}, 2,
	  (char *[]) {"color_gray"}, 1);
} END_TEST;


START_TEST(test_replace) {
	test_expected_pixels((char *[]) {"color_replace (0x00FFFF -> 0xFF0000) 16"}, 1,
	  (SDL_Color []) {{16, 255, 255, 0}, {17, 255, 255, 0}},
	  (SDL_Color []) {{255, 0, 0, 0}, {17, 255, 255, 0}},
	  2);
} END_TEST;

START_TEST(test_white_black) {
	test_expected_pixels((char *[]) {"color_white_black 80"}, 1,
	  (SDL_Color []) {{80, 100, 120, 0}, {10, 15, 35, 0}, {243, 250, 255, 0}},
	  (SDL_Color []) {{255, 255, 255, 0}, {0, 0, 0, 0}, {255, 255, 255, 0}},
	  3);
} END_TEST;

START_TEST(test_ajust_light_contrast) {
	SDL_Surface * surf;
	uint32_t * pixels_surf;

	test_idempotent((char *[]) {"ajust_light_contrast 0"}, 1);

	// ajust light contrast 1000
	surf = genSurface(50, 50);

	exec(&surf, (char *[]) {"ajust_light_contrast 1000"}, 1);

	pixels_surf = (uint32_t *) surf->pixels;

	for (int i = 0; i < 50 * 50; ++i) {
		uint8_t rs, gs, bs;

		SDL_GetRGB(pixels_surf[i], surf->format, &rs, &gs, &bs);

		ck_assert(rs == 255 || rs == 128 || rs == 0);
		ck_assert(gs == 255 || gs == 128 || gs == 0);
		ck_assert(bs == 255 || bs == 128 || bs == 0);
	}

	SDL_FreeSurface(surf);

	// ajust light contrast -1000
	surf = genSurface(50, 50);

	exec(&surf, (char *[]) {"ajust_light_contrast -1000"}, 1);

	pixels_surf = (uint32_t *) surf->pixels;

	for (int i = 0; i < 50 * 50; ++i) {
		uint8_t rs, gs, bs;

		SDL_GetRGB(pixels_surf[i], surf->format, &rs, &gs, &bs);

		ck_assert(rs == 128);
		ck_assert(gs == 128);
		ck_assert(bs == 128);
	}

	SDL_FreeSurface(surf);
} END_TEST;

START_TEST(test_cut_copy_paste) {
	test_idempotent((char *[]) {"cut (0 0 25 25)", "paste (0 0)"}, 2);
	test_idempotent((char *[]) {"copy (20 20 15 27)", "paste (20 20)"}, 2);
	test_idempotent((char * []) {"cut (10 29 15 14)", "paste (10 29)"}, 2);
	test_idempotent((char * []) {"copy (47 35 3 15)", "paste (47 35)"}, 2);

	test_same_treatment((char *[]) {"copy (15 16 22 15)", "paste (14 12)"}, 2,
	  (char *[]) {"select (15 16 22 15)", "copy", "paste (14 12)"}, 3);
	test_same_treatment((char *[]) {"cut (5 2 17 28)", "paste (4 32)"}, 2,
	  (char *[]) {"select (5 2 17 28)", "cut", "paste (4 32)"}, 3);

	test_same_treatment((char *[]) {"cut (0 0 10 10)", "paste (0 0)"}, 2,
	  (char *[]) {"cut (10 10 10 10)", "paste (10 10)"}, 2);
	test_same_treatment((char * []) {"copy (0 0 10 10)", "paste (10 10)"}, 2,
	  (char * []) {"cut (0 0 10 10)", "paste (0 0)", "paste (10 10)"}, 3);
	test_same_treatment((char * []) {"copy (0 0 10 10)", "paste (48 48)"}, 2,
	  (char * []) {"cut (0 0 10 10)", "paste (0 0)", "paste (48 48)"}, 3);

	test_same_treatment((char *[]) {"cut (3 5 7 22)"}, 1,
	  (char *[]) {"fill 0x000000 (3 5 7 22)"}, 1);
	test_same_treatment((char *[]) {"cut (42 14 8 22)"}, 1,
	  (char *[]) {"fill 0x000000 (42 14 8 22)"}, 1);
} END_TEST;

START_TEST(test_scale) {
	test_same_treatment((char *[]) {"scale_rect (100 100)"}, 1,
	  (char * []) {"scale_ratio 2"}, 1);
	test_same_treatment((char *[]) {"scale_rect (500 500)"}, 1,
	  (char * []) {"scale_ratio 10"}, 1);

	test_idempotent((char * []) {"scale_rect (100 100)", "scale_rect (50 50)"}, 2);
	test_idempotent((char * []) {"scale_ratio 2", "scale_rect (50 50)"}, 2);
} END_TEST;

START_TEST(test_crop_reduce) {
	test_same_treatment((char *[]) {"crop_reduce (10 10 10 10)"}, 1,
	  (char * []) {"select (10 10 10 10)", "crop_reduce"}, 2);
} END_TEST;

TCase * modif_test() {
	TCase * tc_modif = tcase_create("Modif images");

	tcase_add_test(tc_modif, test_rotate);
	tcase_add_test(tc_modif, test_sym);
	tcase_add_test(tc_modif, test_fill);
	tcase_add_test(tc_modif, test_gray);
	tcase_add_test(tc_modif, test_negative);
	tcase_add_test(tc_modif, test_replace);
	tcase_add_test(tc_modif, test_white_black);
	tcase_add_test(tc_modif, test_ajust_light_contrast);
	tcase_add_test(tc_modif, test_cut_copy_paste);
	tcase_add_test(tc_modif, test_scale);
	tcase_add_test(tc_modif, test_crop_reduce);
	return tc_modif;
}
