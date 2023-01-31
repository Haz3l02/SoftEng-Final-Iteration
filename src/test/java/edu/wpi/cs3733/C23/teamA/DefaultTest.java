/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.databases.*;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class DefaultTest {
  @Test
  public void test() throws SQLException, ClassNotFoundException {
    new Adb();
    System.out.println(Node.getAll());
    System.out.println(Move.getAll());
    System.out.println(LocationName.getAll());
    System.out.println(Edge.getAll());
    System.out.println(Move.mostRecentLoc("L1X2665Y1043"));
  }
}
