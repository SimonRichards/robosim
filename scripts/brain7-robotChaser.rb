class Collector < SimRobot
    def initialize
        super
        @sideSensor = add_sensor :DistanceSensor, Math::PI/5, 0, 20
        @compass = add_sensor :CompassSensor
        @speedo   = add_sensor :VelocitySensor
        @radar = add_sensor :RobotRadar

        @chase = add_behaviour :MoveToRobotRadar, :@radar, :@speedo, :@compass
        @followWall = add_behaviour :FollowWall, :@compass,  :@speedo, :@sideSensor, 150, true

    end

    def update
      @output = @chase.update

      # If no cup is found follow wall
      if !@radar.isRobot
        @output = @followWall.update
      end

    end
end
