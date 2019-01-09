# Uncaught Exception Rule
It is a junit rule which causes test failure when an uncaught exception is occured on secandary threads (threads other than main thread of the test).


## Motivation
Using JUnit, you can have a passed test, even when it throws an uncaught exception in a secondary thread (any thread other than the main thread of the test). For example, the following test, will pass:
```
@Test
public void test() throws InterruptedException {
  Thread t = new Thread(() -> {
      int ignored = 10 / 0;
  });
  t.start();
  t.join();
}
```
Even asserting from a secondary thread, does not cause test failure. This test still passes:
```
@Test
public void test() throws InterruptedException {
  Thread t = new Thread(() -> {
      assertTrue(1 > 2);
  });
  t.start();
  t.join();
}
```
You may apply workaround soloutions like this:
```
@Test
public void test() throws InterruptedException {
  AtomicBoolean finishedSuccessfully = new AtomicBoolean(false);
  Thread t = new Thread(() -> {
       int ignored = 10 / 0;
       finishedSuccessfully.set(true);
  });
  t.start();
  t.join();
  
  assertTrue(finishedSuccessfully);
}
```
This time, it works and you will see a failed report by JUnit. But how about the threads which are not created directly in unit test and it is created because of an asynch. call in your test?
This JUnit rule provides a simple solution. It is enough to define the rule in test class:
```
@Rule
public UncaughtExceptionRule rule = new UncaughtExceptionRule();
```
By defining this rule, any uncaugh exception in any thread will cause test failure. For example, the first two examples here, both fail if you define this rule. 

But we have another use case for this rule too and that's when you expect to see an specific uncaught exception. When you expect it and you want to check it, again you can use this rule:
```
@Test
public void testAssertOnUnhandledException() throws InterruptedException {
    Thread t = new Thread(() -> {
        int ignored = 10 / 0;
    });
    t.start();
    t.join();

    Assert.assertNotNull(rule.getException());
    Assert.assertTrue(rule.getException() instanceof ArithmeticException);
}
```
 
## Add it to your project
You can reference to this library by either of java build systems (Maven, Gradle, SBT or Leiningen) using snippets from this jitpack link:
[![](https://jitpack.io/v/sahabpardaz/uncaught-exception-rule.svg)](https://jitpack.io/#sahabpardaz/uncaught-exception-rule)
