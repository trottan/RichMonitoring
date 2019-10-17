#!/bin/bash
ls
echo
echo "Enter File Name"
read filename
#read filename
#cd $filename
#filename=materials_new.pl
#filename=Material.java
echo $filename

read -n1 -p "Load File? (L Linux -> Desktop) Save File? (S Desktop -> Linux): " doit
case $doit in
l|L)
echo " Loading"
cp $filename /mnt/c/Users/ntpie/Desktop/Linux/$filename #../mnt/c/Users/ntpie/AppData/Local/atom/app-1.37.0/$filename
temp=/Users/ntpie/Desktop/Linux/$filename
;;


s|S)
echo " Saving"
location=$(pwd)
cd $location
cp /mnt/c/Users/ntpie/Desktop/Linux/$filename $filename
;;
*) ;;

esac

