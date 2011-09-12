class Collector < SimRobot
    def initialize
        super
        #standard sensors
        @speedo = add_sensor :VelocitySensor
        @avoid = add_behaviour :Drive, :@speedo, 110, 100

    end

    def update
      if @VelocitySensor.get_Value <= 0.01
        @avoid.setVelocity -200
      end
      @output = @avoid.update
    end
end