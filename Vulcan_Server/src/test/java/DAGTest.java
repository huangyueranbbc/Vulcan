import org.huangyr.project.vulcan.common.dag.DAG;
import org.junit.Test;

public class DAGTest {

  @Test
  public void testDag() {
    DAG dag = DAG.create();
    dag.addVertex("A");
    dag.addVertex("B");
    dag.addVertex("C");
    dag.addVertex("D");
    dag.addEdge("A", "B");
    dag.addEdge("A", "C");
    System.out.println(dag);
    System.out.println("入度为0 的节点 ： " + dag.getSources());
    System.out.println("拓扑排序检测是否有环 : " + dag.isCircularity());
  }

  /**
   *     A
   *   /  \
   *   B  C
   *   | //
   *   D
   *   |
   *   E
   */
  @Test
  public void testIsCircularity() {
    DAG dag = DAG.create();
    dag.addVertex("A");
    dag.addVertex("B");
    dag.addVertex("C");
    dag.addVertex("D");
    dag.addVertex("E");
    dag.addEdgeWithNoCheck("A", "B");
    dag.addEdgeWithNoCheck("A", "C");

    dag.addEdgeWithNoCheck("B", "D");
    dag.addEdgeWithNoCheck("C", "D");

    dag.addEdgeWithNoCheck("D", "C");
    dag.addEdgeWithNoCheck("D", "E");
    System.out.println(dag);
    System.out.println("入度为0 的节点 ： " + dag.getSources());
    System.out.println("child:"+dag.getChildren("D"));
    System.out.println("拓扑排序检测是否有环 : " + dag.isCircularity());
  }
}
