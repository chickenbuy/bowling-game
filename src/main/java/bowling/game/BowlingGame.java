package bowling.game;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculates the final score of a bowling game.
 */
public class BowlingGame
{
    private final int numberOfFrames;
    private final int numberOfPins;
    private final char strikeSymbol;
    private final char spareSymbol;
    private final char missSymbol;

    /**
     * Creates a standard ten pin, ten frame {@link BowlingGame} with 'X' strike symbol, '/' spare symbol and '-' miss symbol.
     * @return The non-null {@link BowlingGame} with standard values.
     */
    @NonNull
    public static BowlingGame createStandardGame()
    {
        return new BowlingGame(10, 10, 'X', '/', '-');
    }

    /**
     * Creates a custom {@link BowlingGame}.
     * @param numberOfFrames The number of frames.
     * @param numberOfPins The number of pins.
     * @param strikeSymbol The strike symbol.
     * @param spareSymbol The spare symbol.
     * @param missSymbol The miss symbol.
     * @return The non-null {@link BowlingGame} with custom values.
     * @throws IllegalArgumentException If numberOfFrames or numberOfPins is not positive.
     */
    @NonNull
    public static BowlingGame createCustomGame(final int numberOfFrames, final int numberOfPins, final char strikeSymbol, final char spareSymbol, final char missSymbol)
    {
        return new BowlingGame(numberOfFrames, numberOfPins, strikeSymbol, spareSymbol, missSymbol);
    }

    /**
     * Constructor for {@link BowlingGame}.
     * @param numberOfFrames The number of frames.
     * @param numberOfPins The number of pins.
     * @param strikeSymbol The strike symbol.
     * @param spareSymbol The spare symbol.
     * @param missSymbol The miss symbol.
     * @throws IllegalArgumentException If numberOfFrames or numberOfPins is not positive.
     */
    private BowlingGame(final int numberOfFrames, final int numberOfPins, final char strikeSymbol, final char spareSymbol, final char missSymbol)
    {
        Preconditions.checkArgument(numberOfFrames > 0, "numberOfFrames must be positive.");
        Preconditions.checkArgument(numberOfPins > 0, "numberOfPins must be positive.");

        this.numberOfFrames = numberOfFrames;
        this.numberOfPins = numberOfPins;
        this.strikeSymbol = strikeSymbol;
        this.spareSymbol = spareSymbol;
        this.missSymbol = missSymbol;
    }

