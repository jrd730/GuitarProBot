
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent; 

/**
* Wrapper for the Java Robot class. Designed specifically to interface with 
* GuitarPro software with functions to move to a note pitch, place a note,
* and change note duration.
*/
public class GuitarProBot
{
	/*
	* Robot object used to simulate key presses within GuitarPro
	*/
	private Robot robot;
	
	private CONSTANTS c;
	
	/*
	*	Represents how many 128th notes in the beat. A 64th note 
	* is the shortest selectable duration in GuitarPro, however 
	* a dotted 64th note requires the use of 128th notes.
	* Defaults to a quarter note or 32/128 of a beat.
    */
	private int currentNoteDuration;
	
	/*
	* Represents the number of half steps above the lowest
    * selectable pitch that the current note is on. In 
    * GuitarPro the lowest note is an open E on the 6th 
    * string unless the instrument tuning is changed.
    * Defaults to middle C or 20 half steps above low E.
	*/
	private int currentNotePitch;
	
	/*
	* Corresponds to an element within the staff interval
	* array in CONSTANTS. Each element represents the number
	* of half steps it takes to get from the current line or
	* space on the musical staff to the next line or space. 
	* If the cursor starts on a C note, the intervals follow 
	* the major pattern of step intervals.
	*/
	private int intervalIndex;
	
	/*
	* Is true if the dotted note selector is active
	*/
	private boolean dottedNote;
	
	GuitarProBot ()
	{
		makeBot();
		currentNoteDuration = 32;
		currentNotePitch    = 20;
		intervalIndex 		= 0;
		dottedNote			= false;
		robot.setAutoDelay (30);
	}
	
	/*
	* Creates the Robot object necessary for simulating
	* key presses within GuitarPro
	*/
	private void makeBot ()
	{
		try {
			robot = new Robot ();
		}
		catch (AWTException e) {
		// TODO Auto-generated catch bloc
				e.printStackTrace();
		}
	}
	
	public void doPause (int milliseconds)
	{
		robot.delay(milliseconds);
	}
	
	/*
	* Puts a natural note of the current note duration at 
	* GuitarPro's cursor's position on the staff 
	*/
	public void putNote ()
	{
		robot.keyPress  (KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
	}
	
	/*
	* Puts the target pitch of the current note duration 
	* on the staff, where target pitch is the nmber of half
	* steps above the lowest selectable pitch. 
	*/
	public void putNote (int targetPitch)
	{
		if (targetPitch > currentNotePitch)
		{
			while (currentNotePitch + c.staffInterval[intervalIndex] <= targetPitch )
			{
				currentNotePitch += c.staffInterval[intervalIndex];
				incrementIntervalIndex();
				robot.keyPress  (KeyEvent.VK_UP);  
				robot.keyRelease(KeyEvent.VK_UP);
				//shiftCount++;
			}
			putNote();
			if (currentNotePitch < targetPitch)
			{
				shiftUp (1);
			}
		}
		else if (targetPitch < currentNotePitch)
		{
			while (currentNotePitch > targetPitch )
			{
				decrementIntervalIndex ();
				currentNotePitch -= c.staffInterval[intervalIndex];
				robot.keyPress  (KeyEvent.VK_DOWN);  
				robot.keyRelease(KeyEvent.VK_DOWN);
			}
			putNote();
			if (currentNotePitch < targetPitch)
			{
				shiftUp (1);
			}
		}
		else 
			putNote();
	}
	
	/*
	* Resets GuitarPro's note placement cursor to the lowest note on the 
	* staff. Requires the number of intervals shifted up from the last step
	* to be passed in.
	*/
	public void resetCursor (int shiftCount)
	{
		for (int i=0; i<shiftCount; i++)
		{
			robot.keyPress  (KeyEvent.VK_DOWN);  
			robot.keyRelease(KeyEvent.VK_DOWN);
		}
		currentNotePitch = 0;
		intervalIndex = 2;
	}
	
	/*
	* Cycles to the next element in staff intervals. If the next element 
	* is beyond the last, interval index is set to the first element. 
	*/
	private void incrementIntervalIndex ()
	{
		intervalIndex++;
		if (intervalIndex > 6)
			intervalIndex = 0;
	}
	
	/*
	* Cycles to the previous element in staff intervals. If the previous
	* element precedes the first, interval index is set to the last element.
	*/
	private void decrementIntervalIndex ()
	{
		intervalIndex--;
		if (intervalIndex < 0)
			intervalIndex = 6;
	}
	
	/*
	* Shift note up by one half step in order to make sharp notes.
	*/
	public void shiftUp (int halfSteps)
	{
		robot.keyPress  (KeyEvent.VK_SHIFT);  
		for (int i=0; i<halfSteps; i++)
		{
			robot.keyPress  (KeyEvent.VK_ADD);  
			robot.keyRelease(KeyEvent.VK_ADD);
		}
		robot.keyRelease(KeyEvent.VK_SHIFT);
	}
	
	/*
	* Places a rest of the current note duration on the staff
	*/
	public void putRest ()
	{
		robot.keyPress  (KeyEvent.VK_R);  
		robot.keyRelease(KeyEvent.VK_R);	
	}
	
	/*
	* Moves the cursor one position to the right.
	*/
	public void nextNote ()
	{
		robot.keyPress  (KeyEvent.VK_RIGHT);
		robot.keyRelease(KeyEvent.VK_RIGHT);
		if (dottedNote){
			toggleDottedNote();
			dottedNote = false;
		}
	}
	
	/*
	* Sets the current note duration to a new duration which 
	* represents the number of 128th notes in the beat. New
	* duration must be at least 2 and no more than 192. 
	*/
	public void setNoteDuration (int newDuration)
	{
		if (newDuration < currentNoteDuration)
		{
			while (currentNoteDuration > newDuration)
			{
				currentNoteDuration /= 2;
				robot.keyPress  (KeyEvent.VK_ADD);  
				robot.keyRelease(KeyEvent.VK_ADD);
			}
			if (currentNoteDuration < newDuration)
			{
				dottedNote = true;
				toggleDottedNote();
			}
		}
		else if (newDuration > currentNoteDuration)
		{
			while (currentNoteDuration < newDuration)
			{
				currentNoteDuration *= 2;
				robot.keyPress  (KeyEvent.VK_SUBTRACT);  
				robot.keyRelease(KeyEvent.VK_SUBTRACT);
			}
			if (currentNoteDuration > newDuration)
			{
				currentNoteDuration /= 2;
				robot.keyPress  (KeyEvent.VK_ADD);  
				robot.keyRelease(KeyEvent.VK_ADD);
				dottedNote = true;
				toggleDottedNote();
			}
		}
	}
	
	/*
	* Makes the current note a dotted note adding half it's duration to itself.
	*/
	public void toggleDottedNote ()
	{
		robot.keyPress  (KeyEvent.VK_DECIMAL);  
		robot.keyRelease(KeyEvent.VK_DECIMAL);
	}
}