//import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhangzhonghua on 2016/3/30.
 */
public class TestDeepLoopTree {

//    public static void main(String[] args) {
//        TestDeepLoopTree dlt = new TestDeepLoopTree();
//        TreeNode[] tree = dlt.initTree();
//        dlt.deepLoop(tree);
//    }
//
//    public List<TreeNode> deepLoop(TreeNode[] tree) {
//        quicksort(tree, 0, tree.length-1);
//
//        for(TreeNode node : tree) {
//            System.out.println(node.getId() + " | " + node.getPid());
//        }
//        return null;
//    }
//
//    public static void quicksort(TreeNode[] a,int left,int right){
//        int tmpLeft, tmpRight;
//        Long middle;
//        TreeNode temp;
//        middle = a[(left+right)/2].getPid();
//        tmpLeft = left;
//        tmpRight = right;
//        do{
//            //从左边寻找大于中间值的下标
//            while(tmpLeft<right&&a[tmpLeft].getPid()<middle){
//                tmpLeft++;
//            }
//            //从右边寻找小于中间值的下标
//            while(tmpRight>left&&a[tmpRight].getPid()>middle){
//                tmpRight--;
//            }
//            //找到一对，对换他们
//            //注意：如果少了等于号，会长生死循环
//            if(tmpLeft<=tmpRight){
//                temp=a[tmpLeft];
//                a[tmpLeft]=a[tmpRight];
//                a[tmpRight]=temp;
//                tmpLeft++;
//                tmpRight--;
//            }
//
//        }while(tmpRight>=tmpLeft); //直到左右对换
//
//        if(left<tmpRight){
//            quicksort(a, left, tmpRight);
//        }
//
//        if(right>tmpLeft){
//            quicksort(a, tmpLeft, right);
//        }
//    }
//
//    public TreeNode[] initTree() {
//        List<TreeNode> tree = new ArrayList<TreeNode>();
//        TreeNode node2 = new TreeNode(2L, 1L);
//        TreeNode node3 = new TreeNode(3L, 2L);
//        TreeNode node4 = new TreeNode(4L, 1L);
//        TreeNode node5 = new TreeNode(5L, 3L);
//        TreeNode node6 = new TreeNode(6L, 4L);
//        TreeNode node1 = new TreeNode(1L, -1L);
//        TreeNode node7 = new TreeNode(7L, 4L);
//        TreeNode node8 = new TreeNode(8L, 6L);
//        TreeNode node9 = new TreeNode(9L, 6L);
//        TreeNode node10 = new TreeNode(10L, 4L);
//        TreeNode node11 = new TreeNode(11L, 4L);
//        TreeNode node12 = new TreeNode(12L, 10L);
//        TreeNode node13 = new TreeNode(13L, 12L);
//        TreeNode node14 = new TreeNode(14L, 11L);
//        TreeNode node15 = new TreeNode(15L, 13L);
//        TreeNode node16 = new TreeNode(16L, 12L);
//        TreeNode node17 = new TreeNode(17L, 12L);
//        TreeNode node18 = new TreeNode(18L, 17L);
//        TreeNode node19 = new TreeNode(19L, 18L);
//        TreeNode node20 = new TreeNode(20L, 19L);
//
//        tree.add(node1);
//        tree.add(node2);
//        tree.add(node3);
//        tree.add(node4);
//        tree.add(node5);
//        tree.add(node6);
//        tree.add(node7);
//        tree.add(node8);
//        tree.add(node9);
//        tree.add(node10);
//        tree.add(node11);
//        tree.add(node12);
//        tree.add(node13);
//        tree.add(node14);
//        tree.add(node15);
//        tree.add(node16);
//        tree.add(node17);
//        tree.add(node18);
//        tree.add(node19);
//        tree.add(node20);
//
//        TreeNode[] treeNodes = new TreeNode[tree.size()];
//        return tree.toArray(treeNodes);
//    }
//
//    public class TreeNode {
//        private Long id;
//        private Long pid;
//
//        public TreeNode(Long id, Long pid) {
//            this.id = id;
//            this.pid = pid;
//        }
//
//        public Long getId() {
//            return id;
//        }
//
//        public Long getPid() {
//            return pid;
//        }
//    }
}
