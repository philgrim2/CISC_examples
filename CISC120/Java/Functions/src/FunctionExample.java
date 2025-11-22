public class FunctionExample {

    public static void happy()
    {
        System.out.println("Happy Birthday to you!");
    }

    public static void happy(String name)
    {
        System.out.println("Happy Birthday, dear " + name + ",");
    }

    public static String coinFlip()
    {
        if (Math.random() < 0.5) {
            return "Heads";
        }
        else {
            return "Tails";
        }
    }

    public static int addOne(int n)
    {
        n = n + 1;
        return n;
    }

    public static void printArray(int[] arr)
    {
        System.out.print("[");
        for (int i =0; i < arr.length; i++)
        {
            System.out.print(arr[i] + " ");
        }
        System.out.println("]");
    }

    public static void printArray(String[] arr)
    {
        System.out.print("[");
        for (int i =0; i < arr.length; i++)
        {
            System.out.print(arr[i] + " ");
        }
        System.out.println("]");
    }

    public static void doubleArray(int[] arr)
    {
        for (int i =0; i < arr.length; i++)
        {
            arr[i] = arr[i] * 2;
        }
    }

    public static int[] doubleArr(int[] arr)
    {
        int[] newArr = new int[arr.length];
        for (int i =0; i < arr.length; i++)
        {
            newArr[i] = arr[i] * 2;
        }
        return newArr;
    }

    // swaps array elements i and j
    public static void exchange(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // returns a random integer between 0 and n-1
    public static int uniformInt(int n) {
        return (int) (Math.random() * n);
    }

    // take as input an array of strings and rearrange them in random order
    public static void shuffle(String[] a) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            int r = i + uniformInt(n-i);   // between i and n-1
            exchange(a, i, r);
        }
    }

    public static void main(String[] args)
    {
        happy();
        happy();
        happy("Alice");
        happy();


        int x = 5;
        addOne(x);
        System.out.println(x);

        int[] nums = {1, 2, 3, 4, 5};
        printArray(nums);
        int[] dubs = doubleArr(nums);
        printArray(nums);
        printArray(dubs);

        String[] strs = {"One", "Two", "Three", "Four", "Five"};
        printArray(strs);
        shuffle(strs);
        printArray(strs);

    }

}
