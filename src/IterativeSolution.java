class IterativeSolution {

  public static int tri(int n) {
    int sum = 0;
    for (int i = 1; i <= n; i++) {
      sum += i;
    }
    return sum;
  }

  public static int fib(int n) {

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
    return str;
  }

}
