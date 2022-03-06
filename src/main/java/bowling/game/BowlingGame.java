package bowling.game;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class BowlingGame
{
    private static final int NUMBER_OF_FRAMES = 10;
    private static final int NUMBER_OF_PINS = 10;

    public static int calculateScore(@NonNull final String game)
    {
        Preconditions.checkArgument(!game.isBlank(), "game cannot be null, empty, or blank");
        final List<Frame> frames = parseBowlingGame(game);
        int totalScore = 0;
        int firstRollMultiplier = 1;
        int secondRollMultiplier = 1;

        for (final Frame currentFrame : frames)
        {
            totalScore += (currentFrame.getFirstRollValue() * firstRollMultiplier) + (currentFrame.getSecondRollValue() * secondRollMultiplier);
            switch (currentFrame.getFrameType())
            {
                case PINS:
                    firstRollMultiplier = 1;
                    secondRollMultiplier = 1;
                    break;
                case SPARE:
                    firstRollMultiplier = 2;
                    secondRollMultiplier = 1;
                    break;
                case STRIKE:
                    firstRollMultiplier = secondRollMultiplier + 1;
                    secondRollMultiplier = 2;
                    break;
                default:
                    throw new RuntimeException("Something went wrong.");
            }
        }
        return totalScore;
    }

    private static List<Frame> parseBowlingGame(@NonNull final String bowlingGame)
    {
        final String[] splitBowlingGame = bowlingGame.split("\\|\\|", 2);
        final String[] frameStrings = splitBowlingGame[0].split("\\|");
        final String bonusFrameString = splitBowlingGame[1];

        if (hasCorrectNumberOfFrames(frameStrings))
        {
            final List<Frame> frames = new ArrayList<>(frameStrings.length);
            for (final String frameString : frameStrings)
            {
                final FrameType frameType = getFrameType(frameString);
                frames.add(getBaseFrame(frameString, frameType, Frame.newBuilder()).build());
            }


            return frames;
        }

        throw new IllegalArgumentException(String.format("[%s] is not a valid bowling game.", bowlingGame));
    }

    private static FrameType getFrameType(@NonNull final String frame)
    {
        final char strikeSymbol = 'X';
        final char spareSymbol = '/';

        if (frame.length() == 1)
        {
            if (frame.charAt(0) == strikeSymbol)
            {
                return FrameType.STRIKE;
            }
            throw new IllegalArgumentException(String.format("[%s] is not a valid frame.", frame));
        }

        if (frame.length() == 2)
        {
            if (frame.charAt(1) == spareSymbol)
            {
                return FrameType.SPARE;
            }
            return FrameType.PINS;
        }
        throw new IllegalArgumentException(String.format("[%s] is not a valid frame.", frame));
    }

    private static Frame.Builder getBaseFrame(final String frameString, final FrameType frameType, final Frame.Builder frameBuilder)
    {
        switch (frameType)
        {
            case STRIKE:
                frameBuilder.withBaseValue(NUMBER_OF_PINS).withFrameType(FrameType.STRIKE).withFirstRollValue(NUMBER_OF_PINS).withSecondRollValue(0);
                return frameBuilder;
            case SPARE:
                final int spareFirstRollValue = getRollValue(frameString.charAt(0));
                if (spareFirstRollValue > NUMBER_OF_PINS)
                {
                    throw new IllegalArgumentException(String.format("Number of pins exceeds limit of %d.", NUMBER_OF_PINS));
                }
                frameBuilder.withBaseValue(NUMBER_OF_PINS).withFrameType(FrameType.SPARE).withFirstRollValue(spareFirstRollValue).withSecondRollValue(NUMBER_OF_PINS - spareFirstRollValue);
                return frameBuilder;
            case PINS:
                final int firstRollValue = getRollValue(frameString.charAt(0));
                final int secondRollValue = getRollValue(frameString.charAt(1));
                final int baseFrameValue = firstRollValue + secondRollValue;
                if (baseFrameValue > NUMBER_OF_PINS)
                {
                    throw new IllegalArgumentException("Number of pins exceeds limit of 10.");
                }
                frameBuilder.withBaseValue(baseFrameValue).withFrameType(FrameType.PINS).withFirstRollValue(firstRollValue).withSecondRollValue(secondRollValue);
                return frameBuilder;
            default:
                throw new RuntimeException("Invalid FrameType encountered.");
        }
    }

    private static int getRollValue(final char roll)
    {
        final char gutterBallSymbol = '-';
        if (roll == gutterBallSymbol)
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
        return frames.length == NUMBER_OF_FRAMES;
    }

    private static boolean isValidBonusFrame(@NonNull final Frame lastFrame, @NonNull final String bonusFrame)
    {
        Preconditions.checkArgument(lastFrame.getFrameType() != FrameType.BONUS, "The bonus frame already happened.");

        return bonusFrame.length() == lastFrame.getFrameType().getNumBonusRolls();
    }
}
