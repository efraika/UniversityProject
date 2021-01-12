#include "cimp.h"

t_type_img      get_type_img(char * filepath) {
	char * ext_ptr;
	char * ext;
	size_t len;

	if (filepath == NULL)
		return (NO_SUPPORTED);

	if ((ext_ptr = basename(filepath)) == NULL) {
		return (NO_SUPPORTED);
	}
	if ((ext_ptr = strrchr(ext_ptr, '.')) == NULL) {
		printf("Cannot infer image format from name\n");
		return (NO_SUPPORTED);
	}
	ext_ptr++;
	len = strlen(ext_ptr) + 1;
	if ((ext = (char *) malloc(sizeof(char) * len)) == NULL) {
		printf("%s\n", MALLOC_FAIL);
		return (NO_SUPPORTED);
	}
	memset(ext, 0, len);
	for (size_t i = 0; i < len - 1; ++i) {
		ext[i] = tolower(ext_ptr[i]);
	}

	for (size_t i = 0; i < g_extension_img_size; ++i) {
		for (size_t j = 0; j < g_extension_img_list[i].len_nb_extensions; j++) {
			if (strcmp(ext, g_extension_img_list[i].li_extensions[j]) == 0) {
				free(ext);
				return (g_extension_img_list[i].type);
			}
		}
	}

	free(ext);
	return (NO_SUPPORTED);
} /* get_type_img */
