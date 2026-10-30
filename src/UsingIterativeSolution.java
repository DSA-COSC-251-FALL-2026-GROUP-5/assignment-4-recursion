class UsingIterativeSolution {
  public static void main(String[] args) {
    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, IterativeSolution.fib(i));
     * }
     */
    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, IterativeSolution.tri(i));
     * }
     */

    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, IterativeSolution.fact(i));
     * }
     */

    // System.out.println(IterativeSolution.reverseStr("hello"));

    // System.out.println(IterativeSolution.hasChar("foo", 'o'));
    // System.out.println(IterativeSolution.hasChar("foo", 'l'));

    int gcdInput[][] = {
        { 4, 2 },
        { 2, 4 },
        { 5, 3 },
        { 12, 6 },
        { 8, 12 },
    };
    for (int i = 0; i < gcdInput.length; i++) {
      int x1 = gcdInput[i][0];
      int x2 = gcdInput[i][1];
      System.out.printf("x1: %d, x2: %d, gcd(x1, x2): %d\r\n",
          x1,
          x2,
          IterativeSolution.gcd(x1, x2));
    }
  }
}
