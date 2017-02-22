package wsc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class FragmentEncapsulation {

	public static void main(String[] args) {
		if (args.length != 0)
			processDirectory(args[0]);
		else
			processDirectory(".");
		
		System.out.println("Done!");
	}
	
	public static void processDirectory(String directory) {
		// Get initial time
				long startTime = System.currentTimeMillis();

				Map<String, Integer> fragmentCountMap = new HashMap<String, Integer>();

				// Read in each fragment file, and add the counts to the map
				File dir = new File(directory);
				File [] files = dir.listFiles(new FilenameFilter() {
				    @Override
				    public boolean accept(File dir, String name) {
				        return name.startsWith("fragments");
				    }
				});

				for (int i = 0; i < 30; i++) {
					File f = files[i];
					System.out.printf("Reading file '%s'\n", f.getName());
					try {
						Scanner scan = new Scanner(f);
						while (scan.hasNext()) {
							String fragment = scan.next();
							int count = scan.nextInt();
							addToCountMap(fragment, count, fragmentCountMap);
						}
						scan.close();
					}
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}

				/* Throw out the start fragment, then sort the remaining fragments
				 * in a decreasing order of frequency.*/
				fragmentCountMap.remove("start|");
				List<Entry<String, Integer>> fragmentList = new ArrayList<Entry<String, Integer>>(fragmentCountMap.entrySet());
				Collections.sort(fragmentList, new Comparator<Entry<String, Integer>> () {
					public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
						if (e1.getValue() < e2.getValue())
							return 1;
						else if (e1.getValue() > e2.getValue())
							return -1;
						else
							return 0;
					}
				});

				// Write the fragments to a file
				System.out.println("Writing fragments to file...");
				File encapsulatedFile = new File(directory + "/encapsulated.stat");
				try {
					FileWriter writer = new FileWriter(encapsulatedFile);
					for (Entry<String, Integer> fragment: fragmentList) {
						if (!fragment.getKey().startsWith("end")) {
							writer.append(String.format("%s %d", fragment.getKey(), fragment.getValue()));
							writer.append("\n");
						}
					}
					writer.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				// Get final time
				long finalTime = System.currentTimeMillis();

				try {
					FileWriter writer = new FileWriter(new File(directory + "/encapsulationTime.txt"));
					writer.append(String.format("%d", finalTime - startTime));
					writer.close();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
	}

	public static void addToCountMap(String key, int count, Map<String, Integer> map) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + count);
		}
		else {
			map.put(key, count);
		}
	}
}
