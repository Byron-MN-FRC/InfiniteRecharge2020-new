// RobotBuilder Version: 3.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.LimelightUtility.StreamMode;
import frc.robot.commands.AutoSelector;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot {

    private Command m_autonomousCommand;

    private RobotContainer m_robotContainer;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    CameraServer server;
    @Override
    public void robotInit() {
        server = CameraServer.getInstance();
        server.startAutomaticCapture("forward",0);
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
        // autonomous chooser on the dashboard.
        m_robotContainer = RobotContainer.getInstance();
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_RobotBuilder);
    }

    /**
    * This function is called every robot packet, no matter the mode. Use this for items like
    * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
    *
    * <p>This runs after the mode specific periodic functions, but before
    * LiveWindow and SmartDashboard integrated updating.
    */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
        // commands, running already-scheduled commands, removing finished or interrupted commands,
        // and running subsystem periodic() methods.  This must be called from the robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }


    /**
    * This function is called once each time the robot enters Disabled mode.
    */
    @Override
    public void disabledInit() {
        LimelightUtility.EnableDriverCamera(false);
        LimelightUtility.StreamingMode(StreamMode.Standard);
        RobotContainer.getInstance().getBallShooter().teleopWithIdle = false;
        RobotContainer.getInstance().getBallShooter().setShootIdleVelocity(0);
     //   Robot.climb.getBuddyPiston().set(false);
    }

    @Override
    public void disabledPeriodic() {
    }

    /**
    * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
    */
    @Override
    public void autonomousInit() {
        initializeSubsystems();
        RobotContainer.getInstance().getDriveTrain().autonomousLimiting();        
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();

        // schedule the autonomous command (example)
      //  if (m_autonomousCommand != null) {
      //      m_autonomousCommand.schedule();
      //  }
        m_autonomousCommand = new AutoSelector();
        m_autonomousCommand.schedule();
    }

    /**
    * This function is called periodically during autonomous.
    */
    @Override
    public void autonomousPeriodic() {
        RobotContainer.getInstance().getBallShooter().inAuton = true;
        SmartDashboard.putBoolean("drive/LimeLight Target", LimelightUtility.ValidTargetFound());
        CommandScheduler.getInstance().run();
        SmartDashboard.putNumber("drive/Game Timer", Timer.getMatchTime());
 }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
        initializeSubsystems();
        RobotContainer.getInstance().getDriveTrain().teleopLimiting();
   }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        RobotContainer.getInstance().getBallShooter().inAuton = false;
        CommandScheduler.getInstance().run();
        LimelightUtility.RefreshTrackingData();
        SmartDashboard.putBoolean("drive/LimeLight Target", LimelightUtility.ValidTargetFound());
        SmartDashboard.putNumber("drive/Game Timer", Timer.getMatchTime());
   }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    /**
    * This function is called periodically during test mode.
    */
    @Override
    public void testPeriodic() {
    }
    public void initializeSubsystems() {
        RobotContainer.getInstance().getDriveTrain().motorConfig();
        RobotContainer.getInstance().getDriveTrain().zeroSensors();
        RobotContainer.getInstance().getBallIndexer().reinitializeIndexer();
        RobotContainer.getInstance().getBallIndexer().resetCount();
        RobotContainer.getInstance().getDriveTrain().reinitializeDriveTrain();
        RobotContainer.getInstance().getBallShooter().reinitializeShooter();
        RobotContainer.getInstance().getShifter().reinitializeShifter();
        RobotContainer.getInstance().getBallAcquisition().reinitializeAquisition();
       // Robot.climb.reInitializeClimb();
       // Robot.controlPanel.reinitializeControlPanel();
       // LimelightUtility.WriteDouble("ledMode", 1); // 3 = Limelight O
    }
}
