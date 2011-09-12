class DemoMain < SimRobot
    def initialize
        super
        @camera = add_sensor :Camera, 0, 1000, 500
        @compass = add_sensor :CompassSensor
        @speedo = add_sensor :VelocitySensor
        @radar = add_sensor :RobotRadar, 100
        @left_prox = add_sensor :DistanceSensor, Math::PI/5, -5, 0
        @right_prox = add_sensor :DistanceSensor, -Math::PI/10, 5, 0
        @bucket = add_sensor :CollectionSensor
        @compass = add_sensor :CompassSensor
        
        @abs_turn = add_behaviour :AbsoluteTurn, :@compass, -Math::PI/2, 0.0001
        @cup_finder = add_behaviour :MoveToObject, :@camera, :@speedo, :@compass
        @follow_wall = add_behaviour :FollowWall, :@compass, :@speedo, :@left_prox, 100, 80, true
        @velocity = add_sensor :VelocitySensor
        @drive = add_behaviour :Drive, :@velocity
        @drive.set_desired_distance 255.0
        
        @state = :collecting
        p 'collecting'
    end

    def update
        
        case @state
            
        when :collecting
            @output = @cup_finder.update
            if @cup_finder.is_finished
                @output.set_arm true
                @cup_finder.reset
            else
                @output.set_arm false
            end
            if @bucket.get_collection_count() == 4
                @state = :turning 
                p 'turning'
            end
            
        when :turning
            unless @abs_turn.is_finished
                @output = @abs_turn.update
            else
                @state = :grabbing
                p 'grabbing'
            end
            
        when :grabbing
            @output.set_motor 0.0
            @output.set_arm true
            if @bucket.is_holding
                @state = :shoving 
                p 'shoving'
            end
            
        when :shoving
            @output = @drive.update unless @drive.is_finished
            @output.set_arm true
            if @drive.is_finished
                @output.set_arm false
                @drive.set_desired_distance 150.0
                @drive.set_desired_velocity -50.0
                @state = :reversing
            end
            
        when :reversing
            unless @drive.is_finished
                @output = @drive.update 
            else
                @output.set_motor 0.0
                @state = :meandering
            end
            
        when :meandering
            @output = @follow_wall.update unless @follow_wall.is_finished
            
        when :racing
            
        when :exploring
            
        end
            
    end
end
