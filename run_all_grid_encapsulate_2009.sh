#!/bin/sh

NUM_RUNS=50

for i in {1..5}; do
  qsub -t 1-$NUM_RUNS:1 encapsulated_fragmented_tree.sh ~/workspace/wsc2009/Testset0${i} 2009-fragments${i} nsga2-wsc.params true;
done
