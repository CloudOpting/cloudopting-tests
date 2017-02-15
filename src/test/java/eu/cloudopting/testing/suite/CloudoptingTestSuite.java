package eu.cloudopting.testing.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import eu.cloudopting.testing.tests.TestLogin;
import eu.cloudopting.testing.tests.TestServiceManagement;
import eu.cloudopting.testing.tests.TestUserManagement;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  TestLogin.class,
  TestUserManagement.class,
  TestServiceManagement.class
})

public class CloudoptingTestSuite {

}
