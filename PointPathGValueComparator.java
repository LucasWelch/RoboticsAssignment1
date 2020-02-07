/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robotics_assignment_1;
/**
 *
 * @author Lucas
 */
import java.util.Comparator;
public class PointPathGValueComparator implements Comparator<PointPathGValue>
{
    public int compare(PointPathGValue o1, PointPathGValue o2)
    {
        if (o1.fValue == o2.fValue) return 0;
        else if (o1.fValue <= o2.fValue) return -1;
        else return 1;
    }
}
