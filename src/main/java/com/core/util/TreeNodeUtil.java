package com.core.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Bonjour
 *
 */
public class TreeNodeUtil {
	
	/**
     * 物件欄位是否包含關鍵字
     * 
     * @param obj : 物件
     * @param keyword : 關鍵字
     * @param columns : 關鍵字要比對的欄位 (null 時, 則比對全部欄位)
     * @return
     */
    private static boolean isValueMatch(Object obj, String keyword, String[] columns) {
        boolean result = false;
        
        if (keyword == null || "".equals(keyword.trim())) {
            result = true;
        } else {
            if (obj != null) {
                List<String> colList = new ArrayList<String>(); // 需要比對的欄位
                boolean compareAll = false; // 是否比對全部欄位
                if (columns == null || columns.length == 0) {
                    compareAll = true;
                } else {
                    colList = Arrays.asList(columns);
                }
                
                try {
                    BeanInfo info = Introspector.getBeanInfo(obj.getClass());
                    PropertyDescriptor[] pds = info.getPropertyDescriptors();
                    if (pds != null && pds.length > 0) {
                        Object[] empty = new Object[]{};
                        for (PropertyDescriptor p : pds) {
                            String pName = p.getName();
                            Method read = p.getReadMethod();
                            if (read != null) {
                                if (compareAll || colList.contains(pName)) {
                                    Object value = read.invoke(obj, empty);
                                    if (value != null && value.toString().toUpperCase().contains(keyword.toUpperCase())) {
                                        result = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
        }
        
        return result;
    }
    
    /**
     * 關鍵字查詢
     * 
     * @param node : 一個節點
     * @param keyword : 關鍵字
     * @param columns : 關鍵字要比對的欄位 (null 時, 則比對全部欄位)
     * @return
     */
    private static <T> boolean doFilterOneNode(TreeNode<T> node, String keyword, String[] columns) {
        boolean match1 = false;
        boolean match2 = false;
        
        if (node != null && node.getNode() != null) {
            // 判斷節點本身是否符合關鍵字
            match1 = isValueMatch(node.getNode(), keyword, columns);
            
            if (node.getSubNodes() != null) {
                match2 = false;

                // newSubs 用來放符合關鍵字的子節點
                List<TreeNode<T>> newSubs = new ArrayList<TreeNode<T>>();
                
                for (int i = 0 ; i < node.getSubNodes().size() ; i++) {
                    
                    TreeNode<T> sub = node.getSubNodes().get(i);
                    // 判斷子節點及其更下層的子節點是否符合關鍵字
                    boolean tmpMatch = doFilterOneNode(sub, keyword, columns);
                    if (!match2) {
                        match2 = tmpMatch;
                    }
                    // 有符合就加入
                    if (tmpMatch) {
                        newSubs.add(sub);
                    }
                }
                // 替換成新的子節點
                node.setSubNodes(newSubs);
            }
        }
        
        // 最後回傳符合結果
        // (節點本身 或 子節點 符合就回傳 true)
        return match1 || match2;
    }

    /**
     * 用關鍵字篩選 TreeNode 資料
     * 
     * @param tree : 樹結構資料
     * @param keyword : 用來篩選的關鍵字
     */
    public static <T> List<TreeNode<T>> filterByKeyword(List<TreeNode<T>> tree, String keyword) {
        return filterByKeyword(tree, keyword, null);
    }
    
    /**
     * 用關鍵字篩選 TreeNode 資料
     * 
     * @param tree : 樹結構資料
     * @param keyword : 用來篩選的關鍵字
     * @param columns : 關鍵字要比對的欄位 (null 時, 則比對全部欄位)
     */
    public static <T> List<TreeNode<T>> filterByKeyword(List<TreeNode<T>> tree, String keyword, String[] columns) {
        List<TreeNode<T>> result = null;

        if (tree != null && keyword != null) {
            result = new ArrayList<TreeNode<T>>();

            for (TreeNode<T> node : tree) {
                if (node != null && node.getNode() != null) {
                    // 判斷節點本身是否符合關鍵字
                    boolean match1 = isValueMatch(node.getNode(), keyword, columns);
                    
                    // 判斷節點的子節點是否關鍵字
                    boolean match2 = doFilterOneNode(node, keyword, columns);
                    
                    // 節點本身或是子節點符合, 就把節點加入
                    if (match1 || match2) {
                        result.add(node);
                    }
                }
            }
        } else {
            result = tree;
        }
        
        return result;
    }
}
