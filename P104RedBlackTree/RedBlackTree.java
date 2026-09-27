package P104RedBlackTree;

public class RedBlackTree<T extends Comparable<T>> extends BSTRotation<T> {

    protected RedBlackNode<T> root  = null;

    /**
     * Checks if a new red node in the RedBlackTree causes a red property violation
     * by having a red parent. If this is not the case, the method terminates without
     * making any changes to the tree. If a red property violation is detected, then
     * the method repairs this violation and any additional red property violations
     * that are generated as a result of the applied repair operation.
     * Using this method might cause nodes with a value equal to the value of one of
     * their ancestors to appear within the left and the right subtree of that ancestor,
     * even if the original insertion procedure consistently inserts such nodes into only
     * the left or the right subtree. But it will preserve the ordering of nodes within
     * the tree.
     * @param newNode a newly inserted red node, or a node turned red by previous repair
     */
    protected void ensureRedProperty(RedBlackNode<T> newNode) {
        // TODO: Implement this method.
    }

    @Override 
    public void add(T data) throws NullPointerException {
       if(data == null) {
        throw new NullPointerException("Null data! Throwing!"); 
       }
       RedBlackNode<T> newNode = new RedBlackNode<T>(data);
       if(this.root == null) {
        this.root = newNode;
         return;
       }
       addHelper(newNode,this.root);
        if(newNode.isBlackNode()) {
            newNode.flipColor();
        }
        ensureRedProperty(newNode);
    }


}