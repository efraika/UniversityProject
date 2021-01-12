#include "libtest.h"

START_TEST(strjoin_test) {
	char * str1 = "Un";
	char * str2 = "Deux";
	char * res  = strjoin(str1, str2);

	ck_assert_str_eq(res, "UnDeux");
} END_TEST;

START_TEST(nb_digit_test10) {
	ck_assert(nb_digit(10) == 2);
} END_TEST;

START_TEST(nb_digit_test1000) {
	ck_assert(nb_digit(1000) == 4);
} END_TEST;

START_TEST(nb_digit_test999) {
	ck_assert(nb_digit(999) == 3);
} END_TEST;

START_TEST(nb_digit_test1000000000) {
	ck_assert(nb_digit(1000000000) == 10);
} END_TEST;

START_TEST(nb_digit_test9998554) {
	ck_assert(nb_digit(9998554) == 7);
} END_TEST;

START_TEST(nb_digit_test_neg4571) {
	ck_assert(nb_digit(-4571) == 4);
} END_TEST;

START_TEST(nb_digit_test0) {
	ck_assert(nb_digit(0) == 1);
} END_TEST;

START_TEST(itoa_testpos) {
	char * a = itoa(254);

	ck_assert_str_eq(a, "254");
	free(a);
} END_TEST;

START_TEST(itoa_test0pos) {
	char * a = itoa(0);

	ck_assert_str_eq(a, "0");
	free(a);
} END_TEST;

START_TEST(itoa_test0neg) {
	char * a = itoa(-0);

	ck_assert_str_eq(a, "0");
	free(a);
} END_TEST;

START_TEST(itoa_testneg) {
	char * a = itoa(-18948);

	ck_assert_str_eq(a, "-18948");
	free(a);
} END_TEST;

START_TEST(dupstr_test_null) {
	ck_assert(dupstr(NULL) == NULL);
} END_TEST;

TCase * utils_test() {
	TCase * tc_utils = tcase_create("Utils");

	tcase_add_test(tc_utils, strjoin_test);
	tcase_add_test(tc_utils, nb_digit_test10);
	tcase_add_test(tc_utils, nb_digit_test1000);
	tcase_add_test(tc_utils, nb_digit_test999);
	tcase_add_test(tc_utils, nb_digit_test1000000000);
	tcase_add_test(tc_utils, nb_digit_test9998554);
	tcase_add_test(tc_utils, nb_digit_test0);
	tcase_add_test(tc_utils, nb_digit_test_neg4571);
	tcase_add_test(tc_utils, itoa_testpos);
	tcase_add_test(tc_utils, itoa_test0pos);
	tcase_add_test(tc_utils, itoa_testneg);
	tcase_add_test(tc_utils, itoa_test0neg);
	tcase_add_test(tc_utils, dupstr_test_null);

	return tc_utils;
}
