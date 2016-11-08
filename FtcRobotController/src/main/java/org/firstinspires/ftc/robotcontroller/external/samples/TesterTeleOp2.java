package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tyler on 11/7/2016.
 */
public class TesterTeleOp2 extends OpMode{

    static DcMotor driveOne;
    static DcMotor driveTwo;
    static DcMotor driveThree;

    static HashMap<DcMotor, Integer>[] driveMotorDirections = new HashMap[6];
    static DcMotor[] adjustmentMotors = new DcMotor[6];

    public void init(){
        driveOne = hardwareMap.dcMotor.get("driveOne");
        driveTwo = hardwareMap.dcMotor.get("driveTwo");
        driveThree = hardwareMap.dcMotor.get("driveThree");
    }

    public static void setupDirectionData() {

        for(int i = 0; i < driveMotorDirections.length; i++) {
            driveMotorDirections[i] = new HashMap<DcMotor, Integer>();
        }

        driveMotorDirections[0].put(driveTwo, -1);
        driveMotorDirections[0].put(driveThree, +1);
        adjustmentMotors[0] = driveOne;

        driveMotorDirections[1].put(driveOne, +1);
        driveMotorDirections[1].put(driveTwo, -1);
        adjustmentMotors[1] = driveThree;

        driveMotorDirections[2].put(driveOne, +1);
        driveMotorDirections[2].put(driveThree, -1);
        adjustmentMotors[2] = driveTwo;

        driveMotorDirections[3].put(driveTwo, +1);
        driveMotorDirections[3].put(driveThree, -1);
        adjustmentMotors[3] = driveOne;

        driveMotorDirections[4].put(driveOne, -1);
        driveMotorDirections[4].put(driveTwo, +1);
        adjustmentMotors[4] = driveThree;

        driveMotorDirections[5].put(driveOne, -1);
        driveMotorDirections[5].put(driveThree, +1);
        adjustmentMotors[5] = driveTwo;


        // System.out.println(Arrays.toString(driveMotorDirections));
        // System.out.println(Arrays.toString(adjustmentMotors));

    }

    public void loop(){
        setupDirectionData();

        float dirY = gamepad1.right_stick_y;
        float dirX = gamepad1.right_stick_x;

        dirY = (float) scaleInput(dirY);
        dirX = (float) scaleInput(dirX);

        double spin = 0; // spin is the x value of the second joystick

        // The magnitude of the speed of the drive motors
        // may need to be scaled based on range of motor power inputs
        double driveMotorSpeed = Math.hypot(dirX, dirY);

        // The magnitude of the speed of the adjustment motor
        double spinMotorSpeed = spin;

        // finds the bearing of the desired direction of the robot
        double bearing = 90-Math.toDegrees(Math.atan2(dirY, dirX));
        if(bearing < 0) bearing += 360;

        //this is the region of the joystick the input control is in
        //integer from 0 to 5
        //inclusive on lower end
        //corresponds to the index of driveMotorDirections and adjustmentMotors to
        //use for determining what motors to use
        int region;
        if(bearing >= 330) {
            region = 0;
        } else {
            region = (int)((bearing + 30) / 60);
        }
        System.out.println("region = " + region);


        // set the value of the drive motors by parsing through
        // the driveMotorDirections[region] HashMap to get the sign of the
        // rotation required of the number
        for(Map.Entry<DcMotor, Integer> p : driveMotorDirections[region].entrySet()) {
            DcMotor m = p.getKey();
            int dir = p.getValue();
            m.setPower(dir * driveMotorSpeed);
        }

        // set the power of the adjustment wheel
        adjustmentMotors[region].setPower(spinMotorSpeed);

//        System.out.println(driveOne);
//        System.out.println(driveTwo);
//        System.out.println(driveThree);

    }
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }


}
