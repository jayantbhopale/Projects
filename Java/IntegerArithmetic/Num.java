/**+
 * Implementation of Data Structures
 * Author: Jayant Bhopale 
 * Graduate Student
 * Department of Computer Science
 * The University of Texas at Dallas
 * Date: Feb 23, 2020
 */

package IntegerArithmetic;
import java.util.*;
public class Num  implements Comparable<Num> {
    static long defaultBase = 1000;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    /**+
     * Constructures of Num class to create new Num as per
     * requirements and available parameters.
     * @param s
     */
    public Num(String s) {
        base = defaultBase;
        createNumString(s);
    }
    private Num(long[] arr, int size, boolean isNegative, long base) {
        this.arr = arr;
        this.len = size;
        this.isNegative = isNegative;
        this.base = base;
    }
    public Num(long x) {
        createNumLong(x, defaultBase);
    }
    private Num(long x, long base) {
        createNumLong(x, base);
    }

    private long getBaseLength() {
        return Long.toString(base()).length();
    }

    private void createNumString(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Not a valid String argument");
        }

        if ((s.charAt(0) == '-') || (s.charAt(0) == '+')) {
            isNegative = s.charAt(0) == '-' ? true : false;
            s = s.substring(1, s.length());
            if (s.isEmpty()) {
                throw new IllegalArgumentException("Needs a valid String argument");
            }
        }

        long arrLength = (long) Math.ceil(((s.length() + 1) * (Math.log(10) / Math.log(base))) + 1);

        arr = new long[(int) arrLength];

        try {
            stringToArray(s, 0);
        } catch (Exception E) {
            E.printStackTrace();
            throw new IllegalArgumentException("Error in handling input");
        }

        len = stripZeros(arr);

