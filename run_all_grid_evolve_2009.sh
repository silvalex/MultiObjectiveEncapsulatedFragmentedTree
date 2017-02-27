#!/bin/sh

NUM_RUNS=50
BASE=/vol/grid-solar/sgeusers/sawczualex/MO_encapsulated/run4_2009/2009-fragments
FILE=/encapsulated.stat

for i in {1..5}; do
  qsub -t 1-$NUM_RUNS:1 encapsulated_fragmented_tree.sh ~/workspace/wsc2009/Testset0${i} 2009-encapsulated-fragmented-tree${i} nsga2-wsc.params false $BASE${i}$FILE;
done
