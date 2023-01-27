// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants;
import frc.robot.subsystems.Manipulator;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ManipulatorPIDCommand extends PIDCommand {
  /** Creates a new ManipulatorPIDCommand. */
  Manipulator manipulator;
  double newSetpoint;
  public ManipulatorPIDCommand(Manipulator manipulator, double manipulatorSetpoint) {
    super(
        // The controller that the command will use
        new PIDController(Constants.kP, Constants.kI, Constants.kD),
        // Returns current intake speed
        () -> manipulator.getSpeed(),
        // This should return the setpoint (can also be a constant)
        () -> 0,
        // This uses the output
        output -> {
          manipulator.set(output);
        });
    // Use addRequirements() here to declare subsystem dependencies.
    newSetpoint = manipulatorSetpoint;
    this.manipulator = manipulator;
    addRequirements(manipulator);
  }
  @Override
  public void execute(){
      setSetpoint(newSetpoint);
  }
  public void setSetpoint(double setpoint) {
    getController().setSetpoint(setpoint);
    newSetpoint = setpoint;
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
