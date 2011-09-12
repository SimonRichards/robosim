class SimRobot 
    require 'java'
    java_import java.util.LinkedList

    attr_accessor :sensors, :output
       
    def initialize
        @output = Java::SimulationRobot.RobotOutput.new
        @sensors = LinkedList.new
    end
    
    def add_sensor type, *args
        new_sensor = eval "Java::SimulationSensors.#{type}.new " << args.join(", ")
        @sensors.add(new_sensor)
        new_sensor
    end
    
    def add_behaviour type, *args
        eval "Java::SimulationEntitiesBehaviour.#{type}.new " << args.join(", ")
    end
end
