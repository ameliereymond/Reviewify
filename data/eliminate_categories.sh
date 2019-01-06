#!/usr/bin/env bash

LANGUAGES="de fr jp uk us"
UNWANTED_CATEGORIES="Book|Video DVD|Digital_Ebook_Purchase|Digital_Music_Purchase|Digital_Video_Download|Mobile_Apps|Music|Video|Video Games"

for lang in ${LANGUAGES};do
    echo "Eliminating unwanted categories for language $lang"
    grep -Ev "^.*($UNWANTED_CATEGORIES).*$" ${lang}.tsv > ${lang}_reduced.tsv
    echo "$(du -kh ${lang}.tsv | cut -f1) => $(du -kh ${lang}_reduced.tsv | cut -f1)"
    rm ${lang}.tsv
    mv ${lang}_reduced.tsv ${lang}.tsv
done
