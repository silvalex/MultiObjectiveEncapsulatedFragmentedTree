#!/bin/sh

NUM_RUNS=50
BASE=/vol/grid-solar/sgeusers/sawczualex/MO_encapsulated/run4_2008/2008-fragments
FILE=/encapsulated.stat

for i in {1..8}; do
  qsub -t 1-$NUM_RUNS:1 encapsulated_fragmented_tree.sh ~/workspace/wsc2008/Set0${i}MetaData 2008-encapsulated-fragmented-tree${i} nsga2-wsc.params false $BASE${i}$FILE;
done
