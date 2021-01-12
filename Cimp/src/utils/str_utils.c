#include "cimp.h"

/**
 * strjoin renvoie la concaténation de deux chaînes de charactères
 * @param  s1
 * @param  s2
 * @return    un pointeur allouée
 */
char * strjoin(const char * s1, const char * s2) {
	size_t len1;
	size_t len2;
	size_t len_tot;
	char * res;

	len_tot = strlen(s1) + strlen(s2);
	res     = (char *) malloc(sizeof(char) * (len_tot + 1));
	if (res == NULL)
		return (NULL);

	len1 = 0;
	len2 = 0;
	while (s1[len1]) {
		res[len1] = s1[len1];
		len1++;
	}
	while (s2[len2]) {
		res[len1 + len2] = s2[len2];
		len2++;
	}
	res[len1 + len2] = 0;
	return (res);
}

/**
 * strjoin_c renvoie la concaténation de la première chaîne de charactères,
 * d'un charactère dit de connexion, et d'une seconde chaîne de charactères.
 * @param  dir    la première chaîne de charactères.
 * @param  file   la seconde chaîne de charactères.
 * @param  joiner le charactère dit de connexion.
 * @return        un pointerur allouée
 */
char * strjoin_c(const char * dir, const char * file, const char joiner) {
	size_t len1;
	size_t len2;
	size_t len_tot;
	char * res;

	len1    = strlen(dir);
	len_tot = len1 + strlen(file) + 1;
	res     = (char *) malloc(sizeof(char) * (len_tot + 1));
	if (res == NULL)
		return (NULL);

	len1 = 0;
	len2 = 0;
	while (dir[len1]) {
		res[len1] = dir[len1];
		len1++;
	}
	res[len1] = joiner;
	len1++;
	while (file[len2]) {
		res[len1 + len2] = file[len2];
		len2++;
	}
	res[len1 + len2] = 0;
	return (res);
}

/*Une fonction dupliquant la chaine de caractere s (comportement similaire a strdup) si elle n'est aps NULL*/
char * dupstr(const char * s) {
	size_t l;

	if (s == NULL) {
		return NULL;
	}
	l = strlen(s);
	char * r = malloc(l + 1);
	if (r == NULL) {
		return NULL;
	}
	strncpy(r, s, l);
	r[l] = 0;
	return r;
}

/** Une foncton qui retounre le nombre de decimal de l'entier i**/
int nb_digit(int i) {
	int res = 0;
	int nb  = i;

	do {
		res++;
		nb = nb / 10;
	} while (nb);
	return res;
}

/**La fonction itoa : convertit un int en chaine de caractere **/
char * itoa(int i) {
	char const digit[] = "0123456789";
	int num_digit      = nb_digit(i);
	char * p;
	char * b;

	if (i < 0) {
		p    = malloc(sizeof(char) * (num_digit + 2));
		b    = p;
		*p++ = '-';
		i   *= -1;
	}
	else {
		p = malloc(sizeof(char) * (num_digit + 1));
		b = p;
	}
	p  = p + num_digit;
	*p = '\0';
	do {
		*--p = digit[i % 10];
		i    = i / 10;
	} while (i);
	return b;
}

t_bool safe_strtol10(const char * word, char ** next, int * nump) {
	if (word == NULL || nump == NULL)
		return (FALSE);

	errno = 0;
	*nump = strtol(word, next, 10);
	if (errno == EINVAL || errno == ERANGE) {
		*nump = -1;
		return (FALSE);
	}
	return (TRUE);
}
