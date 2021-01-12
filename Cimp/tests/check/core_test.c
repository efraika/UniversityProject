#include "libtest.h"

START_TEST(test_focus) {
	g_cimp->focus = -1;
	treat_line("open images/untitled5.bmp");
	treat_line("open images/untitled17.bmp");
	treat_line("focus [1]");
	ck_assert(g_cimp->focus == 0);
	treat_line("focus [2]");
	ck_assert(g_cimp->focus == 1);
} END_TEST;


TCase * core_test() {
	TCase * tc_window = tcase_create("Core tests");

	tcase_add_test(tc_window, test_focus);
	return tc_window;
}
