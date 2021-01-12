#include "libtest.h"

START_TEST(type_test_NULL) {
	ck_assert(get_type_img(NULL) == (NO_SUPPORTED));
} END_TEST;

START_TEST(type_test_Dossier) {
	char * str = malloc(sizeof(char) * 100);

	strcpy(str, "images");
	ck_assert(get_type_img(str) == (NO_SUPPORTED));
	free(str);
} END_TEST;

START_TEST(type_test_sanstype) {
	char * str = malloc(sizeof(char) * 100);

	strcpy(str, "images.");
	ck_assert(get_type_img(str) == (NO_SUPPORTED));
	free(str);
} END_TEST;

START_TEST(type_test_PNG) {
	char * str = malloc(sizeof(char) * 100);

	strcpy(str, "images.PNG");
	ck_assert(get_type_img(str) == g_extension_img_list[1].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.png");
	ck_assert(get_type_img(str) == g_extension_img_list[1].type);
	free(str);
} END_TEST;

START_TEST(type_test_BMP) {
	char * str = malloc(sizeof(char) * 100);

	strcpy(str, "images.BMP");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.bmp");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.DIB");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.dib");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.RLE");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.rle");
	ck_assert(get_type_img(str) == g_extension_img_list[0].type);
	free(str);
} END_TEST;

START_TEST(type_test_JPG) {
	char * str = malloc(sizeof(char) * 100);

	strcpy(str, "images.JPG");
	ck_assert(get_type_img(str) == g_extension_img_list[2].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.jpg");
	ck_assert(get_type_img(str) == g_extension_img_list[2].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.JPEG");
	ck_assert(get_type_img(str) == g_extension_img_list[2].type);
	free(str);
	str = malloc(sizeof(char) * 100);
	strcpy(str, "images.jpeg");
	ck_assert(get_type_img(str) == g_extension_img_list[2].type);
	free(str);
} END_TEST;

TCase * util_images_test() {
	TCase * tc_utils_images = tcase_create("Utils images");

	tcase_add_test(tc_utils_images, type_test_NULL);
	tcase_add_test(tc_utils_images, type_test_Dossier);
	tcase_add_test(tc_utils_images, type_test_sanstype);
	tcase_add_test(tc_utils_images, type_test_PNG);
	tcase_add_test(tc_utils_images, type_test_BMP);
	tcase_add_test(tc_utils_images, type_test_JPG);

	return tc_utils_images;
}
