open Graphics
open Bsp
open View
open Fnc

let quit = ref false;;

(*Teste si la couleur des aretes de l'arbre de la solution sont les memes que celles de l'arbre du joueur*)
(*Retourne true si le joueur a trouve une solution compatible et false sinon*)
let game_is_over (user_bsp : bsp) : bool =
    let rec parcours_bsp (bsp : bsp) =
        match bsp with
        | R None -> false
        | R Some _ -> true
        | L (lab, left, right) -> if (lab.col <> (dominant_color (adjacent_rect_list bsp ""))) then false
                                    else ((parcours_bsp left) && (parcours_bsp right))
    in parcours_bsp user_bsp
;;


(*Fonction qui regarde si le bsp usr admet une solution avec son coloriage actuel.
Si c'est le cas affiche qu'une solution existe et propose a l'utilisateur d'afficher la solution ou de continuer.
Sinon affiche qu'il n'y a pas de solution et un bouton pour continuer.*)
let is_there_sol_f (usr : bsp ref) (coord : (int * int)): unit =
  try (match Sat.solve(bsp_to_fnc !usr) with
  |None -> begin
            let phrase = "Il n'y a pas de solution avec le remplissage courant :(" in
            let continuer = {coord = (!taille_x + (49 *(dim_x - !taille_x ) /100), 3 * dim_y / 8); label = "Continuer"; isPressed = false} in
            set_color blue;
            moveto (fst coord + ((fst taille_bouton - fst(text_size phrase)) / 2)) (snd coord - (snd (text_size phrase) + epaisseur_ligne));
            draw_string phrase;
            draw_bouton continuer;
            synchronize ();
            while not(continuer.isPressed) do
              let coord2 = coord_mouse_event () in
              g_bouton continuer coord2;
            done
          end
  |Some a -> begin
              let phrase = "Il a une solution avec le remplissage courant :)" in
              let afficher = {coord = (!taille_x + (20 *(dim_x - !taille_x ) /100), 3 * dim_y / 8); label = "Afficher la solution"; isPressed = false} in
              let continuer = {coord = (!taille_x + (69 *(dim_x - !taille_x ) /100), 3 * dim_y / 8); label = "Continuer"; isPressed = false} in
              set_color blue;
              moveto (fst coord + ((fst taille_bouton - fst(text_size phrase)) / 2)) (snd coord - (snd (text_size phrase) + epaisseur_ligne));
              draw_string phrase;
              draw_bouton afficher;
              draw_bouton continuer;
              synchronize();
              while (not(afficher.isPressed) && not(continuer.isPressed)) do
                let coord2 = coord_mouse_event () in
                g_bouton afficher coord2;
                g_bouton continuer coord2;
              done;
              if(afficher.isPressed) then usr := complete_bsp !usr a;
            end)
    with
    | Stack_overflow ->
                begin
                    let phrase = "Solution trop couteuse a calculer" in
                    let continuer = {coord = (!taille_x + (49 *(dim_x - !taille_x ) /100), 3 * dim_y / 8); label = "Continuer"; isPressed = false} in
                    set_color blue;
                    moveto (fst coord + ((fst taille_bouton - fst(text_size phrase)) / 2)) (snd coord - (snd (text_size phrase) + epaisseur_ligne));
                    draw_string phrase;
                    draw_bouton continuer;
                    synchronize ();
                    while not(continuer.isPressed) do
                        let coord2 = coord_mouse_event () in
                        g_bouton continuer coord2;
                    done
                end

;;


(*Panneau lateral a cote du jeu
Lorsque l'on passe (-1,-1) en argument affiche les boutons , sinon regarde si les coordonnées correspondent a un bouton et agit en fonction :
Si solution est presse change la valeur de usr pour pouvoir afficher la solution
Si c'est is_there_sol dit a l'utilisateur s'il y a une solution ou non, s'il y en a une lui propose de l'afficher ou de continuer
Si quitter est presse ferme la fenetre*)
let panneau_lateral (coord : (int * int)) (sol : bsp ) (usr : bsp ref): unit =
  let solution = {coord = (!taille_x + (49 *(dim_x - !taille_x ) /100), 6 * dim_y / 8); label = "Solution"; isPressed = false} in
  let is_there_sol = {coord = (!taille_x + (49 *(dim_x - !taille_x ) /100), 4 * dim_y / 8); label = "Solution courrante"; isPressed = false} in
  let quitter ={coord = (!taille_x + (49 *(dim_x - !taille_x ) /100), 2 * dim_y / 8); label = "Quitter"; isPressed = false} in
  if(compare_tuple (=) coord (-1,-1)) then
    begin
    draw_bouton solution;
    draw_bouton is_there_sol;
    draw_bouton quitter;
    end
  else
    begin
      g_bouton solution coord;
      g_bouton is_there_sol coord;
      g_bouton quitter coord;
      if (solution.isPressed) then usr := sol
      else if (is_there_sol.isPressed) then is_there_sol_f usr is_there_sol.coord
      else if (quitter.isPressed) then close_graph ();
      quitter.isPressed <- false;
      is_there_sol.isPressed <- false;
      solution.isPressed <- false;
      synchronize();
    end
  ;;

