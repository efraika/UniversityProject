#include "cimp.h"

/**
 * Concatère 2 chemins l'un à la suite de l'autre
 * @param  f1 le préfixe
 * @param  f2 le suffixe
 * @return    un nouveau pointeur allouée.
 */
static char * concat_dir_str(char * f1, char * f2) {
	size_t len;

	len = strlen(f1);
	if (len > 0 && f1[len - 1] == '/')
		return (strjoin(f1, f2));

	return (strjoin_c(f1, f2, '/'));
}

/**
 * Simplifie une chaîne de charactères en supprimant les ../ et les ./
 * de manière répétitive.
 * Renvoie 0 si ça s'est bien passé.
 * Renvoie 1 sinon
 * @param  res la chaîne de charactères à manipuler.
 * @return     un code d'erreur.
 */
static int      normalize_absolute_path(char * res) {
	int size;
	int index;

	size = 1;
	while (res[size] != 0 && (res[size] != '/' || size++)) {
		if (strncmp(res + size, "../", 3) == 0) {
			size--;
			index = 4;
			while (--size >= 0 && res[size] != '/')
				index++;
			if (size < 0)
				return (1);

			memmove(res + size, res + size + index, strlen(res + size + index) + 1);
			size++;
		}
		else if (strncmp(res + size, "./", 2) == 0) {
			memmove(res + size, res + size + 2, strlen(res + size + 2) + 1);
		}
		else {
			while (res[size] != '/' && res[size] != 0)
				size++;
		}
	}
	if (size == 1 && res[size] != 0)
		return (1);

	return (0);
}

/**
 * normalize_path prend une chaîne de charactères et construit le chemin absolu
 * associé à ce chemin.
 * Si le chemin commence par ~/ ou ~, met en préfixe le HOME directory.
 * Si le chemin ne commence pas par '/', met en préfixe le répertoire courant.
 * Puis simplifie la chaîne à l'aide de normalize_absolute_path.
 * Renvoie une chaîne de charactères allouée.
 * @param  curpath le chemin
 * @return         le chemin absolu
 */
char * normalize_path(char * curpath) {
	char * res;
	char * tmp;
	char pwd[1025];
	size_t l;

	if (strncmp(curpath, "~/", 2) == 0 || strcmp(curpath, "~") == 0) {
		l = 1;
		if (strncmp(curpath, "~/", 2) == 0)
			l = 2;
		tmp = getenv("HOME");
		if (tmp == NULL)
			return (NULL);

		tmp = concat_dir_str(tmp, curpath + l);
	}
	else if (*curpath != '/') {
		memset(pwd, 0, 1025);
		if (getcwd(pwd, 1024) == NULL)
			return (NULL);

		tmp = concat_dir_str(pwd, curpath);
	}
	else {
		tmp = dupstr(curpath);
	}
	if (tmp == NULL)
		return (NULL);

	res = concat_dir_str(tmp, "");
	free(tmp);
	if (res == NULL)
		return (NULL);

	if (normalize_absolute_path(res)) {
		free(res);
		res = NULL;
		return (NULL);
	}
	l = strlen(res);
	if (l > 0 && res[l - 1] == '/')
		res[l - 1] = '\0';
	return (res);
} /* normalize_path */
