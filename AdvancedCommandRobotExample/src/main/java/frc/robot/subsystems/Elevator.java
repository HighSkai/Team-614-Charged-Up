package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.Elevator;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Elevator extends SubsystemBase {
    /*Creates a new Elevation subsystem */ 
  CANSparkMax elevatorRightMotor = null;
  CANSparkMax elevatorLeftMotor = null;

  public Elevator() {
    elevatorRightMotor = new CANSparkMax(Constants.ELEVATOR_RIGHT_MOTOR, MotorType.kBrushless);
    elevatorLeftMotor = new CANSparkMax(Constants.ELEVATOR_LEFT_MOTOR, MotorType.kBrushless);

    elevatorRightMotor.follow(elevatorLeftMotor, false);
    elevatorRightMotor.setSmartCurrentLimit(40);
    elevatorLeftMotor.setSmartCurrentLimit(40);
  }
  public void periodic() {
    //Called once per scheduler run
  }
  public double getHeight() {
    SmartDashboard.putNumber("Position is", elevatorLeftMotor.getEncoder().getPosition());
    return elevatorLeftMotor.getEncoder().getPosition();
  }
  public void set(double val){
    elevatorLeftMotor.set(val);
  }
}