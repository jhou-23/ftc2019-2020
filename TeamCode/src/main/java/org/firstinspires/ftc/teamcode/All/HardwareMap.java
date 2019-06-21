package org.firstinspires.ftc.teamcode.All;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class HardwareMap {
    public DcMotorEx backLeft, backRight, frontLeft, frontRight, linearSlider, firstJoint, secondJoint, intakeJoint;

    public Servo samplingServo, pawServo, doorServo;
    public CRServo intake;
    public MotorServo firstJointVirtualServo, secondJointVirtualServo;
    public GyroSensor gyro;
    public IntegratingGyroscope gyros;

    public com.qualcomm.robotcore.hardware.HardwareMap hardwareMap;
    public boolean encoderDone;
    public boolean gyroDone;

    public HardwareMap(com.qualcomm.robotcore.hardware.HardwareMap hwMap) {
        backLeft = (DcMotorEx)hwMap.get(DcMotor.class, "backLeft");
        backRight = (DcMotorEx)hwMap.get(DcMotor.class, "backRight");
        frontLeft = (DcMotorEx)hwMap.get(DcMotor.class,"frontLeft");
        frontRight = (DcMotorEx)hwMap.get(DcMotor.class,"frontRight");
        linearSlider = (DcMotorEx)hwMap.get(DcMotor.class, "linearSlider");
        firstJoint = (DcMotorEx)hwMap.get(DcMotor.class, "firstJoint");
        secondJoint = (DcMotorEx)hwMap.get(DcMotor.class, "secondJoint");

        samplingServo = hwMap.get(Servo.class, "samplingServo");
        intakeJoint = (DcMotorEx)hwMap.get(DcMotor.class, "intakeJoint");
        intake = hwMap.get(CRServo.class, "intake");
        pawServo = hwMap.get(Servo.class, "pawServo");
        doorServo = hwMap.get(Servo.class, "doorServo");
        gyro = hwMap.get(GyroSensor.class,"gyroSensor");
        gyros = (IntegratingGyroscope)gyro;


        firstJointVirtualServo = new MotorServo(firstJoint, MotorServo.MotorConfiguration.firstJoint);
        secondJointVirtualServo = new MotorServo(secondJoint, MotorServo.MotorConfiguration.secondJoint);

        this.hardwareMap = hwMap;
    }

    public void calibrateGyro(){
        gyro.calibrate();
    }
    public void gyroTurn(int power, double angle, boolean clockwise){
        gyroDone = false;
        if(clockwise){
            frontLeft.setPower(power);
            frontRight.setPower(-power);
            backLeft.setPower(-power);
            backRight.setPower(power);
        } else {
            frontLeft.setPower(-power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(-power);
        }
        while(!gyroDone) {
            if(gyros.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle >= angle){
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                gyroDone = true;
                break;
            }
        }
    }

    public void resetEncoders() throws InterruptedException{
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Thread.sleep(150);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void encoderDriveRotation(int power, double rotations){
        encoderDone = false;
        double initFrontLeft=frontLeft.getCurrentPosition();
        double initFrontRight=frontRight.getCurrentPosition();
        double initBackLeft=backLeft.getCurrentPosition();
        double initBackRight=backLeft.getCurrentPosition();
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(-power);
        while(!encoderDone) {
            if (Math.abs(initFrontLeft - frontLeft.getCurrentPosition()) >= rotations &&
                    Math.abs(initFrontRight - frontRight.getCurrentPosition()) >= rotations &&
                    Math.abs(initBackLeft - backLeft.getCurrentPosition()) >= rotations &&
                    Math.abs(initBackRight - backRight.getCurrentPosition()) >= rotations) {
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
                encoderDone = true;
                break;
            }
        }
    }
}
