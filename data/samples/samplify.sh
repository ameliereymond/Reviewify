#!/usr/bin/env bash

for file in $(ls . | grep -e ".tsv"); do
    echo "Sampling from $file"
    cat ${file} | head -n100 > ${file}-sample.tsv
done
