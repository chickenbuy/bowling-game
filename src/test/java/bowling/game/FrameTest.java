package bowling.game;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link Frame}.
 */
public class FrameTest
{
    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link Frame.Builder#withFrameType} is called with a {@code null} {@link FrameType}.
     */
    @Test
    public void testNullFrameType()
    {
        assertThatThrownBy(() ->
                Frame.newBuilder().withFrameType((FrameType) null))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("frameType cannot be null.");
    }

    /**
     * Ensures that an {@link IllegalStateException} is thrown with the correct message when
     * {@link Frame.Builder#build()} is called without setting the {@link FrameType}.
     */
    @Test
    public void testWithoutFrameType()
    {
        assertThatThrownBy(() ->
                Frame.newBuilder().withFirstRollValue(1).withSecondRollValue(2).build())
                .isInstanceOf(IllegalStateException.class).hasMessage("frameType cannot be null.");
    }

    /**
     * Ensures that the correct {@link FrameType} is set on the {@link Frame} when supplied.
     */
    @Test
    public void testWithFrameType()
    {
        final Frame frame =
                Frame.newBuilder().withFrameType(FrameType.PINS).withFirstRollValue(1).withSecondRollValue(2).build();
        assertThat(frame.getFrameType()).isEqualTo(FrameType.PINS);
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link Frame.Builder#withFirstRollValue} is called with a negative firstRollValue.
     */
    @Test
    public void testNegativeFirstRollValue()
    {
        assertThatThrownBy(() ->
                Frame.newBuilder().withFirstRollValue(-1))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("firstRollValue must be non-negative.");
    }

    /**
     * Ensures that the correct firstRollValue is set on the {@link Frame} when not supplied.
     */
    @Test
    public void testWithoutFirstRollValue()
    {
        final Frame frame =
                Frame.newBuilder().withFrameType(FrameType.PINS).withSecondRollValue(2).build();
        assertThat(frame.getFirstRollValue()).isEqualTo(0);
    }

    /**
     * Ensures that the correct firstRollValue is set on the {@link Frame} when supplied.
     */
    @Test
    public void testWithFirstRollValue()
    {
        final Frame frame =
                Frame.newBuilder().withFrameType(FrameType.PINS).withFirstRollValue(1).withSecondRollValue(2).build();
        assertThat(frame.getFirstRollValue()).isEqualTo(1);
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link Frame.Builder#withFirstRollValue} is called with a negative secondRollValue.
     */
    @Test
    public void testNegativeSecondRollValue()
    {
        assertThatThrownBy(() ->
                Frame.newBuilder().withSecondRollValue(-1))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("secondRollValue must be non-negative.");
    }

    /**
     * Ensures that the correct secondRollValue is set on the {@link Frame} when not supplied.
     */
    @Test
    public void testWithoutSecondRollValue()
    {
        final Frame frame =
                Frame.newBuilder().withFrameType(FrameType.PINS).withFirstRollValue(1).build();
        assertThat(frame.getSecondRollValue()).isEqualTo(0);
    }

    /**
     * Ensures that the correct secondRollValue is set on the {@link Frame} when supplied.
     */
    @Test
    public void testWithSecondRollValue()
    {
        final Frame frame =
                Frame.newBuilder().withFrameType(FrameType.PINS).withFirstRollValue(1).withSecondRollValue(2).build();
        assertThat(frame.getSecondRollValue()).isEqualTo(2);
    }
}
