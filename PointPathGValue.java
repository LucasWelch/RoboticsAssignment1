/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotics_assignment_1;
import java.util.ArrayList;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;
/**
 *
 * @author Lucas
 */
public class PointPathGValue
{
    Node point;
    ArrayList<Node> path;
    int gValue;
    float fValue;
    
    public PointPathGValue(Node point, ArrayList<Node> path, int gValue, float fValue)
    {
        this.point = point;
        this.path = path;
        this.gValue = gValue;
        this.fValue = fValue;
        
    }   
}