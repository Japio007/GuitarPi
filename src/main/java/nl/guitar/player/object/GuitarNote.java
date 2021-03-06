package nl.guitar.player.object;

import nl.guitar.player.GuitarPlayer;
import nl.guitar.player.strategy.StringStrategy;
import nl.guitar.player.tuning.GuitarTuning;
import org.jfugue.theory.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GuitarNote {
    private static final Logger logger = LoggerFactory.getLogger(GuitarNote.class);
    private final static short NO_STRING = -1;
    private short stringNumber = NO_STRING;
    private int fred = 0;
    private boolean hit = true;
    private String name;
    private int noteValue;
    private String noteDuration;

    public GuitarNote() {
        super();
    }

    public GuitarNote(Note note, GuitarTuning guitarTuning, int[] stringsTaken, double duration, StringStrategy stringStrategy) {
        this.noteDuration = String.valueOf(duration);
        name = note.toString();
        noteValue = note.getValue();

        List<Short> possibleStringNumber = new ArrayList<>(6);

        for (short i = 0; i < 6; i++) {
            if (noteValue >= guitarTuning.getStartNote(i) && noteValue <= guitarTuning.getEndNote(i)) {
                possibleStringNumber.add(i);
            }
        }
        if (!possibleStringNumber.isEmpty()) {
            Optional<Short> firstString = stringStrategy.getBestString(possibleStringNumber, stringsTaken, note.getValue());
            stringNumber = firstString.orElse(NO_STRING);
        }
        if (stringNumber >= 0) {
            fred = noteValue - guitarTuning.getStartNote(stringNumber);
        } else {
            hit = false;
            logger.warn("[" + stringStrategy.getClass().getSimpleName() + "] No Strings available for note " + noteValue + "[" + guitarTuning.getStartNote(0) + " - " + guitarTuning.getEndNote(5) + "] => " + Arrays.toString(stringsTaken));
        }
    }

    public GuitarNote(short stringNumber, int fred, boolean hit) {
        this.stringNumber = stringNumber;
        this.fred = fred;
        this.hit = hit;
        this.noteValue = -1;
    }

    public int getNoteValue() {
        return noteValue;
    }

    public short getStringNumber() {
        return stringNumber;
    }

    public int getFred() {
        return fred;
    }

    public boolean isHit() {
        return hit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GuitarNote that = (GuitarNote) o;

        if (stringNumber != that.stringNumber) return false;
        return fred == that.fred;
    }

    @Override
    public int hashCode() {
        int result = stringNumber;
        result = 31 * result + fred;
        return result;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GuitarNote{" +
                "noteValue=" + noteValue +
                ", noteDuration=" + noteDuration +
                ", stringNumber=" + stringNumber +
                ", fred=" + fred +
                ", hit=" + hit +
                ", name='" + name + '\'' +
                '}';
    }
}
