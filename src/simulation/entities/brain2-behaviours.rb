class BehaviourRobot < SimRobot
    def initialize
        super
        @front_prox  = add_sensor :DistanceSensor, 0
        @rear_prox   = add_sensor :DistanceSensor, Math::PI

        @forward     = add_behaviour :Bounce, :@front_prox, :@rear_prox
    end

    def update

        @output = @forward.update
      1
    end
end
