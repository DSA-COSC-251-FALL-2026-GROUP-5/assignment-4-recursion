class RecursiveSolution {

  public static int tri(int n) {
    // 1.a. this takes O(n) time because we essentially make n calls to tri, and in
    // every call, it takes constant time to evaulate
    if (n <= 0) {
      return 0;
    }
    return n + tri(n - 1);
  }

  public static int fact(int n) {
    // 1.b. this takes O(n) time because we essentially make n calls to fact, and in
    // every call, it takes constant time to evaulate (assuming that the product
    // operation is atomic enough)
    if (n <= 0) {
      return 1;
    }
    return n * fact(n - 1);
  }

  public static int fib(int n) {
    // 1.c. In the naive recursive implementation of fib, it will make around
    // 2**n
    // different calls, with each call being constant time, thus, it will take
    // O(2**n) time (without memoization).
    if (n <= 0) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    return fib(n - 1) + fib(n - 2);
  }

  public static String reverseStr(String str) {
    // 1.d. this takes O(n) time because we need to perform n calls, and each call
    // takes O(1) time to evaulate (assuming that string slicing is constant time)
    int strLength = str.length();
    if (strLength <= 0) {
      return "";
    }
    return Character.toString(str.charAt(strLength - 1)) + reverseStr(str.substring(0, strLength - 1));
  }

  public static boolean hasChar(String str, char c) {
    // 1.e. this takes O(n) time in the worst case, where n is the size of the
    // string
    // because in the case that the character isn't in the string, we must make n =
    // str.length() calls, with each call taking O(1) time to evaulate
    int strLength = str.length();
    if (strLength == 0) {
      // if the length is 0, then clearly we don't have it
      return false;
    }

    if (str.charAt(0) == c) {
      return true;
    }

    return hasChar(str.substring(1, strLength), c);
  }

  private static int orderedGcd(int x1, int x2) {
    // we maintain that x1 > x2

    // we use the Euclidean algorithm, which works because of the following
    // identity:
    // gcd(a, b) = gcd(a % b, b) = gcd(b, a % b)

    // the terminating condition is when a % b is 0, meaning that a is divisible by
    // b
    if (x1 % x2 == 0) {
      return x2;
    }
    return orderedGcd(x2, x1 % x2);
  }

  public static int gcd(int x1, int x2) {
    // 1.f. this is a bit tricky to evaulate
    // first, we begin with an observation that:
    //
    // a % b < a/2
    // for b > 1
    //
    // Because we're performing:
    //
    // a_1 = b_0
    // b_1 = (a_0 % b_0) < a_0/2
    // a_2 = b_1
    // b_2 = (a_1 % b_1) < a_1 / 2
    // a_3 = b_2
    // b_3 = (a_2 % b_2) < a_2 / 2 <= a_0 / 4
    // a_4 = b_3
    // b_4 = (a_3 % b_3) < a_3 / 2 <= a_1 / 4
    // a_5 = b_4
    // b_5 = (a_4 % b_4) < a_4 / 2 <= a_0 / 8
    // etc...
    //
    // spotting the pattern, we stop at somewhere around:
    //
    // b_n = 1 < a_{n - 1} / 2 <= a_1 / (2^(n/2))
    // b_n = 1 <= b_0 / (2^{n / 2})
    //
    // Thus, solving for n, we get:
    // 2^{n / 2} <= b_0
    // n/ 2 <= log_2 (b_0)
    // n <= 2*log_2(b_0)
    //
    // Thus, it should be around O(log_2(b_0)), or O(log_2(x_2))

    if (x1 > x2) {
      return orderedGcd(x1, x2);
    }
    if (x1 < x2) {
      return orderedGcd(x1, x2);
    }
    return x1;
  }

  public static int countUniquePaths(int n, int m) {
    // 1.g. 
    // if we write an algorithm that traverses it explicitly, we branch it into 2,
    // that is, we either explore the right side, and then count the number of
    // possible paths from there to the end, and then explore downwards and do the
    // same, and then sum the number of total paths from there.

    // notice then we perform calls as such:
    // * -> * -> * -> *
    // |    |    |
    // v    v    v
    // * -> * -> * -> *
    // |    |    |
    // v    v    v
    // *    *    *    *

    // without memoization, we see that we revisit old nodes multiple times, so this will
    // take exponential time

    if (n <= 1) {
      return 1;
    }

    if (m <= 1) {
      return 1;
    }

    int countIfMoveRight = countUniquePaths(n, m - 1);
    int countIfMoveDown = countUniquePaths(n - 1, m);
    return countIfMoveRight + countIfMoveDown;
  }

}
