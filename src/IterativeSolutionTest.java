import java.util.Objects;

/**
 * Plain-Java unit tests for {@link IterativeSolution}. No JUnit required.
 *
 * <p>Run with:
 *
 * <pre>
 *   ./build
 *   ./run_test IterativeSolutionTest
 * </pre>
 *
 * <p>The tests encode the mathematical contract from assignment_details.pdf. Inputs are
 * limited to the non-negative integers required by the assignment. Expected values are
 * the mathematically correct results, not quirks of the current implementation.
 */
public class IterativeSolutionTest {

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
        "IterativeSolutionTest: " + passedTests + " passed, " + failedTests + " failed");
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
    test("tri(0) == 0", () -> assertEquals("tri(0)", 0L, IterativeSolution.tri(0)));
    test("tri(1) == 1", () -> assertEquals("tri(1)", 1L, IterativeSolution.tri(1)));
    test("tri(2) == 3", () -> assertEquals("tri(2)", 3L, IterativeSolution.tri(2)));
    test("tri(3) == 6", () -> assertEquals("tri(3)", 6L, IterativeSolution.tri(3)));
    test("tri(10) == 55", () -> assertEquals("tri(10)", 55L, IterativeSolution.tri(10)));
    test("tri(100) == 5050", () -> assertEquals("tri(100)", 5050L, IterativeSolution.tri(100)));

    test("tri matches n(n+1)/2 for every n in [0, 1000]", () -> {
      for (int n = 0; n <= 1000; n++) {
        long expected = (long) n * (n + 1L) / 2L;
        assertEquals("tri(" + n + ")", expected, IterativeSolution.tri(n));
      }
    });

    test("tri(n) - tri(n-1) == n for every n in [1, 1000]", () -> {
      for (int n = 1; n <= 1000; n++) {
        assertEquals("tri(" + n + ") - tri(" + (n - 1) + ")",
            n, IterativeSolution.tri(n) - IterativeSolution.tri(n - 1));
      }
    });

    test("tri(65535) is the largest triangular number that fits in an int", () -> {
      assertEquals("tri(65535)", 2147450880L, IterativeSolution.tri(65535));
    });
  }

  // ---------------------------------------------------------------------------
  // fact(int n)
  // ---------------------------------------------------------------------------

  private static void testFact() {
    test("fact(0) == 1", () -> assertEquals("fact(0)", 1L, IterativeSolution.fact(0)));
    test("fact(1) == 1", () -> assertEquals("fact(1)", 1L, IterativeSolution.fact(1)));
    test("fact(2) == 2", () -> assertEquals("fact(2)", 2L, IterativeSolution.fact(2)));
    test("fact(3) == 6", () -> assertEquals("fact(3)", 6L, IterativeSolution.fact(3)));
    test("fact(5) == 120", () -> assertEquals("fact(5)", 120L, IterativeSolution.fact(5)));
    test("fact(10) == 3628800", () -> assertEquals("fact(10)", 3628800L, IterativeSolution.fact(10)));

    test("fact(12) is the largest factorial that fits in an int", () -> {
      assertEquals("fact(12)", 479001600L, IterativeSolution.fact(12));
    });

    test("fact matches the iterative product for every n in [0, 12]", () -> {
      long product = 1L;
      for (int n = 0; n <= 12; n++) {
        assertEquals("fact(" + n + ")", product, IterativeSolution.fact(n));
        product *= (n + 1L);
      }
    });

    test("fact(n) == n * fact(n-1) for every n in [1, 12]", () -> {
      for (int n = 1; n <= 12; n++) {
        assertEquals("fact(" + n + ")", n * (long) IterativeSolution.fact(n - 1),
            IterativeSolution.fact(n));
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
        assertEquals("fib(" + n + ")", expected[n], IterativeSolution.fib(n));
      }
    });

    test("fib matches the Fibonacci recurrence for every n in [0, 46]", () -> {
      long previous = 0L;
      long current = 1L;
      for (int n = 0; n <= 46; n++) {
        assertEquals("fib(" + n + ")", previous, IterativeSolution.fib(n));
        long next = previous + current;
        previous = current;
        current = next;
      }
    });

    test("fib(46) is the largest Fibonacci number that fits in an int", () -> {
      assertEquals("fib(46)", 1836311903L, IterativeSolution.fib(46));
    });

    test("fib(n) == fib(n-1) + fib(n-2) for every n in [2, 46]", () -> {
      for (int n = 2; n <= 46; n++) {
        assertEquals("fib(" + n + ")", IterativeSolution.fib(n - 1) + IterativeSolution.fib(n - 2),
            IterativeSolution.fib(n));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // reverseStr(String)
  // ---------------------------------------------------------------------------

  private static void testReverseStr() {
    test("reverseStr(\"\") == \"\"", () -> assertEquals("reverseStr(\"\")", "",
        IterativeSolution.reverseStr("")));
    test("reverseStr(\"a\") == \"a\"", () -> assertEquals("reverseStr(\"a\")", "a",
        IterativeSolution.reverseStr("a")));
    test("reverseStr(\"ab\") == \"ba\"", () -> assertEquals("reverseStr(\"ab\")", "ba",
        IterativeSolution.reverseStr("ab")));
    test("reverseStr(\"abc\") == \"cba\"", () -> assertEquals("reverseStr(\"abc\")", "cba",
        IterativeSolution.reverseStr("abc")));
    test("reverseStr(\"hello\") == \"olleh\"", () -> assertEquals("reverseStr(\"hello\")", "olleh",
        IterativeSolution.reverseStr("hello")));
    test("reverseStr(\"racecar\") == \"racecar\"", () -> assertEquals("reverseStr(\"racecar\")",
        "racecar", IterativeSolution.reverseStr("racecar")));
    test("reverseStr(\"banana\") == \"ananab\"", () -> assertEquals("reverseStr(\"banana\")",
        "ananab", IterativeSolution.reverseStr("banana")));
    test("reverseStr(\"aaabbb\") == \"bbbaaa\"", () -> assertEquals("reverseStr(\"aaabbb\")",
        "bbbaaa", IterativeSolution.reverseStr("aaabbb")));
    test("reverseStr(\"a b c\") == \"c b a\"", () -> assertEquals("reverseStr(\"a b c\")", "c b a",
        IterativeSolution.reverseStr("a b c")));
    test("reverseStr(\"  padded  \") preserves spaces",
        () -> assertEquals("reverseStr(\"  padded  \")", "  deddap  ",
            IterativeSolution.reverseStr("  padded  ")));
    test("reverseStr(\"a\\tb\\nc\") handles tabs and newlines",
        () -> assertEquals("reverseStr(\"a\\tb\\nc\")", "c\nb\ta",
            IterativeSolution.reverseStr("a\tb\nc")));
    test("reverseStr(\"AaBbCc\") is case-sensitive",
        () -> assertEquals("reverseStr(\"AaBbCc\")", "cCbBaA",
            IterativeSolution.reverseStr("AaBbCc")));
    test("reverseStr(\"12345\") == \"54321\"", () -> assertEquals("reverseStr(\"12345\")", "54321",
        IterativeSolution.reverseStr("12345")));
    test("reverseStr(\"!@#$%^&*()\") == \")(*&^%$#@!\"",
        () -> assertEquals("reverseStr(\"!@#$%^&*()\")", ")(*&^%$#@!",
            IterativeSolution.reverseStr("!@#$%^&*()")));
    test("reverseStr(\"caf\\u00e9 \\u65e5\\u672c\\u8a9e\") handles non-ASCII BMP characters",
        () -> {
          String input = "caf\u00e9 \u65e5\u672c\u8a9e";
          assertEquals("reverseStr(\"" + input + "\")", new StringBuilder(input).reverse().toString(),
              IterativeSolution.reverseStr(input));
        });

    test("reverseStr reverses a long string (10,000 chars)", () -> {
      StringBuilder input = new StringBuilder(10000);
      for (int i = 0; i < 10000; i++) {
        input.append((char) ('a' + (i % 26)));
      }
      String actual = IterativeSolution.reverseStr(input.toString());
      assertEquals("reverseStr(long string)", new StringBuilder(input).reverse().toString(), actual);
    });

    test("reverseStr(reverseStr(s)) == s for representative strings", () -> {
      String[] inputs = {"", "a", "ab", "hello", "racecar", "a b c", "AaBbCc", "\u00e9\u65e5"};
      for (String input : inputs) {
        assertEquals("double reverse of \"" + input + "\"", input,
            IterativeSolution.reverseStr(IterativeSolution.reverseStr(input)));
      }
    });
  }

  // ---------------------------------------------------------------------------
  // hasChar(String, char)
  // ---------------------------------------------------------------------------

  private static void testHasChar() {
    test("hasChar(\"\", 'a') == false",
        () -> assertEquals("hasChar(\"\", 'a')", false, IterativeSolution.hasChar("", 'a')));
    test("hasChar(\"a\", 'a') == true",
        () -> assertEquals("hasChar(\"a\", 'a')", true, IterativeSolution.hasChar("a", 'a')));
    test("hasChar(\"a\", 'b') == false",
        () -> assertEquals("hasChar(\"a\", 'b')", false, IterativeSolution.hasChar("a", 'b')));
    test("hasChar finds the first character",
        () -> assertEquals("hasChar(\"abc\", 'a')", true, IterativeSolution.hasChar("abc", 'a')));
    test("hasChar finds a middle character",
        () -> assertEquals("hasChar(\"abc\", 'b')", true, IterativeSolution.hasChar("abc", 'b')));
    test("hasChar finds the last character",
        () -> assertEquals("hasChar(\"abc\", 'c')", true, IterativeSolution.hasChar("abc", 'c')));
    test("hasChar rejects an absent character",
        () -> assertEquals("hasChar(\"abc\", 'd')", false, IterativeSolution.hasChar("abc", 'd')));
    test("hasChar is case-sensitive",
        () -> assertEquals("hasChar(\"hello\", 'L')", false, IterativeSolution.hasChar("hello", 'L')));
    test("hasChar finds a repeated character",
        () -> assertEquals("hasChar(\"banana\", 'a')", true, IterativeSolution.hasChar("banana", 'a')));
    test("hasChar finds a space",
        () -> assertEquals("hasChar(\"hello world\", ' ')", true,
            IterativeSolution.hasChar("hello world", ' ')));
    test("hasChar rejects a space when there is none",
        () -> assertEquals("hasChar(\"helloworld\", ' ')", false,
            IterativeSolution.hasChar("helloworld", ' ')));
    test("hasChar finds a tab",
        () -> assertEquals("hasChar(\"a\\tb\", '\\t')", true,
            IterativeSolution.hasChar("a\tb", '\t')));
    test("hasChar finds a newline",
        () -> assertEquals("hasChar(\"a\\nb\", '\\n')", true,
            IterativeSolution.hasChar("a\nb", '\n')));
    test("hasChar finds a digit",
        () -> assertEquals("hasChar(\"abc123\", '2')", true,
            IterativeSolution.hasChar("abc123", '2')));
    test("hasChar rejects a digit when there is none",
        () -> assertEquals("hasChar(\"abcdef\", '2')", false,
            IterativeSolution.hasChar("abcdef", '2')));
    test("hasChar finds the NUL character",
        () -> assertEquals("hasChar(\"a\\0b\", '\\0')", true,
            IterativeSolution.hasChar("a\0b", '\0')));
    test("hasChar finds a non-ASCII BMP character",
        () -> assertEquals("hasChar(\"caf\\u00e9\", '\\u00e9')", true,
            IterativeSolution.hasChar("caf\u00e9", '\u00e9')));

    test("hasChar matches String.indexOf for every ASCII character", () -> {
      StringBuilder alphabet = new StringBuilder();
      for (int i = 0; i < 128; i++) {
        alphabet.append((char) i);
      }
      String text = alphabet.toString();
      for (int i = 0; i < 128; i++) {
        char c = (char) i;
        assertEquals("hasChar(ASCII text, " + i + ")", text.indexOf(c) >= 0,
            IterativeSolution.hasChar(text, c));
      }
    });

    test("hasChar handles a long string (10,000 chars), present at the end", () -> {
      StringBuilder text = new StringBuilder(10000);
      for (int i = 0; i < 9999; i++) {
        text.append('a');
      }
      text.append('z');
      assertEquals("hasChar(long string, 'z')", true, IterativeSolution.hasChar(text.toString(), 'z'));
    });

    test("hasChar handles a long string (10,000 chars), absent", () -> {
      String text = "a".repeat(10000);
      assertEquals("hasChar(long string, 'z')", false, IterativeSolution.hasChar(text, 'z'));
    });
  }

  // ---------------------------------------------------------------------------
  // gcd(int, int)
  // ---------------------------------------------------------------------------

  private static void testGcd() {
    test("gcd(4, 2) == 2", () -> assertEquals("gcd(4, 2)", 2L, IterativeSolution.gcd(4, 2)));
    test("gcd(2, 4) == 2", () -> assertEquals("gcd(2, 4)", 2L, IterativeSolution.gcd(2, 4)));
    test("gcd(5, 3) == 1", () -> assertEquals("gcd(5, 3)", 1L, IterativeSolution.gcd(5, 3)));
    test("gcd(12, 6) == 6", () -> assertEquals("gcd(12, 6)", 6L, IterativeSolution.gcd(12, 6)));
    test("gcd(8, 12) == 4", () -> assertEquals("gcd(8, 12)", 4L, IterativeSolution.gcd(8, 12)));
    test("gcd(48, 18) == 6", () -> assertEquals("gcd(48, 18)", 6L, IterativeSolution.gcd(48, 18)));
    test("gcd(18, 48) == 6", () -> assertEquals("gcd(18, 48)", 6L, IterativeSolution.gcd(18, 48)));
    test("gcd(17, 13) == 1", () -> assertEquals("gcd(17, 13)", 1L, IterativeSolution.gcd(17, 13)));
    test("gcd(17, 17) == 17", () -> assertEquals("gcd(17, 17)", 17L, IterativeSolution.gcd(17, 17)));
    test("gcd(100, 75) == 25", () -> assertEquals("gcd(100, 75)", 25L, IterativeSolution.gcd(100, 75)));
    test("gcd(1071, 462) == 21",
        () -> assertEquals("gcd(1071, 462)", 21L, IterativeSolution.gcd(1071, 462)));
    test("gcd(123456, 7890) == 6",
        () -> assertEquals("gcd(123456, 7890)", 6L, IterativeSolution.gcd(123456, 7890)));
    test("gcd(Integer.MAX_VALUE, Integer.MAX_VALUE - 1) == 1",
        () -> assertEquals("gcd(MAX, MAX-1)", 1L,
            IterativeSolution.gcd(Integer.MAX_VALUE, Integer.MAX_VALUE - 1)));
    test("gcd(1_000_000_000, 250_000_000) == 250_000_000",
        () -> assertEquals("gcd(1e9, 2.5e8)", 250000000L,
            IterativeSolution.gcd(1_000_000_000, 250_000_000)));

    // The assignment says the inputs are non-negative, so zero is part of the domain.
    // gcd(0, n) == gcd(n, 0) == n, and gcd(0, 0) == 0 by the usual programming convention.
    test("gcd(0, 1) == 1", () -> assertEquals("gcd(0, 1)", 1L, IterativeSolution.gcd(0, 1)));
    test("gcd(1, 0) == 1", () -> assertEquals("gcd(1, 0)", 1L, IterativeSolution.gcd(1, 0)));
    test("gcd(0, 5) == 5", () -> assertEquals("gcd(0, 5)", 5L, IterativeSolution.gcd(0, 5)));
    test("gcd(5, 0) == 5", () -> assertEquals("gcd(5, 0)", 5L, IterativeSolution.gcd(5, 0)));
    test("gcd(0, 0) == 0", () -> assertEquals("gcd(0, 0)", 0L, IterativeSolution.gcd(0, 0)));
    test("gcd(0, Integer.MAX_VALUE) == Integer.MAX_VALUE",
        () -> assertEquals("gcd(0, MAX)", (long) Integer.MAX_VALUE,
            IterativeSolution.gcd(0, Integer.MAX_VALUE)));

    test("gcd is symmetric for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          assertEquals("gcd(" + a + ", " + b + ") == gcd(" + b + ", " + a + ")",
              IterativeSolution.gcd(b, a), IterativeSolution.gcd(a, b));
        }
      }
    });

    test("gcd matches a reference Euclidean algorithm for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          assertEquals("gcd(" + a + ", " + b + ")", referenceGcd(a, b),
              IterativeSolution.gcd(a, b));
        }
      }
    });

    test("gcd(a, b) divides both a and b for every pair in [1, 100]", () -> {
      for (int a = 1; a <= 100; a++) {
        for (int b = 1; b <= 100; b++) {
          int g = IterativeSolution.gcd(a, b);
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
        () -> assertEquals("countUniquePaths(1, 1)", 1L, IterativeSolution.countUniquePaths(1, 1)));
    test("countUniquePaths(1, 2) == 1",
        () -> assertEquals("countUniquePaths(1, 2)", 1L, IterativeSolution.countUniquePaths(1, 2)));
    test("countUniquePaths(2, 1) == 1",
        () -> assertEquals("countUniquePaths(2, 1)", 1L, IterativeSolution.countUniquePaths(2, 1)));
    test("countUniquePaths(2, 2) == 2",
        () -> assertEquals("countUniquePaths(2, 2)", 2L, IterativeSolution.countUniquePaths(2, 2)));
    test("countUniquePaths(2, 3) == 3",
        () -> assertEquals("countUniquePaths(2, 3)", 3L, IterativeSolution.countUniquePaths(2, 3)));
    test("countUniquePaths(3, 2) == 3",
        () -> assertEquals("countUniquePaths(3, 2)", 3L, IterativeSolution.countUniquePaths(3, 2)));
    test("countUniquePaths(3, 3) == 6",
        () -> assertEquals("countUniquePaths(3, 3)", 6L, IterativeSolution.countUniquePaths(3, 3)));
    test("countUniquePaths(4, 4) == 20",
        () -> assertEquals("countUniquePaths(4, 4)", 20L, IterativeSolution.countUniquePaths(4, 4)));
    test("countUniquePaths(3, 7) == 28",
        () -> assertEquals("countUniquePaths(3, 7)", 28L, IterativeSolution.countUniquePaths(3, 7)));
    test("countUniquePaths(7, 3) == 28",
        () -> assertEquals("countUniquePaths(7, 3)", 28L, IterativeSolution.countUniquePaths(7, 3)));
    test("countUniquePaths(5, 5) == 70",
        () -> assertEquals("countUniquePaths(5, 5)", 70L, IterativeSolution.countUniquePaths(5, 5)));
    test("countUniquePaths(6, 6) == 252",
        () -> assertEquals("countUniquePaths(6, 6)", 252L, IterativeSolution.countUniquePaths(6, 6)));
    test("countUniquePaths(7, 7) == 924",
        () -> assertEquals("countUniquePaths(7, 7)", 924L, IterativeSolution.countUniquePaths(7, 7)));

    test("countUniquePaths matches C(n+m-2, n-1) for every grid in [1, 7] x [1, 7]", () -> {
      for (int n = 1; n <= 7; n++) {
        for (int m = 1; m <= 7; m++) {
          assertEquals("countUniquePaths(" + n + ", " + m + ")", expectedPaths(n, m),
              IterativeSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths is symmetric for every grid in [1, 10] x [1, 10]", () -> {
      for (int n = 1; n <= 10; n++) {
        for (int m = 1; m <= 10; m++) {
          assertEquals("countUniquePaths(" + n + ", " + m + ") == countUniquePaths(" + m + ", " + n + ")",
              IterativeSolution.countUniquePaths(m, n), IterativeSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths satisfies P(n,m) == P(n-1,m) + P(n,m-1) for [2, 7] x [2, 7]", () -> {
      for (int n = 2; n <= 7; n++) {
        for (int m = 2; m <= 7; m++) {
          assertEquals("countUniquePaths recurrence at (" + n + ", " + m + ")",
              IterativeSolution.countUniquePaths(n - 1, m) + IterativeSolution.countUniquePaths(n, m - 1),
              IterativeSolution.countUniquePaths(n, m));
        }
      }
    });

    test("countUniquePaths handles a single row and a single column", () -> {
      assertEquals("countUniquePaths(1, 13)", 1L, IterativeSolution.countUniquePaths(1, 13));
      assertEquals("countUniquePaths(13, 1)", 1L, IterativeSolution.countUniquePaths(13, 1));
    });

    test("countUniquePaths is correct on the largest grids for which factorial intermediates fit",
        () -> {
          assertEquals("countUniquePaths(2, 12)", 12L, IterativeSolution.countUniquePaths(2, 12));
          assertEquals("countUniquePaths(12, 2)", 12L, IterativeSolution.countUniquePaths(12, 2));
          assertEquals("countUniquePaths(3, 11)", 66L, IterativeSolution.countUniquePaths(3, 11));
          assertEquals("countUniquePaths(11, 3)", 66L, IterativeSolution.countUniquePaths(11, 3));
          assertEquals("countUniquePaths(4, 10)", 220L, IterativeSolution.countUniquePaths(4, 10));
          assertEquals("countUniquePaths(10, 4)", 220L, IterativeSolution.countUniquePaths(10, 4));
          assertEquals("countUniquePaths(5, 9)", 495L, IterativeSolution.countUniquePaths(5, 9));
          assertEquals("countUniquePaths(9, 5)", 495L, IterativeSolution.countUniquePaths(9, 5));
          assertEquals("countUniquePaths(6, 8)", 792L, IterativeSolution.countUniquePaths(6, 8));
          assertEquals("countUniquePaths(8, 6)", 792L, IterativeSolution.countUniquePaths(8, 6));
          assertEquals("countUniquePaths(7, 7)", 924L, IterativeSolution.countUniquePaths(7, 7));
        });

    // These answers fit in an int even though the factorial-based intermediate values
    // overflow an int. They are valid tests of the specified result type.
    test("countUniquePaths is correct when only intermediate values overflow", () -> {
      assertEquals("countUniquePaths(2, 13)", 13L, IterativeSolution.countUniquePaths(2, 13));
      assertEquals("countUniquePaths(13, 2)", 13L, IterativeSolution.countUniquePaths(13, 2));
      assertEquals("countUniquePaths(8, 8)", 3432L, IterativeSolution.countUniquePaths(8, 8));
      assertEquals("countUniquePaths(10, 10)", 48620L, IterativeSolution.countUniquePaths(10, 10));
      assertEquals("countUniquePaths(12, 12)", 705432L, IterativeSolution.countUniquePaths(12, 12));
      assertEquals("countUniquePaths(17, 17)", 601080390L,
          IterativeSolution.countUniquePaths(17, 17));
    });

    test("countUniquePaths handles long one-row and one-column grids", () -> {
      assertEquals("countUniquePaths(1, 35)", 1L, IterativeSolution.countUniquePaths(1, 35));
      assertEquals("countUniquePaths(35, 1)", 1L, IterativeSolution.countUniquePaths(35, 1));
      assertEquals("countUniquePaths(1, 100)", 1L, IterativeSolution.countUniquePaths(1, 100));
      assertEquals("countUniquePaths(100, 1)", 1L, IterativeSolution.countUniquePaths(100, 1));
    });
  }
}
