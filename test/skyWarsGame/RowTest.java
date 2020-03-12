package skyWarsGame;

import static org.junit.Assert.*;

import org.junit.Test;

public class RowTest {

	@Test
	public void testSetRowNumber() {
		Row row = new Row();
		int testNum = 1;
		
		row.setRowNumber(testNum);
		
		int actual = row.getRowNumber();
		int expected = testNum;
		
		assertTrue(actual == expected);
		
	}

}
