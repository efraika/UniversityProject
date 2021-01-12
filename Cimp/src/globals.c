#include "cimp.h"

#define	SIZE_TAB(t) (sizeof(t) / sizeof(*(t)))

t_cimp * g_cimp = NULL;

// Take care to configure the opts properly
// No opts of type ARG_PATH | ARG_OPT_PATH
const t_cmd_config g_command_list[] = {
	{"help",                 cimp_help,                 0,                    0                             },
	{"open",                 cimp_open,                 ARG_PATH,             0                             },
	{"close",                cimp_close,                0,                    ARG_FOCUS                     },
	{"list",                 cimp_list,                 0,                    0                             },
	{"sym_verti",            cimp_sym_verti,            0,                    ARG_FOCUS                     },
	{"sym_hori",             cimp_sym_hori,             0,                    ARG_FOCUS                     },
	{"rotate",               cimp_rotate,               ARG_NUM,              ARG_FOCUS                     },
	{"select",               cimp_select,               ARG_RECT,             ARG_FOCUS                     },
	{"unselect",             cimp_unselect,             0,                    0                             },
	{"save",                 cimp_save,                 0,                    ARG_PATH | ARG_FOCUS          },
	{"update",               cimp_update,               0,                    ARG_PATH | ARG_FOCUS          },
	{"copy",                 cimp_copy,                 0,                    ARG_RECT | ARG_FOCUS          },
	{"cut",                  cimp_cut,                  0,                    ARG_RECT | ARG_FOCUS          },
	{"paste",                cimp_paste,                ARG_PT,               ARG_FOCUS                     },
	{"fill",                 cimp_fill,                 ARG_COLOR,            ARG_RECT | ARG_FOCUS          },
	{"color_replace",        cimp_color_replace,        ARG_COLOR2 | ARG_NUM, ARG_RECT | ARG_FOCUS          },
	{"color_negative",       cimp_color_negative,       0,                    ARG_RECT | ARG_FOCUS          },
	{"color_gray",           cimp_color_gray,           0,                    ARG_RECT | ARG_FOCUS          },
	{"color_white_black",    cimp_color_white_black,    0,                    ARG_RECT | ARG_FOCUS | ARG_NUM},
	{"ajust_light_contrast", cimp_ajust_light_contrast, ARG_NUM,              ARG_RECT | ARG_FOCUS          },
	{"focus",                cimp_focus,                0,                    ARG_FOCUS                     },
	{"scale_rect",           cimp_scale_rect,           ARG_PT,               ARG_FOCUS                     },
	{"scale_ratio",          cimp_scale_ratio,          ARG_NUM,              ARG_FOCUS                     },
	{"crop_reduce",          cimp_crop_reduce,          0,                    ARG_FOCUS | ARG_RECT          },
	{"crop_extend",          cimp_crop_extend,          ARG_RECT,             ARG_FOCUS | ARG_COLOR         },
	{"QUIT",                 NULL,                      0,                    0                             }, // QUIT must have NULL as func_ptr
};

const size_t g_command_list_size = SIZE_TAB(g_command_list);

static const char * bmp_list[] = {"bmp", "dib", "rle"};
static const char * png_list[] = {"png"};
static const char * jpg_list[] = {"jpg", "jpeg"};

const t_extension_img g_extension_img_list[] = {
	{BMP, (char **) bmp_list, SIZE_TAB(bmp_list)},
	{PNG, (char **) png_list, SIZE_TAB(png_list)},
	{JPG, (char **) jpg_list, SIZE_TAB(jpg_list)},
};

const size_t g_extension_img_size = SIZE_TAB(g_extension_img_list);

const t_assoc_type_img_func g_assoc_type_img_func_list[] = {
	{BMP, save_surface_bmp },
	{PNG, save_surface_png },
	{JPG, save_surface_jpeg},
};

const size_t g_assoc_type_img_func_size = SIZE_TAB(g_assoc_type_img_func_list);

int g_fd_readline = -1;
int g_fd_callback = -1;

#ifndef _VIEWING_ENABLED

int g_viewing_enabled = 1;

#else /* ifndef _VIEWING_ENABLED */

int g_viewing_enabled = 0;

#endif /* ifndef _VIEWING_ENABLED */
