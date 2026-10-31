import java.util.Objects;

/**
 * Plain-Java unit tests for {@link RecursiveSolution}. No JUnit required.
 *
 * <p>Run with:
 *
 * <pre>
 *   ./build
 *   ./run_test RecursiveSolutionTest
 * </pre>
 *
 * <p>The tests encode the mathematical contract from assignment_details.pdf. Inputs are
 * limited to the non-negative integers required by the assignment. Expected values are
 * the mathematically correct results, not quirks of the current implementation.
 *
 * <p>Large depths are deliberately avoided for the recursive implementation because
 * every recursive call consumes stack space; the values chosen are large enough to
 * exercise the logic while keeping the tests deterministic and fast.
 */
public class RecursiveSolutionTest {

  private static int passedTests = 0;
  private static int failedTests = 0;

  public static void main(String[] args) {
    testTri();
    testFact();
    testFib();
    testReverseStr();
    testHasChar();
    testGcd();
    testCountUniquePaths();

    System.out.println();
    System.out.println(
        "RecursiveSolutionTest: " + passedTests + " passed, " + failedTests + " failed");
    if (failedTests > 0) {
      System.exit(1);
    }
  }

  // ---------------------------------------------------------------------------
  // Test harness
  // ---------------------------------------------------------------------------

  private static void test(String name, Runnable body) {
    try {
      body.run();
      passedTests++;
      System.out.println("PASS  " + name);
    } catch (Throwable t) {
      failedTests++;
      String detail = (t instanceof AssertionError && t.getMessage() != null)
          ? t.getMessage()
          : t.getClass().getSimpleName() + (t.getMessage() == null ? "" : ": " + t.getMessage());
      System.out.println("FAIL  " + name + " -> " + detail);
      if (!(t instanceof AssertionError)) {
        t.printStackTrace(System.out);
      }
    }
  }

  private static void assertEquals(String label, long expected, long actual) {
    if (expected != actual) {
      throw new AssertionError(label + ": expected <" + expected + "> but was <" + actual + ">");
    }
  }

  private static void assertEquals(String label, boolean expected, boolean actual) {
    if (expected != actual) {
      throw new AssertionError(label + ": expected <" + expected + "> but was <" + actual + ">");
    }
  }

  private static void assertEquals(String label, String expected, String actual) {
    if (!Objects.equals(expected, actual)) {
      throw new AssertionError(label + ": expected <" + expected + "> but was <" + actual + ">");
    }
  }

  private static int referenceGcd(int a, int b) {
    while (b != 0) {
      int remainder = a % b;
      a = b;
      b = remainder;
    }
    return a;
  }

  /** Binomial coefficient C(n, k), computed without overflowing for the values used here. */
  private static long binomial(int n, int k) {
    if (k < 0 || k > n) {
      return 0L;
    }
    k = Math.min(k, n - k);
    long result = 1L;
    for (int i = 1; i <= k; i++) {
      result = result * (n - k + i) / i;
    }
    return result;
  }

  private static long expectedPaths(int n, int m) {
    if (n < 1 || m < 1) {
      throw new IllegalArgumentException("grid dimensions must be at least 1");
    }
    return binomial(n + m - 2, Math.min(n - 1, m - 1));
  }

  // ---------------------------------------------------------------------------
  // tri(int n)
  // ---------------------------------------------------------------------------

