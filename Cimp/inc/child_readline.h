#ifndef CHILD_READLINE_H
#define	CHILD_READLINE_H

int initialize_readline(void);
void        child_send_ok();
void        child_stop();
char * child_getline();

#endif // ifndef CHILD_READLINE_H
