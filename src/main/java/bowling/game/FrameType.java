package bowling.game;

public enum FrameType
{
    BONUS(0),

    PINS(0),

    SPARE(1),

    STRIKE(2);

    private final int numBonusRolls;

    FrameType(final int numBonusRolls)
    {
        this.numBonusRolls = numBonusRolls;
    }

    public int getNumBonusRolls()
    {
        return numBonusRolls;
    }
}
