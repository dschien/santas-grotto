package ac.uk.bristol.cs.santa.grotto.business.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Travelling Salesman Implementation to calculate shortest tour around grottos.
 * Liberally helped to from here: http://www.brendanweinstein.me/2012/03/13/c-to-java-computing-optimal-tour-between-islands/
 */
@Service
public class LocationRoutePlanning {
    private static final Logger logger = LoggerFactory.getLogger(LocationRoutePlanning.class);

    private class OptimalDistance {
        double dist;
    }

    /* Uses Pythagorean theorem to find the distance between two sets of coordinates */
    private double distance(Location one, Location two) {
        double a = Math.abs((two.getLatitude() - one.getLatitude()));
        double b = Math.abs((two.getLongitude() - one.getLongitude()));
        double distance = Math.sqrt((a * a) + (b * b));
        return distance;
    }

    /* Calculate the distance of a tour */
    private double computeTourCost(ArrayList<Location> tour) {
        double cost = 0.0;
        for (int i = 0; i < tour.size() - 1; i++) {
            cost += distance(tour.get(i), tour.get(i + 1));
        }
        return cost;
    }

    /* Copies one ArrayList over another */
    private void copyArrayListOverAnother(ArrayList overwriteThis, ArrayList copyThis) {
        overwriteThis.clear();
        for (int i = 0; i < copyThis.size(); i++) {
            overwriteThis.add(copyThis.get(i));
        }
    }


    /* Parent method to the recursive computeOptimalTour method */
    public ArrayList<Location> computeOptimalTour(ArrayList<Location> locations, double maxDistance) {
        OptimalDistance optimalDistance = new OptimalDistance();
        optimalDistance.dist = Double.MAX_VALUE;
        int initialSize = locations.size();
        ArrayList<Location> workingTour = new ArrayList<>(initialSize);
        ArrayList<Location> optimalTour = new ArrayList<>(initialSize);
        computeOptimalTour(workingTour, locations, maxDistance, optimalTour, optimalDistance);
        logger.debug("optimal distance: " + optimalDistance.dist);
        return optimalTour;
    }

    /* Recursive Breadth-First Search Method */
    private void computeOptimalTour(ArrayList<Location> tourSoFar, ArrayList<Location> remaining, double maxDistance, ArrayList optimalTour, OptimalDistance optimalDistance) {

        if (remaining.isEmpty()) {
            tourSoFar.add(tourSoFar.get(0));
            double totalCost = computeTourCost(tourSoFar);
            if (totalCost < optimalDistance.dist) {
                copyArrayListOverAnother(optimalTour, tourSoFar);
                optimalDistance.dist = totalCost;
            }
            tourSoFar.remove(tourSoFar.size() - 1);
            return;
        }

        for (int i = 0; i < remaining.size(); i++) {
            Location next = remaining.get(i);
            remaining.remove(i);
            tourSoFar.add(next);
            computeOptimalTour(tourSoFar, remaining, maxDistance, optimalTour, optimalDistance);
            tourSoFar.remove(tourSoFar.size() - 1);
            remaining.add(i, next);
        }
    }
}