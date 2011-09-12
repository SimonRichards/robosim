class Collector < SimRobot
    def initialize
        super
        @camera = add_sensor :Camera, 0, 1000, 500
        @compass = add_sensor :CompassSensor
        @speedo = add_sensor :VelocitySensor
        @left_prox   = add_sensor :DistanceSensor, Math::PI/4, 0, 20
        @speed       = add_sensor :VelocitySensor
        @collector  = add_sensor :CollectionSensor

        @followWall = add_behaviour :FollowWall, :@compass,  :@speed, :@left_prox,100,  true

        @move     = add_behaviour :FindMoveToCup, :@camera, :@speedo, :@compass, :@collector, :@followWall

    end

    def update
      @output = @move.update

    end
end
