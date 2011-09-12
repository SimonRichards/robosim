package ux.usercontrol;

import ux.usercontrol.SimulatorController.Mode;

public interface SimulatorControlGui {
    void setResetted();

    void setStarted();

    void setPaused();

    void setMode(Mode mode);
}



