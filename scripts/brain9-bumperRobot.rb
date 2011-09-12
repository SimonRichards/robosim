class BoringRobot < SimRobot
    def initialize
        super        
        @output.set_motor 20.0

        @dist = add_sensor :DistanceSensor

        #Square bumper in front
        @bumperF = add_sensor :Bumper, 0,40,40,10,0

        #Small circular bumpers in rear left
        @bumperL = add_sensor :Bumper, -20,-30,10
        
        #small elliptical bumper in rear right
        @bumperR = add_sensor :Bumper, 20,-30,12,5,0,Math::PI/2

        @compass = add_sensor :CompassSensor

        @steerR = add_behaviour :RelativeTurn, :@compass, -Math::PI/2
        @steerL = add_behaviour :RelativeTurn, :@compass, Math::PI/2
        

    end
  
    def update

      @output.setSteering 0.0

      if @bumperF.getOutput
        @output.setMotor -20
      end

      if @bumperL.getOutput
        @output = @steerR.update
      end

      if @bumperR.getOutput
        @output = @steerL.update
      end

    end
end
  