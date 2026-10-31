class IterativeSolution {

  public static int tri(int n) {
    // 1.a. this takes O(n) time because we are looping from 1 to n (including n)
    // and performing a sum over them
    int sum = 0;
    for (int i = 1; i <= n; i++) {
      sum += i;
    }
    return sum;
  }

  public static int fact(int n) {
    // 1.b. this takes O(n) time because we are looping from 1 to n (including n)
    // and performing a product over them (although, performing products take a
    // longer time to perform than sums)
    int product = 1;
    for (int i = 1; i <= n; i++) {
      product *= i;
    }
    return product;
  }

  public static int fib(int n) {
    // 1.c. this takes O(n) time, because we simply perform a loop from 2 to n
    // (including n), and then perform a sum, which takes constant time
    int prevPrev = 0;
    if (n == 0) {
      return prevPrev;
    }

    int prev = 1;
    if (n == 1) {
      return prev;
    }

    for (int i = 2; i <= n; i++) {
      int tmp = prev + prevPrev;
      prevPrev = prev;
      prev = tmp;
    }
    return prev;
  }

  public static String reverseStr(String str) {
    // 1.d. this takes O(n) time because we are simply looping the string in
    // reversed order and then appending it (where appending takes O(1) time).
    String reversedStr = "";
    int strLength = str.length();
    for (int i = 0; i < strLength; i++) {
      reversedStr += String.valueOf(str.charAt(strLength - 1 - i));
    }
    return reversedStr;
  }

  public static boolean hasChar(String str, char c) {
    // 1.e. this takes O(n) time in the worst case, where n is the size of the
    // string
    // because we would have to loop through the whole string of size n to confirm
    // that c isn't in the string
    int strLength = str.length();
    for (int i = 0; i < strLength; i++) {
      if (str.charAt(i) == c) {
        return true;
      }
    }
    return false;
  }

  public static int gcd(int x1, int x2) {
    // 1.f. this should take somewhere around O(log_2(b)) with b defined below
    // because it's similar to the recursive process
    int a = Math.max(x1, x2);
    int b = Math.min(x1, x2);
    if (b == 0) {
      return a;
    }
    while (a % b != 0) {
      int tmp = b;
      b = a % b;
      a = tmp;
    }
    return b;
  }

  public static int countUniquePaths(int n, int m) {
    // 1.g. This is a counting problem, we must make (n - 1)*(m - 1) moves to get to
    // our destination, as such, we can encode
    // our possible moves as a string like:
    // ddd...dddrrrr...rrrr
    // where we have n - 1 amount of d moves, and m - 1 amount of r moves
    // thus, the different number of unique paths will be the possible permutations
    // ((n + m - 2)!)/((n - 1)! * (m - 1)!)
    // this will take O(n + m) time because our factorial function takes O(n) time

    int a = Math.max(n - 1, m - 1);
    int b = Math.min(n - 1, m - 1);
    long prodAToAPlusB = 1;
    for (int i = a + 1; i <= (a + b); i++) {
      prodAToAPlusB *= i;
    }

    long factB = 1;
    for (int i = 1; i <= b; i++) {
      factB *= i;
    }

    return (int) (prodAToAPlusB / factB);
  }
}
