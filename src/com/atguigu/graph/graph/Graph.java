package com.atguigu.graph.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 使用邻接矩阵实现图类
 *
 * @author songquanheng
 * @Time: 2020/6/20-11:32
 */
public class Graph {
    /**
     * 顶点数组
     */
    private String[] vertexs;
    private int numberOfVertex;
    /**
     * 边数
     */
    private int numberOfEdges;


    /**
     * 边集合，采用二维数组表示
     */
    private int[][] edges;

    public static void main(String[] args) {
        String[] vertices = "A B C D E F G".split(" ");
        Graph graph = new Graph(vertices);
        graph.show();

        graph.insertEdge(0, 1, 5);
        graph.insertEdge(0, 2, 7);
        graph.insertEdge(0, 6, 2);
        graph.insertEdge(1, 6, 3);
        graph.insertEdge(1, 3, 9);
        graph.insertEdge(2, 4, 8);
        graph.insertEdge(3, 5, 4);
        graph.insertEdge(4, 5, 5);
        graph.insertEdge(4, 6, 4);
        graph.insertEdge(5, 6, 6);

        graph.show();

        graph.dfs();
        System.out.println();
        graph.dfs(2);
        System.out.println();

        graph.bfs();
        System.out.println();

        graph.bfs(2);
        System.out.println();

        MinTree minTree = graph.prim(0);
        minTree.show();
        System.out.println("minTree.getMinWeight() = " + minTree.getMinWeight());

        MinTree minTree2 = graph.prim2(0);
        minTree.show();
        System.out.println("minTree2.getMinWeight() = " + minTree2.getMinWeight());

    }

