#include "cimp.h"

t_export_img_func_ptr   get_func_img(t_type_img type) {
	for (size_t i = 0; i < g_assoc_type_img_func_size; ++i) {
		if (g_assoc_type_img_func_list[i].type == type)
			return (g_assoc_type_img_func_list[i].func_export);
	}
	return (NULL);
}
