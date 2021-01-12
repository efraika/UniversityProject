
################################# HEADERS CODE #################################

INC_FOLDER = inc/

INC_FILES = \
	cimp.h \
	env.h \
	typedefs.h \
	utils.h \
	parser.h \
	commands.h \
	child_readline.h \

INC = $(addprefix $(INC_FOLDER), $(INC_FILES))

################################# SOURCE CODE ##################################

SRC_FOLDER			= src/
ENV_FOLDER			= src/env/
UTILS_FOLDER		= src/utils/
FORMAT_FOLDER		= src/utils/format/
COMMANDS_FOLDER     = src/commands/
CHILD_RD_FOLDER		= src/child_readline/
LEX_PAR_FOLDER		= src/lexer_parser/

MAIN_FILE = $(SRC_FOLDER)main.c

SRC_FILES = \
			globals.c \
			cimp_exe.c \
			core.c \

CHILD_RD_FILES = \
			child_readline.c \
			child_communicate.c \
			core_readline.c \

ENV_FILES = \
			t_cimp.c \
			t_cimp_screen.c \
			t_cimp_select.c \
			t_cimp_event.c \
			t_arg_type.c \
			t_cmd.c \

UTILS_FILES = \
			path_utils.c \
			str_utils.c \
			sdl_utils.c \

FORMAT_FILES = \
			save_surface_png.c \
			save_surface_jpeg.c \
			save_surface_bmp.c \
			get_type_img.c \
			get_func_img.c \

COMMANDS_FILES = \
			cimp_open.c \
			cimp_close.c \
			cimp_help.c \
			cimp_list.c \
			cimp_sym.c \
			cimp_rotate.c \
			cimp_select.c \
			cimp_save.c \
			cimp_update.c \
			cimp_copy.c \
			cimp_cut.c \
			cimp_paste.c \
			cimp_fill.c \
			cimp_color_replace.c \
			cimp_color_negative.c \
			cimp_color_gray.c \
			cimp_color_white_black.c \
			cimp_ajust_light_contrast.c \
			cimp_focus.c \
			cimp_scale.c \
			cimp_crop_reduce.c \
			cimp_crop_extend.c \

LEX_PAR_FILES = \
			parser.c \
			tokens.c \
			li_tokens.c \

LEX_FILE = $(LEX_PAR_FOLDER)cimp.l
PAR_FILE = $(LEX_PAR_FOLDER)cimp.y

SRC_NO_MAIN = \
		$(addprefix $(SRC_FOLDER), $(SRC_FILES)) \
		$(addprefix $(CHILD_RD_FOLDER), $(CHILD_RD_FILES)) \
		$(addprefix $(ENV_FOLDER), $(ENV_FILES)) \
		$(addprefix $(UTILS_FOLDER), $(UTILS_FILES)) \
		$(addprefix $(FORMAT_FOLDER), $(FORMAT_FILES)) \
		$(addprefix $(COMMANDS_FOLDER), $(COMMANDS_FILES)) \
		$(addprefix $(LEX_PAR_FOLDER), $(LEX_PAR_FILES)) \

SRC = \
		$(SRC_NO_MAIN) \
		$(MAIN_FILE) \

############################ CHECK TEST SOURCE CODE ###########################

TEST_FOLDER			= tests/check/

TEST_FILES = \
			util_surface.c \
			main_test.c \
			modif_test.c \
			window_test.c \
			parsing_test.c \
			util_images_test.c \
			save_test.c \
			meta_test.c \
			utils_test.c \
			core_test.c \

TEST_SRC = \
		$(addprefix $(TEST_FOLDER), $(TEST_FILES)) \
