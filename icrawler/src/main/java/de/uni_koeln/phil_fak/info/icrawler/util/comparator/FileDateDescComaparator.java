package de.uni_koeln.phil_fak.info.icrawler.util.comparator;

import java.io.File;
import java.util.Comparator;
import java.util.Date;

public class FileDateDescComaparator implements Comparator<File> {

	@Override
	public int compare(File o1, File o2) {
		Date date1 = new Date(o1.lastModified());
		Date date2 = new Date(o2.lastModified());
		return date2.compareTo(date1);
	}

}
