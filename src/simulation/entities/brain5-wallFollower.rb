class WallFollower < SimRobot

    def initialize
        super
        #@speedo = add_sensor :RobotRadar
        @compass = add_sensor :CompassSensor
        @leftDist = add_sensor :DistanceSensor, Math::PI/5, 0, 20
        @speed = add_sensor :VelocitySensor
        #by changing the true and false, you can make it go clockwise or anti-clockwise
        @move = add_behaviour :FollowWall, :@compass, :@speed, :@leftDist,  100, true

    end

    def update

      @output = @move.update
    end
end
