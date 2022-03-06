package bowling.game;

import com.google.common.base.Preconditions;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Represents a frame of bowling.
 */
public class Frame
{
    private final FrameType frameType;
    private final int firstRollValue;
    private final int secondRollValue;

    /**
     * Creates a new {@link Frame.Builder}.
     * @return The non-null {@link Frame.Builder}.
     */
    @NonNull
    public static Builder newBuilder()
    {
        return new Builder();
    }

    /**
     * @return The non-null {@link FrameType}.
     */
    public FrameType getFrameType()
    {
        return frameType;
    }

    /**
     * @return The non-negative first roll value.
     */
    public int getFirstRollValue()
    {
        return firstRollValue;
    }

    /**
     * @return The non-negative second roll value.
     */
    public int getSecondRollValue()
    {
        return secondRollValue;
    }

    /**
     * Constructor for {@link Frame}. Declared private to prevent direct instantiation.
     * @param builder The {@link Builder}.
     * @throws IllegalStateException If frameType is null.
     */
    private Frame(@NonNull final Builder builder)
    {
        Preconditions.checkState(builder.frameType != null, "frameType cannot be null.");

        frameType = builder.frameType;
        firstRollValue = builder.firstRollValue;
        secondRollValue = builder.secondRollValue;
    }

    /**
     * Builder for {@link Frame}.
     */
    public static final class Builder
    {
        private FrameType frameType;
        private int firstRollValue;
        private int secondRollValue;

        /**
         * Constructor for {@link Builder}. Declared private to prevent direct instantiation.
         */
        private Builder()
        {
        }

        /**
         * @param frameType The {@link FrameType}.
         * @return The non-null {@link Builder}.
         * @throws IllegalArgumentException If the frameType is {@code null}.
         */
        public Builder withFrameType(@NonNull final FrameType frameType)
        {
            Preconditions.checkArgument(frameType != null, "frameType cannot be null.");
            this.frameType = frameType;
            return this;
        }

        /**
         * @param firstRollValue The first roll value.
         * @return The non-null {@link Builder}.
         * @throws IllegalArgumentException If the firstRollValue is negative.
         */
        public Builder withFirstRollValue(final int firstRollValue)
        {
            Preconditions.checkArgument(firstRollValue >= 0, "firstRollValue must be non-negative.");
            this.firstRollValue = firstRollValue;
            return this;
        }

        /**
         * @param secondRollValue The second roll value.
         * @return The non-null {@link Builder}.
         * @throws IllegalArgumentException If the secondRollValue is negative.
         */
        public Builder withSecondRollValue(final int secondRollValue)
        {
            Preconditions.checkArgument(secondRollValue >= 0, "secondRollValue must be non-negative.");
            this.secondRollValue = secondRollValue;
            return this;
        }

        /**
         * @return The non-null instance of a {@link Frame}.
         */
        @NonNull
        public Frame build()
        {
            return new Frame(this);
        }
    }
}
