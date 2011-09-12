class Collector < SimRobot
    def initialize
        super
        #standard sensors
        @camera = add_sensor :Camera, 0, 500, 500
        @compass = add_sensor :CompassSensor
        @speedo = add_sensor :VelocitySensor

        #Avoid behavior stuff
        @leftDist = add_sensor :DistanceSensor, Math::PI/5, -5, 0
        @rightDist = add_sensor :DistanceSensor, -Math::PI/5, 5, 0


        @avoid = add_behaviour :AvoidObstacle, :@speedo, :@compass, :@leftDist, :@rightDist, 200

        @move     = add_behaviour :FindMoveToRobot, :@camera, :@speedo, :@compass, :@avoid

    end

    def update
      @output = @move.update

    end
end
