import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SongGenUI
{
	private Scanner fin;
	public  GuitarProBot bot;
	SongGenUI ()
	{
		bot = new GuitarProBot ();
	}
		
	public void go (String args[])
	{
		System.out.println ("Loading song data...");
		if (args.length > 0){
			loadSongData (args[0]);
		}
		else {
			System.out.print ("Enter the name of a text file from which to read in song data: ");
			Scanner input = new Scanner (System.in);
			loadSongData (input.nextLine());
		}
	}
	
	/*
	* Opens the input file containing song data specified in the command line. Pauses for 3 seconds so the 
	* user can switch window to GuitarPro. Uses key presses to change note durations and place notes.
	*/
	public boolean loadSongData (String fileName)
	{
		try {
			fin = new Scanner (new FileInputStream(fileName)); //input.next()
			System.out.println ("opened " + fileName);
			System.out.println ("Please select the Guitar Pro window and place the note cursor on middle C.");
		}
		catch (FileNotFoundException e){
			System.out.println ("File not found.");
			return false;
		}
		
		bot.doPause (3000);
		while (fin.hasNext())
		{
			int tone = fin.nextInt();
			int duration = fin.nextInt();
			bot.setNoteDuration(duration);
			bot.putNote(tone);
			bot.nextNote();
		}
		return true;
	}
}