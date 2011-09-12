package ux.usercontrol;

import simulation.Simulator;

import simulation.geometry.Environment;

import simulation.entities.Cup;

import simulation.entities.Robot;

import java.io.IOException;

import java.util.Collection;
import java.util.LinkedList;

import javax.script.ScriptException;

/**
 *
 * @author Simon
 */
public class DeflatedSimulator {
    public Environment           environment;
    public Collection<Cup> inanimates;
    public Collection<Robot>     robots;

    DeflatedSimulator(Simulator simulator) throws IOException, ScriptException {
        robots     = new LinkedList<Robot>();
        inanimates = new LinkedList<Cup>();

        for (Robot robot : simulator.getRobots()) {
            robots.add(new Robot(robot));
        }

        for (Cup inanimate : simulator.getThings()) {
            inanimates.add(new Cup(inanimate));
        }

        environment = new Environment(simulator.getEnvironment());
    }
}



