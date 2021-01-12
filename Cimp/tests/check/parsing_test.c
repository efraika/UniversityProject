#include "libtest.h"

START_TEST(test_quit) {
	treat_line("QUIT");
	ck_assert_int_eq(g_cimp->running, 0);
} END_TEST;

START_TEST(empty_line) {
	treat_line("");
	ck_assert(g_cimp->running);
} END_TEST;

TCase * parsing_test() {
	TCase * tc_parsing = tcase_create("Parsing");

	tcase_add_test(tc_parsing, empty_line);
	tcase_add_test(tc_parsing, test_quit);
	return tc_parsing;
}
