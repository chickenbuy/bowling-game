package bowling.game;

/**
 * Represents a frame type in bowling.
 */
public enum FrameType
{
    /**
     * Bonus frame.
     */
    BONUS(0),

    /**
     * Not all pins knocked down in frame.
     */
    PINS(0),

    /**
     * All pins knocked down in 2 rolls.
     */
    SPARE(1),

    /**
     * All pins knocked down in 1 roll.
     */
    STRIKE(2);

    private final int numberOfBonusRolls;

    /**
     * Constructor for {@link FrameType}.
     * @param numberOfBonusRolls The number of bonus rolls the {@link FrameType gives}.
     */
    FrameType(final int numberOfBonusRolls)
    {
        this.numberOfBonusRolls = numberOfBonusRolls;
    }

    /**
     * @return The number of bonus rolls.
     */
    public int getNumberOfBonusRolls()
    {
        return numberOfBonusRolls;
    }
}
