package bowling.game;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests for {@link BowlingGame}.
 */
public class BowlingGameTest
{
    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a {@code null} game.
     */
    @Test
    public void testCalculateScore_NullGame()
    {
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore((String) null)).
                isInstanceOf(IllegalArgumentException.class).hasMessage("game cannot be null, empty, or blank");
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with an empty game.
     */
    @Test
    public void testCalculateScore_EmptyGame()
    {
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(""))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("game cannot be null, empty, or blank");
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a blank game.
     */
    @Test
    public void testCalculateScore_BlankGame()
    {
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore("   "))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("game cannot be null, empty, or blank");
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game with too few frames.
     */
    @Test
    public void testIsValidBowlingGame_TooFewFrames()
    {
        final String game = "X|9-||";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage(String.format("[%s] is not a valid bowling game.", game));
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game with too many frames.
     */
    @Test
    public void testIsValidBowlingGame_TooManyFrames()
    {
        final String game = "1-|2-|3-|4-|5-|6-|7-|8-|9-|1-|11||";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage(String.format("[%s] is not a valid bowling game.", game));
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game with too few bonus frame rolls.
     */
    @Test
    public void testIsValidBowlingGame_TooFewBonusFrameRolls()
    {
        final String game = "1-|2-|3-|4-|5-|6-|7-|8-|9-|X||9";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage(String.format("[%s] is not a valid bowling game.", game));
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game with too many bonus frame rolls.
     */
    @Test
    public void testIsValidBowlingGame_TooManyBonusFrameRolls()
    {
        final String game = "1-|2-|3-|4-|5-|6-|7-|8-|9-|1/||91";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage(String.format("[%s] is not a valid bowling game.", game));
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game with too many bonus frames.
     */
    @Test
    public void testIsValidBowlingGame_TooManyBonusFrames()
    {
        final String game = "1-|2-|3-|4-|5-|6-|7-|8-|9-|1/||9||9";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage(String.format("[%s] is not a valid bowling game.", game));
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game having a frame with an invalid strike symbol.
     */
    @Test
    public void testGetFrameType_InvalidStrikeSymbol()
    {
        final String game = "1-|2-|T|4-|5-|6-|7-|8-|9-|1-||";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[T] is not a valid frame.");
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game having a frame with too many rolls.
     */
    @Test
    public void testGetFrameType_TooManyRolls()
    {
        final String game = "123|2-|3-|4-|5-|6-|7-|8-|9-|1-||";
        assertThatThrownBy(() ->
                BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("[123] is not a valid frame.");
    }

    /**
     * Ensures that an {@link IllegalArgumentException} is thrown with the correct message when
     * {@link BowlingGame#calculateScore} is called with a game having a frame with too many pins knocked down.
     */
    @Test
    public void testGetFrame_TooManyPins()
    {
        final String game = "1-|2-|3-|99|5-|6-|7-|8-|9-|1-||";
        assertThatThrownBy(() -> BowlingGame.createStandardGame().calculateScore(game))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Number of pins exceeds limit of 10.");
    }

    /**
     * Ensures that an {@link NumberFormatException} is thrown when {@link BowlingGame#calculateScore} is called with
     * a game having a frame with a roll that cannot be parsed to an integer.
     */
    @Test
    public void testGetRollValue_RollNotParsableToInteger()
    {
        final String game = "1-|2-|3-|9G|5-|6-|7-|8-|9-|1-||";
        assertThatThrownBy(() -> BowlingGame.createStandardGame()
                .calculateScore(game)).isInstanceOf(NumberFormatException.class);
    }

    /**
     * Ensures that the correct score is returned from {@link BowlingGame#calculateScore} when called with a game of
     * all strikes.
     */
    @Test
    public void testCalculateScore_PerfectGame()
    {
        final String game = "X|X|X|X|X|X|X|X|X|X||XX";
        assertThat(BowlingGame.createStandardGame().calculateScore(game)).isEqualTo(300);
    }

    /**
     * Ensures that the correct score is returned from {@link BowlingGame#calculateScore} when called with a game of
     * all spares.
     */
    @Test
    public void testCalculateScore_OnlySpares()
    {
        final String game = "5/|5/|5/|5/|5/|5/|5/|5/|5/|5/||5";
        assertThat(BowlingGame.createStandardGame().calculateScore(game)).isEqualTo(150);
    }

    /**
     * Ensures that the correct score is returned from {@link BowlingGame#calculateScore} when called with a game
     * with no spares or strikes.
     */
    @Test
    public void testCalculateScore_NoSparesOrStrikes()
    {
        final String game = "9-|9-|9-|9-|9-|9-|9-|9-|9-|9-||";
        assertThat(BowlingGame.createStandardGame().calculateScore(game)).isEqualTo(90);
    }

    /**
     * Ensures that the correct score is returned from {@link BowlingGame#calculateScore} when called with a game
     * with some strikes, some spares, some pins.
     */
    @Test
    public void testCalculateScore_Random()
    {
        final String game = "X|7/|9-|X|-8|8/|-6|X|X|X||81";
        assertThat(BowlingGame.createStandardGame().calculateScore(game)).isEqualTo(167);
    }
}
