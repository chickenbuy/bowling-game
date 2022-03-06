package bowling.game;

public class Frame
{
    private final int baseValue;
    private final FrameType frameType;
    private final int firstRollValue;
    private final int secondRollValue;

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public int getBaseValue()
    {
        return baseValue;
    }

    public FrameType getFrameType()
    {
        return frameType;
    }

    public int getFirstRollValue()
    {
        return firstRollValue;
    }

    public int getSecondRollValue()
    {
        return secondRollValue;
    }


    private Frame(Builder builder)
    {
        baseValue = builder.baseValue;
        frameType = builder.frameType;
        firstRollValue = builder.firstRollValue;
        secondRollValue = builder.secondRollValue;
    }

    public static final class Builder
    {
        private int baseValue;
        private FrameType frameType;
        private int firstRollValue;
        private int secondRollValue;

        public Builder()
        {
        }

        public Builder withBaseValue(final int baseValue)
        {
            this.baseValue = baseValue;
            return this;
        }

        public Builder withFrameType(final FrameType frameType)
        {
            this.frameType = frameType;
            return this;
        }

        public Builder withFirstRollValue(final int firstRollValue)
        {
            this.firstRollValue = firstRollValue;
            return this;
        }

        public Builder withSecondRollValue(final int secondRollValue)
        {
            this.secondRollValue = secondRollValue;
            return this;
        }

        public Frame build()
        {
            return new Frame(this);
        }
    }
}