    /**
     * @param vertex 通过普利姆算法获取最小支撑树的开始顶点
     * @return 获取最小支撑树
     */
    public MinTree prim(int vertex) {
        MinTree minTree = new MinTree(numberOfVertex);
        // 1. 初始化邻接矩阵 ，当图已经得到良好的初始化了之后，邻接矩阵未初始化的边默认未0
        for (int i = 0; i < numberOfVertex; i++) {
            for (int j = 0; j < numberOfVertex; j++) {
                if (edges[i][j] == 0) {
                    edges[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        show();

        // 2. 初始化closedge数组, 以顶点vertex未初始顶点，初始化数组closedge
        MinEdge[] closedge = new MinEdge[numberOfVertex];
        for (int i = 0; i < numberOfVertex; i++) {
            // 设置了每个顶点到已访问顶点vertex的距离
            closedge[i] = new MinEdge(vertex, edges[i][vertex]);
        }

        closedge[vertex].setVertex(-1);
        closedge[vertex].setLowcost(0);

        // 3. 构造图的最小支撑树
        // 循环n-1次，获取n-1条最小的边
        for (int i = 0; i < numberOfVertex - 1; i++) {
            int minCost = Integer.MAX_VALUE;
            // 未访问的目标顶点索引，其与lowcost[unAccessedTargetVertexIndex].getVertex()构成了未访问顶点集合到已访问顶点集合的最短边
            int unAccessedTargetVertexIndex = -1;

            for (int j = 0; j < numberOfVertex; j++) {
                // 采用选择排序获取未访问顶点到已访问顶点的最小值。第一次循环是找出各个顶点到vertex顶点的距离的最小值
                if (closedge[j].getVertex() != -1 && closedge[j].getLowcost() < minCost) {
                    minCost = closedge[j].getLowcost();
                    // 寻找最短边的位置序号，不断更新直到找到一个最小的。
                    unAccessedTargetVertexIndex = j;
                }
            }

            // 如果在执行了通过选择排序查找未访问顶点集合到已访问顶点集合的最短边和顶点信息之后，查找的unAccessedTargetVertexIndex为-1表示未查找到有效的顶点
            assert unAccessedTargetVertexIndex != -1;

            MSTEdge edge = new MSTEdge(unAccessedTargetVertexIndex, closedge[unAccessedTargetVertexIndex].getVertex(), minCost);
            minTree.addMstEdge(edge);

            // 把找到的未访问顶点标记为已经访问
            closedge[unAccessedTargetVertexIndex].setVertex(-1);
            closedge[unAccessedTargetVertexIndex].setLowcost(0);

            for (int j = 0; j < numberOfVertex; j++) {
                // j代表未访问顶点， unAccessedTargetVertexIndex代表已经访问的新的顶点。
                // 主要是为了更新，最新的顶点加入到已经访问的集合对未访问的顶点集合的影响。
                // 采用选择排序获取未访问顶点到已访问顶点的最小值。第一次循环是找出各个顶点到vertex顶点的距离的最小值
                if (closedge[j].getVertex() != -1 && edges[j][unAccessedTargetVertexIndex] < closedge[j].getLowcost()) {
                    closedge[j].setLowcost(edges[j][unAccessedTargetVertexIndex]);
                    closedge[j].setVertex(unAccessedTargetVertexIndex);
                }
            }

        }

        return minTree;

    }

    /**
     * 通过普利姆算法获取图形的最小支撑树
     *
     * @param vertex 普利姆算法的起始顶点
     * @return 返回当前图形的最小支撑树，注意：这要求当前图形是连通图

     */
    public MinTree prim2(int vertex) {
        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        // 表示vertex结点已经加入最小支撑树
        visited[vertex] = true;
        MinTree minTree = new MinTree(numberOfVertex);


        // 感觉这个过程还是不太好理解。
        // 注意选择排序的运用
        while (minTree.numberOfMstEdge() < numberOfVertex - 1) {
            MSTEdge edge = getShortestEdge(visited);
            minTree.addMstEdge(edge);
            visited[edge.getEnd()] = true;

        }
        return minTree;
    }

    /**
     * 获取已访问顶点和未访问顶点之间相连的最短边
     * @param visited 辅助遍历数组
     * @return 获取最短边，一端是已经访问的点，一端是未访问的顶点。通过遍历求出最短的边
     */
    private MSTEdge getShortestEdge(boolean[] visited) {
        int minWeight = Integer.MAX_VALUE;
        int minStart = Integer.MAX_VALUE;
        int minEnd = Integer.MAX_VALUE;

        // i 表示已经访问过的集合中的顶点
        for (int i = 0; i < numberOfVertex; i++) {
            // 表示未访问的顶点集合中的顶点
            for (int j = 0; j < numberOfVertex; j++) {
                if (visited[i] && !visited[j] && edges[i][j] < minWeight) {
                    minWeight = edges[i][j];
                    minStart = i;
                    minEnd = j;
                }
            }
        }

        return new MSTEdge(minStart, minEnd, minWeight);
    }

    public Graph(String[] vertexs) {
        numberOfVertex = vertexs.length;
        this.vertexs = new String[numberOfVertex];
        int i = 0;
        for (String item : vertexs) {
            this.vertexs[i++] = item;
        }

        // 初始化邻接矩阵
        this.edges = new int[numberOfVertex][numberOfVertex];


    }

    public void show() {
        System.out.println("Graph.show");
        System.out.println(Arrays.toString(vertexs));
        System.out.println();
        for (int[] row : edges) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("graph.getNumberOfEdges() = " + getNumberOfEdges());
        System.out.println("graph.getNumberOfVertex() = " + getNumberOfVertex());
        System.out.println();
    }

    /**
     * @param v1 边的起点的序号
     * @param v2 边的终点的序号
     * @param w  边的权值 无向图赋值为1即可
     */
    public void insertEdge(int v1, int v2, int w) {
        assert v1 != v2;
        edges[v1][v2] = w;
        edges[v2][v1] = w;
        numberOfEdges++;
    }

    /**
     * 深度优先遍历，此时不考虑起始点，即以0号序列的顶点为起始顶点
     */
    public void dfs() {
        System.out.println("Graph.dfs");
        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);
        for (int i = 0; i < numberOfVertex; i++) {
            if (!visited[i]) {
                dfs(i, visited);
            }

        }
        System.out.println();
    }

    /**
     * 从指定顶点进行深度优先遍历
     *
     * @param vertex 开始顶点的序号
     */
    public void dfs(int vertex) {
        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        dfs(vertex, visited);
        System.out.println();
    }

    /**
     * @param vertex 深度优先遍历的开始顶点所在的序号
     */
    private void dfs(int vertex, boolean[] visited) {
        System.out.print(vertexs[vertex] + "->");
        visited[vertex] = true;

        int w = getFirstNeighbour(vertex);
        while (w != -1) {
            if (!visited[w]) {
                dfs(w, visited);
            } else {
                // 如果w已经被访问过，则访问w的下一个邻接顶点
                w = getNextNeighbour(vertex, w);
            }

        }
    }

    /**
     * 广度优先遍历
     */
    public void bfs() {
        System.out.println("Graph.bfs");

        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        for (int i = 0; i < numberOfVertex; i++) {
            if (!visited[i]) {
                bfs(i, visited);
            }
        }
    }

    /**
     * 从指定顶点vertex开始进行广度优先遍历
     *
     * @param vertex 从vertex顶点开始进行广度优先遍历
     */
    public void bfs(int vertex) {
        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        bfs(vertex, visited);

    }

    /**
     * 从顶点vertex开始进行广度优先遍历
     *
     * @param vertex  顶点序号
     * @param visited 辅助遍历数组
     */
    private void bfs(int vertex, boolean[] visited) {
        System.out.print(vertexs[vertex] + "->");
        visited[vertex] = true;

        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(vertex);
        while (!queue.isEmpty()) {
            // 此时head所在的顶点已经访问过了
            int head = queue.remove();
            int w = getFirstNeighbour(head);

            while (w != -1) {
                if (!visited[w]) {
                    // 深度优先遍历从此处开始递归，但广度优先不进行递归
                    System.out.print(vertexs[w] + "->");
                    visited[w] = true;
                    queue.addLast(w);
                }
                w = getNextNeighbour(head, w);
            }
        }
    }


    /**
     * 返回序号为vertex的第一个邻接顶点的序号
     *
     * @param vertex 顶点的序号，对于A顶点，则传入的vertex为A顶点所在的序号0
     * @return 返回该顶点的第一个邻接顶点所在的序号, 如果存在，返回顶点所在的序号，否则返回-1表示不存在
     */
    public int getFirstNeighbour(int vertex) {
        return neighbour(vertex, 0);
    }

    /**
     * 返回序号为vertex的顶点相对于序号为currentAdjacentVertex的顶点的下一个邻接顶点的序号
     *
     * @param vertex                顶点序号
     * @param currentAdjacentVertex currentAdjacentVertex为vertex序号顶点的邻接点，求相对于这个currentAdjacentVertex的下一个邻接顶点的序号
     * @return 返回下一个邻接顶点的序号
     */
    public int getNextNeighbour(int vertex, int currentAdjacentVertex) {
        return neighbour(vertex, currentAdjacentVertex + 1);
    }

    /**
     * 从firstSearchLocation查找获取顶点vertex序号的顶点的邻接点的序号，
     *
     * @param vertex           顶点序号
     * @param firstSearchIndex 查找位置值的范围为[0, numberOfVertex - 1]
     * @return 如果从firstSearchIndex开始查找存在返回邻接顶点，则返回邻接顶点的序号，否则返回1
     */
    private int neighbour(int vertex, int firstSearchIndex) {
        for (int i = firstSearchIndex; i < numberOfVertex; i++) {
            if (edges[vertex][i] > 0) {
                return i;
            }
        }
        return -1;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public int getNumberOfVertex() {
        return numberOfVertex;
    }
}

class MinTree {
    ArrayList<MSTEdge> mstEdges;

    public MinTree(int numberOfVertex) {
        mstEdges = new ArrayList<>(numberOfVertex - 1);
    }


    public void show() {
        System.out.println("MinTree.show");
        System.out.println("最小支撑树如下所示：");
        mstEdges.stream()
                .forEach(System.out::println);
    }

    public void addMstEdge(MSTEdge mstEdge) {
        mstEdges.add(mstEdge);
    }

    public int numberOfMstEdge() {
        return mstEdges.size();
    }

    /**
     * @return 返回最小支撑树的最小权重
     */
    public int getMinWeight() {

        return mstEdges.stream()
                .mapToInt(edge -> edge.getWeight())
                .sum();
    }
}

/**
 * 最小支撑树的边类
 */
class MSTEdge {
    int start;
    int end;
    int weight;

    public MSTEdge(int start, int end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "MSTEdge{" +
                "start=" + start +
                ", end=" + end +
                ", weight=" + weight +
                '}';
    }

    public int getEnd() {
        return end;
    }

    public int getWeight() {
        return weight;
    }
}


/**
 * 刘大有 普利姆算法实现
 */
class MinEdge {
    /**
     * vertex顶点，含义是已访问的顶点序号
     */
    private int vertex;
    /**
     * 某个未访问顶点到vertex顶点所需要的最短的开销
     */
    private int lowcost;

    public int getVertex() {
        return vertex;
    }

    public void setVertex(int vertex) {
        this.vertex = vertex;
    }

    public int getLowcost() {
        return lowcost;
    }

    public void setLowcost(int lowcost) {
        this.lowcost = lowcost;
    }

    public MinEdge(int vertex, int lowcost) {
        this.vertex = vertex;
        this.lowcost = lowcost;
    }
}


