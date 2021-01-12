#                                  |\    /|
#                               ___| \,,/_/
#                            ---__/ \/    \
#                           __--/     (D)  \
#                           _ -/    (_      \
#                          // /       \_ / ==\
#    __-------------------/           / \_ O o)
#   /                                 /   \==/`
#  /                                 /
# ||          )                   \_/\
# ||         /              _      /  |
# | |      /--------      ___\    /\  :
# | /   __-  - _/   ------    |  |   \ \
#  |   -  -   /                | |     \ )
#  |  |   -  |                 | )     | |
#   | |    | |                 | |    | |
#   | |    < |                 | |   |_/
#   < |    /__\                <  \
#   /__\                       /___\

NAME = cimp

_RED=$(shell tput setaf 1 2> /dev/null || echo "")
_GREEN=$(shell tput setaf 2 2> /dev/null || echo "")
_YELLOW=$(shell tput setaf 3 2> /dev/null || echo "")
_BLUE=$(shell tput setaf 4 2> /dev/null || echo "")
_PURPLE=$(shell tput setaf 5 2> /dev/null || echo "")
_CYAN=$(shell tput setaf 6 2> /dev/null || echo "")
_WHITE=$(shell tput setaf 7 2> /dev/null || echo "")
_END=$(shell tput sgr0 2> /dev/null || echo "")

CC = gcc
FL = flex
BS = bison
PKG = pkg-config

SRC := ""
INC := ""
include files.mk # On charge la liste des fichiers depuis le fichier files.mk

LIBS_DEP = \
			sdl2 \
			SDL2_image \
			libjpeg \
			libpng \

RD_LIBS = -lreadline
M_LIB = -lm

CFLAGS = -Wall -Wextra -Werror -std=c11
IFLAGS = -I $(INC_FOLDER) -I $(LEX_PAR_FOLDER) $(shell $(PKG) $(LIBS_DEP) --cflags)
DFLAGS =
LFLAGS = $(shell $(PKG) $(LIBS_DEP) --libs) $(RD_LIBS) $(M_LIB)
FLAGS = $(CFLAGS) $(DFLAGS) $(IFLAGS)

ALL_FILES = $(SRC) $(INC)

OBJ_NO_MAIN = $(SRC_NO_MAIN:%.c=%.o)
OBJ_TEST = $(TEST_SRC:%.c=%.o)
OBJ_SRC = $(SRC:%.c=%.o)
OBJ_LEX_PAR = $(PAR_FILE:%.y=%.parser.o) $(LEX_FILE:%.l=%.lexer.o)
OBJ = $(OBJ_SRC) $(OBJ_LEX_PAR)

#################################### COMPILATION ###############################

.PHONY: all
all: $(NAME)

$(NAME): $(OBJ)
	@printf "\\t%sLD%s\\t" "$(_GREEN)" "$(_END)"
	@$(CC) $(FLAGS) $(OBJ) -o $(NAME) $(LFLAGS)
	@printf "%s\\n" "$@"

%.o: %.c
	@printf "\\t%sCC%s\\t" "$(_CYAN)" "$(_END)"
	@$(CC) $(FLAGS) -c $? -o $@
	@printf "%s\\n" "$?"

$(LEX_PAR_FOLDER)%.lexer.o: $(LEX_PAR_FOLDER)%.l
	@printf "\\t%sFL%s\\t" "$(_YELLOW)" "$(_END)"
	@$(FL) -o $(@:.o=.c) $?
	@printf "%s\\n" "$?"
	@printf "\\t%sCC%s\\t" "$(_CYAN)" "$(_END)"
	@$(CC) $(IFLAGS) -c $(@:.o=.c) -o $@
	@printf "%s\\n" "$(@:.o=.c)"
	@rm -f $(@:.o=.c)

