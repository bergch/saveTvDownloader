package de.da_bubu.savetvdownloader.renamer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class Levenshtein {

    public static int diff(String orig, String eing) {

	int matrix[][] = new int[orig.length() + 1][eing.length() + 1];
	for (int i = 0; i < orig.length() + 1; i++) {
	    matrix[i][0] = i;
	}
	for (int i = 0; i < eing.length() + 1; i++) {
	    matrix[0][i] = i;
	}
	for (int a = 1; a < orig.length() + 1; a++) {
	    for (int b = 1; b < eing.length() + 1; b++) {
		int right = 0;
		if (orig.charAt(a - 1) != eing.charAt(b - 1)) {
		    right = 1;
		}
		int mini = matrix[a - 1][b] + 1;
		if (matrix[a][b - 1] + 1 < mini) {
		    mini = matrix[a][b - 1] + 1;
		}
		if (matrix[a - 1][b - 1] + right < mini) {
		    mini = matrix[a - 1][b - 1] + right;
		}
		matrix[a][b] = mini;
	    }
	}
	return matrix[orig.length()][eing.length()];
    }

    public static List<String> getStringsWithMinimalDist(String toBeSearched, Collection<String> repolist) {
	int minDist = getMinDist(toBeSearched, repolist);
	return getAllElementsWithDist(minDist, toBeSearched, repolist);
    }

    private static List<String> getAllElementsWithDist(int minDist, String fileName, Collection<String> epsList) {
	List<String> res = new ArrayList<String>();
	for (String string : epsList) {
	    int diff = diff(fileName, string);
	    if (diff <= minDist) {
		res.add(string);
	    }
	}

	return res;
    }

    private static int getMinDist(String fileName, Collection<String> epsList) {
	int min = 99999999; // seed
	for (String string : epsList) {
	    int diff = diff(fileName, string);
	    if (min > diff) {
		min = diff;
	    }
	}
	return min;
    }

    public static String getRepo(String downloadFileName, Set<String> keySet) {
	for (String string : keySet) {
	    if (downloadFileName.toLowerCase().startsWith(string.toLowerCase())) {
		return string;
	    }
	    if (downloadFileName.substring(1).toLowerCase().startsWith(string.toLowerCase())) {
	        return string;
	        }
	    if (downloadFileName.substring(2).toLowerCase().startsWith(string.toLowerCase())) {
            return string;
            }
	    if (downloadFileName.substring(3).toLowerCase().startsWith(string.toLowerCase())) {
            return string;
            }
	}
	return null;
    }

    public static String extractSeriesName(String fileName, Set<String> seriesNames) {
        for (String seriesName : seriesNames) {
            if(fileName.contains(seriesName)){
                return seriesName;
            }
        }
        return "";
    }
}
