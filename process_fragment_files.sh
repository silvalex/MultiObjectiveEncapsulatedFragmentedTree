#!/bin/sh

BASE=/vol/grid-solar/sgeusers/sawczualex/MO_encapsulated/
WSC2008=$BASE"run1_2008/2008-fragments"
WSC2009=$BASE"run1_2009/2009-fragments"

# Process WSC-2008
for i in {1..8}; do
	java -cp program.jar wsc.FragmentEncapsulation WSC2008${i}
done

# Process WSC-2009
for i in {1..5}; do
	java -cp program.jar wsc.FragmentEncapsulation WSC2009${i}
done