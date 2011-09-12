class Mover < SimRobot
    def initialize
        super        
        @output.set_motor 0.0
        @compass = add_sensor :CompassSensor
        @speedo = add_sensor :VelocitySensor

        @camera  = add_sensor :Camera
        @holder = add_sensor :CollectionSensor

        @leftDist = add_sensor :DistanceSensor, Math::PI/5, -5, 0
        @rightDist = add_sensor :DistanceSensor, -Math::PI/5, 5, 0

        @search = add_behaviour :AvoidObstacle, :@speedo, :@compass, :@leftDist, :@rightDist, 200
        @drop = add_behaviour :MoveDropReturn, :@compass, :@speedo
        @move = add_behaviour :FindMoveToBlock, :@camera, :@speedo, :@compass, :@holder, :@drop



        @flag = false
    end
  
    def update
      @output = @move.update
    end
end
  