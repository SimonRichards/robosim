class Collector < SimRobot
    def initialize
        super
        @speed       = add_sensor :VelocitySensor
        @compass     = add_sensor :CompassSensor
        spin        = add_behaviour :RelativeTurn, :@compass, Math::PI/2
        spin2       = add_behaviour :RelativeTurn, :@compass, 3.2*Math::PI/4
        spin3       = add_behaviour :RelativeTurn, :@compass, -1.0*Math::PI/4
        drive       = add_behaviour :Drive, :@speed, 100.0
        drive2      = add_behaviour :Drive, :@speed, 100.0
        drive3      = add_behaviour :Drive, :@speed, 100.0
        crash       = add_behaviour :Drive, :@speed, -100.0
        crash2      = add_behaviour :Drive, :@speed, 100.0
        @forward     = add_behaviour :Drive, :@speed, 100.0
        @reverse     = add_behaviour :Drive, :@speed, -100.0
        drive.set_desired_distance 600
        drive2.set_desired_distance 30
        drive3.set_desired_time 4
        crash.set_desired_distance 200
        crash2.set_desired_distance 200
        
        @forward.set_desired_time 1
        @reverse.set_desired_time 1
        
        @behaviours = Array.new
        @behaviours.push(drive).push(spin).push(spin2).push(drive2).push(spin3).push(drive3).push(crash).push(crash2)        
        @behaviours.reverse!
        @current = @behaviours.pop
        
        @repeater = @forward
    end

    def update
        # Run through the initial queue
        if @current.is_finished
            @current = @behaviours.pop
        else
            @output = @current.update
        end if @current 
        
        #repeat forward and reverse behaviours ad inifinitum
        unless @current
            @output = @repeater.update
            if @repeater.is_finished
                @repeater.reset
                @repeater = @repeater == @forward ? @reverse : @forward                
            end
        end
    end
end
