package com.atguigu.graph.graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * 邻接表实现图
 *
 * @author songquanheng
 */
public class GraphList {
    private int numberOfVertex;
    private int numberOfEdge;

    /**
     * 顶点集合
     */
    private Vertex[] vertices;

    public static void main(String[] args) {
        GraphList graphList = new GraphList();
        graphList.show();

        // 8 1 2 3 4 5 6 7 8
        graphList.initializeVertex();
        // 9 1 2 1 1 3 1 2 4 1 2 5 1 3 6 1 3 7 1 6 7 1 4 8 1 5 8 1
        graphList.initializeEdge();
        graphList.show();
        System.out.println();

        graphList.dfs(0);
        graphList.dfs("1");
        System.out.println();

        graphList.bfs(0);
        graphList.bfs("1");


    }

    /**
     * 解决离散图像-即非连通图像的深度优先遍历
     */
    public void dfs() {
        System.out.println("GraphList.dfs");

        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);
        for (int i = 0; i < getNumberOfVertex(); i++) {
            if (!visited[i]) {
                dfs(i, visited);
            }
        }

        System.out.println();
    }

    public void dfs(String vertexName) {
        System.out.println("GraphList.dfs");
        System.out.println("vertexName = [" + vertexName + "]");


        int vertex = vertexIndexByName(vertexName);
        dfs(vertex);
        System.out.println();
    }

    /**
     * 从指定顶点进行深度优先遍历，对于连通的图可以完整的遍历
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
        System.out.print(vertices[vertex].getVertexName() + "->");
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
     * 从指定顶点名称开始进行广度优先遍历
     *
     * @param vertexName 顶点名称
     */
    public void bfs(String vertexName) {
        System.out.println("GraphList.bfs");
        System.out.println("vertexName = [" + vertexName + "]");

        int vertex = vertexIndexByName(vertexName);

        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        bfs(vertex, visited);
        System.out.println();
    }

    /**
     * 从指定顶点开始进行广度优先遍历
     *
     * @param vertex 顶点所在的位置
     */
    public void bfs(int vertex) {
        System.out.println("GraphList.bfs");

        boolean[] visited = new boolean[numberOfVertex];
        Arrays.fill(visited, false);

        bfs(vertex, visited);
        System.out.println();
    }

    private void bfs(int vertex, boolean[] visited) {
        System.out.print(vertices[vertex].getVertexName() + "->");
        visited[vertex] = true;

        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(vertex);

        while (!queue.isEmpty()) {
            traverseVertex(visited, queue);
        }

        System.out.println();
    }

    /**
     * @param visited 访问的辅助数组
     * @param queue   待访问的顶点队列
     */
    private void traverseVertex(boolean[] visited, LinkedList<Integer> queue) {
        int vertex = queue.remove();
        int neighbour = getFirstNeighbour(vertex);

        // 一个循环走完，就把一行走位了，在这一行中，顶点vertexIndex不变，而neighbour作为vertexIndex的邻接顶点一直在遍历
        // 然后在下一个循环中，vertexIndex更新成第一个顶点的邻接顶点了
        while (neighbour != -1) {
            if (!visited[neighbour]) {
                System.out.print(vertices[neighbour].getVertexName() + "->");
                visited[neighbour] = true;
                queue.addLast(neighbour);
            }
            neighbour = getNextNeighbour(vertex, neighbour);
        }
    }

    /**
     * 负责图的初始化
     */
    public void initializeVertex() {
        System.out.println("GraphList.initializeVertex");
        Scanner scan = new Scanner(System.in);
        System.out.println("请输入顶点数");
        this.numberOfVertex = scan.nextInt(); // 8

        this.vertices = new Vertex[this.numberOfVertex];
        System.out.println("请依次输入顶点名称：");

        for (int i = 0; i < numberOfVertex; i++) {
            String name = scan.next(); // 1 2 3 4 5 6 7 8
            Vertex vertex = new Vertex(name);
            vertices[i] = vertex;
        }

        System.out.println("顶点初始化完成");
    }

    public void initializeEdge() {
        System.out.println("GraphList.initializeEdge");
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入顶点数"); // 9
        this.numberOfEdge = scanner.nextInt();


        for (int i = 0; i < this.numberOfEdge; i++) {
            System.out.println("开始输入边，形式为(顶点1名称 顶点2名称 权重)");
            // 此处应校验顶点名称是合法的，即用户输入的顶点在之前的初始化顶点中
            // 1 2 1 1 3 1 2 4 1 2 5 1 3 6 1 3 7 1 6 7 1 4 8 1 5 8 1
            String vertex1Name = scanner.next();
            String vertex2Name = scanner.next();
            int weight = scanner.nextInt();

            int vertex1Index = vertexIndexByName(vertex1Name);
            int vertex2Index = vertexIndexByName(vertex2Name);

            Edge edge1 = new Edge(vertex2Index, weight);
            vertices[vertex1Index].add(edge1);

            Edge edge2 = new Edge(vertex1Index, weight);
            vertices[vertex2Index].add(edge2);

        }
    }

    /**
     * @param index 顶点序号
     * @return 返回顶点序号的第一个邻接顶点的序号
     */
    public int getFirstNeighbour(int index) {
        Edge edge = vertices[index].getAdjacentEdge();
        if ((edge != null)) {
            return edge.getAdjacentVertex();
        }
        return -1;
    }

    /**
     * 获取index顶点，相对于currentNeighbourIndex的下一个邻接顶点
     *
     * @param index                 顶点序号
     * @param currentNeighbourIndex 在顶点序号的邻接顶点的当前邻接顶点
     * @return 相对于currentNeighbourIndex的，index顶点的下一个邻接顶点
     */
    public int getNextNeighbour(int index, int currentNeighbourIndex) {
        Edge edge = vertices[index].getAdjacentEdge();

        while (edge != null && edge.getAdjacentVertex() != currentNeighbourIndex) {
            edge = edge.getNext();
        }

        edge = edge.getNext();
        if (edge == null) {
            return -1;
        } else {
            return edge.getAdjacentVertex();
        }


    }

    /**
     * 输出图的邻接链表表示
     */
    public void show() {
        System.out.println("GraphList.show");
        System.out.println("numberOfVertex = " + getNumberOfVertex());
        System.out.println("numberOfEdge = " + getNumberOfEdge());

        Edge edge;
        for (int i = 0; i < numberOfVertex; i++) {
            System.out.print(vertices[i].vertexName);

            edge = vertices[i].adjacentEdge;
            while (edge != null) {
                // edge.getAdjacentVertex() 获取边上的邻接顶点
                // vertices[edge.getAdjacentVertex()].getVertexName()
                System.out.print("-->" + vertexNameByIndex(edge.getAdjacentVertex()));
                edge = edge.getNext();
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * @param index 根据顶点的索引号获取顶点的名称
     * @return 顶点名称
     */
    public String vertexNameByIndex(int index) {
        assert index < vertices.length;

        return vertices[index].getVertexName();
    }

    /**
     * 根据顶点名称返回顶点的索引号
     *
     * @param vertexName 顶点名称
     * @return 返回的是边对象的标签
     */
    public int vertexIndexByName(String vertexName) {
        List<String> validVertexNames = Arrays.stream(vertices)
                .map(vertex -> vertex.getVertexName())
                .collect(Collectors.toList());
        assert validVertexNames.contains(vertexName);

        for (int i = 0; i < getNumberOfVertex(); i++) {
            if (vertices[i].vertexName.equals(vertexName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return 获取图中的顶点数
     */
    public int getNumberOfVertex() {
        return numberOfVertex;
    }

    /**
     * @return 获取图中的边数
     */
    public int getNumberOfEdge() {
        return numberOfEdge;
    }
}

/**
 * 边类
 */
class Edge {
    /**
     * 邻接顶点
     */
    private int adjacentVertex;
    /**
     * 权重
     */
    private int weight;
    /**
     * 下一个邻接顶点
     */
    private Edge next;

    public Edge getNext() {
        return next;
    }

    public void setNext(Edge next) {
        this.next = next;
    }

    public Edge(int adjacentVertex, int weight) {
        this.adjacentVertex = adjacentVertex;

        this.weight = weight;
    }

    public int getAdjacentVertex() {
        return adjacentVertex;
    }
}

/**
 * 顶点类
 */
class Vertex {
    /**
     * 顶点名称
     */
    String vertexName;
    /**
     * 边链表的头指针
     */
    Edge adjacentEdge;

    public Vertex(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public Edge getAdjacentEdge() {
        return adjacentEdge;
    }

    public void setAdjacentEdge(Edge adjacentEdge) {
        this.adjacentEdge = adjacentEdge;
    }

    /**
     * 把待添加的邻接边添加到末尾
     *
     * @param edge 待添加的邻接边
     */
    public void add(Edge edge) {
        // 表示待添加的邻接表为第一条边
        if (adjacentEdge == null) {
            adjacentEdge = edge;
        } else {
            Edge tail = tail();
            tail.setNext(edge);

        }
    }

    /**
     * 获取当前顶点的链表的尾部, 在该顶点的邻接表不为空的情况下调用
     *
     * @return 获取邻接表的尾部
     */
    private Edge tail() {
        assert adjacentEdge != null;
        Edge tail = adjacentEdge;
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        return tail;
    }


}
