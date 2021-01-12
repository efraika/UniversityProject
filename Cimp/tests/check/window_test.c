#include "libtest.h"

START_TEST(test_close) {
	t_bool must_abort;

	g_cimp->screen[0] = cimp_screen_init("images/untitled17.bmp", &must_abort);
	g_cimp->focus     = 0;
	ck_assert(g_cimp->screen[0] && must_abort == FALSE);
	treat_line("close");
	ck_assert(g_cimp->screen[0] == NULL);
	g_cimp->screen[0] = cimp_screen_init("images/untitled17.bmp", &must_abort);
	ck_assert(must_abort == FALSE);
	g_cimp->screen[1] = cimp_screen_init("images/untitled5.bmp", &must_abort);
	ck_assert(must_abort == FALSE);
	g_cimp->focus = 1;
	ck_assert(g_cimp->screen[0]);
	ck_assert(g_cimp->screen[1]);
	treat_line("close [1]");
	ck_assert(g_cimp->screen[0] == NULL);
	treat_line("close");
	ck_assert(g_cimp->screen[1] == NULL);
} END_TEST;

START_TEST(test_open) {
	ck_assert(cimp_open(NULL) == FAIL);
	treat_line("open images/untitled17.bmp");
	ck_assert(g_cimp->screen[0] != NULL && g_cimp->screen[0]->buff_screen != NULL &&
	  strcmp("images/untitled17.bmp", g_cimp->screen[0]->path) && g_cimp->focus == 0);
	treat_line("open images/untitled5.bmp");
	ck_assert(strcmp("images/untitled17.bmp", g_cimp->screen[0]->path));
	ck_assert(g_cimp->screen[1] != NULL && g_cimp->focus == 1);
} END_TEST;

TCase * window_test() {
	TCase * tc_window = tcase_create("Ouverture et fermeture");

	tcase_add_test(tc_window, test_open);
	tcase_add_test(tc_window, test_close);
	return tc_window;
}
