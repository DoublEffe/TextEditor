package org.project.texteditor;

import java.util.ArrayDeque;

class Rope {
    Rope left, right;
    String s;
    int weight;
    
    public static void create(Rope node, int len, String s) {
        len = len / 2;
        Rope left = node.left = new Rope();
        Rope right = node.right = new Rope();
        if(len <= 1) {
            left.s = s.substring(0, len);
            right.s = s.substring(len, s.length());
            return;
        }
        Rope.create(left, len, s.substring(0, len));
        Rope.create(right, len, s.substring(len, s.length()));
    }

    public static String traverse(Rope node) {
        ArrayDeque<Rope> stack = new ArrayDeque<>();
        stack.add(node);
        String result = "";
        while (!stack.isEmpty()) {
            Rope visit = stack.remove();
            if(visit.s != null) {
                result += visit.s;
            }
            if (visit.left != null) {
                stack.add(visit.left);
            }
            if (visit.right != null) {
                stack.add(visit.right);
            }
        }
        return result;
    }

    public static String getLine(Rope node, int idx) {
        if(node.weight == 0) {
            return Rope.traverse(node);
        }
        if(node.weight == idx) {
            return Rope.traverse(node.right);
        }
        return Rope.getLine(node.left, idx);
    }

    public static void setLine(Rope node, int idx, String s) {
        Rope newLine = new Rope();
        Rope.create(newLine, s.length(), s);
        if(node.left.weight == idx) {
            newLine.weight = 0;
            node.left = newLine;
            return;
        }
        if(node.weight == idx) {
            node.right = newLine;
            return;
        }
        Rope.setLine(node.left, idx, s);
    }

    public static Rope appendLine(Rope node, String s) {
        Rope lineRoot = new Rope();
        Rope.create(lineRoot, s.length(), s);
        Rope newRoot = new Rope();
        newRoot.left = node;
        newRoot.right = lineRoot;
        newRoot.weight = node.weight + 1;
        return newRoot;
    }

    public static String[] allLines(Rope node) {
        String[] allLines = new String[node.weight +1];
        Rope.collect(node, allLines);
        return allLines;
    }

    public static void collect(Rope node, String[] result) {
       if(node.weight == 0) {
            result[0] = Rope.traverse(node);
            return;      
       }
       else {
        result[node.weight] = Rope.traverse(node.right);
       }
       Rope.collect(node.left, result);
    }

    public static Rope appendEmptyLine(Rope node) {
        Rope emptyRoot = new Rope();
        Rope newRoot = new Rope();
        newRoot.left = node;
        newRoot.right = emptyRoot;
        newRoot.weight = node.weight + 1;
        return newRoot;
    }

}