        if ((len == 1) && (arr[0] == 0)) {
            isNegative = false;
        }
    }
    private void stringToArray(String quotient, int idx) {
        long quotientLen = quotient.length();

        if (quotientLen < 19) {
            if (Long.parseLong(quotient) == 0) {
                len = idx;
                return;
            }
        }

        long baseLen = getBaseLength();
        long arrLen = 19 - baseLen;
        String temp;
        long remLen, subLen, temp1, temp2, rem = 0;
        long tempLen = (quotientLen / arrLen) + ((quotientLen % arrLen) == 0 ? 0 : 1);

        String quotientString = "";
        int i = 0;
        while(i<tempLen) {
            subLen = i * arrLen;
            remLen = Long.toString(rem).length();
            temp = Long.toString(rem).concat(quotient.substring((int) (subLen), (int) ((subLen + arrLen) < quotientLen ? (subLen + arrLen) : quotientLen)));
            temp1 = Long.valueOf(temp);

            // Handle zero
            if (i != 0) {
                if (i != (tempLen - 1)) {
                    if (rem == 0) {
                        int j = 0;
                        temp2 = Long.valueOf(quotient.substring((int) (subLen), (int) (subLen + j + 1)));
                        while ((j < (arrLen - 1)) && (temp2 < base())) {
                            quotientString = quotientString.concat("0");
                            j++;
                            temp2 = Long.valueOf(quotient.substring((int) (subLen), (int) (subLen + j + 1)));
                        }
                    } else if (baseLen != remLen) {
                        temp2 = Long.valueOf(Long.toString(rem).concat(quotient.substring((int) (subLen), (int) ((subLen + baseLen) - remLen))));
                        if (base() <= temp2) {
                            for (int z = 0; z < (baseLen - remLen - 1); z++) {
                                quotientString = quotientString.concat("0");
                            }
                        } else {
                            for (int z = 0; z < (baseLen - remLen); z++) {
                                quotientString = quotientString.concat("0");
                            }

                        }
                    }
                } else if (i == (tempLen - 1)) {
                    if (rem == 0) {

                        temp2 = Long.valueOf(quotient.substring((int) (subLen), (int) (subLen + 1)));
                        int j = (int) subLen + 1;
                        while ((j < quotient.length()) && (temp2 < base())) {
                            quotientString = quotientString.concat("0");
                            j++;
                            temp2 = Long.valueOf(quotient.substring((int) (subLen), (j)));
                        }
                    } else if (baseLen != remLen) {

                        Long endString = Long.parseLong(Long.toString(rem).concat(quotient.substring((int) subLen, (int) quotientLen)));
                        if (endString < base()) {
                            for (int z = 0; z < (quotient.substring((int) subLen, (int) quotientLen).length() - 1); z++) {
                                quotientString = quotientString.concat("0");
                            }

                        } else {
                            temp2 = Long.valueOf(Long.toString(rem).concat(quotient.substring((int) (subLen), (int) ((subLen + baseLen) - remLen))));
                            if (base() <= temp2) {
                                for (int z = 0; z < (baseLen - remLen - 1); z++) {
                                    quotientString = quotientString.concat("0");
                                }
                            } else {
                                for (int z = 0; z < (baseLen - remLen); z++) {
                                    quotientString = quotientString.concat("0");
                                }

                            }
                        }
                    }

                }
            }

            rem = temp1 % base();

            if (!quotientString.isEmpty() && (quotientString.length() < 19) && (Long.parseLong(quotientString) == 0)) {
                quotientString = "";
            }
            quotientString = quotientString.concat(Long.toString(temp1 / base()));
            i++;
        }

        arr[idx] = rem;
        stringToArray(quotientString, idx + 1);
    }

    private void createNumLong(long x, long base) {
        this.base = base;
        if (x == Long.MIN_VALUE) {
            createNumString(Long.toString(x)); // Long.MIN_VALUE overflows. So process it as a string.
        }
        else {
            if (x == 0) {
                len = 1;
                arr = new long[len];
                arr[len - 1] = x;
            }
            else {
                if(x<0)
                    this.isNegative = true;
                else
                    this.isNegative = false;
                x = Math.abs(x);
                long temp = x;
                int count = 0;
                while (temp > 0) {
                    temp = temp / base();
                    count++;
                }
                len = count;
                arr = new long[len];
                int i = 0;
                for(x = Math.abs(x); x > 0; x = x / base) {
                    arr[i++] = x % base;
                }
            }
        }
    }
    private static int unsignedCompare(Num a, Num b) {
        int len1 = a.len;
        int len2 = b.len;
        if (len1 > len2) return 1;
        else if (len1 < len2) return -1;
        else {
            for(int x = len1 - 1; x >= 0; x--) {
                if (a.arr[x] < b.arr[x]) return -1;
                else if (a.arr[x] > b.arr[x]) return 1;
            }
            return 0;
        }
    }

    private static boolean isInputZero(Num a) {
        return ((a.len == 1) && (a.arr[0] == 0));
    }
    private static boolean compareSign(Num a, Num b) {
        return ((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative));
    }
    private static int compareUnsigned(Num a, Num b) {
        if (a.len > b.len)
            return 1;
        else if (a.len < b.len)
            return -1;
        else {
            for(int index = a.len-1; index >=0; index--){
                if (a.arr[index] < b.arr[index])
                    return -1;
                else if(a.arr[index] > b.arr[index])
                    return 1;
            }
            return 0;
        }
    }
    // Method that returns new Num as negative of the Num passed to the method
    private static Num negateNumber(Num a) {
        return new Num(a.arr, a.len, !a.isNegative, a.base());
    }

    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @param b is a object of class Num which can take any arbitary large number
     * @return This method returns the addition of two Nums a and b
     */
    public static Num add(Num a, Num b) {
        if (isInputZero(a)) {
            return b;
        }
        if (isInputZero(b)) {
            return a;
        }
        boolean isNegative;
        if (compareSign(a, b)) {
            isNegative = a.isNegative && b.isNegative ? true : false;
            long[] num1 = a.arr;
            long[] num2 = b.arr;
            int len1 = a.len;       // Length of num1
            int len2 = b.len;   // Length of num2
            int sumLen = (len1 > len2 ? len1 : len2) + 1;
            long[] sum = new long[sumLen];
            long carry = 0;
            int i = 0, j = 0, k = 0;
            long base = a.base();
            while ((i < len1) && (j < len2)) {
                sum[k] = (num1[i] + num2[j] + carry) % base;
                carry = (num1[i] + num2[j] + carry) / base;
                i++;
                j++;
                k++;
            }
            if (i < len1) {
                while (i < len1) {
                    sum[k] = (num1[i] + carry) % base;
                    carry = (num1[i] + carry) / base;
                    i++;
                    k++;
                }
            } else if (j < len2) {
                while (j < len2) {
                    sum[k] = (num2[j] + carry) % base;
                    carry = (num2[j] + carry) / base;
                    j++;
                    k++;
                }
            }
            if (carry != 0) sum[k] = carry;
            else k--;

            return new Num(sum, k + 1, isNegative, base);
        } else
            return subtract(a, negateNumber(b));
    }
    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @param b is a object of class Num which can take any arbitary large number
     * @return This method returns the difference of two Nums a and b
     */
    public static Num subtract(Num a, Num b) {
        if (isInputZero(a)) {
            return negateNumber(b);
        }
        if (isInputZero(b)) {
            return a;
        }

        if (a.compareTo(b) == 0) {
            return new Num(0, a.base());
        }

        if (compareSign(a, b)) {
            boolean isNegative = a.compareTo(b) == -1 ? true : false;
            int len1 = a.len, len2 = b.len;
            int size, num1len, num2len;
            long[] num1;
            long[] num2;
            long base = a.base();
            if (unsignedCompare(a, b) == -1) {
                size = len2;
                num1 = b.arr;
                num2 = a.arr;
                num1len = len2;
                num2len = len1;
            } else {
                size = len1;
                num1 = a.arr;
                num2 = b.arr;
                num1len = len1;
                num2len = len2;
            }
            long[] leftover = new long[size];
            int i = 0, j = 0, k = 0;
            long carry = 0;
            while ((i < num1len) && (j < num2len)) {
                if ((num1[i] - carry) >= num2[j]) {
                    leftover[k++] = num1[i++] - num2[j++] - carry;
                    carry = 0;
                } else {
                    leftover[k++] = (num1[i++] + base) - num2[j++] - carry;
                    carry = 1;
                }
            }
            while (i < num1len) {
                if ((num1[i] - carry) >= 0) {
                    leftover[k++] = num1[i++] - carry;
                    carry = 0;
                } else {
                    leftover[k++] = (num1[i++] + base) - carry;
                    carry = 1;
                }
            }
            return new Num(leftover, stripZeros(leftover), isNegative, base);
        } else
            return add(a, negateNumber(b));
    }
    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @param b is a object of class Num which can take any arbitary large number
     * @return This method returns the product of two Nums a and b
     */
    public static Num product(Num a, Num b) {
        if (isInputZero(a) || isInputZero(b)) {
            return new Num(0, a.base());
        }
        boolean isNegative = compareSign(a,b)? false: true;
        int smallNumLen;
        int largeNumLen;
        int compareUnsigned = compareUnsigned(a,b);
        long[] num1;
        long[] num2;
        long base = a.base();
        if (compareUnsigned == -1) {
            num1 = b.arr;
            largeNumLen = b.len;
            num2 = a.arr;
            smallNumLen = a.len;

        } else {
            num1 = a.arr;
            largeNumLen = a.len;
            num2 = b.arr;
            smallNumLen = b.len;
        }
        long prodTemp;
        long carry = 0;
        int p = 0;
        long[] product = new long[largeNumLen + smallNumLen];
        for (int i = 0; i < largeNumLen; i++) {
            carry = 0;
            for (int j = 0; j < smallNumLen; j++) {
                p = i + j;
                prodTemp = product[p] + (num1[i] * num2[j]) + carry;
                product[p] = prodTemp % base;
                carry = prodTemp / base;
            }
            if (carry != 0) product[p + 1] = product[p + 1] + carry;

        }
        if (carry != 0)
            product[++p] = carry;
        if ((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative))
            isNegative = false;
        else isNegative = true;

        return new Num(product, p + 1, isNegative, base);
    }
    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @return This method returns the nth power of Num a
     */
    public static Num power(Num a, long n) {
        return power(a, new Num(n, a.base()));
    }
    private static Num power(Num a, Num n) {
        if (isInputZero(n))
            return new Num(1, a.base());
        else {
            Num p = power(product(a, a), n.by2());
            return mod(n, new Num(2, a.base())).compareTo(new Num(1)) == 0 ? product(p, a) : p;
        }
    }
    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @param b is a object of class Num which can take any arbitary large number
     * @return This method returns the division of two Nums a and b
     */
    public static Num divide(Num a, Num b) {
      Num zero = new Num(0, a.base());
        Num one = new Num(1, a.base());
        Num minus_one = new Num(-1, a.base());

        Num sign = null;
        if (compareSign(a, b)) sign = one;
        else
            sign = minus_one;

        Num dividend = a.isNegative ? negateNumber(a) : a;
        Num divisor = b.isNegative ? negateNumber(b) : b;

        if (divisor.compareTo(zero) == 0) {
            throw new IllegalArgumentException("Divide by zero is not allowed");
        }
        if (divisor.compareTo(one) == 0) {
            return product(dividend, sign);
        }
        if (divisor.compareTo(dividend) == 0) {
            return sign;
        }
        if (divisor.compareTo(dividend) > 0) {
            return zero;
        }
        Num lower = zero;
        Num upper = dividend;
        while (true) {
            Num quotient = add(lower, (subtract(upper, lower)).by2());
            Num divisorQuotient = product(divisor, quotient);
            Num remainder = subtract(divisorQuotient, dividend);
            if (remainder.isNegative) {
                remainder.isNegative = false;
                if ((remainder).compareTo(divisor) < 0)
                    return product(quotient, sign);
            } else {
                if ((remainder).compareTo(zero) == 0)
                    return product(quotient, sign);
            }
            if (divisorQuotient.compareTo(dividend) == -1)
                lower = quotient;
            else
                upper = quotient;
        }
    }
    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @param b is a object of class Num which can take any arbitary large number
     * @return This method returns the modulus of two Nums a and b (a mod b)
     */
    public static Num mod(Num a, Num b) {
        Num zero = new Num(0, a.base());
        Num one = new Num(1, a.base());
        if (b.compareTo(zero) == 0) {
            throw new IllegalArgumentException("Divide by zero is not allowed");
        }
        if (b.compareTo(one) == 0) {
            return zero;
        }
        if (b.compareTo(a) == 0) {
            return zero;
        }
        if (b.compareTo(a) > 0) {
            return a;
        }
        Num lower = zero;
        Num upper = a;
        while (true) {
            Num quotient = add(lower, ((subtract(upper, lower)).by2()));
            Num divisorQuotient = product(b, quotient);
            Num remainder = subtract(divisorQuotient, a);
            if (remainder.isNegative) {
                remainder.isNegative = false;
                if (remainder.compareTo(b) < 0)
                    return remainder;
            } else {
                if ((remainder).compareTo(zero) == 0)
                    return remainder;
            }
            if (divisorQuotient.compareTo(a) == -1) {
                lower = quotient;
            } else {
                upper = quotient;
            }
        }
    }

    /**+
     *
     * @param a is a object of class Num which can take any arbitary large number
     * @return square root of Num a
     */
    public static Num squareRoot(Num a) {
        if (a.isNegative) throw new IllegalArgumentException("Imaginary Number");
        if (a.compareTo(new Num(0)) == 0) return a;

        Num low = new Num(0, a.base());
        Num high = a;
        Num sqrt = new Num(1, a.base());
        while (low.compareTo(high) <= 0) {
            Num mid = add(low, ((subtract(high, low)).by2()));
            Num operation = product(mid, mid);
            int comparison = operation.compareTo(a);
            if (comparison == -1) {
                low = add(mid, new Num(1, a.base()));
                sqrt = mid;
            } else if (comparison == 0) {
                return mid;
            } else {
                high = subtract(mid, new Num(1, a.base()));
            }
        }
        return sqrt;
    }
    public int compareTo(Num other) {
        if ((this.isNegative && other.isNegative) || (!this.isNegative && !other.isNegative)) {
            if (this.len > other.len) {
                return (this.isNegative && other.isNegative) ? -1 : 1;
            } else if (this.len == other.len) {
                int isNumEqual = 0;
                for(int i = this.len-1; i>=0; i--){
                    if (this.arr[i] < other.arr[i]) {
                        isNumEqual = (this.isNegative && other.isNegative) ? 1 : -1;
                        break;
                    } else if (this.arr[i] > other.arr[i]) {
                        isNumEqual = (this.isNegative && other.isNegative) ? -1 : 1;
                        break;
                    }
                }
                return isNumEqual;
            } else {
                return (this.isNegative && other.isNegative) ? 1 : -1;
            }
        } else if (this.isNegative) return -1;
        else return 1;

    }

    /**+
     * Method to print list
     */
    public void printList() {
        System.out.print(base() + ": ");
        System.out.print(isNegative ? "- " : "");
        for (int i = 0; i < len; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
    public String toString() {
        Num n = convertBase(1000);

        StringBuilder sb = new StringBuilder();
        if (n.isNegative && !isInputZero(n)) {
            sb.append('-');
        }
        for (int i = n.len - 1; i >= 0; i--) {
            String str = Long.toString(n.arr[i]);
            int zeroes = 9 - str.length();
            if(i != n.len - 1 && zeroes > 0) {
                switch(zeroes) {
                    case 1 : sb.append("0");
                        break;
                    case 2 : sb.append("00");
                        break;
                    case 3 : sb.append("000");
                        break;
                    case 4 : sb.append("0000");
                        break;
                    case 5 : sb.append("00000");
                        break;
                    case 6 : sb.append("000000");
                        break;
                    case 7 : sb.append("0000000");
                        break;
                    case 8 : sb.append("00000000");
                        break;
                    default: break;
                }

            }
            sb.append(str);
        }
        return sb.toString();
    }
    public long base() {
        return base;
    }
    public Num convertBase(int newBase) {
        if (newBase != base) {
            Num newNumber = new Num(this.arr[this.len - 1], newBase);
            Num baseNumber = new Num(base(), newBase);

            for (int i = this.len - 1; i > 0; i--) {
                Num prodNum = product(newNumber, baseNumber);
                newNumber = add(prodNum, new Num(this.arr[i - 1], newBase));
            }
            newNumber.isNegative = this.isNegative;
            return newNumber;
        }
        return this;
    }
    public Num by2() {
        long carry = 0;
        long[] newArray = new long[this.len];
        for (int i = this.len - 1; i >= 0; i--) {
            long remainder = this.arr[i] + (carry * base());
            newArray[i] = remainder >> 1;
            carry = remainder - (newArray[i] * 2);
        }

        return new Num(newArray, stripZeros(newArray), this.isNegative, base());
    }

    private static int stripZeros(long[] a) {
        int count = 0;
        for(int i = a.length - 1; i>0; i--){
            if(a[i] == 0)
                count++;
        }
        return a.length - count;
    }
    private static boolean isOperator(String str) {
        return (str.equals("*") || str.equals("+") || str.equals("-") || str.equals("/") || str.equals("%") || str.equals("^"));
    }
    private static Num doOperation(Num operand1, Num operand2, String operator) throws Exception {
        Num result = null;
        switch (operator) {
            case "+":
                result = add(operand1, operand2);
                break;
            case "-":
                result = subtract(operand1, operand2);
                break;
            case "*":
                result = product(operand1, operand2);
                break;
            case "/":
                result = divide(operand1, operand2);
                break;
            case "%":
                result = mod(operand1, operand2);
                break;
            case "^":
                result = power(operand1, operand2);
                break;
            default:
                throw new Exception("Unsupported Operation");
        }
        return result;
    }
    public static Num evaluatePostfix(String[] expr) throws Exception {
        Deque<String> stack = new ArrayDeque<String>();
        for (String exp : expr) {
            if (!isOperator(exp)) {
                stack.push(exp);
            } else {
                String operandString2 = stack.pop();
                String operandString1 = stack.pop();
                Num operand1 = new Num(operandString1);
                Num operand2 = new Num(operandString2);
                Num result = doOperation(operand1, operand2, exp);
                stack.push(result.toString());
            }
        }
        Num answer = new Num(stack.pop());
        return answer;
    }
    private static int getOperatorPriority(String operator) throws Exception {
        int priority = 0;
        switch (operator) {
            case "+":
                priority = 1;
                break;
            case "-":
                priority = 1;
                break;
            case "*":
                priority = 2;
                break;
            case "/":
                priority = 2;
                break;
            case "%":
                priority = 2;
                break;
            case "^":
                priority = 3;
                break;
            default:
                throw new Exception("Operator is not available");
        }
        return priority;

    }

    /**+
     *
     * @param qt Queue of Strings
     * @return Num after evaluation of expression
     * @throws Exception when the expression is invalid
     */
    private static Num evalF(Queue<String> qt) throws Exception{
        Num val;
        if( !qt.isEmpty() && qt.peek().equals("(")){
            String oper= qt.remove();
            val= evalE(qt);
            oper = qt.remove();
        }
        else{
            StringBuilder num = new StringBuilder(100);
            while (isNumeric(qt.peek())){
                num.append(qt.remove());
            }
            val = new Num(num.toString());
        }

        return val;
    }

    /**+
     *
     * @param qt Queue of Strings that is an input of expression
     * @return Num after evaluation of expression in priority for multiplication and division
     * @throws Exception for invalid operations
     */
    private static Num evalT(Queue<String> qt) throws Exception{
        Num val1 = evalF(qt);
        while(!qt.isEmpty() && ((qt.peek().equals("*")) ||(qt.peek().equals("/")))){
            String oper= qt.remove();
            Num val2 = evalF(qt);
            if(oper.equals("*")) val1 = Num.product(val1,val2);
            else val1 = Num.divide(val1,val2);
        }
        return val1;
    }
    /**+
     *
     * @param qt Queue of Strings that is an input of expression
     * @return Num after evaluation of expression in priority for addition and subtraction
     * @throws Exception for invalid operations
     */
    private static Num evalE(Queue<String> qt) throws Exception{
        Num val1 = evalT(qt);
        while(!qt.isEmpty() && ((qt.peek().equals("+")) ||(qt.peek().equals("-")))){
            String oper= qt.remove();
            Num val2 = evalT(qt);
            if(oper.equals("+")) val1 = Num.add(val1,val2);
            else val1 = Num.subtract(val1,val2);
        }
        return val1;
    }
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfexp) {
            return false;
        }
        return true;
    }

    /**+
     *
     * @param expr Input expression as a String
     * @return Result of expression after evaluation
     * @throws Exception if invalid expression occures
     */
    public static Num evaluateExp(String expr) throws Exception{
        Queue<String> qt = new LinkedList<String>();
        expr = expr.replaceAll("\\s+","");
        String[] exprArray = expr.split("");
        for (String exprElement : exprArray){
            qt.offer(exprElement);
        }
        Num result = new Num(0);
        try{
            result =  evalE(qt);
            System.out.println(result);
        }
        catch (Exception e){
            System.out.println("Invalid Expression Occured. ");
        }
        return result;
    }
    public static void main(String[] args) {
        Num x = new Num(999);
        Num y = new Num("8");
        Num z = Num.add(x, y);
        System.out.println(z);
        Num a = Num.power(x, 8);
        System.out.println(a);
        if(z != null) z.printList();
    }
}


