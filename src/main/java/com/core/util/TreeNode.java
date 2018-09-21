package com.core.util;

import java.util.List;

/**
 * 樹狀結構物件
 * 
 * @author Bonjour
 *
 * @param <T>
 */
public class TreeNode<T> {

	private T node;
    private List<TreeNode<T>> subNodes;
    
    public TreeNode() {
        
    }

    public TreeNode(T node) {
        this.node = node;
    }
    
    /**
     * @return 
     */
    public T getNode() {
        return node;
    }
    /**
     * @param node : 
     */
    public void setNode(T node) {
        this.node = node;
    }

    /**
     * @return 
     */
    public List<TreeNode<T>> getSubNodes() {
        return subNodes;
    }

    /**
     * @param subNodes : 
     */
    public void setSubNodes(List<TreeNode<T>> subNodes) {
        this.subNodes = subNodes;
    }
    
}
