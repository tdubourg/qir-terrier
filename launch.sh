#!/bin/sh

ant core-jar
rm -vfr ./var/index/*
bin/desktop_terrier.sh 2>/dev/null # because the INFO logs are showing on stderr... 
