#ifndef COMMANDS_H
#define	COMMANDS_H

t_rc_cmd cimp_open(t_cmd * cmd);
t_rc_cmd cimp_close(t_cmd * cmd);
t_rc_cmd cimp_help(t_cmd * cmd);
t_rc_cmd cimp_list(t_cmd * cmd);
t_rc_cmd cimp_sym_verti(t_cmd * cmd);
t_rc_cmd cimp_sym_hori(t_cmd * cmd);
t_rc_cmd cimp_rotate(t_cmd * cmd);
t_rc_cmd cimp_select(t_cmd * cmd);
t_rc_cmd cimp_unselect(t_cmd * cmd);
t_rc_cmd cimp_save(t_cmd * cmd);
t_rc_cmd cimp_update(t_cmd * cmd);
t_rc_cmd cimp_copy(t_cmd * cmd);
t_rc_cmd cimp_paste(t_cmd * cmd);
t_rc_cmd cimp_cut(t_cmd * cmd);
t_rc_cmd cimp_fill(t_cmd * cmd);
t_rc_cmd cimp_color_negative(t_cmd * cmd);
t_rc_cmd cimp_color_gray(t_cmd * cmd);
t_rc_cmd cimp_color_white_black(t_cmd * cmd);
t_rc_cmd cimp_color_replace(t_cmd * cmd);
t_rc_cmd cimp_ajust_light_contrast(t_cmd * cmd);
t_rc_cmd cimp_focus(t_cmd * cmd);
t_rc_cmd cimp_scale_rect(t_cmd * cmd);
t_rc_cmd cimp_scale_ratio(t_cmd * cmd);
t_rc_cmd cimp_crop_reduce(t_cmd * cmd);
t_rc_cmd cimp_crop_extend(t_cmd * cmd);

#endif // ifndef COMMANDS_H
