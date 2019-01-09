# uncaught-exception-rule
A junit rule which causes test failure when an uncaught exception is occured on threads other than main thread of the test.

## Sample Usage

```
@Rule
public UncaughtExceptionRule rule = new UncaughtExceptionRule();

@Test
public void test() throws InterruptedException {
  Thread t = new Thread(() -> {
      int ignored = 10 / 0;
  });
  t.start();
  t.join();

  Assert.assertNotNull(rule.getException());
  Assert.assertTrue(rule.getException() instanceof ArithmeticException);
  rule.clearException();
}
```
 
## Add it to your project
You can reference to this library by either of java build systems (Maven, Gradle, SBT or Leiningen) using snippets from this jitpack link:
[![](https://jitpack.io/v/sahabpardaz/uncaught-exception-rule.svg)](https://jitpack.io/#sahabpardaz/uncaught-exception-rule)
