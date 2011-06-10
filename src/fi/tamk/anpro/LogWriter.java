package fi.tamk.anpro;

import java.io.FileWriter;
import java.util.ArrayList;

public class LogWriter {
	private static LogWriter instance = null;
	
	// Ajanoton tulokset
	private static ArrayList<String> clockTags; // Funktioiden tunnukset
	private static ArrayList<Long>   calls;     // Kutsukerrat
	private static ArrayList<Long>   totalTime; // Kokonaisaika
	private static ArrayList<Long>   shortest;  // Lyhin aika
	private static ArrayList<Long>   longest;   // Pisin aika
	private static ArrayList<Long>   average;   // Keskiarvo
	private static ArrayList<Long>   priority;  // Prioriteetti
	
	// Kellon tila
	private static ArrayList<String> activeTags;
	private static ArrayList<Long>   startTimes;
	private static long              endTime;
	
	// Väliaikaismuuttujia
	private static int indexA;
	private static int indexB;
	

	/*
	 * Rakentaja
	 */
	protected LogWriter() {
		if (activeTags == null) {
			initializeAll();
		}
    }
	
	/*
	 * Alustaa kaikki taulukot
	 */
	private static void initializeAll() {
		clockTags = new ArrayList<String>();
		calls     = new ArrayList<Long>();
		totalTime = new ArrayList<Long>();
		shortest  = new ArrayList<Long>();
		longest   = new ArrayList<Long>();
		average   = new ArrayList<Long>();
		priority  = new ArrayList<Long>();

		activeTags = new ArrayList<String>();
		startTimes = new ArrayList<Long>();
	}

	/*
	 *  Palauttaa pointterin tähän luokkaan
	 */
    public static LogWriter getInstance() {
        if(instance == null){
            instance = new LogWriter();
        }
        return instance;
    }
    
    /*
     * Aloittaa ajanoton
     */
    public static void startClock(String _tag) {
    	if (activeTags == null) {
    		initializeAll();
    	}
    	if (!activeTags.contains(_tag)) {
    		activeTags.add(_tag);
    		startTimes.add(android.os.SystemClock.uptimeMillis());
    	}
    }
    
    /*
     *  Lopettaa ajanoton
     */
    public static void stopClock(String _tag) {
    	if (activeTags.contains(_tag)) {
    		// Ota aika ja laske aikaero
    		endTime = android.os.SystemClock.uptimeMillis();
    		long timeDifference = endTime - startTimes.get(indexA);
    		
    		// Luo tagi, jos ei ole jo luotu
    		if (!clockTags.contains(_tag)) {
    			clockTags.add(_tag);
    			calls.add((long) 0);
    			totalTime.add((long) 0);
    			shortest.add((long) 0);
    			longest.add((long) 0);
    		}
    		
    		// Hae indeksit
    		indexA = activeTags.indexOf(_tag);
    		indexB = clockTags.indexOf(_tag);
    		
    		// Päivitä kutsukerrat
    		calls.set(indexB, calls.get(indexB) + 1);
    		
    		// Päivitä kokonaisaika
    		totalTime.set(indexB, totalTime.get(indexB) + timeDifference);
    		
    		// Päivitä lyhin aika
    		if (shortest.get(indexB) == 0) {
    			shortest.set(indexB, timeDifference);
    		}
    		else {
    			if (timeDifference < shortest.get(indexB)) {
    				shortest.set(indexB, timeDifference);
    			}
    		}
    		
    		// Päivitä pisin aika
    		if (longest.get(indexB) == 0) {
    			longest.set(indexB, timeDifference);
    		}
    		else {
    			if (timeDifference > longest.get(indexB)) {
    				longest.set(indexB, timeDifference);
    			}
    		}
    		
    		// Poista tämä tagi aktiivisista kellotageista
    		activeTags.remove(indexA);
    		startTimes.remove(indexA);
    	}
    }
    
    /*
     * Tallentaa tiedot
     */
    public static void saveData() {
    	FileWriter fileWriter;
    	String     line;
    	
    	// Laske keskiarvot ja prioriteetit
    	for (int i = 0; i < clockTags.size(); ++i) {
    		average.add((long)((double)totalTime.get(i) / (double)calls.get(i)));
    		priority.add(average.get(i) * calls.get(i));
        	
        	// Tallennetaan tiedosto
        	try {
        		// Avataan tiedosto
        		fileWriter = new FileWriter("/sdcard/anpro_debug.txt");
        		
        		// Kasataan kirjoitettava rivi yhteen
        		line = clockTags.get(i) + ":" +Long.toString(calls.get(i)) + ":" +
        			   Long.toString(shortest.get(i)) + ":" +Long.toString(longest.get(i)) + ":" +
        			   Long.toString(average.get(i)) + ":" + Long.toString(priority.get(i));
        		
        		// Kirjoitetaan rivi
        		fileWriter.write(line);
        		
        		// Tyhjätään puskuri ja suljetaan tiedosto
        		fileWriter.flush();
        		fileWriter.close();
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        	}
    	}
    }
}
