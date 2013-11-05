package example.ClientTest;

import div.Sheep;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Eivind
 * Date: 05.11.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class ArraylistSpeedTest {
    public static void main(String args[]) {


        long startTime = System.nanoTime();
        method();
        long endTime = System.nanoTime();

        long duration = endTime - startTime;

        double timeinmilliseconds = (double)duration / (double)1000000;
        System.out.println((long)timeinmilliseconds);
    }

    public static void method() {

        ArrayList<Sheep> al = new ArrayList<Sheep>();
        int size = 100000;

        for (int i = 0; i < size; i++) {
            Sheep s = null;
            try {
                s = new Sheep(i, "testsheep", 2008, 20, 'm', "test0@test.test", "test2@test.test");
                s.newLocation(10 + "," + 9, "00/00/0000");
                al.add(s);
                //Sheep sheep = al.get(i);
                //System.out.println(sheep.getId());
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

}

