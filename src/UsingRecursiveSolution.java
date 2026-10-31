class UsingRecursiveSolution {
  public static void main(String[] args) {
    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, RecursiveSolution.fib(i));
     * }
     */

    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, RecursiveSolution.tri(i));
     * }
     */

    /*
     * for (int i = 0; i < 10; i++) {
     * System.out.printf("%d: %d\r\n", i, RecursiveSolution.fact(i));
     * }
     */

    // System.out.println(RecursiveSolution.reverseStr("hello"));

    // System.out.println(RecursiveSolution.hasChar("foo", 'o'));
    // System.out.println(RecursiveSolution.hasChar("foo", 'l'));

    /*
     * int gcdInput[][] = {
     * { 4, 2 },
     * { 2, 4 },
     * { 5, 3 },
     * { 12, 6 },
     * { 8, 12 },
     * };
     * for (int i = 0; i < gcdInput.length; i++) {
     * int x1 = gcdInput[i][0];
     * int x2 = gcdInput[i][1];
     * System.out.printf("x1: %d, x2: %d, gcd(x1, x2): %d\r\n",
     * x1,
     * x2,
     * RecursiveSolution.gcd(x1, x2));
     * }
     */

    System.out.println(RecursiveSolution.countUniquePaths(0, 0));
  }
}
