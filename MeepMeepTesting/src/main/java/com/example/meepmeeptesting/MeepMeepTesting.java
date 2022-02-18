package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
// TODO: Redo ALL the autonomous in RoadRunner, after the bot is tuned
public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(57.73611498784813, 60, Math.toRadians(180), Math.toRadians(180), 17)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-40, -62, Math.toRadians(0))) // COPY PASTE YOUR CODE AFTER THIS LINE, edit this line to the start pos of robot
                                .back(20) // Align with Carousel Wheel
                                .waitSeconds(1)
                                .addDisplacementMarker(() -> { // Spin Carousel Wheel
                                    // TODO: Put carousel wheel spinner here
                                })
                                .splineTo(new Vector2d(-12, -40), Math.toRadians(90)) // Go To Blue Shipping Hub
                                .waitSeconds(1)
                                .addDisplacementMarker(() -> { // Ship the hub
                                    // TODO: Put the stuff to ship the hub here
                                })
                                .setReversed(true)
                                .splineTo(new Vector2d(50, -40), Math.toRadians(0))
                        .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}