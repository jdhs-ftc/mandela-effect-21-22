/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file provides basic Teleop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleopMain", group="TeleOp")
//@Disabled
public class TeleopMain extends OpMode {

    /* Declare OpMode members. */
    HardwareConfig robot = new HardwareConfig(); // use the class created to define a Pushbot's hardware
    ElapsedTime period = new ElapsedTime();

    //Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        //hello check
        //deploy arm
        period.reset();
        robot.Arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.Arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Say", "Hello Driver");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // TODO: update for new arm
        final double CLAW_SPEED = .4;
        final double MAX_EXTEND = 1190;
        final double ARM_SPEED = 1;
        double armPower = -gamepad2.left_stick_y;
        double leftStick = -gamepad1.left_stick_y;
        double rightStick = -gamepad1.right_stick_y;
        double leftTrigger = gamepad1.left_trigger;
        double leftTrigger2 = gamepad2.left_trigger;
        double rightTrigger = gamepad1.right_trigger;
        double rightTrigger2 = gamepad2.right_trigger;
        boolean rightBumper = gamepad2.right_bumper;
        boolean leftBumper = gamepad2.left_bumper;
        boolean fast = gamepad1.left_bumper;
        boolean slow = gamepad1.right_bumper;
        double currentArmPosition = robot.Arm.getCurrentPosition();
        double rightPower = 0;
        double leftPower = 0;
        double clawPower = 0;
        double finalArmPower = 0;
        double carouselPower = 0;
        /* Original pivoting
        //left pivot
        if (leftTrigger > 0.01 && rightTrigger < 0.01) {
            leftPower = -leftTrigger;
            rightPower = leftTrigger;
        }
        //right pivot
        else if (rightTrigger > 0.01 && leftTrigger < 0.01) {
            leftPower = rightTrigger;
            rightPower = -rightTrigger;
        }
        //normal
        else {
            leftPower = leftStick;
            rightPower = rightStick;
        }
        */
        leftPower = leftStick  - leftTrigger + rightTrigger;
        rightPower = rightStick - rightTrigger + leftTrigger;
        //fast
        if (fast && !slow) {
            leftPower *= 1;
            rightPower *= 1;
        }
        //slow
        else if (!fast && slow) {
            leftPower *= .3;
            rightPower *= .3;
        }
        //normal
        else {
            leftPower *= .7;
            rightPower *= .7;
        }

        //carousel
        if (leftBumper && !rightBumper) {
            carouselPower = -1;
        } else if (!leftBumper && rightBumper) {
            carouselPower = 1;
        }

        //servo
        if (rightTrigger2 > 0.1 && leftTrigger2 < 0.1) {
            clawPower = rightTrigger2 * CLAW_SPEED;

        } else if (rightTrigger2 < 0.1 && leftTrigger2 > 0.1) {
            clawPower = -leftTrigger2 * CLAW_SPEED;

        } else {
            clawPower = 0;
        }

        //prevents arm from going to far
        if ((currentArmPosition > MAX_EXTEND) && (armPower > 0.01)) {
            finalArmPower = 0;
        } else if ((currentArmPosition < 10) && (armPower < -0.01)) {
            finalArmPower = 0;
        } else {
            finalArmPower = armPower * ARM_SPEED;

        }

        //declaring powers
        robot.LeftFront.setPower(leftPower);
        robot.LeftBack.setPower(leftPower);
        robot.RightFront.setPower(rightPower);
        robot.RightBack.setPower(rightPower);
        robot.CarouselWheel.setPower(carouselPower);
        robot.Arm.setPower(finalArmPower);
        robot.ClawRight.setPower(clawPower);
        robot.ClawLeft.setPower(clawPower);

        //telemetry
        telemetry.addData("Left:", "%.2f", leftStick);
        telemetry.addData("Right:", "%.2f", rightStick);
        telemetry.addData("Claw:", clawPower);
        telemetry.addData("Current Arm Position", robot.Arm.getCurrentPosition());
        telemetry.addData("Slow", slow);
        telemetry.addData("Fast", fast);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

    //timer function
    public void timer(double howLong) {
        period.reset();
        while (period.seconds() < howLong) {
            telemetry.addData("Timer", "%.2f", period.seconds());
            telemetry.update();
        }
    }


}