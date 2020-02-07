/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package robotics_assignment_1;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

        
/**
 *
 * @author Lucas
 */
public class Robotics_Assignment_1
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Graph graph = new SingleGraph("grid");
        GridGenerator gridGenerator = new GridGenerator();
        
        gridGenerator.addSink(graph);
        gridGenerator.begin();
        int gridSize = 14;
        for (int i = 0; i < gridSize; i++)
        {
            gridGenerator.nextEvents();
        }
        gridGenerator.end();
        Random random = new Random();
        ArrayList<Node> DFSPath = new ArrayList<>();
        ArrayList<Node> aStarPath = new ArrayList<>();
        for (int i = 0; i < 10; i++)
        {
            int StartX = random.nextInt(gridSize + 1);
            int StartY = random.nextInt(gridSize + 1);
            int GoalX = random.nextInt(gridSize + 1);
            int GoalY = random.nextInt(gridSize + 1);
            
            Node randomStart = graph.getNode(Integer.toString(StartX) + "_" + Integer.toString(StartY));
            Node randomGoal = graph.getNode(Integer.toString(GoalX) + "_" + Integer.toString(GoalY));
            System.out.println(randomStart + ";" + randomGoal);
            long startTime = System.currentTimeMillis();
            DFSPath = depthFirstSearch(randomStart, randomGoal);
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Length of DFS: " + (DFSPath.size() - 1));
            System.out.println("Time for DFS: " + totalTime);
            startTime = System.currentTimeMillis();
            aStarPath = aStar(randomStart, randomGoal);
            totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Length of A*: " + (aStarPath.size() - 1));
            System.out.println("Time for A*: " + totalTime);
            
        }
        Node previous = null;
        for (Node node : aStarPath)
        {
            node.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
            if (previous != null)
            {
                node.getEdgeBetween(previous).setAttribute("ui.style", "fill-color: rgb(0,100,255);");
            }
            previous = node;
        }
        
//        Node node = graph.getNode("1_1");
//        node.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
//        Node node = graph.getNode("1_1");
//        node.addAttribute("ui.style", "fill-color: rgb(0,100,255);");
//        node.getEdgeBetween("1_2").addAttribute("ui.style", "fill-color: rgb(0,100,255);");
//        System.out.println((String) node.getAttribute("ui-color"));
        graph.display();
        // TODO code application logic here
    }
    
    public static ArrayList<Node> depthFirstSearch(Node start, Node goal)
    {
        return depthFirstSearch(start, goal, new ArrayList());
    }
    
    public static ArrayList<Node> depthFirstSearch(Node node, Node goal, ArrayList<Node> seen)
    {
        ArrayList<Node> path = new ArrayList();
        path.add(node);
        seen.add(node);
        if (node.equals(goal))
        {
            return path;
        }
        Iterator iter = node.getNeighborNodeIterator();
        ArrayList<Node> toBeSeen = new ArrayList<>();
        while (iter.hasNext())
        {
            Node check = (Node) iter.next();
            if (!seen.contains(check))
            {
                seen.add(check);
                toBeSeen.add(check);
            }
        }
        for (Node child : toBeSeen)
        {
            path.addAll(depthFirstSearch(child, goal, seen));
            if (path.contains(goal)) return path;
            path.add(node);
            seen.addAll(path);
            seen = (ArrayList) seen.stream().distinct().collect(Collectors.toList());
        }
        return path;
    }
    
    public static ArrayList<Node> aStar(Node start, Node goal)
    {
        ArrayList<Node> closed = new ArrayList<>();
        PriorityQueue<PointPathGValue> heap = new PriorityQueue<>(new PointPathGValueComparator());
        
        String[] startStrings = start.getId().split("_");
        int[] startCoordinate = {Integer.parseInt(startStrings[0]), Integer.parseInt(startStrings[1])};
        String[] goalStrings = start.getId().split("_");
        int[] goalCoordinate = {Integer.parseInt(goalStrings[0]), Integer.parseInt(goalStrings[1])};
        int initialFValue = Math.abs(goalCoordinate[0] - startCoordinate[0]) + Math.abs(goalCoordinate[1] - startCoordinate[1]);
        ArrayList<Node> firstPath = new ArrayList<>();
        firstPath.add(start);
        PointPathGValue startingEntry = new PointPathGValue(start, firstPath, 0, initialFValue);
        heap.add(startingEntry);
        while (!heap.isEmpty())
        {
            PointPathGValue popped = heap.remove();
            Node point = popped.point;
            if (!closed.contains(point))
            {
                    if (point.equals(goal)) return popped.path;
                    int oldGValue = popped.gValue;
                    closed.add(point);
                    Iterator iter = point.getNeighborNodeIterator();
                    ArrayList<Node> children = new ArrayList<>();
                    while (iter.hasNext())
                    {
                        Node check = (Node) iter.next();
                        if (!closed.contains(check))
                        {
                            children.add(check);
                        }
                    }
                    for (Node child : children)
                    {
                        int newGValue = oldGValue + 1;
                        String[] pointStrings = point.getId().split("_");
                        int[] pointCoordinate = {Integer.parseInt(pointStrings[0]), Integer.parseInt(pointStrings[1])};
                        //int distanceToGoal = Math.abs(goalCoordinate[0] - pointCoordinate[0]) + Math.abs(goalCoordinate[1] - pointCoordinate[1]);
                        float distanceToGoal = (float) Math.sqrt(Math.pow(((float) goalCoordinate[0] - pointCoordinate[1]), 2) + Math.pow((float) (goalCoordinate[1] - pointCoordinate[1]), 2));
                        float newFValue = newGValue + distanceToGoal;
                        ArrayList<Node> newPath = (ArrayList<Node>) popped.path.clone();
                        newPath.add(child);
                        heap.add(new PointPathGValue(child, newPath, newGValue, newFValue));
                    }
            }
        }
        return null;
    }
}
