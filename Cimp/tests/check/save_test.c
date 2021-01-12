#include "libtest.h"

START_TEST(test_bmp) {
	treat_line("open images/untitled5.bmp");
	treat_line("save /tmp/test.bmp");

	ck_assert(system("file -i /tmp/test.bmp | grep image/x-ms-bmp") == 0);
	ck_assert(system("file -i /tmp/test.bmp | grep image/png") != 0);
	ck_assert(system("file -i /tmp/test.bmp | grep image/jpeg") != 0);
} END_TEST;

START_TEST(test_png) {
	treat_line("open images/untitled5.bmp");
	treat_line("save /tmp/test.png");

	ck_assert(system("file -i /tmp/test.png | grep image/x-ms-bmp") != 0);
	ck_assert(system("file -i /tmp/test.png | grep image/png") == 0);
	ck_assert(system("file -i /tmp/test.png | grep image/jpeg") != 0);
} END_TEST;

START_TEST(test_jpeg) {
	treat_line("open images/untitled5.bmp");
	treat_line("save /tmp/test.jpeg");

	ck_assert(system("file -i /tmp/test.jpeg | grep image/x-ms-bmp") != 0);
	ck_assert(system("file -i /tmp/test.jpeg | grep image/png") != 0);
	ck_assert(system("file -i /tmp/test.jpeg | grep image/jpeg") == 0);
} END_TEST;

TCase * save_test() {
	TCase * tc_save = tcase_create("Saving");

	tcase_add_test(tc_save, test_bmp);
	tcase_add_test(tc_save, test_png);
	tcase_add_test(tc_save, test_jpeg);
	return tc_save;
}
