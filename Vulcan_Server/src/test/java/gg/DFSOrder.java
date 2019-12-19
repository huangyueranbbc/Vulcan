package gg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * 有向图的深度优先遍历实现 在深度遍历中，是否存在一条路径包含了图中所有的顶点??
 * 
 * @author psj
 *
 */
public class DFSOrder {

    public static Map<String, ArrayList<String>> buildGraph(String[] arr) {
        Map<String, ArrayList<String>> graph = new HashMap<String, ArrayList<String>>(
                arr.length);

        for (String str : arr) {
            graph.put(str, new ArrayList<String>());
        }

        String start;
        int startLen;
        String end;
        int endLen;
        for (int i = 0; i < arr.length; i++) {
            start = arr[i];
            startLen = start.length();
            if (startLen == 0)
                continue;
            ArrayList<String> adjs = null;
            for (int j = 0; j < arr.length; j++) {
                end = arr[j];
                endLen = end.length();
                if (endLen == 0)
                    continue;
                if (start.charAt(startLen - 1) == end.charAt(0))// start-->end
                {
                    adjs = graph.get(start);
                    if (!adjs.contains(end))
                        adjs.add(end);
                    graph.put(start, adjs);
                } else if (start.charAt(0) == end.charAt(endLen - 1)) {// end-->start
                    adjs = graph.get(end);
                    if (!adjs.contains(start))
                        adjs.add(start);
                    graph.put(end, adjs);
                }
            }
        }
        return graph;
    }

    /**
     * 从start顶点开始,对graph进行DFS遍历(非递归)
     * 
     * @param graph
     * @param start
     * @return DFS遍历顺序
     */
    public static ArrayList<String> dfs(Map<String, ArrayList<String>> graph,
            String start) {

        assert graph.keySet().contains(start);// 假设 start 一定是图中的顶点
        ArrayList<String> paths = new ArrayList<>(graph.size());

        HashSet<String> visited = new HashSet<>(graph.size());// 用来判断某个顶点是否已经访问了
        LinkedList<String> stack = new LinkedList<>();// 模拟递归遍历中的栈

        stack.push(start);
        paths.add(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            String next = null;// 下一个待遍历的顶点
            String currentVertex = stack.peek();// 当前正在遍历的顶点
            ArrayList<String> adjs = graph.get(currentVertex);// 获取当前顶点的邻接表
            if (adjs != null) {
                for (String vertex : adjs) {
                    if (!visited.contains(vertex))// vertex 未被访问过
                    {
                        next = vertex;
                        break;
                    }
                }
            }// end if

            if (next != null)// 当前顶点还有未被访问的邻接点
            {
                paths.add(next);// 将该邻接点添加到访问路径中
                stack.push(next);
                visited.add(next);
            } else {
                stack.pop();// 回退
            }
        }// end while
        return paths;
    }

    // 打印从某个顶点开始的深度优先遍历路径
    public static void printPath(ArrayList<String> paths,
            Map<String, ArrayList<String>> graph) {
        System.out.println("dfs path:");
        for (String v : paths) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    // 判断有向图中是否存在某顶点，从该顶点进行DFS遍历，能够遍历到图中所有的顶点
    public static boolean containsAllNode(Map<String, ArrayList<String>> graph) {
        boolean result = false;

        ArrayList<String> paths = null;
        Set<String> vertexs = graph.keySet();
        // 从图中的每一个顶点开始DFS遍历
        for (String v : vertexs) {
            paths = dfs(graph, v);

            if (paths.size() == graph.size())// 从 顶点 v 遍历 能够遍历完图中所有的顶点.
            {
                System.out.println("从顶点: " + v + " 开始DFS遍历能够遍历完所有的顶点,路径如下:");
                printPath(paths, graph);
                result = true;
                break;
            }
        }
        return result;
    }

    // hapjin test
    public static void main(String[] args) {
        // String[] words = {"me","cba","agm","abc","eqm","cde"};

        String[] words = { "abc", "cde", "efg", "che" };
        Map<String, ArrayList<String>> graph = buildGraph(words);
        System.out.println(graph);
        System.out.println(containsAllNode(graph));
    }
}