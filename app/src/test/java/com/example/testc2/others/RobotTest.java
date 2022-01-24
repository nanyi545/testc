package com.example.testc2.others;

import org.junit.Test;


/**
 * A width x height grid is on an XY-plane with the bottom-left cell at (0, 0) and the top-right cell at (width - 1, height - 1). The grid is aligned with the four cardinal directions ("North", "East", "South", and "West"). A robot is initially at cell (0, 0) facing direction "East".
 *
 * The robot can be instructed to move for a specific number of steps. For each step, it does the following.
 *
 *     Attempts to move forward one cell in the direction it is facing.
 *     If the cell the robot is moving to is out of bounds, the robot instead turns 90 degrees counterclockwise and retries the step.
 *
 * After the robot finishes moving the number of steps required, it stops and awaits the next instruction.
 *
 *  https://leetcode-cn.com/problems/walking-robot-simulation-ii
 *
 *
 */
public class RobotTest {

    @Test
    public void test1(){
        // robot 1 is slow ... (n time complexity)
        Robot1 r = new Robot1(20,14);
        r.step(32);
        r.step(18);
        r.step(18);
    }


    @Test

    /**
     * ["Robot","step","step","getPos","getDir","step","step","step","getPos","getDir"]
     * [[6,3],   [2],   [2]    ,[],    [],      [2],   [1],    [4],   [],     []]
     */
    public void test2(){
        Robot r = new Robot(6,3);
        int[] p ;

        r.step(2);
//        p=r.getPos();
//        System.out.println("p:"+p[0]+" - "+p[1]);

        r.step(2);
//        p=r.getPos();
//        System.out.println("p:"+p[0]+" - "+p[1]);

        r.step(2);
//        p=r.getPos();
//        System.out.println("p:"+p[0]+" - "+p[1]);

        r.step(1);
//        p=r.getPos();
//        System.out.println("p:"+p[0]+" - "+p[1]);

        r.step(4);
        r.step(1);
        r.step(1);
        p=r.getPos();
        System.out.println("p:"+p[0]+" - "+p[1]);

    }


    /**
     * ["Robot","step","step","step","getPos","getDir","step","getPos","getDir","step","step","step","getPos","getDir","step","step","step","getPos","getDir","step","step","step","step","step","getPos","getDir","step","step","step","step","step","getPos","getDir","step","step","getPos","getDir","step","step","getPos","getDir","step","step","getPos","getDir","step","step"]
     * [[20,14],[32],  [18],   [18],  [],      [],     [18],   [],     [],      [45],  [37],   [39],  [],      [],      [8],   [11],  [18],  [],[],[3],[39],[7],[31],[42],[],[],[35],[11],[36],[29],[10],[],[],[49],[31],[],[],[31],[47],[],[],[29],[1],[],[],[5],[44]]
     */

    @Test
    public void test3(){
        Robot r = new Robot(20,14);
        int[] p ;

        r.step(32);
        r.step(18);
        r.step(18);
        r.step(18);
        r.step(45);
        r.step(37);
        r.step(39);
        r.step(8);
        r.step(11);
        r.step(18);

        p=r.getPos();
        System.out.println("p:"+p[0]+" - "+p[1]);


    }



    static class Robot {

        int w,h,steps,l;

        public Robot(int width, int height) {
            w = width;
            h = height;
            l = w*2+h*2-4;
            steps = 0;
        }


        public void step(int num) {
            steps+=num;
        }

        public int[] getPos() {
            int [] ret = new int[2];
            System.out.println("steps:"+steps);
            if(steps==0){
                return ret;
            }
            int a = steps%l;
            if(a==0){
                return ret;
            }
            if(a>=1 && a<=(w-1)){
                ret[0] = a;
                ret[1] = 0;
                return ret;
            }
            if(a>=w && a<=(w+h-2)){
                ret[0] = w-1;
                ret[1] = a+1-w;
                return ret;
            }
            if(a>=(w+h-1) && a<=(2*w+h-3)){
                ret[0] = 2*w+h-3-a;
                ret[1] = h-1;
                return ret;
            }
            if(a>=(2*w+h-2) && a<=(2*w+2*h-3)){
                ret[0] = 0;
                int t = a -(2*w+h-2);
                ret[1] = h-2-t;
                return ret;
            }
            return ret;
        }

        public String getDir() {
            if(steps==0){
                return "East";
            }
            int a = steps%l;
            if(a==0){
                return "South";
            }
            if(a>=1 && a<=(w-1)){
                return "East";
            }
            if(a>=w && a<=(w+h-2)){
                return "North";
            }
            if(a>=(w+h-1) && a<=(2*w+h-3)){
                return "West";
            }
            if(a>=(2*w+h-2) && a<=(2*w+2*h-3)){
                return "South";
            }
            return "East";
        }
    }




    /**
     *   this is slow ... any better way  ?
     */
    static class Robot1 {

        int[] pos={0,0};
        int[][] directions={{1,0},{0,1},{-1,0},{0,-1}}; // east , up , west , down;
        int direct = 0;
        int w=0,h=0;
        public Robot1(int width, int height) {
            w = width;
            h = height;
            direct = 0;
        }

        private boolean validPos(int x,int y){
            boolean p = (x==-1);
            if(x<0){
                if(p)System.out.println("validPos 1");
                return false;
            }
            if(x>=w){
                if(p)System.out.println("validPos 2");
                return false;
            }
            if(y<0){
                if(p)System.out.println("validPos 3");
                return false;
            }
            if(y>=h){
                if(p)System.out.println("validPos 4");
                return false;
            }
            if(p)System.out.println("validPos 5");
            return true;
        }

        public void step(int num) {
            for (int i=0;i<num;i++){
                int newX = pos[0] + directions[direct][0];
                int newY = pos[1] + directions[direct][1];
                boolean nextValid = validPos(newX,newY);
                if(nextValid) {
                    pos[0] = newX;
                    pos[1] = newY;
                } else {
                    direct++;
                    direct = (direct%4);
                    pos[0] = pos[0] + directions[direct][0];
                    pos[1] = pos[1] + directions[direct][1];
                }
                System.out.println(" step:"+(i+1)+"  x:"+pos[0]+" y:"+pos[1]+"  direct:"+direct);
            }
            System.out.println(" -----------------");
        }

        public int[] getPos() {
            return pos;
        }

        public String getDir() {
            switch(direct){
                case 0:
                    return "East";
                case 1:
                    return "North";
                case 2:
                    return "West";
                case 3:
                    return "South";
            }
            return "East";
        }
    }

/**
 * Your Robot object will be instantiated and called as such:
 * Robot obj = new Robot(width, height);
 * obj.step(num);
 * int[] param_2 = obj.getPos();
 * String param_3 = obj.getDir();
 */
}
