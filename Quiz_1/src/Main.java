import java.io.*;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {

    public static boolean isPrime(int n)
    {
        if (n <= 1)
            return false;
        for (int i = 2; i < n; i++)
            if (n % i == 0)
                return false;
        return true;
    }
    public static boolean isEmirp(int n)
    {
        int a = n;
        if (isPrime(n) == false)
            return false;
        if(n<13){
            return false;
        }
        int reverse = 0;
        while (n != 0)
        {
            int digit = n % 10;
            reverse = reverse * 10 + digit;
            n = n/10;
        }
        if (a == reverse){
            return false;
        }
        return isPrime(reverse);
    }
    static boolean isArmstrong(int n)
    {
        int temp, digits=0, last=0, sum=0;
        if (n == 0){
            return false;
        }
        temp=n;
        while(temp>0)
        {
            temp = temp/10;
            digits++;
        }
        temp = n;
        while(temp>0)
        {
            last = temp % 10;
            sum += (Math.pow(last, digits));
            temp = temp/10;
        }
        if(n==sum)
            return true;
        else return false;
    }

    public static int getSum(int n) {
        int sum = 0;
        for (int i=1; i<=Math.sqrt(n); i++) {
            if (n%i==0) {
                if (n/i == i)
                    sum = sum + i;
                else {
                    sum = sum + i;
                    sum = sum + (n / i);
                }
            }
        }
        sum = sum - n;
        return sum;
    }

    public static boolean isAbundant(int n){
            if(getSum(n)>n){
                return true;
            }else{
                return false;
            }
    }

    public static void main(String[] args) throws IOException {
//         reading command line arguments
        String inputFile = args[0];
//        System.out.println(inputFile);

        FileInput input = new FileInput();
        String[] lines = input.readFile(inputFile,true,true);
//        System.out.println(Arrays.toString(lines));

        FileOutput out = new FileOutput();
//        out.writeToFile("output2.txt","yusuf",true,true);
//        out.writeToFile("output2.txt","demir",true,true);

        int counter = 0;
        for (int i = 0; i < lines.length; i++){
            if(lines[i].startsWith("Armstrong")){
                String sf1 = String.format("Armstrong numbers up to %s:",lines[i+1]);
                out.writeToFile("output.txt",sf1,false,true);
                int n;
                n = Integer.parseInt(lines[i + 1]);
                for (int j = 0; j <= n; j++) {
                    if (isArmstrong(j)) {
                        out.writeToFile("output.txt", j + " ", true, false);
                    }
                }
                out.writeToFile("output.txt","",true,true);
                out.writeToFile("output.txt","",true,true);
                i++;
            } else if (lines[i].startsWith("Emirp")) {
                String sf1 = String.format("Emirp numbers up to %s:",lines[i+1]);
                out.writeToFile("output.txt",sf1,true,true);
                int n;
                n = Integer.parseInt(lines[i + 1]);
                for (int j = 0; j <= n; j++) {
                    if (isEmirp(j)) {
                        out.writeToFile("output.txt", j + " ", true, false);
                    }
                }
                out.writeToFile("output.txt","",true,true);
                out.writeToFile("output.txt","",true,true);
                i++;

            } else if (lines[i].startsWith("Abundant")) {
                String sf1 = String.format("Abundant numbers up to %s:",lines[i+1]);
                out.writeToFile("output.txt",sf1,true,true);
                int n;
                n = Integer.parseInt(lines[i + 1]);
                for (int j = 0; j <= n; j++) {
                    if (isAbundant(j)) {
                        out.writeToFile("output.txt", j + " ", true, false);
                    }
                }
                out.writeToFile("output.txt","",true,true);
                out.writeToFile("output.txt","",true,true);
                i++;
            } else if (lines[i].startsWith("Ascending")){
                out.writeToFile("output.txt","Ascending order sorting:",true,true);
                int n;
                n = Integer.parseInt(lines[i + 1]);
                ArrayList list = new ArrayList();
                while(n != -1){
                    i++;
                    list.add(n);
                    Collections.sort(list);
                    for(Object j : list){
                        out.writeToFile("output.txt", j + " ",true,false);
                    }
                    out.writeToFile("output.txt", "",true,true);
                    n = Integer.parseInt(lines[i + 1]);

                }
                out.writeToFile("output.txt", "",true,true);
                i++;

            } else if (lines[i].startsWith("Descending")){
                out.writeToFile("output.txt","Descending order sorting:",true,true);
                int n;
                n = Integer.parseInt(lines[i + 1]);
                ArrayList list = new ArrayList();
                while(n != -1){
                    i++;
                    list.add(n);
                    Collections.sort(list, Collections.reverseOrder());
                    for(Object j : list){
                        out.writeToFile("output.txt", j + " ",true,false);
                    }
                    out.writeToFile("output.txt", "",true,true);
                    n = Integer.parseInt(lines[i + 1]);

                }
                out.writeToFile("output.txt", "",true,true);
                i++;

            } else if (lines[i].startsWith("Exit")) {
                out.writeToFile("output.txt","Finished...",true,false);
                }
            }



    }
}