    /**
     * Calculates the score of the given game.
     * @param game The game to calculate the score of.
     * @return The non-negative score of the game.
     * @throws IllegalArgumentException If game is null, empty, or blank.
     * @throws RuntimeException If an unexpected {@link FrameType} is found.
     */
    public int calculateScore(@NonNull final String game)
    {
        Preconditions.checkArgument(!StringUtils.isBlank(game), "game cannot be null, empty, or blank");
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
                case BONUS:
                    // For the bonus frame remove base value otherwise it will be counted twice.
                    totalScore -= (currentFrame.getFirstRollValue() + currentFrame.getSecondRollValue());
                    break;
                default:
                    throw new RuntimeException("Something went wrong.");
            }
        }
        return totalScore;
    }

    /**
     * Parses the string bowling game into a List of {@link Frame}s.
     * @param bowlingGame The string bowling game to parse.
     * @return The non-null and non-empty List of {@link Frame}s.
     * @throws IllegalArgumentException If the bowlingGame is invalid.
     */
    @NonNull
    private List<Frame> parseBowlingGame(@NonNull final String bowlingGame)
    {
        final String[] splitBowlingGame = bowlingGame.split("\\|\\|", 2);
        final String[] frameStrings = splitBowlingGame[0].split("\\|");
        final String bonusFrameString = splitBowlingGame[1];

        if (isValidBowlingGame(frameStrings, bonusFrameString))
        {
            final List<Frame> frames = new ArrayList<>(frameStrings.length);
            for (final String frameString : frameStrings)
            {
                final FrameType frameType = getFrameType(frameString);
                frames.add(getFrame(frameString, frameType));
            }
            frames.add(getFrame(bonusFrameString, FrameType.BONUS));
            return frames;
        }
        throw new IllegalArgumentException(String.format("[%s] is not a valid bowling game.", bowlingGame));
    }

    /**
     * Gets the {@link FrameType} of the string frame.
     * @param frame The string frame.
     * @return The non-null {@link FrameType} of the supplied string frame.
     * @throws IllegalArgumentException If the frame is invalid.
     */
    @NonNull
    private FrameType getFrameType(@NonNull final String frame)
    {
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

    /**
     * Creates a {@link Frame} from the given information.
     * @param frameString The frame string.
     * @param frameType The {@link FrameType}.
     * @return The non-null {@link Frame}.
     * @throws IllegalArgumentException If the number of pins knocked down exceeds the limit.
     * @throws RuntimeException If an unsupported FrameType is supplied.
     */
    @NonNull
    private Frame getFrame(@NonNull final String frameString, @NonNull final FrameType frameType)
    {
        switch (frameType)
        {
            case STRIKE:
            {
                return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(numberOfPins).withSecondRollValue(0).build();
            }
            case SPARE:
            {
                final int firstRollValue = getRollValue(frameString.charAt(0));
                // This check is here if the number of pins is less than 9.
                if (firstRollValue > numberOfPins)
                {
                    throw new IllegalArgumentException(String.format("Number of pins exceeds limit of %d.", numberOfPins));
                }
                return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(firstRollValue).withSecondRollValue(numberOfPins - firstRollValue).build();
            }
            case PINS:
            {
                final int firstRollValue = getRollValue(frameString.charAt(0));
                final int secondRollValue = getRollValue(frameString.charAt(1));
                final int baseFrameValue = firstRollValue + secondRollValue;
                if (baseFrameValue > numberOfPins)
                {
                    throw new IllegalArgumentException(String.format("Number of pins exceeds limit of %d.", numberOfPins));
                }
                return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(firstRollValue).withSecondRollValue(secondRollValue).build();
            }
            case BONUS:
            {
                if (frameString.isEmpty())
                {
                    return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(0).withSecondRollValue(0).build();
                }

                final int firstRollValue = getRollValue(frameString.charAt(0));
                if (frameString.length() == 1)
                {
                    return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(firstRollValue).withSecondRollValue(0).build();
                }
                final int secondRollValue = getRollValue(frameString.charAt(1));
                return Frame.newBuilder().withFrameType(frameType).withFirstRollValue(firstRollValue).withSecondRollValue(secondRollValue).build();
            }
            default:
                throw new RuntimeException("Unsupported FrameType encountered.");
        }
    }

    /**
     * Gets the roll value.
     * @param roll The roll.
     * @return The number of pins knocked down for the given roll.
     * @throws NumberFormatException If the roll is not a parsable integer. See {@link Integer#parseInt(String)}.
     */
    private int getRollValue(final char roll)
    {
        if (roll == missSymbol)
        {
            return 0;
        }
        if (roll == strikeSymbol || roll == spareSymbol)
        {
            return 10;
        }
        return Integer.parseInt(String.valueOf(roll));
    }

    /**
     * Validates if the bowling game.
     * @param frames The array of strings of the frames.
     * @param bonusFrame The bonus frame string.
     * @return True if number of frames is equal to {@link #numberOfFrames} and there are the correct number of rolls in the bonus frame, false otherwise.
     */
    private boolean isValidBowlingGame(@NonNull final String[] frames, @NonNull final String bonusFrame)
    {
        if (frames.length != numberOfFrames)
        {
            return false;
        }
        return getFrameType(frames[frames.length - 1]).getNumberOfBonusRolls() == bonusFrame.length();
    }
}
