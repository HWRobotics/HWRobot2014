package org.usfirst.frc1148.modules;

import edu.wpi.first.wpilibj.Solenoid;
import org.usfirst.frc1148.Robot;
import org.usfirst.frc1148.interfaces.RobotModule;

public class CatapultModule implements RobotModule {
    Robot robot;
    Solenoid sol, sol2;

    /*
     * States List
     *    0: Unloaded
     *    1: Loading
     *    2: unloading
     *    3: Loaded
     * */
    int state = 0;

    /*
     * We process the target state when NOT in the mid position.
     * */
    int targetState = 0;

    public CatapultModule(Robot robot) {
        this.robot = robot;
        
        
    }

    public void initModule() {
        System.out.println("Initialzing catapult module!");
        sol = new Solenoid (1); //channel. Make sure right ports!
        sol2 = new Solenoid (2);
        System.out.println("Catapult module initialized.");
    }

    public void activateModule() {
        System.out.println("Catapult module activated!");
        targetState = 3; /* Load */
    }

    public void deactivateModule() {
        System.out.println("Catapult module safely deactivating!");
        System.out.println("Catapult module deactivated!");
    }

    public void Launch()
    {
        targetState = 0;
        System.out.println("LAUNCHING");
    }

    public void Load() {
        targetState = 3;
        System.out.println("LOADING");
    }

    public void updateTick(int mode) {
        
        // JMF -- all we need is to switch the solenoid from load to launch
        
        if (targetState == 0) {
            /* Launch */
            sol2.set(true);
            sol.set(false);
        } else {
            /* Load */
            sol2.set(false);
            sol.set(true);
        }
        
        if (true) return;
        // JMF -- The original state machine is below but I think too complex for this.
        
        switch (state) {
        case 0:
            //do nothing
            break;
        case 1:      
            //Loading, send the signal to the solenoid
            if(sol.get())
                state=3;
            sol.set(true);
            sol2.set(true);
            break;
        case 2:
            //Unloading, send the signal to the solenoid
            if(!sol.get())
                state = 0;
            sol.set(false);
            sol2.set(false);
            break;
        case 3:
            //Loaded, do nothing
            break;
        }

        if(targetState != state && (state == 0 || state == 3)) {
            state += targetState > state ? 1 : -1;
        }
    }
}
