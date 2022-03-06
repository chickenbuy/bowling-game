package bowling.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link BowlingGame}.
 */
public class BowlingGameTest {
    @Test
    public void testExample()
    {
        final String game = "X|7/|9-|X|-8|8/|-6|X|X|X||--";
        assertEquals(0, BowlingGame.calculateScore(game));
    }
}