$(LEX_PAR_FOLDER)%.parser.o: $(LEX_PAR_FOLDER)%.y
	@printf "\\t%sBS%s\\t" "$(_PURPLE)" "$(_END)"
	@$(BS) -d -o $(@:.o=.c) $?
	@printf "%s\\n" "$?"
	@printf "\\t%sCC%s\\t" "$(_CYAN)" "$(_END)"
	@$(CC) $(IFLAGS) -c $(@:.o=.c) -o $@
	@printf "%s\\n" "$(@:.o=.c)"
	@rm -f $(@:.o=.c)

.PHONY: clean
clean:
	@rm -rf $(OBJ) $(OBJ_TEST)
	@echo "Objects removed"

.PHONY: fclean
fclean: clean
	@rm -rf $(NAME)
	@echo "Program $(NAME) removed"

.PHONY: ffclean
ffclean: fclean
	@rm -rf \
		$(CIMP_CHECK) \
		gcovr \
		venv \
		infer.tar.xz infer-out \
		images/
	@find . -name "*.o" -delete
	@find . -name "*.gcda" -delete
	@find . -name "*.gcno" -delete
	@rm -f $(LEX_PAR_FOLDER)cimp.parser.h
	@echo "Projecl all cleaned"

.PHONY: re
re: fclean all

###################################### INSTALL #################################

.PHONY: install
install:
	@test -f $(NAME) || (printf "'make all' before\\n" && false)
	cp $(NAME) /usr/local/bin/$(NAME)

###################################### IMAGES ##################################

BMP_IMAGES_REMOTE = \
			https://neptun.weebly.com/uploads/3/1/3/1/3131773/untitled17.bmp \
			https://neptun.weebly.com/uploads/3/1/3/1/3131773/untitled5.bmp \
			https://neptun.weebly.com/uploads/3/1/3/1/3131773/untitled3.bmp \
			https://upload.wikimedia.org/wikipedia/commons/6/6e/Fruits.png \
			https://upload.wikimedia.org/wikipedia/commons/a/ae/Graphe.jpg \
			https://cdn.pixabay.com/photo/2016/11/23/00/38/abbey-1851493_960_720.jpg \
			https://cdn.pixabay.com/photo/2014/02/27/16/08/splashing-275950_960_720.jpg \
			https://cdn.pixabay.com/photo/2017/09/22/19/05/casserole-dish-2776735_960_720.jpg \
			https://cdn.pixabay.com/photo/2013/06/06/15/36/world-117174_960_720.png \
			https://cdn.pixabay.com/photo/2016/06/13/10/45/polar-1453993_960_720.png \

images:
	mkdir -p $@
	$(foreach url, $(BMP_IMAGES_REMOTE), curl $(url) -o images/$(shell basename $(url));)

###################################### VENV ####################################

venv:
	python3 -m venv venv
	venv/bin/pip3 install pip --upgrade
	venv/bin/pip3 install cpplint
	venv/bin/pip3 install gcovr

###################################### SUBMODULES ##############################

.PHONY: submodule
submodule:
	git submodule init
	git submodule update

###################################### UTILS ###################################
## This part is for continuous integration, testing, linting, etc...

UNCRUSTIFY = uncrustify/build/uncrustify
CPPLINT = venv/bin/cpplint
CPPCHECK = cppcheck
CLANG_TIDY = clang-tidy-6.0
INFER = /usr/local/bin/infer
GCOVR = venv/bin/gcovr

print-%:
	@echo $($*)

.PHONY: lint_apply
lint_apply: uncrustify_apply clang_tidy_fix

.PHONY: lint_check
lint_check: uncrustify_check cpplint_run cppcheck_run clang_tidy_run infer_run

###################################### CHECK ###################################

BLACK_BOX_DIR = tests/black_box
CIMP_CHECK = cimp_check
GCOV_LIBS = -lcheck -lm -lpthread -lrt -lsubunit -lgcov -coverage

