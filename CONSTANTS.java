public class CONSTANTS
{
  /*
  * Corresponds to the lowest note that can be assigned in GuitarPro.
  * In standard tuning represents an open E on the 6th string.
  */
	public static final int 	 lowestTone  		 = 0;
	
	/*
  * Corresponds to the highest note that can be assigned in GuitarPro.
  * In standard tuning represents the 24th fret on the 1st string.
  */
	public static final int 	 highestTone  		 = 48;
	
	
	public static final String chromaticScale [] = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	
	public int								 noteStepper       = 0;
	
	/*
	* Represents the number of half steps from from a space or line on 
	* the staff to the next space or line. The first index is a C note.
	*/
	public static final int 	 staffInterval  [] = {2, 2, 1, 2, 2, 2, 1};
	
	public static final int 	 majorScale  		[] = {0, 1, 3, 5, 7, 8, 10, 12, 13, 15, 17, 19, 20, 22, 24, 25, 27, 29};
}