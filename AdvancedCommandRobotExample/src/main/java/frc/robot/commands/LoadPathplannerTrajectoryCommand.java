package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.RobotContainer;

import java.io.IOException;
import java.nio.file.Path;

public class LoadPathplannerTrajectoryCommand extends CommandBase {
    private final String filename;
    private final boolean resetOdomtry;
    private Trajectory trajectory;

    public LoadPathplannerTrajectoryCommand(String filename, boolean resetOdomtry) {
        this.filename = filename;
        this.resetOdomtry = resetOdomtry;
    }

    @Override
    public void initialize() {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(filename);
            trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException exception) {
            System.out.println("Unable to read from file " + filename);
            cancel();
        }
    }

    @Override
    public void execute() {
        RamseteCommand ramseteCommand = new RamseteCommand(
                trajectory,
                RobotContainer.driveTrainSubsystem::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics,
                RobotContainer.driveTrainSubsystem::getWheelSpeeds,
                new PIDController(Constants.V_kP, 0, 0),
                new PIDController(Constants.V_kP, 0, 0),
                RobotContainer.driveTrainSubsystem::DifferentialDriveVolts,
                RobotContainer.driveTrainSubsystem);

        if (resetOdomtry) {
            new SequentialCommandGroup(
                    new InstantCommand(
                            () -> RobotContainer.driveTrainSubsystem.resetOdometry(trajectory.getInitialPose())),
                    ramseteCommand).schedule();
        } else {
            ramseteCommand.schedule();
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
