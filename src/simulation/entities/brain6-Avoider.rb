class ScardyCat < SimRobot

    def initialize
        super
        @compass = add_sensor :CompassSensor
        @speedo = add_sensor :VelocitySensor
        @leftDist = add_sensor :DistanceSensor, Math::PI/5, -5, 0
        @rightDist = add_sensor :DistanceSensor, -Math::PI/5, 5, 0
        
        @move = add_behaviour :AvoidObstacle, :@speedo, :@compass, :@leftDist, :@rightDist, 200

    end

    def update
      @output = @move.update
    end
end
