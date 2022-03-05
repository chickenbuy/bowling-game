package bowling.game;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame
{
    public static int calculateScore(@NonNull final String game)
    {
        Preconditions.checkArgument(!game.isBlank(), "game cannot be null, empty, or blank");
        final List<Pair<Integer, FrameType>> frameValuesAndFrameTypes = parseBowlingGame(game);
        return frameValuesAndFrameTypes.stream().mapToInt(Pair::getLeft).sum();
    }

    private static List<Pair<Integer, FrameType>> parseBowlingGame(@NonNull final String bowlingGame)
    {
        final String[] splitBowlingGame = bowlingGame.split("\\|\\|", 2);
        final String[] frames = splitBowlingGame[0].split("\\|");
        final String bonusBalls = splitBowlingGame[1];

        if (hasCorrectNumberOfFrames(frames))
        {
            final List<Pair<Integer, FrameType>> baseFrameValuesAndFrameTypes = new ArrayList<>(frames.length);
            for (final String frame : frames)
            {
                final FrameType frameType = getFrameType(frame);
                baseFrameValuesAndFrameTypes.add(ImmutablePair.of(getBaseFrameValue(frame, frameType), frameType));
            }
            return baseFrameValuesAndFrameTypes;
        }

        throw new IllegalArgumentException(String.format("[%s] is not a valid bowling game.", bowlingGame));
    }

    private static FrameType getFrameType(@NonNull final String frame)
    {
        if (frame.length() == 1)
        {
            if (frame.equals("X"))
            {
                return FrameType.STRIKE;
            }
            throw new IllegalArgumentException(String.format("[%s] is not a valid frame.", frame));
        }

        if (frame.length() == 2)
        {
            if (frame.charAt(1) == '/')
            {
                return FrameType.SPARE;
            }
            return FrameType.PINS;
        }
        throw new IllegalArgumentException(String.format("[%s] is not a valid frame.", frame));
    }

    private static int getBaseFrameValue(final String frame, final FrameType frameType)
    {
        switch (frameType)
        {
            case STRIKE:
            case SPARE:
                return 10;
            case PINS:
                final int frameValue = getRollValue(frame.charAt(0)) + getRollValue(frame.charAt(1));
                if (frameValue > 10)
                {
                    throw new IllegalArgumentException("Number of pins exceeds limit of 10.");
                }
                return frameValue;
            default:
                throw new RuntimeException("Invalid FrameType encountered.");
        }
    }

    private static int getRollValue(final char roll)
    {
        if (roll == '-')
        {
            return 0;
        }

        final int rollValue = Character.getNumericValue(roll);
        if (rollValue != -1 && rollValue != -2)
        {
            return rollValue;
        }
        throw new IllegalArgumentException(String.format("[%s] is not a valid roll.", roll));
    }

    private static boolean hasCorrectNumberOfFrames(@NonNull final String[] frames)
    {
        return frames.length == 10;
    }
}
