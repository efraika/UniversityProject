#include "libtest.h"

START_TEST(arg_type) {
	ck_assert_str_eq("NAME", arg_type_to_string(ARG_NAME));
	ck_assert_str_eq("NUM", arg_type_to_string(ARG_NUM));
	ck_assert_str_eq("PATH", arg_type_to_string(ARG_PATH));
	ck_assert_str_eq("RECT", arg_type_to_string(ARG_RECT));
	ck_assert_str_eq("POINT", arg_type_to_string(ARG_PT));
	ck_assert_str_eq("COLOR", arg_type_to_string(ARG_COLOR));
	ck_assert_str_eq("COLOR2", arg_type_to_string(ARG_COLOR2));
} END_TEST;

TCase * meta_test() {
	TCase * tc_meta = tcase_create("Meta");

	tcase_add_test(tc_meta, arg_type);
	return tc_meta;
}
