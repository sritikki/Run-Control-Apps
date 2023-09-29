package org.genevaers.genevaio.fieldnodes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import org.genevaers.genevaio.fieldnodes.FieldNodeBase.Type;

public class Records2Dot {
    
    private static final String LIGHTGREY = "lightgrey";
    private static final String FRAME = "lightgrey";
    private static final String DATASOURCE = "deepskyblue";
    private static final String COMPARISON = "lightgreen";

    private static FileWriter fw;
    private static String idString;
    private static String label;
    private static String colour;
    private static String shape;
    private static boolean nodeEnabled = true;
    private static int nodeNum = 1;

    public static void write(RecordNode root, Path dest) {
        try {
            fw = new FileWriter(dest.toFile());
            recursivelyWriteTheTree(root);
            fw.write("}\n");
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void recursivelyWriteTheTree(RecordNode root) throws IOException {
        writeHeader();
        writeNodes(root);

    }

    private static void writeNodes(RecordNode root) throws IOException {
        String r = dotNode(root);
        Iterator<FieldNodeBase> asti = root.getChildren().iterator();
        while (asti.hasNext()) {
            FieldNodeBase n = (FieldNodeBase) asti.next();
            String child = dotNode(n);
            fw.write(r + " -> " + child + "\n");
            writeBranch(child, n);
        }

    }

    private static void writeBranch(String from, FieldNodeBase next) throws IOException {
        if (next != null ) {
            Iterator<FieldNodeBase> asti = next.getChildren().iterator();
            while (asti.hasNext()) {
                FieldNodeBase node = asti.next();
                String child = dotNode(node);
                if (nodeEnabled) {
                    fw.write(from + " -> " + child);
                    fw.write("\n");
                    writeBranch(child, node);
                }
            }
        }
    }

    private static String dotNode(FieldNodeBase node) throws IOException {
        idString = node.getName();
        label = "";
        colour = "red";
        shape = "oval";
        switch(node.getType()) {
            case NUMBERFIELD:
                label = node.getName() +" (" + Integer.toString(((NumericFieldNode)node).getValue()) + ")";
                colour = "skyblue";
                idString = "num" + nodeNum++;
                break;
            case RECORD:
                label = ((RecordNode)node).getName();
                colour = "pink";
                idString = "rec" + label;
                break;
            case STRINGFIELD:
                label = node.getName() +" (" + dotEscape(((StringFieldNode)node).getValue()) + ")";
                colour = "lightgreen";
                idString = "str" + nodeNum++;
                break;
            default:
                break;
            
        }
        if (node != null) {
                fw.write(idString + "[label=\"" + label + "\" " + "color=" + colour + " shape=" + shape
                        + " style=filled]\n");
            }
        return idString;
    }

    private static void writeHeader() throws IOException {
        fw.write("digraph xml {\nrankdir=LR\n//Nodes\n");
        fw.write(
                "graph [label=\" MR91 Logic Trees\", labelloc=t, labeljust=center, fontname=Helvetica, fontsize=22, concentrate=true];\n");
        fw.write("labeljust=center;\n");
    }

    private static String dotEscape(String string) {
        return string.replace("\"", "\\\"");
    }

}