  private static void testTri() {
    test("tri(0) == 0", () -> assertEquals("tri(0)", 0L, RecursiveSolution.tri(0)));
    test("tri(1) == 1", () -> assertEquals("tri(1)", 1L, RecursiveSolution.tri(1)));
    test("tri(2) == 3", () -> assertEquals("tri(2)", 3L, RecursiveSolution.tri(2)));
    test("tri(3) == 6", () -> assertEquals("tri(3)", 6L, RecursiveSolution.tri(3)));
    test("tri(10) == 55", () -> assertEquals("tri(10)", 55L, RecursiveSolution.tri(10)));
    test("tri(100) == 5050", () -> assertEquals("tri(100)", 5050L, RecursiveSolution.tri(100)));

    test("tri matches n(n+1)/2 for every n in [0, 5000]", () -> {
      for (int n = 0; n <= 5000; n++) {
        long expected = (long) n * (n + 1L) / 2L;
        assertEquals("tri(" + n + ")", expected, RecursiveSolution.tri(n));
      }
    });

    test("tri(n) - tri(n-1) == n for every n in [1, 1000]", () -> {
      for (int n = 1; n <= 1000; n++) {
        assertEquals("tri(" + n + ") - tri(" + (n - 1) + ")",
            n, RecursiveSolution.tri(n) - RecursiveSolution.tri(n - 1));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // fact(int n)
  // ---------------------------------------------------------------------------

  private static void testFact() {
    test("fact(0) == 1", () -> assertEquals("fact(0)", 1L, RecursiveSolution.fact(0)));
    test("fact(1) == 1", () -> assertEquals("fact(1)", 1L, RecursiveSolution.fact(1)));
    test("fact(2) == 2", () -> assertEquals("fact(2)", 2L, RecursiveSolution.fact(2)));
    test("fact(3) == 6", () -> assertEquals("fact(3)", 6L, RecursiveSolution.fact(3)));
    test("fact(5) == 120", () -> assertEquals("fact(5)", 120L, RecursiveSolution.fact(5)));
    test("fact(10) == 3628800", () -> assertEquals("fact(10)", 3628800L, RecursiveSolution.fact(10)));

    test("fact(12) is the largest factorial that fits in an int", () -> {
      assertEquals("fact(12)", 479001600L, RecursiveSolution.fact(12));
    });

    test("fact matches the iterative product for every n in [0, 12]", () -> {
      long product = 1L;
      for (int n = 0; n <= 12; n++) {
        assertEquals("fact(" + n + ")", product, RecursiveSolution.fact(n));
        product *= (n + 1L);
      }
    });

    test("fact(n) == n * fact(n-1) for every n in [1, 12]", () -> {
      for (int n = 1; n <= 12; n++) {
        assertEquals("fact(" + n + ")", n * (long) RecursiveSolution.fact(n - 1),
            RecursiveSolution.fact(n));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // fib(int n)
  // ---------------------------------------------------------------------------

  private static void testFib() {
    test("fib has the expected first 15 values", () -> {
      int[] expected = {
          0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377
      };
      for (int n = 0; n < expected.length; n++) {
        assertEquals("fib(" + n + ")", expected[n], RecursiveSolution.fib(n));
      }
    });

    test("fib matches the Fibonacci recurrence for every n in [0, 35]", () -> {
      long previous = 0L;
      long current = 1L;
      for (int n = 0; n <= 35; n++) {
        assertEquals("fib(" + n + ")", previous, RecursiveSolution.fib(n));
        long next = previous + current;
        previous = current;
        current = next;
      }
    });

    test("fib(35) == 9227465", () -> assertEquals("fib(35)", 9227465L, RecursiveSolution.fib(35)));

    test("fib(n) == fib(n-1) + fib(n-2) for every n in [2, 35]", () -> {
      for (int n = 2; n <= 35; n++) {
        assertEquals("fib(" + n + ")", RecursiveSolution.fib(n - 1) + RecursiveSolution.fib(n - 2),
            RecursiveSolution.fib(n));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // reverseStr(String)
  // ---------------------------------------------------------------------------

  private static void testReverseStr() {
    test("reverseStr(\"\") == \"\"", () -> assertEquals("reverseStr(\"\")", "",
        RecursiveSolution.reverseStr("")));
    test("reverseStr(\"a\") == \"a\"", () -> assertEquals("reverseStr(\"a\")", "a",
        RecursiveSolution.reverseStr("a")));
    test("reverseStr(\"ab\") == \"ba\"", () -> assertEquals("reverseStr(\"ab\")", "ba",
        RecursiveSolution.reverseStr("ab")));
    test("reverseStr(\"abc\") == \"cba\"", () -> assertEquals("reverseStr(\"abc\")", "cba",
        RecursiveSolution.reverseStr("abc")));
    test("reverseStr(\"hello\") == \"olleh\"", () -> assertEquals("reverseStr(\"hello\")", "olleh",
        RecursiveSolution.reverseStr("hello")));
    test("reverseStr(\"racecar\") == \"racecar\"", () -> assertEquals("reverseStr(\"racecar\")",
        "racecar", RecursiveSolution.reverseStr("racecar")));
    test("reverseStr(\"banana\") == \"ananab\"", () -> assertEquals("reverseStr(\"banana\")",
        "ananab", RecursiveSolution.reverseStr("banana")));
    test("reverseStr(\"aaabbb\") == \"bbbaaa\"", () -> assertEquals("reverseStr(\"aaabbb\")",
        "bbbaaa", RecursiveSolution.reverseStr("aaabbb")));
    test("reverseStr(\"a b c\") == \"c b a\"", () -> assertEquals("reverseStr(\"a b c\")", "c b a",
        RecursiveSolution.reverseStr("a b c")));
    test("reverseStr(\"  padded  \") preserves spaces",
        () -> assertEquals("reverseStr(\"  padded  \")", "  deddap  ",
            RecursiveSolution.reverseStr("  padded  ")));
    test("reverseStr(\"a\\tb\\nc\") handles tabs and newlines",
        () -> assertEquals("reverseStr(\"a\\tb\\nc\")", "c\nb\ta",
            RecursiveSolution.reverseStr("a\tb\nc")));
    test("reverseStr(\"AaBbCc\") is case-sensitive",
        () -> assertEquals("reverseStr(\"AaBbCc\")", "cCbBaA",
            RecursiveSolution.reverseStr("AaBbCc")));
    test("reverseStr(\"12345\") == \"54321\"", () -> assertEquals("reverseStr(\"12345\")", "54321",
        RecursiveSolution.reverseStr("12345")));
    test("reverseStr(\"!@#$%^&*()\") == \")(*&^%$#@!\"",
        () -> assertEquals("reverseStr(\"!@#$%^&*()\")", ")(*&^%$#@!",
            RecursiveSolution.reverseStr("!@#$%^&*()")));
    test("reverseStr(\"caf\\u00e9 \\u65e5\\u672c\\u8a9e\") handles non-ASCII BMP characters",
        () -> {
          String input = "caf\u00e9 \u65e5\u672c\u8a9e";
          assertEquals("reverseStr(\"" + input + "\")", new StringBuilder(input).reverse().toString(),
              RecursiveSolution.reverseStr(input));
        });

    test("reverseStr reverses a long string (2,000 chars)", () -> {
      StringBuilder input = new StringBuilder(2000);
      for (int i = 0; i < 2000; i++) {
        input.append((char) ('a' + (i % 26)));
      }
      String actual = RecursiveSolution.reverseStr(input.toString());
      assertEquals("reverseStr(long string)", new StringBuilder(input).reverse().toString(), actual);
    });

    test("reverseStr(reverseStr(s)) == s for representative strings", () -> {
      String[] inputs = {"", "a", "ab", "hello", "racecar", "a b c", "AaBbCc", "\u00e9\u65e5"};
      for (String input : inputs) {
        assertEquals("double reverse of \"" + input + "\"", input,
            RecursiveSolution.reverseStr(RecursiveSolution.reverseStr(input)));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // hasChar(String, char)
  // ---------------------------------------------------------------------------

  private static void testHasChar() {
    test("hasChar(\"\", 'a') == false",
        () -> assertEquals("hasChar(\"\", 'a')", false, RecursiveSolution.hasChar("", 'a')));
    test("hasChar(\"a\", 'a') == true",
        () -> assertEquals("hasChar(\"a\", 'a')", true, RecursiveSolution.hasChar("a", 'a')));
    test("hasChar(\"a\", 'b') == false",
        () -> assertEquals("hasChar(\"a\", 'b')", false, RecursiveSolution.hasChar("a", 'b')));
    test("hasChar finds the first character",
        () -> assertEquals("hasChar(\"abc\", 'a')", true, RecursiveSolution.hasChar("abc", 'a')));
    test("hasChar finds a middle character",
        () -> assertEquals("hasChar(\"abc\", 'b')", true, RecursiveSolution.hasChar("abc", 'b')));
    test("hasChar finds the last character",
        () -> assertEquals("hasChar(\"abc\", 'c')", true, RecursiveSolution.hasChar("abc", 'c')));
    test("hasChar rejects an absent character",
        () -> assertEquals("hasChar(\"abc\", 'd')", false, RecursiveSolution.hasChar("abc", 'd')));
    test("hasChar is case-sensitive",
        () -> assertEquals("hasChar(\"hello\", 'L')", false, RecursiveSolution.hasChar("hello", 'L')));
    test("hasChar finds a repeated character",
        () -> assertEquals("hasChar(\"banana\", 'a')", true, RecursiveSolution.hasChar("banana", 'a')));
    test("hasChar finds a space",
        () -> assertEquals("hasChar(\"hello world\", ' ')", true,
            RecursiveSolution.hasChar("hello world", ' ')));
    test("hasChar rejects a space when there is none",
        () -> assertEquals("hasChar(\"helloworld\", ' ')", false,
            RecursiveSolution.hasChar("helloworld", ' ')));
    test("hasChar finds a tab",
        () -> assertEquals("hasChar(\"a\\tb\", '\\t')", true,
            RecursiveSolution.hasChar("a\tb", '\t')));
    test("hasChar finds a newline",
        () -> assertEquals("hasChar(\"a\\nb\", '\\n')", true,
            RecursiveSolution.hasChar("a\nb", '\n')));
    test("hasChar finds a digit",
        () -> assertEquals("hasChar(\"abc123\", '2')", true,
            RecursiveSolution.hasChar("abc123", '2')));
    test("hasChar rejects a digit when there is none",
        () -> assertEquals("hasChar(\"abcdef\", '2')", false,
            RecursiveSolution.hasChar("abcdef", '2')));
    test("hasChar finds the NUL character",
        () -> assertEquals("hasChar(\"a\\0b\", '\\0')", true,
            RecursiveSolution.hasChar("a\0b", '\0')));
    test("hasChar finds a non-ASCII BMP character",
        () -> assertEquals("hasChar(\"caf\\u00e9\", '\\u00e9')", true,
            RecursiveSolution.hasChar("caf\u00e9", '\u00e9')));

    test("hasChar matches String.indexOf for every ASCII character", () -> {
      StringBuilder alphabet = new StringBuilder();
      for (int i = 0; i < 128; i++) {
        alphabet.append((char) i);
      }
      String text = alphabet.toString();
      for (int i = 0; i < 128; i++) {
        char c = (char) i;
        assertEquals("hasChar(ASCII text, " + i + ")", text.indexOf(c) >= 0,
            RecursiveSolution.hasChar(text, c));
      }
    });

    test("hasChar handles a long string (2,000 chars), present at the end", () -> {
      StringBuilder text = new StringBuilder(2000);
      for (int i = 0; i < 1999; i++) {
        text.append('a');
      }
      text.append('z');
      assertEquals("hasChar(long string, 'z')", true, RecursiveSolution.hasChar(text.toString(), 'z'));
    });

    test("hasChar handles a long string (2,000 chars), absent", () -> {
      String text = "a".repeat(2000);
      assertEquals("hasChar(long string, 'z')", false, RecursiveSolution.hasChar(text, 'z'));
    });
  }

  // ---------------------------------------------------------------------------
  // gcd(int, int)
  // ---------------------------------------------------------------------------

  private static void testGcd() {
    test("gcd(4, 2) == 2", () -> assertEquals("gcd(4, 2)", 2L, RecursiveSolution.gcd(4, 2)));
    test("gcd(2, 4) == 2", () -> assertEquals("gcd(2, 4)", 2L, RecursiveSolution.gcd(2, 4)));
    test("gcd(5, 3) == 1", () -> assertEquals("gcd(5, 3)", 1L, RecursiveSolution.gcd(5, 3)));
    test("gcd(12, 6) == 6", () -> assertEquals("gcd(12, 6)", 6L, RecursiveSolution.gcd(12, 6)));
    test("gcd(8, 12) == 4", () -> assertEquals("gcd(8, 12)", 4L, RecursiveSolution.gcd(8, 12)));
    test("gcd(48, 18) == 6", () -> assertEquals("gcd(48, 18)", 6L, RecursiveSolution.gcd(48, 18)));
    test("gcd(18, 48) == 6", () -> assertEquals("gcd(18, 48)", 6L, RecursiveSolution.gcd(18, 48)));
    test("gcd(17, 13) == 1", () -> assertEquals("gcd(17, 13)", 1L, RecursiveSolution.gcd(17, 13)));
    test("gcd(17, 17) == 17", () -> assertEquals("gcd(17, 17)", 17L, RecursiveSolution.gcd(17, 17)));
    test("gcd(100, 75) == 25", () -> assertEquals("gcd(100, 75)", 25L, RecursiveSolution.gcd(100, 75)));
    test("gcd(1071, 462) == 21",
        () -> assertEquals("gcd(1071, 462)", 21L, RecursiveSolution.gcd(1071, 462)));
    test("gcd(123456, 7890) == 6",
        () -> assertEquals("gcd(123456, 7890)", 6L, RecursiveSolution.gcd(123456, 7890)));
    test("gcd(Integer.MAX_VALUE, Integer.MAX_VALUE - 1) == 1",
        () -> assertEquals("gcd(MAX, MAX-1)", 1L,
            RecursiveSolution.gcd(Integer.MAX_VALUE, Integer.MAX_VALUE - 1)));
    test("gcd(1_000_000_000, 250_000_000) == 250_000_000",
        () -> assertEquals("gcd(1e9, 2.5e8)", 250000000L,
            RecursiveSolution.gcd(1_000_000_000, 250_000_000)));

    // The assignment says the inputs are non-negative, so zero is part of the domain.
    // gcd(0, n) == gcd(n, 0) == n, and gcd(0, 0) == 0 by the usual programming convention.
    test("gcd(0, 1) == 1", () -> assertEquals("gcd(0, 1)", 1L, RecursiveSolution.gcd(0, 1)));
    test("gcd(1, 0) == 1", () -> assertEquals("gcd(1, 0)", 1L, RecursiveSolution.gcd(1, 0)));
    test("gcd(0, 5) == 5", () -> assertEquals("gcd(0, 5)", 5L, RecursiveSolution.gcd(0, 5)));
    test("gcd(5, 0) == 5", () -> assertEquals("gcd(5, 0)", 5L, RecursiveSolution.gcd(5, 0)));
    test("gcd(0, 0) == 0", () -> assertEquals("gcd(0, 0)", 0L, RecursiveSolution.gcd(0, 0)));
    test("gcd(0, Integer.MAX_VALUE) == Integer.MAX_VALUE",
        () -> assertEquals("gcd(0, MAX)", (long) Integer.MAX_VALUE,
            RecursiveSolution.gcd(0, Integer.MAX_VALUE)));

    test("gcd is symmetric for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          assertEquals("gcd(" + a + ", " + b + ") == gcd(" + b + ", " + a + ")",
              RecursiveSolution.gcd(b, a), RecursiveSolution.gcd(a, b));
        }
      }
    });

    test("gcd matches a reference Euclidean algorithm for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          assertEquals("gcd(" + a + ", " + b + ")", referenceGcd(a, b),
              RecursiveSolution.gcd(a, b));
        }
      }
    });

    test("gcd(a, b) divides both a and b for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          int g = RecursiveSolution.gcd(a, b);
          if (g <= 0 || a % g != 0 || b % g != 0) {
            throw new AssertionError(
                "gcd(" + a + ", " + b + ") = " + g + " does not divide both inputs");
          }
        }
      }
    });
  }

  // ---------------------------------------------------------------------------
  // countUniquePaths(int, int)
  // ---------------------------------------------------------------------------

  private static void testCountUniquePaths() {
    test("countUniquePaths(1, 1) == 1",
        () -> assertEquals("countUniquePaths(1, 1)", 1L, RecursiveSolution.countUniquePaths(1, 1)));
    test("countUniquePaths(1, 2) == 1",
        () -> assertEquals("countUniquePaths(1, 2)", 1L, RecursiveSolution.countUniquePaths(1, 2)));
    test("countUniquePaths(2, 1) == 1",
        () -> assertEquals("countUniquePaths(2, 1)", 1L, RecursiveSolution.countUniquePaths(2, 1)));
    test("countUniquePaths(2, 2) == 2",
        () -> assertEquals("countUniquePaths(2, 2)", 2L, RecursiveSolution.countUniquePaths(2, 2)));
    test("countUniquePaths(2, 3) == 3",
        () -> assertEquals("countUniquePaths(2, 3)", 3L, RecursiveSolution.countUniquePaths(2, 3)));
    test("countUniquePaths(3, 2) == 3",
        () -> assertEquals("countUniquePaths(3, 2)", 3L, RecursiveSolution.countUniquePaths(3, 2)));
    test("countUniquePaths(3, 3) == 6",
        () -> assertEquals("countUniquePaths(3, 3)", 6L, RecursiveSolution.countUniquePaths(3, 3)));
    test("countUniquePaths(4, 4) == 20",
        () -> assertEquals("countUniquePaths(4, 4)", 20L, RecursiveSolution.countUniquePaths(4, 4)));
    test("countUniquePaths(3, 7) == 28",
        () -> assertEquals("countUniquePaths(3, 7)", 28L, RecursiveSolution.countUniquePaths(3, 7)));
    test("countUniquePaths(7, 3) == 28",
        () -> assertEquals("countUniquePaths(7, 3)", 28L, RecursiveSolution.countUniquePaths(7, 3)));
    test("countUniquePaths(5, 5) == 70",
        () -> assertEquals("countUniquePaths(5, 5)", 70L, RecursiveSolution.countUniquePaths(5, 5)));
    test("countUniquePaths(6, 6) == 252",
        () -> assertEquals("countUniquePaths(6, 6)", 252L, RecursiveSolution.countUniquePaths(6, 6)));
    test("countUniquePaths(7, 7) == 924",
        () -> assertEquals("countUniquePaths(7, 7)", 924L, RecursiveSolution.countUniquePaths(7, 7)));

    test("countUniquePaths matches C(n+m-2, n-1) for every grid in [1, 7] x [1, 7]", () -> {
      for (int n = 1; n <= 7; n++) {
        for (int m = 1; m <= 7; m++) {
          assertEquals("countUniquePaths(" + n + ", " + m + ")", expectedPaths(n, m),
              RecursiveSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths is symmetric for every grid in [1, 10] x [1, 10]", () -> {
      for (int n = 1; n <= 10; n++) {
        for (int m = 1; m <= 10; m++) {
          assertEquals("countUniquePaths(" + n + ", " + m + ") == countUniquePaths(" + m + ", " + n + ")",
              RecursiveSolution.countUniquePaths(m, n), RecursiveSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths satisfies P(n,m) == P(n-1,m) + P(n,m-1) for [2, 8] x [2, 8]", () -> {
      for (int n = 2; n <= 8; n++) {
        for (int m = 2; m <= 8; m++) {
          assertEquals("countUniquePaths recurrence at (" + n + ", " + m + ")",
              RecursiveSolution.countUniquePaths(n - 1, m) + RecursiveSolution.countUniquePaths(n, m - 1),
              RecursiveSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths handles a single row and a single column", () -> {
      assertEquals("countUniquePaths(1, 13)", 1L, RecursiveSolution.countUniquePaths(1, 13));
      assertEquals("countUniquePaths(13, 1)", 1L, RecursiveSolution.countUniquePaths(13, 1));
      assertEquals("countUniquePaths(1, 100)", 1L, RecursiveSolution.countUniquePaths(1, 100));
      assertEquals("countUniquePaths(100, 1)", 1L, RecursiveSolution.countUniquePaths(100, 1));
    });

    test("countUniquePaths is correct on the largest grids for which factorial intermediates fit",
        () -> {
          assertEquals("countUniquePaths(2, 12)", 12L, RecursiveSolution.countUniquePaths(2, 12));
          assertEquals("countUniquePaths(12, 2)", 12L, RecursiveSolution.countUniquePaths(12, 2));
          assertEquals("countUniquePaths(3, 11)", 66L, RecursiveSolution.countUniquePaths(3, 11));
          assertEquals("countUniquePaths(11, 3)", 66L, RecursiveSolution.countUniquePaths(11, 3));
          assertEquals("countUniquePaths(4, 10)", 220L, RecursiveSolution.countUniquePaths(4, 10));
          assertEquals("countUniquePaths(10, 4)", 220L, RecursiveSolution.countUniquePaths(10, 4));
          assertEquals("countUniquePaths(5, 9)", 495L, RecursiveSolution.countUniquePaths(5, 9));
          assertEquals("countUniquePaths(9, 5)", 495L, RecursiveSolution.countUniquePaths(9, 5));
          assertEquals("countUniquePaths(6, 8)", 792L, RecursiveSolution.countUniquePaths(6, 8));
          assertEquals("countUniquePaths(8, 6)", 792L, RecursiveSolution.countUniquePaths(8, 6));
          assertEquals("countUniquePaths(7, 7)", 924L, RecursiveSolution.countUniquePaths(7, 7));
        });

    test("countUniquePaths is correct on larger grids whose answers fit in an int", () -> {
      assertEquals("countUniquePaths(2, 13)", 13L, RecursiveSolution.countUniquePaths(2, 13));
      assertEquals("countUniquePaths(13, 2)", 13L, RecursiveSolution.countUniquePaths(13, 2));
      assertEquals("countUniquePaths(8, 8)", 3432L, RecursiveSolution.countUniquePaths(8, 8));
      assertEquals("countUniquePaths(10, 10)", 48620L, RecursiveSolution.countUniquePaths(10, 10));
      assertEquals("countUniquePaths(12, 12)", 705432L, RecursiveSolution.countUniquePaths(12, 12));
      assertEquals("countUniquePaths(13, 13)", 2704156L, RecursiveSolution.countUniquePaths(13, 13));
      assertEquals("countUniquePaths(15, 15)", 40116600L, RecursiveSolution.countUniquePaths(15, 15));
    });
  }
}
