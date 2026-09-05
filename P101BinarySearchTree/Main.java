package P101BinarySearchTree;

public class Main {
  public static void main(String[] args) {
    System.out.println("testing :)");
    BinarySearchTree<String> tree = new BinarySearchTree<String>();
    tree.add("mud");
    tree.add("mud");
    tree.add("mud");
    tree.add("mud");
    System.out.println(tree.size());
    tree.clear();
    System.out.println(tree.size());
  }
}
