#!/usr/bin/env bash

for file in $(ls . | grep -e ".tsv"); do
    echo "Sampling from $file"
    cat ${file} | head -n101 > ${file}-sample.tsv
    rm ${file}
    mv ${file}-sample.tsv ${file}
done