(*Fonction qui gere l'ecran d'acceuil du jeu*)
let rec ecran_acceuil () : unit =
  moveto (dim_x/3) (dim_y/2) ;
  set_text_size (dim_y/8) ;
  draw_string "Bienvenue dans le jeu , cliquer n'importe ou pour commencer !";
  synchronize ();
  let _ = wait_next_event [Button_down] in
  ()
;;


(*Fonction qui gere la page de fin de jeu proposant a l'utilisateur de rejouer ou de quitter *)
let game_over () : unit =
  let rejouer = {coord = (40 *dim_x / 150, 4 * dim_y / 8); label = "rejouer"; isPressed = false } in
  let close = {coord = (98 *dim_x / 150, 4 * dim_y / 8); label = "quitter"; isPressed = false } in
  while (not(rejouer.isPressed) && not(close.isPressed)) do
    clear_graph ();
    set_color blue;
    moveto  (47 * dim_x / 100) (7 * dim_y / 8);
    draw_string "Partie Terminee !";
    draw_bouton rejouer;
    draw_bouton close;
    synchronize();
    let coord = coord_mouse_event () in
    g_bouton rejouer coord;
    g_bouton close coord;
  done;
  if(close.isPressed) then quit:= true
;;

(*Fonction qui affiche la solution en attendant l'action du joueur pour continuer*)
let continue (usr_bsp : bsp) : unit =
  let continue = {coord = (!taille_x + (49 *(dim_x - !taille_x ) /200), 4 * dim_y / 8); label = "Continuer"; isPressed = false} in
  clear_graph ();
  draw_bsp usr_bsp (size_x ()) (size_y ());
  draw_bouton continue ;
  synchronize ();
  while not(continue.isPressed) do
    let coord = coord_mouse_event () in
    g_bouton continue coord;
  done
;;

(*Fonction qui gere le deroulement de la partie jeu avec l'affichage du quadrillage et la generation des solutions lorsque l'utilisateur le demande *)
let jeu () : unit =
  let sol_bsp = (add_edge_color (randomArbre !profondeur !taille_x !taille_y !ecart_min)) in
  let user_bsp = ref (delete_rect_color sol_bsp) in
  while (not(game_is_over !user_bsp)) do
      update_window !user_bsp;
      panneau_lateral (-1,-1) sol_bsp user_bsp;
      synchronize ();
      let coord = coord_mouse_event () in
      if(compare_tuple (<=) coord (!taille_x, !taille_y)) then  user_bsp := modifie_feuille !user_bsp coord
      else
        begin
        panneau_lateral coord sol_bsp user_bsp;
        synchronize ();
      end
  done;
  continue !user_bsp;
;;


(*Gere la page des parametres du jeu.
Modifie les valeurs des variables de profondeur, ecart_min, taille_x, taille_y en fonction de ce qui est entre par l'utilisateur.
Affiche un message d'erreur lorsque ces valeurs ne sont pas valide*)
let param () : unit =
  clear_graph () ;
  let prof = { coord = (49 * dim_x / 100 , 6 * dim_y / 8); value = (string_of_int !profondeur ); nom = "PROFONDEUR"} in
  let ecart = { coord = (49 * dim_x / 100, 5 * dim_y / 8); value = (string_of_int !ecart_min); nom = "ECART MINIMUM ENTRE LES LIGNES"} in
  let taillex = { coord = (49 * dim_x / 100, 4 * dim_y / 8); value = (string_of_int !taille_x) ; nom = "LONGUEUR JEU"} in
  let tailley = { coord = (49 * dim_x / 100, 3 * dim_y / 8); value = (string_of_int !taille_y) ; nom = "LARGEUR JEU"} in
  let jouer = { coord = (47 *dim_x / 100, 2 * dim_y / 8); label = "jouer"; isPressed = false } in
  let coord = ref (prof.coord)  in
  while (not(jouer.isPressed) || not(check prof ecart taillex tailley) ) do
      clear_graph();
        moveto (47 * dim_x / 100) (7 * dim_y / 8);
        set_color black;
        draw_string ("Parametres du jeu") ;
        draw_zone_texte prof ;
        draw_zone_texte ecart ;
        draw_zone_texte taillex ;
        draw_zone_texte tailley ;
        draw_bouton jouer;
        synchronize ();
      jouer.isPressed <- false;
      let event =  wait_next_event [Button_down; Key_pressed] in
      if (event.button) then
        begin
        coord := (event.mouse_x, event.mouse_y) ;
        g_bouton jouer (event.mouse_x, event.mouse_y);
        end;
      if (event.keypressed) then
       begin
          g_zone_texte prof (event.key) !coord ;
          g_zone_texte ecart (event.key) !coord ;
          g_zone_texte taillex (event.key) !coord ;
          g_zone_texte tailley (event.key) !coord ;
        end;
  done;
  profondeur := (int_of_string prof.value);
  ecart_min := (int_of_string ecart.value);
  taille_x := (int_of_string taillex.value);
  taille_y := (int_of_string tailley.value);
;;


(*Fonction qui gere le deroulement du jeu*)
let run_game () : unit =
  try
    create_window();
    ecran_acceuil();
    while(not (!quit)) do
      param ();
      jeu ();
      game_over ();
    done;
    close_graph ();
  with
  | Graphics.Graphic_failure s -> ()
;;


let main () : unit =
  run_game();
;;

let _ = main ()
