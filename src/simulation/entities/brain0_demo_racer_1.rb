class DemoRacer < SimRobot
    def initialize
        super
        @compass = add_sensor :CompassSensor
        @speedo   = add_sensor :VelocitySensor
        @radar = add_sensor :RobotRadar, 200.0

        @chase = add_behaviour :MoveToRobotRadar,  :@radar, :@speedo, :@compass
        @race = add_behaviour :Drive, :@speedo, 100
        @race.setDesiredDistance(400)
    end

    def update
      if @radar.isRobot
        if !@race.isFinished()
            @output = @race.update
        else
            @output = @chase.update
        end
      end
    end
end
