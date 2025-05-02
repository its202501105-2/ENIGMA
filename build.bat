@echo off

javac -sourcepath src -d bin -encoding UTF-8 src/*
jar -cfm ENIGMA.jar manifest.mf -C bin .
