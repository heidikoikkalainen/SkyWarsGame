package skyWarsGame;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GameStateTest.class, GridTest.class, RowTest.class, SquareTest.class })
public class AllTests {

}
