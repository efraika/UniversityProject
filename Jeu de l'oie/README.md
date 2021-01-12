# Projet de POO-IG

Réalisé par Pierre Gimalac [pgimalac](https://github.com/pgimalac) et Marie Bétend [efraika](https://github.com/efraika).
Dépôt initial du projet [ici](https://github.com/pgimalac/Projet-POOIG-S3)

## Compilation et execution
#### Compilation
Pour compiler les fichiers du projet il suffit de compiler le fichier *Jouer.java* situé à la racine ; pour être sûr de compiler tous les fichiers on peut executer *javac @fichiers.txt* où fichiers.txt est situé à la racine qui contient la liste des fichiers *.java* du projet en adresse relative.

#### Execution
Le fichier à executer pour lancer le programme est le fichier *Jouer.java* situé à la racine du projet, l'interface lancée dépend du contenu du paramètre *args* du main : si ce paramètre contient uniquement le mot *gui* (avec ou sans majuscules), l'interface lancée est l'interface graphique. Sinon le programme se lancera en interface textuelle (console).

## Organisation et fichiers à regarder
#### Organisation du projet
Les fichiers sont rangés dans des dossiers (qui sont aussi les packages et sous-packages) aux noms assez explicites pour trouver ce que l'on cherche.
Ci-dessous se trouve l'architecture du projet :
(merci à Maxime Flin qui m'a fait découvrir dans son README les caractères pour représenter cette architecture)

```
.
├── assets                                    Images utilisées dans le projet
├── sauvegardes                               Dossier contenant les sauvegardes du jeu
├── Jouer.java                                Classe principale du projet
├── fichiers.txt                              Liste des fichiers .java
├── README.md
└── jeu
    ├── affichage                             Vue     
    │   ├── Affichage.java                    Parties communes des affichages
    │   ├── AffichageCUI.java                 Affichage Console
    │   ├── AffichageGUI.java                 Affichage graphique
    │   ├── AffichagePlateau.java             Affichage du plateau (interface)
    │   └── gui                               Outils utilisés par l'interface graphique
    │   	├── ButtonUp.java
    │   	├── CasePanel.java
    │   	├── Fenetre.java
    │   	├── JeuPanel.java
    │   	├── JPanelUp.java
    │   	├── JRadioButtonUp.java
    │   	├── LabelSized.java
    │   	├── MenuPanel.java
    │   	├── NouveauPanel.java
    │   	└── PionG.java
    ├── events                                
    │   ├── CannotPlayEvent.java
    │   ├── GameEvent.java
    │   ├── GameOverEvent.java
    │   └── PlayEvent.java
    ├── exceptions
    │   ├── ChoiceException.java
    │   ├── GameException.java
    │   ├── GameOverException.java
    │   ├── OptionException.java
    │   ├── QuestionException.java
    │   └── WrongOptionException.java
    ├── listeners
    │   ├── CannotPlayListener.java
    │   ├── DesValueListener.java
    │   ├── GameListener.java
    │   ├── GameOverListener.java
    │   └── PlayListener.java
    ├── options                                  
    │   ├── Option.java
    │   ├── OptionAlignementNumeri.java
    │   ├── OptionFaceSuppNumeri.java
    │   ├── OptionPionCaseOie.java
    │   ├── OptionPositionFinOie.java
    │   ├── OptionQuestionOie.java
    │   └── questions
    │   	├── BanqueQuestions.java
    │   	├── Question.java
    │   	└── Questions.txt
    ├── plateau                               Classes liées au plateau et aux cases 
    │   ├── cases
    │   │	├── Case.java
    │   │	├── CaseDepart.java
    │   │	├── CaseGagnante.java
    │   │	├── CaseHotel.java
    │   │	├── CaseLabyrinthe.java
    │   │	├── CaseMort.java
    │   │	├── CaseOie.java
    │   │	├── CasePont.java
    │   │	├── CasePrison.java
    │   │	├── CasePuit.java
    │   │	└── CaseScore.java
    │   └── Plateau.java
    ├── Jeu.java                              Partie communes des deux jeux
    ├── JeuOie.java                           Jeu de l'oie           
    ├── JeuNumeri.java                        Jeu de numéri              
    └── Joueur.java                                  
```

#### Fichiers à regarder
- Le fichier *Jouer.java* est le fichier à executer, il ne contient qu'un bloc main et lance une des deux interfaces possibles du programme.
- Le fichier *Jeu.java* est la classe abstraite dont héritent les deux jeux, elle regroupe les parties communes des jeux et les méthodes à implémenter
- Les fichiers *JeuOie.java* et *JeuNumeri.java* sont les deux jeux implémentés ici
- Le dossier affichage regroupe tout ce qui touche à la vue
- Les autres dossiers sont pour le modèle (séparés selon leurs spécificités)
