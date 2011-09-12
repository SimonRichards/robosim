class Collector < SimRobot
    def initialize
        super
        @camera  = add_sensor :Camera
        @holding_sensor = add_sensor :CollectionSensor
        @speedo = add_sensor :VelocitySensor
        @compass = add_sensor :CompassSensor
        @traveling = true

        @distF = add_sensor :DistanceSensor, 0
        @distR = add_sensor :DistanceSensor, Math::PI

        @bounce = add_behaviour :Bounce, :@distF, :@distR
        @move = add_behaviour :FindMoveToBlock,  :@camera, :@speedo, :@compass, :@holding_sensor, :@bounce
    end

   def update
      @output = @move.update


    end
end
