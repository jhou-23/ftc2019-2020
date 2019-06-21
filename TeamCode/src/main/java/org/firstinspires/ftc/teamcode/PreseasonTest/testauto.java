package org.firstinspires.ftc.teamcode.PreseasonTest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.All.HardwareMap;

@Autonomous(name="Test Auto", group="Test")

public class testauto extends LinearOpMode{
    String TAG = "Robot_Status:";
    @Override
    public void runOpMode() throws InterruptedException{
        HardwareMap map = new HardwareMap(hardwareMap);
        map.resetEncoders();
        map.calibrateGyro();
        while(!isStopRequested() && map.gyro.isCalibrating()){
            telemetry.log().clear();
            telemetry.addData(TAG,"Gyro Calibrating, please wait...");
            telemetry.update();
        }
        telemetry.log().clear();
        telemetry.log().add("Gyro Calibrated. Press Start.");
        telemetry.update();

        waitForStart();

        map.encoderDriveRotation(20,2);
        sleep(1000);
        map.gyroTurn(20,90,true);
        sleep(1000);

        while(opModeIsActive()){
            telemetry.clear();
            telemetry.addData(TAG,"frontRight: "+ map.frontRight.getCurrentPosition());
            telemetry.addData(TAG,"frontLeft: "+ map.frontLeft.getCurrentPosition());
            telemetry.addData(TAG,"backRight: "+ map.backRight.getCurrentPosition());
            telemetry.addData(TAG,"backLeft: "+ map.backLeft.getCurrentPosition());
            telemetry.update();
        };

    }
}
