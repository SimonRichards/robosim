class FancyRobot < SimRobot
  
    def initialize
        super
        @my_sensor   = add_sensor :DistanceSensor, 0
        @front_prox  = add_sensor :DistanceSensor, 0
        @rear_prox   = add_sensor :DistanceSensor, Math::PI

        @turn = 1.0
        @speed = 100.0
        @proximity = 40

        @state = :forward
        @running = false
        
        @output.set_motor 100.0
        @output.set_steering @turn

        @i = 0
    end
  

    def update
        print @i+=1
        if @state == :forward && @front_prox.get_output < @proximity
            @state = :reverse 
            @output.set_motor -@speed
            @output.set_steering -@turn
        end
        if  @state == :reverse && @rear_prox.get_output < @proximity
            @state = :forward 
            @output.set_motor @speed
            @output.set_steering @turn
        end
    end

end
  