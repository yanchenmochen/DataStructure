package com.atguigu.graph.graph;

/**
 * 边实例， 该元素的对象保存了一条边，包括权重，起点，终点
 *
 * @author songquanheng
 * 2020/7/2-21:31
 */
public class EdgeData implements Comparable<EdgeData> {
    private int start;
    private int end;
    private int cost;


    EdgeData(int start, int end, int cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    int getStart() {
        return start;
    }



    int getEnd() {
        return end;
    }



    int getCost() {
        return cost;
    }

    @Override
    public int compareTo(EdgeData o) {
        return this.cost - o.cost;
    }

    @Override
    public String toString() {
        return "EdgeData{" +
                "start=" + start +
                ", end=" + end +
                ", cost=" + cost +
                '}';
    }
}
