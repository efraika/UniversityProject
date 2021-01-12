open Graphics
open Bsp
open String


(*Constantes du programme*)
let dim_x = 1500 ;;
let dim_y = 1000;;
let profondeur = ref 500;;
let taille_x = ref 1000;;
let taille_y = ref 1000;;
let ecart_min = ref 10;;
let taille_bouton = (130,40);;
let long_ztexte = 70;;
let haut_ztexte = 20 ;;
let epaisseur_ligne = 2;;
let erreur = ref "";;

type zone_texte = { coord : (int * int); mutable value :string; nom : string; }
type bouton = { coord : (int * int); label : string; mutable isPressed : bool; }

(*Crée la fenetre graphique*)
let create_window () : unit =
    open_graph (" "^(string_of_int dim_x)^"x"^(string_of_int dim_y));
    set_window_title "Ceci est une fenetre";
    auto_synchronize false;
;;


(*Attend que le joueur clique dans la fenetre et retourne les coordonnées de l'endroit ou le joueur a cliqué*)
let coord_mouse_event () : (int * int) =
    let event = wait_next_event [Button_down] in
    (event.mouse_x, event.mouse_y)
;;

(*Une fonction qui compare les termes de deux 2-uplets *)
let compare_tuple f a b =
  (f (fst a) (fst b)) && (f (snd a) (snd b))
;;
(*Une fonction qui renvoie la somme de 2 2-uplets*)
let sum_2_upplet ((a,b) : (int * int)) ((c, d) : (int * int)) : (int * int) =
    (a + c, b + d)
;;

(*Une fonction qui renvoie true si c est une charactere representant un nombre de 0 a 9 et false sinon*)
let isNbr (c : char) : bool =
  (c>= '0' && c<='9')
;;

(*Une fonction qui dessine a l'ecran une zone d'entree de texte*)
let draw_zone_texte (z : zone_texte) : unit =
  set_color black;
  draw_rect (fst z.coord) (snd z.coord) long_ztexte haut_ztexte ;
  moveto (fst z.coord + epaisseur_ligne + ((long_ztexte - fst(text_size z.value)) / 2)) (snd z.coord + epaisseur_ligne) ;
  draw_string (z.value);
  moveto (fst z.coord + ((long_ztexte - fst(text_size z.nom)) / 2)) ((snd z.coord) + haut_ztexte + epaisseur_ligne + snd(text_size z.nom) );
  draw_string (z.nom)
;;


(*Une fonction qui dessine un bouton a l'ecran*)
let draw_bouton (b : bouton) : unit =
  set_color black;
  draw_rect (fst b.coord) (snd b.coord) (fst taille_bouton) (snd taille_bouton);
  moveto (fst b.coord + ((fst taille_bouton - fst(text_size b.label)) / 2)) (snd b.coord + ((snd taille_bouton - snd(text_size b.label)) / 2));
  draw_string b.label
;;

(*Une fonction qui ajoute un charactere a la zone de texte z si cela est possible*)
let g_zone_texte (z : zone_texte) (key : char ) (coord : (int * int)): unit =
if (compare_tuple (>=) coord z.coord && compare_tuple (<=) coord (sum_2_upplet z.coord (long_ztexte, haut_ztexte))) then
  begin
  if (key = '\b' && (length z.value) > 0) then z.value <- sub z.value 0 ((length z.value) - 1)
  else
    if (isNbr key && fst (text_size (z.value^(Char.escaped(key)))) <= long_ztexte)
      then  z.value <- (z.value^(Char.escaped(key)))
  end
;;


(*Une fonction qui passe la valeur isPressed du bouton b si les coordonnees passees en arguments sont a l'interieur du bouton*)
let g_bouton (b : bouton) (coord : int * int) : unit =
if( compare_tuple (>=) (mouse_pos ()) b.coord &&  compare_tuple (<=) (mouse_pos ()) (sum_2_upplet b.coord  taille_bouton) )
then b.isPressed <- true ;
draw_bouton b
;;

(*Dessine le bsp du joueur avec la couleur des aretes données par le bsp de la solution*)
let draw_bsp (user_bsp : bsp) (max_x : int) (max_y : int) : unit =
    let rec draw_ss_bsp (ub : bsp) ((xdeb, ydeb) : int * int) ((xfin, yfin) : int* int) (pair : bool) : unit =
        match ub with
        | R None -> ()
        | R (Some c)-> set_color c;
                       fill_rect xdeb ydeb (xfin - xdeb) (yfin - ydeb)
        | L (lab, left, right) ->  let line_color = lab.col in
                                    if (pair) then
                                        let x = lab.coord in
                                        (draw_ss_bsp left (xdeb, ydeb) (x, yfin) (not(pair));
                                        draw_ss_bsp right (x, ydeb) (xfin, yfin) (not(pair));
                                        set_color line_color;
                                        fill_rect (x - (epaisseur_ligne / 2)) ydeb epaisseur_ligne (yfin - ydeb));
                                        set_color white;
                                        fill_rect (x - (epaisseur_ligne)) ydeb 1 (yfin - ydeb);
                                        fill_rect (x + (epaisseur_ligne)) ydeb 1 (yfin - ydeb)
                                    else
                                        let y = lab.coord in
                                        (draw_ss_bsp left (xdeb, ydeb) (xfin, y) (not(pair));
                                        draw_ss_bsp right (xdeb, y) (xfin, yfin) (not(pair));
                                        set_color line_color;
                                        fill_rect xdeb (y - (epaisseur_ligne / 2)) (xfin - xdeb) epaisseur_ligne);
                                        set_color white;
                                        fill_rect xdeb (y - epaisseur_ligne) (xfin - xdeb) 1;
                                        fill_rect xdeb (y + epaisseur_ligne) (xfin - xdeb) 1
    in draw_ss_bsp user_bsp (0, 0) (!taille_x, !taille_y) true
;;


(*Met à jour l'affichage de la fenetre*)
let update_window (user_bsp : bsp) : unit =
    clear_graph ();
    draw_bsp user_bsp (size_x ()) (size_y ());
    synchronize ()
;;


(*Une fonction qui verifie que les valeur rentres dans les boutons b1 b2 b3 b4 sont valides .
Renvoie true si c'est le cas, false sinon et affiche un message d'erreur *)
let check (b1 : zone_texte) (b2 : zone_texte) (b3 : zone_texte) (b4 : zone_texte): bool =
  set_color red;
  if(int_of_string b1.value >= 1073741824 ) then
    begin
    erreur := "La valeur entree dans profondeur est trop grande !" ;
    moveto (fst b1.coord + ((fst taille_bouton - fst(text_size !erreur)) / 2)) (snd b1.coord - (snd(text_size !erreur) + epaisseur_ligne));
    draw_string !erreur;
    synchronize ();
    false;
    end
  else if(int_of_string b2.value>int_of_string b3.value || int_of_string b2.value>int_of_string b4.value) then
    begin
      erreur := "L'ecart entre les lignes ne peut pas etre plus grand que la taille de la fenetre !";
      moveto (fst b2.coord + ((fst taille_bouton - fst(text_size !erreur)) / 2)) (snd b2.coord - (snd(text_size !erreur) + epaisseur_ligne));
      draw_string !erreur;
      synchronize ();
      false;
    end
  else if(int_of_string b3.value > (dim_x - (fst taille_bouton))) then
    begin
      erreur := "La longueur de la fenetre ne peut pas etre plus grande que "^(string_of_int (dim_x - (fst taille_bouton)))^" !";
      moveto (fst b3.coord + ((fst taille_bouton - fst(text_size !erreur)) / 2)) (snd b3.coord - (snd(text_size !erreur) + epaisseur_ligne));
      draw_string !erreur;
      synchronize ();
      false;
    end
  else if(int_of_string b4.value > dim_y) then
    begin
      erreur := "La largeur de la fenetre ne peut pas etre plus grande que "^(string_of_int dim_y)^" !";
      moveto (fst b4.coord + ((fst taille_bouton - fst(text_size !erreur)) / 2)) (snd b4.coord - (snd(text_size !erreur) + epaisseur_ligne));
      draw_string !erreur;
      synchronize ();
      false;
    end
  else true
;;
