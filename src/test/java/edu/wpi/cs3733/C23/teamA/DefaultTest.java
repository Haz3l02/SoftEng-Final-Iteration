/*-------------------------*/
/* DO NOT DELETE THIS TEST */
/*-------------------------*/

package edu.wpi.cs3733.C23.teamA;

import edu.wpi.cs3733.C23.teamA.databases.Adb;
import edu.wpi.cs3733.C23.teamA.databases.Node;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class DefaultTest {
  @Test
  public void test() throws SQLException, ClassNotFoundException {
    new Adb();
    System.out.println(Node.getAll());
  }
}
