package com.hisschemoller.sequencer;

import javax.swing.SwingUtilities;

/**
 * @author Wouter Hisschemoller
 * 
 * Features: 
 * Done: Synchroon starten nieuw patroon. 
 * Done: Animatie van bolletjes. 
 * Done: Midi in combobox disabled zolang hij nog niet gebruikt wordt. 
 * Done: Nieuw patroon synchroon starten. 
 * Done: Mute functie. 
 * Done: Note Off na tijdsduur noot, voorkom overlap, grafisch aanpassen. 
 * Done: Rotate functie. 
 * Done: Solo functie. 
 * Done: Settings venster positie bij verandering breedte scherm.
 * Done: Project XML openen. 
 * Done: Project XML opslaan.
 * Done: Nieuw project starten.
 * Done: Help venster maken.
 * Done: Bij verwijderen patroon volgende patroon selecteren.
 * Done: Settings disabled als er geen patroon geselecteerd is. 
 * Done: Waarom gebruikt die timer thread zoveel processor? (while loop)
 * Done: Help tekst schrijven.
 * 
 * Bugs: 
 * Done: Verwijderd patroon blijft zichtbaar.
 * Done: Als playback verandert zonder knop klik, update dan de knop.
 * Done: Update van Mute checkBox bij wisselen patroon.
 * Done: Er kunnen meer selecties dan stappen zijn. 
 * Done: Nieuw patroon MIDI pitch een octaaf te laag.
 * Done: Note On indicatie verkeerd als meer patronen dezelfde noot spelen. 
 * Done: Locatie patroon wordt verkeerd opgeslagen. Teveel naar linksboven.
 * 
 * Refactor:
 * Done: Geen Vector<SequenceVO> _sequences, maar alleen Vector<PatternVO> _patterns.
 * Done: SeqEventVO vervangen voor javax.sound.MidiEvent.
 * 
 * Optimize:
 * Done: Pattern pointer, center, steps, zero en flag in ŽŽn draw functie.
 * Done: Al het tekenen in PatternPainter class.
 * Done: Al het tekenen in Pattern paintComponent functie.
 * Done: Zoveel mogelijk tekenen in BufferedImage.
 * Done: Swing Timer voor animatie gebruiken.
 * 
 * Nice to have:
 * Done: Verschijnen patroon animatie. 
 * Done: Enkele click om patroon te selecteren.
 * 
 * Next:
 * TODO: Lengte patroon tot 32 of 64 stappen.
 * TODO: Breedte en hoogte venster opslaan / herstellen in prefs.
 * TODO: MIDI panic button.
 * TODO: Tempo tot 300 BPM.
 * TODO: Triplets?
 * TODO: Note names as well as note numbers.
 * 
 * Program argument:
 * -Dcom.apple.macos.useScreenMenuBar = true
 */

public class SequencerMain
{
	public static void main ( String [ ] args )
	{
		SwingUtilities.invokeLater ( new Runnable ( )
		{
			public void run ( )
			{
				new SequencerFrame ( );
			}
		} );
	}
}
