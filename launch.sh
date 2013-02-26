#!/bin/sh

ant core-jar
rm -vfr ./var/index/*
bin/desktop_terrier.sh
