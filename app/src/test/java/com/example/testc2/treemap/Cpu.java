package com.example.testc2.treemap;

import androidx.annotation.NonNull;

import com.example.testc2.TestUtil;

import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 给你一个二维数组 tasks ，用于表示 n​​​​​​ 项从 0 到 n - 1 编号的任务。其中 tasks[i] = [enqueueTimei, processingTimei] 意味着第 i​​​​​​​​​​ 项任务将会于 enqueueTimei 时进入任务队列，需要 processingTimei 的时长完成执行。
 *
 * 现有一个单线程 CPU ，同一时间只能执行 最多一项 任务，该 CPU 将会按照下述方式运行：
 *
 * 如果 CPU 空闲，且任务队列中没有需要执行的任务，则 CPU 保持空闲状态。
 * 如果 CPU 空闲，但任务队列中有需要执行的任务，则 CPU 将会选择 执行时间最短 的任务开始执行。如果多个任务具有同样的最短执行时间，则选择下标最小的任务开始执行。
 * 一旦某项任务开始执行，CPU 在 执行完整个任务 前都不会停止。
 * CPU 可以在完成一项任务后，立即开始执行一项新任务。
 * 返回 CPU 处理任务的顺序。
 *
 *
 * 示例 1：
 * 输入：tasks = [[1,2],[2,4],[3,2],[4,1]]
 * 输出：[0,2,3,1]
 * 解释：事件按下述流程运行：
 * - time = 1 ，任务 0 进入任务队列，可执行任务项 = {0}
 * - 同样在 time = 1 ，空闲状态的 CPU 开始执行任务 0 ，可执行任务项 = {}
 * - time = 2 ，任务 1 进入任务队列，可执行任务项 = {1}
 * - time = 3 ，任务 2 进入任务队列，可执行任务项 = {1, 2}
 * - 同样在 time = 3 ，CPU 完成任务 0 并开始执行队列中用时最短的任务 2 ，可执行任务项 = {1}
 * - time = 4 ，任务 3 进入任务队列，可执行任务项 = {1, 3}
 * - time = 5 ，CPU 完成任务 2 并开始执行队列中用时最短的任务 3 ，可执行任务项 = {1}
 * - time = 6 ，CPU 完成任务 3 并开始执行任务 1 ，可执行任务项 = {}
 * - time = 10 ，CPU 完成任务 1 并进入空闲状态
 *
 *
 *
 * 示例 2：*
 * 输入：tasks = [[7,10],[7,12],[7,5],[7,4],[7,2]]
 * 输出：[4,3,2,0,1]
 * 解释：事件按下述流程运行：
 * - time = 7 ，所有任务同时进入任务队列，可执行任务项  = {0,1,2,3,4}
 * - 同样在 time = 7 ，空闲状态的 CPU 开始执行任务 4 ，可执行任务项 = {0,1,2,3}
 * - time = 9 ，CPU 完成任务 4 并开始执行任务 3 ，可执行任务项 = {0,1,2}
 * - time = 13 ，CPU 完成任务 3 并开始执行任务 2 ，可执行任务项 = {0,1}
 * - time = 18 ，CPU 完成任务 2 并开始执行任务 0 ，可执行任务项 = {1}
 * - time = 28 ，CPU 完成任务 0 并开始执行任务 1 ，可执行任务项 = {}
 * - time = 40 ，CPU 完成任务 1 并进入空闲状态
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/single-threaded-cpu
 */
public class Cpu {


    @Test
    public void test2(){
        System.out.println( convert2("3.12"));
        System.out.println( convert2("3.120") );
        System.out.println( convert2("3.00") );
        System.out.println( convert2("3000.00") );
        System.out.println( convert2("3000.009") );
        System.out.println( convert2("3000.00009") );
    }

    DecimalFormat myFormatter = new DecimalFormat("#.##");

    private String convert2(String value ) {
        float f = 0;
        try {
            f = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String output = myFormatter.format(f);
        return output;
    }

    private String convert(String str){
        float f = 0;
        try {
            f = Float.parseFloat(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return String.format("#.##", f);
    }


    @Test
    public void test1(){
        int[][] a1 ={{1,2},{2,4},{3,2},{4,1}};
        int[] out = getOrder(a1);
        TestUtil.printArr(out,"aa");
    }

    static class Dup {
        int ind;
        int duration;
        int start;
        Dup(int ind,int dur, int start){
            this.ind = ind;
            this.duration = dur;
            this.start = start;
        }
        @NonNull
        @Override
        public String toString() {
            return " ind:"+ind+" dur:"+duration+" start:"+start;
        }
    }
    public int[] getOrder2(int[][] tasks) {
        return null;
    }

    // this is slow ...
    public int[] getOrder(int[][] tasks) {
        int l = tasks.length;
        int[] out = new int[l];
        int outInd = 0;
        TreeMap<Integer, ArrayList<Dup>> map = new TreeMap();
        for (int i=0;i<l;i++) {
            int t1 = tasks[i][0];
            int duration = tasks[i][1];
            ArrayList<Dup> list = map.getOrDefault(t1,new ArrayList());
            list.add(new Dup(i,duration,t1));
            map.put(t1,list);
        }
        int time = map.firstKey();
        while(map.size()>0){
            int smallestKey = map.firstKey();
            ArrayList<Dup> taskslist = new ArrayList();
            if(time==smallestKey){
                taskslist = map.get(smallestKey);
            } else {
                Map<Integer, ArrayList<Dup>> subMap = map.subMap(smallestKey,true,time,true);
                Iterator<ArrayList<Dup>> it = subMap.values().iterator();
                while(it.hasNext()){
                    taskslist.addAll(it.next());
                }
            }
            System.out.println("smallestKey:"+smallestKey+"  time:"+time+"   taskslist:"+taskslist+"   map:"+map);
            Dup smallest = taskslist.get(0);
            int smallestInd = smallest.ind;
            for(int i=0;i<taskslist.size();i++){
                Dup t = taskslist.get(i);
                if(t.duration<smallest.duration){
                    smallest = t;
                    smallestInd = smallest.ind;
                } else {
                    if(t.duration==smallest.duration && (t.ind<smallest.ind)) {
                        smallest = t;
                        smallestInd = smallest.ind;
                    }
                }
            }
            out[outInd] = smallestInd;
            outInd++;
            time = time + smallest.duration;
            ArrayList<Dup> tempList = map.get(smallest.start);
            tempList.remove(smallest);
            if(tempList.size()==0){
                map.remove(smallest.start);
            } else {
                map.put(smallest.start,tempList);
            }
        }
        return out;

    }

}