$(CIMP_CHECK): venv images recompile_with_profile_args $(OBJ_TEST) $(OBJ_LEX_PAR)
	@$(CC) $(OBJ_NO_MAIN) $(OBJ_TEST) $(OBJ_LEX_PAR) $(GCOV_LIBS) $(LFLAGS) -o $@
	./$@
	make LFLAGS="$(GCOV_LIBS) $(LFLAGS)" DFLAGS="-D_VIEWING_ENABLED"
	make -C $(BLACK_BOX_DIR)
	make gcovr

.PHONY: recompile_with_profile_args
recompile_with_profile_args: fclean
	make CFLAGS="$(CFLAGS) -fprofile-arcs -ftest-coverage" DFLAGS="-D_VIEWING_ENABLED" $(OBJ)

.PHONY: gcovr
gcovr:
	rm -rf gcovr
	mkdir -p gcovr
	$(GCOVR) -r . --html --html-details -o gcovr/index.html

###################################### UNCRUSTIFY ##############################

CONFIG_UNCRUSTIFY = UNCRUSTIFY.cfg

$(UNCRUSTIFY): submodule
	mkdir -p uncrustify/build
	(cd uncrustify/build && cmake ..)
	make -C uncrustify/build -j8

.PHONY: uncrustify_apply
uncrustify_apply: $(UNCRUSTIFY)
	$(UNCRUSTIFY) -c $(CONFIG_UNCRUSTIFY) --replace --no-backup --mtime $(ALL_FILES) $(TEST_SRC)

.PHONY: uncrustify_check
uncrustify_check: $(UNCRUSTIFY)
	$(UNCRUSTIFY) -c $(CONFIG_UNCRUSTIFY) --check $(ALL_FILES) $(TEST_SRC)

###################################### CPPLINT #################################

.PHONY: cpplint_run
cpplint_run: venv
	$(CPPLINT) $(ALL_FILES)

##################################### CPPCHECK #################################

.PHONY: cppcheck_run
cppcheck_run:
	$(CPPCHECK) --error-exitcode=1 --enable=all --suppress=missingIncludeSystem -I inc $(SRC)

##################################### CLANG_TIDY ###############################

.PHONY: clang_tidy_run
clang_tidy_run:
	$(CLANG_TIDY) \
		-checks="*,-llvm-header-guard,-google-readability-braces-around-statements,-hicpp-braces-around-statements,-hicpp-signed-bitwise,-readability-braces-around-statements" \
		-warnings-as-errors="*" \
		$(SRC) -- $(IFLAGS) \

.PHONY: clang_tidy_fix
clang_tidy_fix:
	$(CLANG_TIDY) \
		-checks="*,-llvm-header-guard,-google-readability-braces-around-statements,-hicpp-braces-around-statements,-hicpp-signed-bitwise,-readability-braces-around-statements" \
		-header-filter=".*" \
		-fix-errors \
		$(SRC) -- $(IFLAGS) \

###################################### INFER ##################################

INFER_TAR = infer.tar.xz
VERSION_INFER = 0.15.0
PATH_DL_INFER = https://github.com/facebook/infer/releases/download/v$(VERSION_INFER)/infer-linux64-v$(VERSION_INFER).tar.xz
SHASUM_INFER = f6eb98162927735e8c545528bb5a472312e5defcf0761e43c07c73fe214cb18a

$(INFER):
	test -f $(INFER_TAR) || make $(INFER_TAR)
	tar -C /opt -xJf $(INFER_TAR)
	rm -rf $(INFER_TAR)
	ln -s /opt/infer-linux64-v$(VERSION_INFER)/bin/infer $@

$(INFER_TAR):
	curl -sSL $(PATH_DL_INFER) -o $@
	echo "$(SHASUM_INFER)  $@" | shasum -a 256 -c -

.PHONY: infer_run
infer_run: $(INFER) fclean
	make CC="$(INFER) run --fail-on-issue -- $(CC)" $(OBJ_SRC)
	make $(OBJ_LEX_PAR)
	make CC="$(INFER) run --fail-on-issue -- $(CC)" all