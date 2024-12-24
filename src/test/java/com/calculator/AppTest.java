package com.calculator;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    private static App app;
    private static Stage stage;
    private static CountDownLatch latch;

    @BeforeAll
    public static void initJFX() throws InterruptedException {
        latch = new CountDownLatch(1);
        Platform.startup(() -> {
            app = new App();
            stage = new Stage();
            try {
                app.start(stage);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @BeforeEach
    public void setUp() {
        Platform.runLater(() -> {
            app.handleClear();
        });
        waitForFxEvents();
    }

    @AfterAll
    public static void cleanup() {
        Platform.runLater(() -> {
            stage.close();
        });
    }

    private void waitForFxEvents() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testInitialState() {
        Platform.runLater(() -> {
            assertEquals("0", app.getDisplay(), "Initial display should be 0");
        });
        waitForFxEvents();
    }

    @Test
    @Order(2)
    public void testBasicAddition() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleClear();
                app.handleNumber("5");
                app.handleOperator("+");
                app.handleNumber("3");
                app.handleEquals();
                waitForFxEvents();
                assertEquals("8.0", app.getDisplay(), "5 + 3 should equal 8.0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void testBasicSubtraction() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleClear();
                app.handleNumber("10");
                app.handleOperator("-");
                app.handleNumber("4");
                app.handleEquals();
                waitForFxEvents();
                assertEquals("6.0", app.getDisplay(), "10 - 4 should equal 6.0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void testBasicMultiplication() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleClear();
                app.handleNumber("6");
                app.handleOperator("×");
                app.handleNumber("7");
                app.handleEquals();
                waitForFxEvents();
                assertEquals("42.0", app.getDisplay(), "6 × 7 should equal 42.0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(5)
    public void testBasicDivision() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleClear();
                app.handleNumber("15");
                app.handleOperator("÷");
                app.handleNumber("3");
                app.handleEquals();
                waitForFxEvents();
                assertEquals("5.0", app.getDisplay(), "15 ÷ 3 should equal 5.0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(6)
    public void testDivisionByZero() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleClear();
                app.handleNumber("10");
                app.handleOperator("÷");
                app.handleNumber("0");
                app.handleEquals();
                waitForFxEvents();
                assertEquals("NaN", app.getDisplay(), "Division by zero should show NaN");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void testSin() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("0");
                waitForFxEvents();
                app.handleScientificOperation("sin");
                waitForFxEvents();
                assertEquals("0.0", app.getDisplay(), "sin(0) should equal 0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    public void testSquareRoot() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("16");
                waitForFxEvents();
                app.handleScientificOperation("√");
                waitForFxEvents();
                assertEquals("4.0", app.getDisplay(), "√16 should equal 4");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(9)
    public void testClearFunction() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("123");
                waitForFxEvents();
                app.handleClear();
                waitForFxEvents();
                assertEquals("0", app.getDisplay(), "Clear should reset display to 0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(10)
    public void testPlusMinusToggle() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("5");
                waitForFxEvents();
                app.handlePlusMinus();
                waitForFxEvents();
                assertEquals("-5", app.getDisplay(), "Plus/minus should negate the number");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    public void testBackspace() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("1");
                waitForFxEvents();
                app.handleNumber("2");
                waitForFxEvents();
                app.handleNumber("3");
                waitForFxEvents();
                app.handleBackspace();
                waitForFxEvents();
                assertEquals("12", app.getDisplay(), "Backspace should remove the last digit");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(12)
    public void testMemoryOperations() {
        CountDownLatch testLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                app.handleNumber("5");
                waitForFxEvents();
                app.handleMemoryOperation("MS");
                waitForFxEvents();
                assertEquals(5.0, app.getMemory(), "Memory store should save the current number");

                app.handleClear();
                waitForFxEvents();
                app.handleMemoryOperation("MR");
                waitForFxEvents();
                assertEquals("5.0", app.getDisplay(), "Memory recall should show the stored number");

                app.handleMemoryOperation("MC");
                waitForFxEvents();
                assertEquals(0.0, app.getMemory(), "Memory clear should reset memory to 0");
            } finally {
                testLatch.countDown();
            }
        });
        try {
            testLatch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
