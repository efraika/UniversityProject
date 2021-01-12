# Cimp [![Build Status](https://travis-ci.com/FauconFan/Cimp.svg?token=2sRmqJj9p4TAy8ScMWxd&branch=master)](https://travis-ci.com/FauconFan/Cimp)

Ce projet contient notre version finale du projet associé à l'UE Conduite de Projet lors de notre dernier semestre en licence d'Informatique.
Le dépot initial de se projet peut être trouvé [ici](https://github.com/FauconFan/Cimp).

Auteurs:

- Marie Bétend [efraika](https://github.com/efraika)
- Joseph Priou [FauconFan](https://github.com/FauconFan)
- Ugo Salin [ugosalin](https://github.com/ugosalin)

## Sujet

Nous devions implémenter un logiciel d'édition d'images à l'aide de SDL qui peut être utilisé directement depuis le terminal à l'aide de commandes.
Ce projet devait être fait dans un travail de groupes et devait mettre en place une méthode de développement, un tableau de bord, ainsi qu'une documentation.

## Méthode

Nous avons spécifié dans un fichier doc/general.md, comment notre projet est implémenté et nous avons divisé le travail en phases afin de mieux se répartir le travail et se fixer des objectifs.

## Manuel

'make' va compiler notre programme, puis vous pourrez éxécutez le programme en faisant './cimp'.  
'make clean' supprime les fichiers objets.  
'make fclean' supprime les fichiers objets et l'éxécutable.  
'make ffclean' supprime tous les fichiers générés.  
'make re' supprime les fichiers objets et l'éxécutable et recompile.  
'make install' va installer le binaire sur votre ordinateur (dans /usr/local/bin)  
'make uncrustify_check' va vérifier que notre code respect notre norme de code (à l'aide de uncrustify)  
'make lint_check' lance tous nos outils de check pour vérifier que notre code fonctionne.  
'make cimp_check' lance les tests unitaires et les tests en boîtes noires puis va appeler gcovr afin de construire les fichiers html pour le code coverage.  
