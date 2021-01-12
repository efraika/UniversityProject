#ifndef CIMP_H
#define	CIMP_H

#ifndef _POSIX_SOURCE
# define	_POSIX_SOURCE
#endif // ifndef _POSIX_SOURCE

#ifndef _GNU_SOURCE
# define	_GNU_SOURCE
#endif // ifndef _GNU_SOURCE

// Standards includes
#include <errno.h>
#include <fcntl.h>
#include <libgen.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <unistd.h>
#include <math.h>

// External library includes
// - readline
// - sdl
// - libpng
// - libjpeg

#include <readline/history.h>
#include <readline/readline.h>

#include <SDL.h>
#include <SDL_image.h>

#include <png.h>

#include <jpeglib.h>

// Typedef includes, needs to come first
#include "typedefs.h"

// Cimp includes
#include "child_readline.h"
#include "commands.h"
#include "env.h"
#include "parser.h"
#include "utils.h"

#define	CIMP_PROMPT    "cimp>> "
#define	TIMEOUT_USLEEP 50

int cimp_exe(t_cmd * cmd);
int setup_child();
void treat_line(char * line);
void handle_line();
void core();

#endif // ifndef CIMP_H
